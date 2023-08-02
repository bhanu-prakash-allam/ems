package com.project.ems.employee.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class EmployeeException {
    String message;

    public EmployeeException(String message) {
        this.message = message;
    }

    LocalDateTime timestamp;
    String status;
}
