package com.fptu.swt301.demo.calculator;

public class Demo {
//    public  boolean  isPrimeNumber(int input)  {
//        for  (int i = 2; i < input; i++)  {
//            if  (input % i == 0)
//                return false;
//        }
//        return true;
//    }

    public  boolean  isPrimeNumber(int input)  {
        if  (input < 2)  {
            return false;
        }
        for  (int i = 2; i < input; i++)  {
            if  (input % i == 0)
                return false;
        }
        return true;
    }
}
