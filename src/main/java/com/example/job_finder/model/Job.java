package com.example.job_finder.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    private String id;

    private String title;
    private String description;
    private String location;
    private double lat;
    private double lng;
    private String salary;
    private String recruiterId;
    private List<String> skills;
}
