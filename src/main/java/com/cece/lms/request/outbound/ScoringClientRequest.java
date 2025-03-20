package com.cece.lms.request.outbound;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoringClientRequest {
    private String url;
    private String name;
    private String username;
    private String password;
}
