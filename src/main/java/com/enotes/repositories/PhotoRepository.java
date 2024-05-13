package com.enotes.repositories;

import com.enotes.entity.Note;
import com.enotes.entity.Photo;
import com.enotes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {
}
