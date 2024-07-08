package com.enotes.services;

import java.io.UnsupportedEncodingException;

import com.enotes.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User saveUser(User user,String url);

    void sendEmail(User user, String url) throws UnsupportedEncodingException;
    
    public boolean verifyAccount(String verificationCode);
}
