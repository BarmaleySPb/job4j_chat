package ru.job4j.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
    private long id;
    private String name;
    private long creatorId;
}
