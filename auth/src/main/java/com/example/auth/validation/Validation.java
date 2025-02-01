package com.example.auth.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean validate(String input, ValidationI validator) {
        String regex = validator.getRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        ValidationI phoneValidator = new PhoneNumberValidation();
        return validate(phoneNumber, phoneValidator);
    }

    public static boolean isValidEmail(String email) {
        ValidationI emailValidator = new EmailValidation();
        return validate(email, emailValidator);
    }
}
