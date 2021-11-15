// Copyright 2020 Kelin Inc. All rights reserved.

package com.kelin.archetype.common.utils

import com.kelin.archetype.test.KtTestUtils
import org.junit.jupiter.api.Test

/**
 * @author Kelin Tan
 */
class CollectionHelperTest : KtTestUtils {
    @Test
    fun `call in batch`() {
        CollectionHelper.batchCall(listOf(1, 2, 3, 4), 2) { list ->
            list.sum()
        } verify {
            item(0) eq 3
            item(1) eq 7
        }

        CollectionHelper.batchCall(listOf(1, 2, 3), 2) { list ->
            list.sum()
        } verify {
            item(0) eq 3
            item(1) eq 3
        }

        CollectionHelper.batchCall(listOf(1, 2), 2) { list ->
            list.sum()
        } verify {
            item(0) eq 3
        }

        CollectionHelper.batchCall(listOf(1), 2) { list ->
            list.sum()
        } verify {
            item(0) eq 1
        }

        CollectionHelper.batchCall(listOf(1, 2, 3, 4, 5), 2) { list ->
            list.sum()
        } verify {
            item(0) eq 3
            item(1) eq 7
            item(2) eq 5
        }
    }

    @Test
    fun `call in batch default size`() {
        val list = mutableListOf(1)
        repeat(100) {
            list.add(1)
        }
        CollectionHelper.batchCall(list) {
            it.count()
        } verify {
            item(0) eq 100
            item(1) eq 1
        }
    }

    @Test
    fun `consume in batch`() {
        val result = mutableListOf<Int>()
        CollectionHelper.batchRun(listOf(1, 2)) {
            result.addAll(it)
        }

        result verify {
            item(0) eq 1
            item(1) eq 2
        }

        result.clear()

        CollectionHelper.batchRun(listOf(1, 2, 3), 2) {
            result.add(it.count())
        }

        result verify {
            item(0) eq 2
            item(1) eq 1
        }

        result.clear()

        CollectionHelper.batchRun(listOf(1, 2, 3, 4), 2) {
            result.add(it.count())
        }

        result verify {
            item(0) eq 2
            item(1) eq 2
        }

        result.clear()

        CollectionHelper.batchRun(listOf(1), 2) {
            result.add(it.count())
        }

        result verify {
            item(0) eq 1
        }
    }

    @Test
    fun `consume in batch default size`() {
        val list = mutableListOf<Int>()
        repeat(101) {
            list.add(1)
        }

        val result = mutableListOf<Int>()
        CollectionHelper.batchRun(list) {
            result.add(it.count())
        }

        result verify {
            item(0) eq 100
            item(1) eq 1
        }
    }
}