package com.corcino.personbook.json;

import com.corcino.personbook.model.Person;
import lombok.Data;

@Data
public class PersonResponse {

    private Long personId;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;


    public PersonResponse(Person person) {
        this.personId = person.getPersonId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.gender = person.getGender();
    }

}
