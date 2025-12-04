package org.bibliotheque.abonneservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant un abonné de la bibliothèque
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abonne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Genre genre;

    @Column(name = "numero_telephone", nullable = false, unique = true, length = 20)
    private String numeroTelephone;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "nombre_abonnements_consecutifs", nullable = false)
    @Builder.Default
    private Integer nombreAbonnementsConsecutifs = 0;

    @Column(name = "a_des_penalites", nullable = false)
    @Builder.Default
    private Boolean aDesPenalites = false;

    @Column(name = "est_fidele", nullable = false)
    @Builder.Default
    private Boolean estFidele = false;

    @Column(name = "actif", nullable = false)
    @Builder.Default
    private Boolean actif = true;

    /**
     * Énumération pour le genre de l'abonné
     */
    public enum Genre {
        HOMME("Homme"),
        FEMME("Femme"),
        AUTRE("Autre");

        private final String libelle;

        Genre(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    /**
     * Méthode utilitaire pour obtenir le nom complet
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /**
     * Vérifie si l'abonné peut être considéré comme fidèle
     */
    public boolean peutEtreFidele() {
        return nombreAbonnementsConsecutifs >= 3 && !aDesPenalites;
    }
}