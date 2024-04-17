package com.enotes.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import com.enotes.entity.User;
@Service
public interface UserRepo extends MongoRepository<User,Integer> {
    public User findByEmail(String email);

    public User findByverificationCode(String code);
}
