package org.jetbrains.dummy.lang

import org.jetbrains.dummy.lang.tree.*

private class EmptyInitializerProblem(val initializer: Expression) : VisitProblem

class VariableInitializationChecker(private val reporter: DiagnosticReporter) : AbstractChecker() {
    override fun inspect(file: File) {
        file.accept(VariableInitializationVisitor, null)
            .forEach {
                reportEmptyInitializerExpression(it.initializer)
            }
    }

    private fun reportEmptyInitializerExpression(initializer: Expression) {
        reporter.report(initializer, "The expression is empty and cannot be used to initialize a variable")
    }
}

private object VariableInitializationVisitor : RoutedCollectingVisitor<EmptyInitializerProblem, Any?>() {
    override fun visitAssignment(assignment: Assignment, data: Any?): Iterable<EmptyInitializerProblem> {
        val initializerIsBad = !assignment.rhs.accept(EvaluationVisitor(), false)

        return if (initializerIsBad)
            listOf(EmptyInitializerProblem(assignment.rhs))
        else emptyResult()
    }

    override fun visitVariableDeclaration(
        variableDeclaration: VariableDeclaration,
        data: Any?
    ): Iterable<EmptyInitializerProblem> {
        val initializerIsBad = variableDeclaration.initializer
            ?.accept(EvaluationVisitor(), false)?.not()
            ?: false

        return if (initializerIsBad)
            listOf(EmptyInitializerProblem(variableDeclaration.initializer!!))
        else emptyResult()
    }
}
