package com.example.OmokServer.OmokUser;

import com.example.OmokServer.Exceptions.LoginFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public OmokUser createUser(String username, String password) {
        if(findByUsername(username).isPresent())
            throw new IllegalArgumentException("username already exist");
        if(!isAvailableUsername(username))
            throw new IllegalArgumentException("username is not available");
        if(!isAvailablePassword(password))
            throw new IllegalArgumentException("password is not available");
        OmokUser user = new OmokUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<OmokUser> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
    public OmokUser findByUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("userId not exist"));
    }

    public OmokUser authenticateUser(String username, String password){
        OmokUser user = findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new LoginFailException("Invalid password");
        return user;
    }

    public void deleteUser(Long userId){

    }

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");

    private boolean isAvailableUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    private boolean isAvailablePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
