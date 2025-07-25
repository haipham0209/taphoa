package com.example.demo.dto;

public class ErrorResponseDto {
    private String message;
    private int status;

    public ErrorResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
    public ErrorResponseDto() {

    }

    // Getters và setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
