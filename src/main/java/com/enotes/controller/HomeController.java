package com.enotes.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;

import com.enotes.entity.Photo;
import com.enotes.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enotes.entity.Note;
import com.enotes.entity.User;
import com.enotes.repositories.UserRepo;
import com.enotes.services.NoteService;
import com.enotes.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @ModelAttribute
    public User getUser(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userRepo.findByEmail(email);
            if (user != null) {
                m.addAttribute("user", user);
                return user;
            }
        }
        // Handle the case where the user or principal is null
        return null;
    }

    @GetMapping("/")
    public String Home() {
        return "index";
    }

    @GetMapping("/user")
    public String HomeAfterLogin() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @GetMapping("/user/logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session, HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        url = url.replace(req.getServletPath(), "");

        // Create a new User object
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Save the user using userService.saveUser
        User u = userService.saveUser(user, url);

        if (u != null) {
            session.setAttribute("msg", "User Created Successfully,Verify Your Mail!!");
            return "register";
        } else {
            session.setAttribute("msg", "Failed to create user,can be already exist");
            return "register";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, org.springframework.ui.Model m) {
        if (userService.verifyAccount(code)) {
            m.addAttribute("msg", "account verified successfully!");
        } else
            m.addAttribute("msg", "account verification failed!");
        return "message";
    }



    @GetMapping("/user/editNotes/{id}")
    public String editNotes(@PathVariable("id") String id, Model m) {
        Note note = noteService.findById(id);
        m.addAttribute("note", note);
        return "edit_notes";
    }

    @GetMapping("/user/viewNotes")
    public String viewNotes(Model m, Principal p, @RequestParam(defaultValue = "0") Integer pageNo) {
        User user = getUser(p, m);
        Page<Note> notes = noteService.getNotesByUser(user, pageNo);
        m.addAttribute("currentPage", pageNo);
        m.addAttribute("totalElements", notes.getTotalElements());
        m.addAttribute("totalPages", notes.getTotalPages());
        m.addAttribute("notes", notes.getContent());
        return "view_notes";
    }
    @GetMapping("/user/photos/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);
        if (photo == null || photo.getImage() == null || photo.getImage().getData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your image type

        // Return the image data with appropriate headers
        return new ResponseEntity<>(photo.getImage().getData(), headers, HttpStatus.OK);
    }



    @PostMapping("/user/saveNote")
    public String addNote(@ModelAttribute Note note,
                          @RequestParam("image") MultipartFile image, HttpSession session, Principal p, Model m) throws IOException {
        note.setUser(getUser(p, m));
        if (note.getContent().length() > 1000) { // Adjust the maximum length here
            m.addAttribute("msg", "Content is too long! Please limit it to 4000 characters.");
            return "add_notes";
        }
        String id = photoService.addPhoto( image);
        note.setImageId(id);
        note.setDate(LocalDate.now());
        Note n = noteService.addNote(note);
        if (n != null) {
            m.addAttribute("msg", n);
        } else
            m.addAttribute("msg", "Note is null");

        return "redirect:/user/viewNotes";
    }

    @GetMapping("/user/addNotes")
    public String addNotePage() {
        return "add_notes";
    }

    @PostMapping("/user/updateNote/{id}")
    public String updateNote(@PathVariable("id") String id, @RequestParam("image") MultipartFile image, @ModelAttribute Note note, HttpSession session, Principal p,
            Model m) throws IOException {
        note.setUser(getUser(p, m));
        if (note.getContent().length() > 1000) { // Adjust the maximum length here
            m.addAttribute("msg", "Content is too long! Please limit it to 4000 characters.");
            return "add_notes";
        }

        String imageId = photoService.addPhoto( image);
        note.setImageId(imageId);
        note.setDate(LocalDate.now());
        Note n = noteService.updateNote(note, id);
        if (n != null) {
            m.addAttribute("msg", n);
        } else
            m.addAttribute("msg", "Note is null");

        return "redirect:/user/viewNotes";
    }

    @GetMapping("/user/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") String id, @ModelAttribute Note note, HttpSession session, Principal p,
            Model m) {
        noteService.deleteNote(id);
        return "redirect:/user/viewNotes";
    }

}
