package com.project.ems.employee.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    String city;
    String state;
}
