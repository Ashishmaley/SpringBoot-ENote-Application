package com.enotes.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import com.enotes.entity.Note;
import com.enotes.entity.User;

@Service
public interface NoteRepo extends MongoRepository<Note, String> {
    public Page<Note> findNoteByUser(User user, Pageable page);
}
