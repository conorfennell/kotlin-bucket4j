/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.idiomcentric

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppTest {
    @Test
    fun appHasAGreeting() {
        val pattern = java.util.regex.Pattern.compile("some (?<houseNumber>[0-9]+) some")
        val matcher =  pattern.matcher("some 4353543 some")
        matcher.find()
        val houseNumber = matcher.group("houseNumber")
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }
}
