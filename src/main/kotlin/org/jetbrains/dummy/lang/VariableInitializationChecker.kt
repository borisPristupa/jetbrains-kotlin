package org.jetbrains.dummy.lang

import org.jetbrains.dummy.lang.tree.*

class VariableInitializationChecker(private val reporter: DiagnosticReporter) : AbstractChecker() {
    override fun inspect(file: File) {
        file.accept(VariableInitializationVisitor, mutableMapOf())
            .forEach {
                when (it) {
                    is AccessProblem -> reportAccessBeforeInitialization(it.variableAccess)
                    is InitializationProblem -> reportEmptyInitializationExpression(it.initializer)
                    is DeclarationProblem -> reportDeclarationNotFound(it.variableAccess)
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
}

private class AccessProblem(val variableAccess: VariableAccess) : VisitProblem
private class InitializationProblem(val initializer: Expression) : VisitProblem
private class DeclarationProblem(val variableAccess: VariableAccess) : VisitProblem

private object VariableInitializationVisitor :
    RoutedCollectingVisitor<VisitProblem, MutableMap<String, Boolean>>() {

    override fun visitFunctionDeclaration(
        functionDeclaration: FunctionDeclaration,
        data: MutableMap<String, Boolean>
    ): Iterable<VisitProblem> {
        val variableData = mutableMapOf<String, Boolean>()
        functionDeclaration.parameters.forEach { variableData[it] = true }
        return super.visitFunctionDeclaration(functionDeclaration, variableData)
    }

    override fun visitBlock(block: Block, data: MutableMap<String, Boolean>): Iterable<VisitProblem> {
        val varsKnownOutside: Set<String> = mutableSetOf<String>().apply { addAll(data.keys) }
        val result = super.visitBlock(block, data)

        // remove all variables local to this block
        data.keys
            .filterNot { varsKnownOutside.contains(it) }
            .forEach {
                if (it.startsWith("!")) // these variables shadowed some varsKnownOutside
                // variable names must not contain '!', so this substring must work without exceptions
                    it.substring(it.lastIndexOf('!') + 1)
                        .apply { unshadow(this, data) }
                else data.remove(it)
            }

        return result
    }

    override fun visitAssignment(
        assignment: Assignment,
        data: MutableMap<String, Boolean>
    ): Iterable<VisitProblem> {
        var initializerIsBad = false
        if (data.containsKey(assignment.variable)) {
            initializerIsBad = !assignment.rhs.accept(EvaluationVisitor(), false)
            data[assignment.variable] = true
        }

        val initializerProblems = super.visitAssignment(assignment, data)
        return if (initializerIsBad)
            initializerProblems.toMutableList().apply {
                add(InitializationProblem(assignment.rhs))
            }
        else initializerProblems
    }

    // TODO fail redeclaration
    override fun visitVariableDeclaration(
        variableDeclaration: VariableDeclaration,
        data: MutableMap<String, Boolean>
    ): Iterable<VisitProblem> {
        var initializerIsBad: Boolean
        with(variableDeclaration) {
            initializerIsBad = initializer != null && !initializer.accept(EvaluationVisitor(), false)
            declareVariable(name, initializer != null, data)
        }
        val initializerProblems = super.visitVariableDeclaration(variableDeclaration, data)

        return if (initializerIsBad)
            initializerProblems.toMutableList().apply {
                add(InitializationProblem(variableDeclaration.initializer!!))
            }
        else initializerProblems
    }

    // TODO check if shadowing works (data.containsKey() should work even if value is null)
    /**
     * @return Returns **true** if variable declaration caused shadowing
     */
    fun declareVariable(name: String, initialized: Boolean, data: MutableMap<String, Boolean>): Boolean {
        var shadowingName = name
        var shadowingValue = initialized

        var didShadowing = false
        while (data.containsKey(shadowingName)) {
            didShadowing = true
            val shadowedName = "!$shadowingName"

            val shadowedValue = data[shadowingName]!!
            data[shadowingName] = shadowingValue

            shadowingValue = shadowedValue
            shadowingName = shadowedName
        }
        data[shadowingName] = shadowingValue
        return didShadowing
    }

    fun unshadow(name: String, data: MutableMap<String, Boolean>) {
        var shadowedName = name
        while (data.containsKey("!$shadowedName")) {
            data[shadowedName] = data["!$shadowedName"]!!
            shadowedName = "!$shadowedName"
        }
        data.remove(shadowedName)
    }

    override fun visitIfStatement(
        ifStatement: IfStatement,
        data: MutableMap<String, Boolean>
    ): Iterable<VisitProblem> {
        val elseBlock = ifStatement.elseBlock
        if (elseBlock != null) {
            val dataCopy = mutableMapOf<String, Boolean>().apply { putAll(data) }
            val thenProblems = ifStatement.thenBlock.accept(this, data)
            val elseProblems = elseBlock.accept(this, dataCopy)

            // consider variable initialized if and only if it was initialized in both blocks
            data.keys.forEach {
                data[it] = data[it]!! && dataCopy[it]!! // variable cannot be deleted from inside a block
            }
            return mutableListOf<VisitProblem>().apply {
                addAll(ifStatement.condition.accept(this@VariableInitializationVisitor, data))
                addAll(thenProblems)
                addAll(elseProblems)
            }
        } else {
            return mutableListOf<VisitProblem>().apply {
                addAll(ifStatement.condition.accept(this@VariableInitializationVisitor, data))
                addAll(ifStatement.thenBlock.accept(this@VariableInitializationVisitor, data))
            }
        }
    }

    override fun visitVariableAccess(
        variableAccess: VariableAccess,
        data: MutableMap<String, Boolean>
    ): Iterable<VisitProblem> = when (data[variableAccess.name]) {
        true -> emptyList()
        false -> listOf(AccessProblem(variableAccess))
        null -> listOf(DeclarationProblem(variableAccess))
    }
}
