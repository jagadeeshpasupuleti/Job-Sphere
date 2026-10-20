package com.example.job_finder.controller;

import com.example.job_finder.model.Application;
import com.example.job_finder.model.Job;
import com.example.job_finder.model.ProfileSkills;
import com.example.job_finder.model.User;
import com.example.job_finder.repository.ApplicationRepository;
import com.example.job_finder.repository.JobRepository;
import com.example.job_finder.repository.ProfileSkillsRepository;
import com.example.job_finder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProfileSkillsRepository profileSkillsRepository;

    @PostMapping
    public Object apply(@RequestBody Application app){
        Application existing = repo.findByJobIdAndUserId(app.getJobId(), app.getUserId());
        if (existing != null) {
            return "ALREADY_APPLIED";
        }
        return repo.save(app);
    }

    @GetMapping("/job/{jobId}")
    public List<Application> getByJob(@PathVariable String jobId){
        return repo.findByJobId(jobId);
    }

    @GetMapping("/user/{userId}")
    public List<Application> getByUser(@PathVariable String userId){
        return repo.findByUserId(userId);
    }

    @GetMapping("/job/{jobId}/details")
    public List<Map<String, Object>> getByJobWithDetails(@PathVariable String jobId){
        List<Application> applications = repo.findByJobId(jobId);
        Optional<Job> jobOpt = jobRepository.findById(jobId);

        List<String> jobSkills = new ArrayList<>();
        if (jobOpt.isPresent() && jobOpt.get().getSkills() != null) {
            jobSkills = normalizeSkills(jobOpt.get().getSkills());
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Application app : applications) {
            Map<String, Object> row = new HashMap<>();
            row.put("applicationId", app.getId());
            row.put("jobId", app.getJobId());
            row.put("userId", app.getUserId());

            User user = userRepository.findById(app.getUserId()).orElse(null);

            String name = "Unknown";
            String email = "";
            String role = "SEEKER";
            List<String> candidateSkills = new ArrayList<>();

            if (user != null) {
                name = user.getName() != null ? user.getName() : "Unknown";
                email = user.getEmail() != null ? user.getEmail() : "";
                role = user.getRole() != null ? user.getRole() : "SEEKER";

                if (user.getSkills() != null && !user.getSkills().trim().isEmpty()) {
                    candidateSkills = normalizeSkills(Arrays.asList(user.getSkills().split(",")));
                }
            }

            Optional<ProfileSkills> persistedSkills = profileSkillsRepository.findByUserId(app.getUserId());
            if (persistedSkills.isPresent() && persistedSkills.get().getSkills() != null && !persistedSkills.get().getSkills().isEmpty()) {
                candidateSkills = normalizeSkills(persistedSkills.get().getSkills());
            }

            int matchScore = computeMatchScore(candidateSkills, jobSkills);

            row.put("name", name);
            row.put("email", email);
            row.put("role", role);
            row.put("candidateSkills", candidateSkills);
            row.put("jobSkills", jobSkills);
            row.put("matchScore", matchScore);

            result.add(row);
        }

        result.sort((a, b) -> Integer.compare((int) b.get("matchScore"), (int) a.get("matchScore")));
        return result;
    }

    private List<String> normalizeSkills(List<String> skills) {
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String s : skills) {
            if (s == null) continue;
            String v = s.trim().toLowerCase();
            if (!v.isEmpty()) normalized.add(v);
        }
        return new ArrayList<>(normalized);
    }

    private int computeMatchScore(List<String> candidateSkills, List<String> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) return 0;
        if (candidateSkills == null || candidateSkills.isEmpty()) return 0;

        Set<String> candidateSet = new HashSet<>(candidateSkills);
        int overlap = 0;
        for (String js : jobSkills) {
            if (candidateSet.contains(js)) overlap++;
        }

        return (int) Math.round((overlap * 100.0) / jobSkills.size());
    }
}
