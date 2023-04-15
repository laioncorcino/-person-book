package com.corcino.personbook.json;

import com.corcino.personbook.model.Person;
import lombok.Data;

@Data
public class PersonRequest {

    private String firstName;
    private String lastName;
    private String cpf;
    private String address;
    private String gender;

    public Person toModel() {
        return new Person(firstName, lastName, cpf, address, gender);
    }

}
