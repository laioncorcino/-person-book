package com.corcino.personbook.api;

import com.corcino.personbook.json.PersonRequest;
import com.corcino.personbook.json.PersonResponse;
import com.corcino.personbook.model.Person;
import com.corcino.personbook.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonResponse>> getAll() {
        List<PersonResponse> persons = personService.getAllPersons();
        return ResponseEntity.ok().body(persons);
    }

    @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonResponse> getById(@PathVariable Long personId) {
        PersonResponse person = personService.getPersonById(personId);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PersonRequest personRequest, UriComponentsBuilder uriBuilder) throws Exception {
        Person person = personService.createPerson(personRequest);
        URI uri = uriBuilder.path("/v1/persons/{personId}").buildAndExpand(person.getPersonId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value ="/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonResponse> update(@RequestBody PersonRequest personRequest, @PathVariable Long personId) throws Exception {
        PersonResponse person = personService.updatePerson(personRequest, personId);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> delete(@PathVariable Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }

}
