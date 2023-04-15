package com.corcino.personbook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tb_person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String cpf;
    private String address;
    private String gender;

    public Person(String firstName, String lastName, String cpf, String address, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.address = address;
        this.gender = gender;
    }

}
