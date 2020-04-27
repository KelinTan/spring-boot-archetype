// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.testing

import com.kelin.archetype.base.http.HttpRequest
import com.kelin.archetype.base.http.HttpUtils
import com.kelin.archetype.base.json.JsonConverter
import org.junit.Assert.assertArrayEquals
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Test utilities using Kotlin.
 *
 * @author Kelin Tan
 */
interface KtTestUtils {
    infix fun <T> T?.eq(expectedValue: T?): T? {
        assertEquals(expectedValue, this)
        return this
    }

    infix fun <T> T?.be(expectedValue: T?): T? {
        assertSame(this, expectedValue)
        return this
    }

    infix fun Int.eq(expectedValue: Int): Int {
        assertEquals(expectedValue, this, "expected: $expectedValue but was: $this")
        return this
    }

    infix fun BigDecimal.eq(expectedValue: BigDecimal): BigDecimal {
        assertTrue(this.compareTo(expectedValue) == 0, "expected: $expectedValue but was: $this")
        return this
    }

    infix fun LocalDateTime.eq(expectedValue: LocalDateTime): LocalDateTime {
        assertEquals(expectedValue, this, "expected: $expectedValue but was: $this")
        return this
    }

    infix fun ByteArray.eq(expectedValue: ByteArray): ByteArray {
        assertArrayEquals(expectedValue, this)
        return this
    }

    infix fun <T> Array<T>.eq(expectedValue: Array<T>): Array<T> {
        assertArrayEquals(expectedValue, this)
        return this
    }

    infix fun <T> T?.not(expectedValue: T?): T? {
        assertNotEquals(expectedValue, this)
        return this
    }

    infix fun Long.greater(expectedValue: Long): Long {
        assertTrue(this > expectedValue, "Expect $this is greater than $expectedValue")
        return this
    }

    infix fun Int.greater(expectedValue: Int): Int {
        assertTrue(this > expectedValue, "Expect $this is greater than $expectedValue")
        return this
    }

    infix fun Double.greater(expectedValue: Double): Double {
        assertTrue(this > expectedValue, "Expect $this is greater than $expectedValue")
        return this
    }

    infix fun BigDecimal.greater(expectedValue: BigDecimal): BigDecimal {
        assertTrue(this > expectedValue, "Expect $this is greater than $expectedValue")
        return this
    }

    infix fun Long.less(expectedValue: Long): Long {
        assertTrue(this < expectedValue, "Expect $this is less than $expectedValue")
        return this
    }

    infix fun Int.less(expectedValue: Int): Int {
        assertTrue(this < expectedValue, "Expect $this is less than $expectedValue")
        return this
    }

    infix fun Double.less(expectedValue: Double): Double {
        assertTrue(this < expectedValue, "Expect $this is less than $expectedValue")
        return this
    }

    infix fun BigDecimal.less(expectedValue: BigDecimal): BigDecimal {
        assertTrue(this <= expectedValue, "Expect $this is less than $expectedValue")
        return this
    }

    infix fun <T> T?.verify(f: T.() -> Unit): T {
        this!!.f()
        return this
    }

    infix fun <T> List<T>?.verify(f: List<T>.() -> Unit): List<T> {
        this!!.f()
        return this
    }

    fun <T> List<T>?.item(i: Int, f: T.() -> Unit) {
        this!![i].f()
    }

    fun <T> List<T>?.item(i: Int): T {
        return this!![i]
    }

    fun <K, V> Map<K, V>?.item(i: K, f: V.() -> Unit) {
        this!!.getValue(i).f()
    }

    fun <T> List<T>?.item(i: IntRange, f: T.() -> Unit) {
        this!!.subList(i.first, i.last + 1).forEach { it.f() }
    }

    fun HttpRequest.json(): JsonNodeWrapper {
        val entity = HttpUtils.safeEntityToString(this.response().entity)
        return JsonNodeWrapper(
            JsonConverter.readTree(
                entity
            )
        )
    }

    infix fun JsonNodeWrapper.verify(f: JsonNodeWrapper.() -> Unit) {
        this.f()
    }

    infix fun <T> JsonNodeWrapper.eq(value: T) {
        when (value) {
            is Boolean -> asBoolean() eq value
            is Int -> asInt() eq value
            is Long -> asLong() eq value
            is Short -> asInt() eq value.toInt()
            is Byte -> asInt() eq value.toInt()
            is Float -> asDouble() eq value.toDouble()
            is Double -> asDouble() eq value
            is BigDecimal -> asBigDecimal() eq value
            is String -> asText() eq value
            else -> fail("Cannot verify values for unsupported type.")
        }
    }

    infix fun <T> JsonNodeWrapper.eq(value: List<T>) {
        assertEquals(value.size, jsonNode.size())
        for (i in value.indices) {
            JsonNodeWrapper(jsonNode[i]).eq(value[i])
        }
    }

    infix fun JsonNodeWrapper.startsWith(value: String) {
        asText().startsWith(value) eq true
    }

    infix fun JsonNodeWrapper.endsWith(value: String) {
        asText().endsWith(value) eq true
    }
}
