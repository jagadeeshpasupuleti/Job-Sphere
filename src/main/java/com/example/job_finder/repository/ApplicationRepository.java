package com.example.job_finder.repository;

import com.example.job_finder.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByJobId(String jobId);
    List<Application> findByUserId(String userId);
    Application findByJobIdAndUserId(String jobId, String userId);
}
