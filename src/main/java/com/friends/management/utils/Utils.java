package com.friends.management.utils;

import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class Utils {

    public static boolean isValidEmail(String email) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return email.matches(regex);
    }
}
