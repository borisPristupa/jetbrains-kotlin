package org.jetbrains.dummy.lang.tree

abstract class RoutedVisitor<R, in D> : DummyLangVisitor<R, D>() {
    abstract fun emptyResult(): R

    abstract fun Iterable<R>.collect(data: D): R

    private fun <T : Element> Iterable<T>.visit(data: D): R =
        map { it.accept(this@RoutedVisitor, data) }.collect(data)

    override fun visitElement(element: Element, data: D): R =
        element.accept(this, data)

    override fun visitFile(file: File, data: D): R =
        file.functions.visit(data)

    override fun visitFunctionDeclaration(functionDeclaration: FunctionDeclaration, data: D): R =
        functionDeclaration.body.accept(this, data)

    override fun visitBlock(block: Block, data: D): R =
        block.statements.visit(data)

// --------------- Statements ---------------

    override fun visitStatement(statement: Statement, data: D): R =
        statement.accept(this, data)

    override fun visitAssignment(assignment: Assignment, data: D): R =
        assignment.rhs.accept(this, data)

    override fun visitIfStatement(ifStatement: IfStatement, data: D): R =
        listOfNotNull(ifStatement.condition, ifStatement.thenBlock, ifStatement.elseBlock)
            .visit(data)

    override fun visitVariableDeclaration(variableDeclaration: VariableDeclaration, data: D): R =
        variableDeclaration.initializer
            ?.run { accept(this@RoutedVisitor, data) }
            ?: emptyResult()

    override fun visitReturnStatement(returnStatement: ReturnStatement, data: D): R =
        returnStatement.result
            ?.run { accept(this@RoutedVisitor, data) }
            ?: emptyResult()

// --------------- Expressions ---------------

    override fun visitExpression(expression: Expression, data: D): R =
        expression.accept(this, data)

    override fun visitVariableAccess(variableAccess: VariableAccess, data: D): R = emptyResult()

    override fun visitIntConst(intConst: IntConst, data: D): R = emptyResult()

    override fun visitBooleanConst(booleanConst: BooleanConst, data: D): R = emptyResult()

    override fun visitFunctionCall(functionCall: FunctionCall, data: D): R =
        functionCall.arguments.visit(data)
}
