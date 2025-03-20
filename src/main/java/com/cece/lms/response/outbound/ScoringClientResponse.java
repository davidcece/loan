package com.cece.lms.response.outbound;

import lombok.Data;

@Data
public class ScoringClientResponse {
    private long id;
    private String url;
    private String username;
    private String password;
    private String token;
}
