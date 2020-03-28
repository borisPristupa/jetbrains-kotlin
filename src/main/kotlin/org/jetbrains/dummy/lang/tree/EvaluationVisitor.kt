package org.jetbrains.dummy.lang.tree

import org.jetbrains.dummy.lang.functionDeclarations

/**
    This visitor checks whether an element has some value.
    A function has some value if all of its return statements return something.
 */
internal class EvaluationVisitor : RoutedVisitor<Boolean, Any?>() {
    override fun emptyResult() = false

    override fun Iterable<Boolean>.collect(data: Any?): Boolean = any { it }

    override fun visitAssignment(assignment: Assignment, data: Any?): Boolean = emptyResult()

    override fun visitIfStatement(ifStatement: IfStatement, data: Any?): Boolean =
        listOfNotNull(ifStatement.thenBlock, ifStatement.elseBlock)
            .all { it.accept(this, data) }

    override fun visitVariableDeclaration(variableDeclaration: VariableDeclaration, data: Any?) = emptyResult()

    override fun visitReturnStatement(returnStatement: ReturnStatement, data: Any?): Boolean =
        returnStatement.result
            ?.accept(this, data)
            ?: false

    override fun visitVariableAccess(variableAccess: VariableAccess, data: Any?): Boolean = true

    override fun visitIntConst(intConst: IntConst, data: Any?): Boolean = true

    override fun visitBooleanConst(booleanConst: BooleanConst, data: Any?): Boolean = true

    override fun visitFunctionCall(functionCall: FunctionCall, data: Any?): Boolean {
        val functionsOverloaded = functionDeclarations[functionCall.function]
        val functionDeclaration = functionsOverloaded?.get(functionCall.arguments.size)

        return functionsOverloaded != null && functionDeclaration != null && functionDeclaration.accept(this, data)
    }
}
