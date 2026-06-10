package edu.phystech.hw1;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class NumberPresentationTest {

    private static String toBinary(int x) {
        if (x == 0) return "0";
        StringBuilder result = new StringBuilder();
        int num = x;
        while (num > 0) {
            result.insert(0, num % 2);
            num = num / 2;
        }
        return result.toString();
    }

    private static String toOct(int x) {
        if (x == 0) return "0";
        StringBuilder result = new StringBuilder();
        int num = x;
        while (num > 0) {
            result.insert(0, num % 8);
            num = num / 8;
        }
        return result.toString();
    }

    private static String toHex(int x) {
        if (x == 0) return "0";
        StringBuilder result = new StringBuilder();
        int num = x;
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        while (num > 0) {
            result.insert(0, hexDigits[num % 16]);
            num = num / 16;
        }
        return result.toString();
    }

    @Test
    public void binaryPresentation() {
        Assertions.assertEquals("0", toBinary(0));
        Assertions.assertEquals("1", toBinary(1));

        Assertions.assertEquals("10", toBinary(2));
        Assertions.assertEquals("100", toBinary(4));
        Assertions.assertEquals("10000000", toBinary(128));

        Assertions.assertEquals("11111111", toBinary(255));
        Assertions.assertEquals("11111111", toBinary(255));

        Assertions.assertEquals("100101100100110000010", toBinary(1231234));
    }


    @Test
    public void octPresentation() {
        for (int i = 0; i < 8; ++i) {
            Assertions.assertEquals(String.valueOf(i), toHex(i));
        }

        Assertions.assertEquals("10", toOct(8));
        Assertions.assertEquals("100", toOct(64));
        Assertions.assertEquals("200", toOct(128));

        Assertions.assertEquals("377", toOct(255));

        Assertions.assertEquals("4544602", toOct(1231234));
    }

    @Test
    public void hexPresentation() {
        for (int i = 0; i < 10; ++i) {
            Assertions.assertEquals(String.valueOf(i), Integer.toHexString(i));
        }

        Assertions.assertEquals("10", toHex(16));
        Assertions.assertEquals("100", toHex(256));
        Assertions.assertEquals("200", toHex(512));

        Assertions.assertEquals("21e", toHex(542));

        Assertions.assertEquals("45b0c2", toHex(4567234));
    }


}

