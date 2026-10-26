package com.amaan.futurex

import org.junit.Assert.*
import org.junit.Test

private class FakeStore : XpStore {
    val events = LinkedHashMap<String, Int>()
    override fun insertIfAbsent(id: String, xp: Int): Boolean {
        if (events.containsKey(id)) return false
        events[id] = xp
        return true
    }
    fun total() = events.values.sum()
}

class XpLogicTest {
    @Test fun taskAwardedOnce_evenIfToggledRepeatedly() {
        val s = FakeStore(); val a = XpAwarder(s)
        assertTrue(a.awardTask(1))
        repeat(5) { assertFalse(a.awardTask(1)) }
        assertEquals(10, s.total())
    }
    @Test fun differentTasksEachAward() {
        val s = FakeStore(); val a = XpAwarder(s)
        a.awardTask(1); a.awardTask(2)
        assertEquals(20, s.total())
    }
    @Test fun focusSessionAwardedOnce() {
        val s = FakeStore(); val a = XpAwarder(s)
        assertTrue(a.awardFocus("abc", 25))
        assertFalse(a.awardFocus("abc", 25))
        assertEquals(25, s.total())
    }
    @Test fun zeroMinuteSessionGivesNothing() {
        val s = FakeStore()
        assertFalse(XpAwarder(s).awardFocus("z", 0))
        assertEquals(0, s.total())
    }
    @Test fun levelBoundaries() {
        assertEquals(1, XpLogic.level(0))
        assertEquals(1, XpLogic.level(49))
        assertEquals(2, XpLogic.level(50))
        assertEquals(3, XpLogic.level(200))
    }
    @Test fun progressStaysInRange() {
        assertEquals(0f, XpLogic.progress(0), 0.001f)
        assertEquals(0.5f, XpLogic.progress(25), 0.001f)
        assertTrue(XpLogic.progress(10_000) in 0f..1f)
    }
}
