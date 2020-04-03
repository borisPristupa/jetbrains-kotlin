package org.jetbrains.dummy.lang

import org.junit.Test

class DummyLanguageTestGenerated : AbstractDummyLanguageTest() {
    @Test
    fun testBad() {
        doTest("testData/bad.dummy")
    }
    
    @Test
    fun testGood() {
        doTest("testData/good.dummy")
    }
    
    @Test
    fun testVariable_access() {
        doTest("testData/variable_access.dummy")
    }
    
    @Test
    fun testVariable_initialization() {
        doTest("testData/variable_initialization.dummy")
    }
}
