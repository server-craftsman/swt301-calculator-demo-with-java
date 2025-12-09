package com.fptu.calculator.csv.testing.danhuy.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDemo {

    @Test
    public  void  testIsPrimeNumber1()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(-1);
        assertFalse(result);
    }

    @Test
    public  void  testIsPrimeNumber2()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(0);
        assertFalse(result);
    }
    @Test
    public  void  testIsPrimeNumber3()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(1);
        assertFalse(result);
    }

    @Test
    public  void  testIsPrimeNumber4()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(2);
        assertTrue(result);
    }
    @Test
    public  void  testIsPrimeNumber5()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(4);
        assertFalse(result);
    }

    @Test
    public  void  testIsPrimeNumber6()  {
        Demo demo1 = new  Demo();
        boolean result = demo1.isPrimeNumber(5);
        assertTrue(result);
    }
}
