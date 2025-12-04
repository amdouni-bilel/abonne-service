package org.bibliotheque.abonneservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour les réponses contenant les informations d'un abonné
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonneResponseDto {

    private Long id;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String email;
    private Integer age;
    private String genre;
    private String numeroTelephone;
    private Integer nombreAbonnementsConsecutifs;
    private Boolean aDesPenalites;
    private Boolean estFidele;
    private Boolean actif;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModification;
}
