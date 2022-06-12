package ru.job4j.domain;

import lombok.Getter;
import lombok.Setter;
import ru.job4j.handler.Operation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private long id;
    @NotBlank(message = "Name must be not empty")
    private String name;
    @Email(message = "Email is not valid")
    @Pattern(regexp = ".+@.+\\..+", message = "Email is not valid")
    private String email;
    @Size(min = 6, message = "Invalid password. Password length must be more than 5 characters")
    private String password;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    public Person() {

    }

    public static Person of(int id, String name, String password, String email, Role role) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setRole(role);
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
