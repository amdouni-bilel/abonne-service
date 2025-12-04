package org.bibliotheque.abonneservice.repository;


import org.bibliotheque.abonneservice.entity.Abonne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des abonnés
 */
@Repository
public interface AbonneRepository extends JpaRepository<Abonne, Long> {

    /**
     * Recherche un abonné par email
     */
    Optional<Abonne> findByEmail(String email);

    /**
     * Recherche un abonné par numéro de téléphone
     */
    Optional<Abonne> findByNumeroTelephone(String numeroTelephone);

    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un numéro de téléphone existe déjà
     */
    boolean existsByNumeroTelephone(String numeroTelephone);

    /**
     * Récupère tous les abonnés fidèles
     */
    List<Abonne> findByEstFideleTrue();

    /**
     * Récupère tous les abonnés avec des pénalités
     */
    List<Abonne> findByADesPenalitesTrue();

    /**
     * Récupère tous les abonnés actifs
     */
    List<Abonne> findByActifTrue();

    /**
     * Recherche des abonnés par nom ou prénom (insensible à la casse)
     */
    @Query("SELECT a FROM Abonne a WHERE " +
            "LOWER(a.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Abonne> searchByNomOrPrenom(@Param("keyword") String keyword);

    /**
     * Compte le nombre d'abonnés fidèles
     */
    long countByEstFideleTrue();

    /**
     * Récupère les abonnés par genre
     */
    List<Abonne> findByGenre(Abonne.Genre genre);
}
