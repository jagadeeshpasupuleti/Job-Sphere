package com.example.job_finder.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "profile_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSkills {

    @Id
    private String id;

    private String userId;
    private List<String> skills;
}
