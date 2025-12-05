package org.bibliotheque.abonneservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← Ajoutez ceci !
    private Long id;

    private String firstname;
    private String lastname;
    private String phone;
    private String email;
}