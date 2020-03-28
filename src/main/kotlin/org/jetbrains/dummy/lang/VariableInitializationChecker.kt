package org.jetbrains.dummy.lang

import org.jetbrains.dummy.lang.tree.*

class VariableInitializationChecker(private val reporter: DiagnosticReporter) : AbstractChecker() {
    override fun inspect(file: File) {
        file.accept(VariableInitializationVisitor, Context(mutableMapOf(), mutableMapOf()))
            .forEach {
                when (it) {
                    is AccessProblem -> reportAccessBeforeInitialization(it.variableAccess)
                    is InitializationProblem -> reportEmptyInitializationExpression(it.initializer)
                    is MissingDeclarationProblem -> reportDeclarationNotFound(it.variableAccess)
                    is DuplicateDeclarationProblem -> reportDuplicateDeclaration(it.variableDeclaration)
                }
            }
    }

    private fun reportAccessBeforeInitialization(access: VariableAccess) {
        reporter.report(access, "Variable '${access.name}' is accessed before initialization")
    }

    private fun reportEmptyInitializationExpression(initializer: Expression) {
        reporter.report(initializer, "Empty expression cannot be used for initialization")
    }

    private fun reportDeclarationNotFound(access: VariableAccess) {
        reporter.report(access, "Variable declaration not found: '${access.name}'")
    }

    private fun reportDuplicateDeclaration(declaration: VariableDeclaration) {
        reporter.report(declaration, "Variable '${declaration.name}' cannot be redeclared")
    }
}

typealias VariablesInitialized = MutableMap<String, Boolean>

private class Context(val outerVars: VariablesInitialized, val localVars: VariablesInitialized) {
    fun createInnerContext(): Context =
        Context(mutableMapOf<String, Boolean>().apply {
            putAll(localVars)
            putAll(outerVars.filterNot { localVars.containsKey(it.key) })
        }, mutableMapOf())

    fun updateOuterContext(outerContext: Context) = outerVars.forEach {
        when (outerContext.localVars.containsKey(it.key)) {
            true -> outerContext.localVars
            false -> outerContext.outerVars
        }[it.key] = it.value
    }
}

private class AccessProblem(val variableAccess: VariableAccess) : VisitProblem
private class InitializationProblem(val initializer: Expression) : VisitProblem
private class MissingDeclarationProblem(val variableAccess: VariableAccess) : VisitProblem
private class DuplicateDeclarationProblem(val variableDeclaration: VariableDeclaration) : VisitProblem

private object VariableInitializationVisitor :
    RoutedCollectingVisitor<VisitProblem, Context>() {

    override fun visitFunctionDeclaration(
        functionDeclaration: FunctionDeclaration,
        data: Context
    ): Iterable<VisitProblem> {
        val parameters = mutableMapOf<String, Boolean>()
        functionDeclaration.parameters.forEach { parameters[it] = true }
        return super.visitFunctionDeclaration(functionDeclaration, Context(mutableMapOf(), parameters))
    }

    override fun visitBlock(block: Block, data: Context): Iterable<VisitProblem> {
        val blockContext = data.createInnerContext()
        return super.visitBlock(block, blockContext).also {
            blockContext.updateOuterContext(data)
        }
    }

    override fun visitAssignment(assignment: Assignment, data: Context): Iterable<VisitProblem> {
        val problems = arrayListOf<VisitProblem>()

        val initializerIsBad = !assignment.rhs.accept(EvaluationVisitor(), false)
        if (initializerIsBad)
            problems.add(InitializationProblem(assignment.rhs))

        listOf(data.localVars, data.outerVars)
            .firstOrNull { it.containsKey(assignment.variable) }
            ?.set(assignment.variable, true)
            ?: problems.add(MissingDeclarationProblem(VariableAccess(assignment.line, assignment.variable)))

        return problems.apply {
            addAll(super.visitAssignment(assignment, data))
        }
    }

    // TODO fail redeclaration
    override fun visitVariableDeclaration(
        variableDeclaration: VariableDeclaration,
        data: Context
    ): Iterable<VisitProblem> {
        val problems = arrayListOf<VisitProblem>()

        val initializerIsBad = variableDeclaration.initializer
            ?.accept(EvaluationVisitor(), false)?.not()
            ?: false
        if (initializerIsBad)
            problems.add(InitializationProblem(variableDeclaration.initializer!!))

        if (data.localVars.containsKey(variableDeclaration.name)) {
            problems.add(DuplicateDeclarationProblem(variableDeclaration))
        }
        data.localVars[variableDeclaration.name] = variableDeclaration.initializer != null

        return problems.apply {
            addAll(super.visitVariableDeclaration(variableDeclaration, data))
        }
    }

    override fun visitIfStatement(ifStatement: IfStatement, data: Context): Iterable<VisitProblem> {
        val elseBlock = ifStatement.elseBlock
        if (elseBlock == null) {
            return mutableListOf<VisitProblem>().apply {
                addAll(ifStatement.condition.accept(this@VariableInitializationVisitor, data))
                addAll(ifStatement.thenBlock.accept(this@VariableInitializationVisitor, data))
            }
        } else {
            val thenContext = data.createInnerContext()
            val elseContext = data.createInnerContext()
            val thenProblems = ifStatement.thenBlock.accept(this, thenContext)
            val elseProblems = elseBlock.accept(this, elseContext)

            // consider variable initialized if and only if it was initialized in both blocks
            elseContext.outerVars.forEach { (k, v) ->
                thenContext.outerVars.merge(k, v, Boolean::and)
            }
            thenContext.updateOuterContext(data)

            return mutableListOf<VisitProblem>().apply {
                addAll(ifStatement.condition.accept(this@VariableInitializationVisitor, data))
                addAll(thenProblems)
                addAll(elseProblems)
            }
        }
    }

    override fun visitVariableAccess(variableAccess: VariableAccess, data: Context): Iterable<VisitProblem> =
        listOf(data.localVars, data.outerVars)
            .firstOrNull { it.containsKey(variableAccess.name) }
            ?.run {
                when (this[variableAccess.name]!!) {
                    true -> emptyList()
                    false -> listOf(AccessProblem(variableAccess))
                }
            } ?: listOf(MissingDeclarationProblem(variableAccess))
}
