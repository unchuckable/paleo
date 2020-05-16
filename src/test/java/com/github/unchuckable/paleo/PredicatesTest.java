package com.github.unchuckable.paleo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gitlab.unchuckable.paleo.Predicates;

import org.junit.jupiter.api.Test;

public class PredicatesTest {

    @Test
    public void testStringMatcher() {
        assertTrue(Predicates.matchesString("hans").test("hans"));
        assertFalse(Predicates.matchesString("hans").test("peter"));
        assertEquals(Predicates.matchesString("hans"), Predicates.matchesString("hans"));
    }

    @Test
    public void testRegexMatcher() {
        assertTrue(Predicates.matchesRegex(".*Hans").test("Hans"));
        assertFalse(Predicates.matchesRegex(".*Hans").test("Peter"));
        assertEquals(Predicates.matchesRegex("a"), Predicates.matchesRegex("a"));
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(Predicates.matchesString("a"), Predicates.matchesRegex("a"));
    }

}