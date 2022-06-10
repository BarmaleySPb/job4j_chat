package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private int roleId;
}
