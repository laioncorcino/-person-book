package com.corcino.personbook.service;

import com.corcino.personbook.error.exception.BadRequestException;
import com.corcino.personbook.error.exception.NotFoundException;
import com.corcino.personbook.json.PersonRequest;
import com.corcino.personbook.json.PersonResponse;
import com.corcino.personbook.model.Person;
import com.corcino.personbook.repository.PersonRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .map(PersonResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long personId) {
        Person person = getById(personId);
        return new PersonResponse(person);
    }

    private Person getById(Long personId) {
        log.info("Buscando pessoa de id {}", personId);
        Optional<Person> person = personRepository.findById(personId);

        return person.orElseThrow(() -> {
            log.error("pessoa de id {} nao encontrado", personId);
            return new NotFoundException("pessoa nao encontrado");
        });
    }

    @Transactional
    public Person createPerson(PersonRequest personRequest) throws Exception {
        Person person = personRequest.toModel();
        log.info("Salvando pessoa");
        return savePerson(person);
    }

    private Person savePerson(Person person) throws Exception {
        try {
            return personRepository.save(person);
        }
        catch (DataIntegrityViolationException e) {
            log.error("pessoa com cpf ja existente");
            throw new BadRequestException("pessoa com cpf ja existente");
        }
        catch (Exception | Error e) {
            log.error("Erro ao salvar pessoa", e.getCause());
            throw new Exception(e.getCause());
        }
    }

    public PersonResponse updatePerson(PersonRequest personRequest, Long personId) throws Exception {
        Person person = getById(personId);

        if (StringUtils.isNotBlank(personRequest.getFirstName())) {
            person.setFirstName(personRequest.getFirstName());
        }

        if (StringUtils.isNotBlank(personRequest.getLastName())) {
            person.setLastName(personRequest.getLastName());
        }

        if (StringUtils.isNotBlank(personRequest.getAddress())) {
            person.setAddress(personRequest.getAddress());
        }

        if (StringUtils.isNotBlank(personRequest.getGender())) {
            person.setGender(personRequest.getGender());
        }

        log.info("Atualizando pessoa " + person.getPersonId());

        Person savedperson = savePerson(person);
        return new PersonResponse(savedperson);
    }

    @Transactional
    public void deletePerson(Long personId) {
        getById(personId);
        log.info("Deletando pessoa de id {}", personId);
        personRepository.deleteById(personId);
    }

}
