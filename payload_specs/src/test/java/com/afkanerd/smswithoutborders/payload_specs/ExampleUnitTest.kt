package com.afkanerd.smswithoutborders.payload_specs

import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.getBitsDownTo
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val value: Short = 32767
        val expected: Short = 8191
        val gotten = value.getBitsDownTo(12)
        assertEquals(expected, gotten)
    }
}