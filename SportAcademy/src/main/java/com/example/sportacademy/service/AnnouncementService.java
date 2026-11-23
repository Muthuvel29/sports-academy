package com.example.sportacademy.service;

import com.example.sportacademy.entity.Announcement;
import com.example.sportacademy.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    // Save a new announcement
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // Get announcements for a particular sport
    public List<Announcement> getAnnouncementsForSport(String sport) {

        List<Announcement> announcements = new ArrayList<>();

        announcements.addAll(
                announcementRepository.findByTargetSport(sport)
        );

        announcements.addAll(
                announcementRepository.findByTargetSport("ALL")
        );

        return announcements;
    }

    // Get all announcements for admin
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
}

