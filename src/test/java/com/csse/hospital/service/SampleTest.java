package com.csse.hospital.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void simpleTest1() {
        assertFalse(1==0);
    }

    @Test
    public void simpleTest2() {
        String a = "xxx";
        assertEquals("xxx", a);
    }

    @Test
    public void simpleTest3() {
        String a = "1";
        String b = "2";
        assertNotEquals(a, b);
    }

    @Test
    public void addEmp() {

    }
}