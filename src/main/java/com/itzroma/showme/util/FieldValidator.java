package com.itzroma.showme.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@UtilityClass
public class FieldValidator {
    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern PASSWORD_PATTERN_NO_SPECIAL =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validatePassword(String password, boolean withSpecial) {
        if (withSpecial) {
            return PASSWORD_PATTERN.matcher(password).matches();
        }
        return PASSWORD_PATTERN_NO_SPECIAL.matcher(password).matches();
    }

    public static boolean validateStringEmpty(String name) {
        return !StringUtils.hasText(name);
    }
}
