package ru.kata.spring.boot_security.demo.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ToString.Exclude
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "height")
    private double height;

    @Column(name = "weight")
    private double weight;


    public User(String name, String surname, int birthYear, double height, double weight) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.height = height;
        this.weight = weight;
    }
}
