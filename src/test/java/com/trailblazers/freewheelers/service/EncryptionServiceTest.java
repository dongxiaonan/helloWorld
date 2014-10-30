package com.trailblazers.freewheelers.service;

import org.junit.Test;
import static org.testng.Assert.*;

public class EncryptionServiceTest {

    @Test
    public void shouldReturn61ForLowerCaseA() throws Exception {
        assertEquals("61",new EncryptionService().getStringToHex("a").toLowerCase());
    }

    @Test
    public void shouldReturn0aForLineFeed() throws Exception {
        assertEquals("0a",new  EncryptionService().getStringToHex("\n").toLowerCase());
    }

    @Test
    public void shouldReturnCorrectHexStringForInput() throws Exception {
        assertEquals("4920616d20746865206b696e672f717565656e21",
                new EncryptionService().getStringToHex("I am the king/queen!").toLowerCase());
    }

    @Test
    public void shouldReturnLowerCaseAWhenInputIs61() throws Exception {
        assertEquals("a",new  EncryptionService().getHexToString("61"));
    }

    @Test
    public void shouldReturnCorrectCharacterStringForInputHexString() throws Exception {
        assertEquals("I am the king/queen!",
                new EncryptionService().getHexToString("4920616d20746865206b696e672f717565656e21"));
    }
}