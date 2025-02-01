package com.example.backend.validation;

public class EmailValidation implements ValidationI{
    @Override
    public String getRegex() {
        return "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    }
}
