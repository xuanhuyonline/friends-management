package com.friends.management.service;

import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.friends.management.utils.UtilsEmail.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    //Questions 2
    @Override
    public List<String> findFriendByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + email, HttpStatus.BAD_REQUEST.value()));

        return userRepository.findFriendByEmail(user.getId());
    }

    //Questions 3
    @Override
    public List<String> getCommonFriends(List<String> friends) {
        if (!isValidEmail(friends.get(0)) || !isValidEmail(friends.get(1))) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        if (friends.size() != 2) {
            throw new ApplicationException("Exactly two email addresses are required", HttpStatus.BAD_REQUEST.value());
        }

        User user1 = userRepository.findByEmail(friends.get(0))
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + friends.get(0), HttpStatus.BAD_REQUEST.value()));
        User user2 = userRepository.findByEmail(friends.get(1))
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + friends.get(1), HttpStatus.BAD_REQUEST.value()));

        checkUsersEmail(user1, user2);

        return userRepository.findCommonEmails(user1.getId(), user2.getId());
    }

    //Questions 6
    @Override
    public List<String> findFriendSubscribedByEmail(String sender, String text) {
        if (!isValidEmail(sender)) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        User user = userRepository.findByEmail(sender)
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + sender, HttpStatus.BAD_REQUEST.value()));

        List<String> recipients = userRepository.findFriendSubscribedByEmail(user.getId());

        List<String> emails = emailExtractor(text);
        recipients.addAll(emails);
        return recipients;
    }
}
