package com.example.job_finder.controller;

import com.example.job_finder.model.ProfileSkills;
import com.example.job_finder.repository.ProfileSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile-skills")
@CrossOrigin
public class ProfileSkillsController {

    @Autowired
    private ProfileSkillsRepository profileSkillsRepository;

    @GetMapping("/{userId}")
    public ProfileSkills getByUserId(@PathVariable String userId) {
        return profileSkillsRepository.findByUserId(userId).orElse(null);
    }

    @PostMapping
    public ProfileSkills upsert(@RequestBody ProfileSkills payload) {
        ProfileSkills existing = profileSkillsRepository.findByUserId(payload.getUserId()).orElse(null);
        if (existing != null) {
            existing.setSkills(payload.getSkills());
            return profileSkillsRepository.save(existing);
        }
        return profileSkillsRepository.save(payload);
    }
}
