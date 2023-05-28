package com.imams.core

import com.imams.core.util.asStringWithComma
import com.imams.core.util.simpleFormattedDate
import org.junit.Assert
import org.junit.Test

class DataCoreTest {

    @Test
    fun test_date() {
        val input = "2023-05-27T14:46:00.000000Z"
        val format = input.simpleFormattedDate()
        Assert.assertEquals("27 May 2023, 14:46:00", format)
    }

    @Test
    fun test_label_category() {
        val input = listOf("news", "for", "me")
        val res = input.asStringWithComma()
        Assert.assertEquals("news, for, me", res)

    }
}