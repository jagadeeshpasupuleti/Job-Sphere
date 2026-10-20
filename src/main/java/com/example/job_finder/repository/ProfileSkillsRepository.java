package com.example.job_finder.repository;

import com.example.job_finder.model.ProfileSkills;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileSkillsRepository extends MongoRepository<ProfileSkills, String> {
    Optional<ProfileSkills> findByUserId(String userId);
}
