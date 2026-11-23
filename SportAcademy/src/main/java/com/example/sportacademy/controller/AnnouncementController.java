package com.example.sportacademy.controller;

import com.example.sportacademy.entity.Announcement;
import com.example.sportacademy.service.AnnouncementService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(
            AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // Admin creates an announcement
    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(
            @RequestBody Announcement announcement,
            HttpSession session) {

        if (session.getAttribute("adminId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin login required"));
        }

        Announcement saved =
                announcementService.saveAnnouncement(announcement);

        return ResponseEntity.ok(saved);
    }

    // Students get announcements for their sport
    @GetMapping("/sport/{sport}")
    public List<Announcement> getAnnouncementsForSport(
            @PathVariable String sport) {

        return announcementService.getAnnouncementsForSport(sport);
    }

    // Only admin can view all announcements
    @GetMapping("/all")
    public ResponseEntity<?> getAllAnnouncements(
            HttpSession session) {

        if (session.getAttribute("adminId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin login required"));
        }

        return ResponseEntity.ok(
                announcementService.getAllAnnouncements()
        );
    }
}
