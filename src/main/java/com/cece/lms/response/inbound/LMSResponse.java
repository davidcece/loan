package com.cece.lms.response.inbound;

import lombok.Data;

@Data
public class LMSResponse<T> {
    private String message;
    private boolean success;
    private T body;

    public LMSResponse(T body) {
        this.message = "Success";
        this.success = true;
        this.body = body;
    }

    public LMSResponse(Exception e){
        this.message = e.getMessage();
        this.success = false;
    }
}
