package com.project.ems.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    @Id
    @GeneratedValue
    @Column(name="employee_id")
    Integer eid;
    @NotNull
    String name;
    String designation;
    @Embedded
    Address address;
    Float salary;
    String timesheet;

}
