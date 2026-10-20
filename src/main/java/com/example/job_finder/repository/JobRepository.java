package com.example.job_finder.repository;

import com.example.job_finder.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {
}
