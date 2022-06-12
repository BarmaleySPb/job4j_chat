package ru.job4j.domain;

import lombok.Getter;
import lombok.Setter;
import ru.job4j.handler.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private long id;
    @NotBlank(message = "Room name must be not empty")
    private String name;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person creator;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "rooms_persons",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private Set<Person> persons = new HashSet<>();
    @OneToMany
    private Set<Message> messages = new HashSet<>();

    public Room() {

    }

    public static Room of(long id, String name, Person creator) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCreator(creator);
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Room{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", persons=" + persons
                + ", messages=" + messages
                + '}';
    }
}
