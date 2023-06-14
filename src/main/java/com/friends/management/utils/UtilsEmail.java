package com.friends.management.utils;

import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsEmail {

    public static boolean isValidEmail(String email) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return email.matches(regex);
    }

    public static List<String> emailExtractor(String text) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        List<String> emails = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String email = matcher.group();
            emails.add(email);
        }
        return emails;
    }

    public static void checkSameEmail(User user1, User user2) {
        if (user1 == user2) {
            throw new ApplicationException("Two emails cannot be the same", HttpStatus.BAD_REQUEST.value());
        }
    }
}
