package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.dto.PersonDTO;
import ru.job4j.handler.Operation;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final ObjectMapper objectMapper;
    private final PersonService personService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    public PersonController(final PersonService personService, final RoleService roleService,
                            BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personService = personService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public Iterable<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        var person = this.personService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with id: " + id + " not found.")
        );
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        checkNull(person);
        this.personService.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.personService.delete(person);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        checkNull(person);
        checkPassword(person.getPassword());
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(roleService.findByName("user"));
        return new ResponseEntity<Person>(personService.save(person), HttpStatus.CREATED);
    }

    @PatchMapping("/")
    public ResponseEntity<Person> patch(@Valid @RequestBody PersonDTO personDTO) {
        if (personDTO.getId() < 1 || personDTO.getName() == null || personDTO.getPassword() == null
                || personDTO.getEmail() == null) {
            throw new NullPointerException("ID, userName, email, and password mustn't be empty");
        }
        checkPassword(personDTO.getPassword());
        Role role = roleService.findById(personDTO.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Role with id: " + personDTO.getRoleId() + " not found. Please check roleId."));
        Person person = Person.of(personDTO.getId(), personDTO.getName(),
                encoder.encode(personDTO.getPassword()), personDTO.getEmail(), role);
        return new ResponseEntity<>(personService.save(person), HttpStatus.OK);
    }

    private void checkNull(Person person) {
        if (person.getName() == null || person.getPassword() == null || person.getEmail() == null) {
            throw new NullPointerException("Username, email, and password mustn't be empty");
        }
    }

    private void checkPassword(String password) {
        if (!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()???\\[{}\\]:;',?%/*~$^+=<>]).{6,20}$")) {
            throw new IllegalArgumentException("Invalid password. Password length must be from five to twenty"
                    + " characters, and include uppercase/lowercase letters, numbers and special characters.");
        }
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}