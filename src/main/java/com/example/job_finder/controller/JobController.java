package com.example.job_finder.controller;

import com.example.job_finder.model.Job;
import com.example.job_finder.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // ✅ CREATE JOB
    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobRepository.save(job);
    }

    // ✅ GET ALL JOBS
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
