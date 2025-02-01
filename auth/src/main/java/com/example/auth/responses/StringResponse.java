package com.example.auth.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringResponse {
    private String message;

    public StringResponse(String message) {
        this.message = message;
    }

}
