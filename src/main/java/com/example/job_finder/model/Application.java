package com.example.job_finder.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    private String id;

    private String jobId;
    private String userId;
}
