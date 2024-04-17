package com.enotes.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.enotes.entity.Note;
import com.enotes.entity.User;
import com.enotes.repositories.NoteRepo;
@Component
public class NoteService {
    @Autowired
    private NoteRepo noteRepo;

    public Page<Note> getNotesByUser(User user, int pageNo) {
        
        Pageable page = PageRequest.of(pageNo, 5);
        return noteRepo.findNoteByUser(user,page);
    }

    public Note findById(String id) {
        Optional<Note> optionalNote = noteRepo.findById(id);
        return optionalNote.orElse(null); // Returns null if optionalNote is empty
    }

    public Note addNote(Note note) {
        Note n = noteRepo.save(note);
        return n;
    }

    public void deleteNote(String id) {
        noteRepo.deleteById(id);
    }

    public void deleteAllNote() {
        noteRepo.deleteAll();
    }

    public Note updateNote(Note note, String id) {
        note.setId(id);
        noteRepo.save(note);
        return note;
    }

}
