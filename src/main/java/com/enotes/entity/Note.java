package com.enotes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "Notes")
public class Note {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDate date;
    private String imageId;
    @DBRef
    private User user;

    public Note() {
        this.date = LocalDate.now();
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDate.now();
        this.imageId = null;
    }

    // Getters and setters

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
