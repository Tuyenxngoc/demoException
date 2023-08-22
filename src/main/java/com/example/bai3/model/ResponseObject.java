package com.example.bai3.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseObject {
    private HttpStatus status;
    private String message;
    private Object data;
}
