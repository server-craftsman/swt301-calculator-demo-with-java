package com.fptu.swt301.demo.calculator;

public class PasswordValidator {
    public boolean isPasswordStrong (String password) throws Exception{
        if (password.length() >12)
            throw new Exception("Password is too long!");
        return password.length()>7;
    }
}

