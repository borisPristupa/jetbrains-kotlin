package org.jetbrains.dummy.lang

import org.jetbrains.dummy.lang.tree.File
import org.jetbrains.dummy.lang.tree.FunctionDeclaration
import java.io.OutputStream

class DummyLanguageAnalyzer(outputStream: OutputStream) {
    companion object {
        private val CHECKERS: List<(DiagnosticReporter) -> AbstractChecker> = listOf(
            ::VariableAccessChecker,
            ::VariableInitializationChecker
        )
    }

    private val reporter: DiagnosticReporter = DiagnosticReporter(outputStream)

    fun analyze(path: String) {
        val transformer = DummySourceToTreeTransformer()
        val file = transformer.transform(path)

        resolveFunctionDeclarations(file)

        val checkers = CHECKERS.map { it(reporter) }
        for (checker in checkers) {
            checker.inspect(file)
        }
    }

    private fun resolveFunctionDeclarations(file: File) {
        val functions = mutableMapOf<String, MutableMap<Int, FunctionDeclaration>>()
        file.functions.forEach {
            if (functions[it.name] == null) {
                functions[it.name] = mutableMapOf()
            }
            functions[it.name]!![it.parameters.size] = it
        }
        functionDeclarations = functions
    }
}
// Consider function's name and number of parameters to be its signature
internal lateinit var functionDeclarations: Map<String, Map<Int, FunctionDeclaration>>
