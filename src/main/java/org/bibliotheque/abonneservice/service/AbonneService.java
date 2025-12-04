package org.bibliotheque.abonneservice.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.bibliotheque.abonneservice.entity.Abonne;
import org.bibliotheque.abonneservice.event.AbonneCreeEvent;
import org.bibliotheque.abonneservice.event.AbonneStatutFideleEvent;
import org.bibliotheque.abonneservice.repository.AbonneRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de gestion des abonnés
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AbonneService {

    private final AbonneRepository abonneRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_ABONNE_CREE = "abonne-cree-topic";
    private static final String TOPIC_STATUT_FIDELE = "abonne-fidele-topic";

    /**
     * Crée un nouvel abonné
     */
    @Transactional
    public Abonne creerAbonne(Abonne abonne) {
        log.info("Création d'un nouvel abonné: {}", abonne.getEmail());

        // Vérifier si l'email existe déjà
        if (abonneRepository.existsByEmail(abonne.getEmail())) {
            throw new IllegalArgumentException("Un abonné avec cet email existe déjà");
        }

        // Vérifier si le numéro de téléphone existe déjà
        if (abonneRepository.existsByNumeroTelephone(abonne.getNumeroTelephone())) {
            throw new IllegalArgumentException("Un abonné avec ce numéro de téléphone existe déjà");
        }

        // Initialiser les valeurs par défaut
        if (abonne.getNombreAbonnementsConsecutifs() == null) {
            abonne.setNombreAbonnementsConsecutifs(0);
        }
        if (abonne.getADesPenalites() == null) {
            abonne.setADesPenalites(false);
        }
        if (abonne.getEstFidele() == null) {
            abonne.setEstFidele(false);
        }
        if (abonne.getActif() == null) {
            abonne.setActif(true);
        }

        Abonne saved = abonneRepository.save(abonne);
        log.info("Abonné créé avec succès, ID: {}", saved.getId());

        // Publier événement Kafka
        publierAbonneCree(saved);

        return saved;
    }

    /**
     * Récupère un abonné par son ID
     */
    @Transactional(readOnly = true)
    public Abonne getAbonne(Long id) {
        log.info("Récupération de l'abonné ID: {}", id);
        return findAbonneById(id);
    }

    /**
     * Récupère tous les abonnés
     */
    @Transactional(readOnly = true)
    public List<Abonne> getAllAbonnes() {
        log.info("Récupération de tous les abonnés");
        return abonneRepository.findAll();
    }

    /**
     * Récupère tous les abonnés actifs
     */
    @Transactional(readOnly = true)
    public List<Abonne> getAbonnesActifs() {
        log.info("Récupération des abonnés actifs");
        return abonneRepository.findByActifTrue();
    }

    /**
     * Récupère tous les abonnés fidèles
     */
    @Transactional(readOnly = true)
    public List<Abonne> getAbonnesFideles() {
        log.info("Récupération des abonnés fidèles");
        return abonneRepository.findByEstFideleTrue();
    }

    /**
     * Met à jour les informations d'un abonné
     */
    @Transactional
    public Abonne updateAbonne(Long id, Abonne abonneUpdate) {
        log.info("Mise à jour de l'abonné ID: {}", id);

        Abonne abonne = findAbonneById(id);

        // Vérifier l'unicité de l'email si modifié
        if (!abonne.getEmail().equals(abonneUpdate.getEmail()) &&
                abonneRepository.existsByEmail(abonneUpdate.getEmail())) {
            throw new IllegalArgumentException("Un abonné avec cet email existe déjà");
        }

        // Vérifier l'unicité du numéro de téléphone si modifié
        if (!abonne.getNumeroTelephone().equals(abonneUpdate.getNumeroTelephone()) &&
                abonneRepository.existsByNumeroTelephone(abonneUpdate.getNumeroTelephone())) {
            throw new IllegalArgumentException("Un abonné avec ce numéro de téléphone existe déjà");
        }

        // Mettre à jour les champs
        abonne.setNom(abonneUpdate.getNom());
        abonne.setPrenom(abonneUpdate.getPrenom());
        abonne.setEmail(abonneUpdate.getEmail());
        abonne.setAge(abonneUpdate.getAge());
        abonne.setGenre(abonneUpdate.getGenre());
        abonne.setNumeroTelephone(abonneUpdate.getNumeroTelephone());

        Abonne updated = abonneRepository.save(abonne);
        log.info("Abonné mis à jour avec succès, ID: {}", updated.getId());

        return updated;
    }

    /**
     * Marque ou retire une pénalité à un abonné
     */
    @Transactional
    public void marquerPenalite(Long abonneId, Boolean hasPenalty, String raison) {
        log.info("Marquage de pénalité pour l'abonné ID: {}, pénalité: {}", abonneId, hasPenalty);

        Abonne abonne = findAbonneById(abonneId);
        abonne.setADesPenalites(hasPenalty);

        // Si pénalité, réinitialiser les abonnements consécutifs
        if (hasPenalty) {
            abonne.setNombreAbonnementsConsecutifs(0);
        }

        abonneRepository.save(abonne);
        verifierEtMettreAJourStatutFidele(abonne);

        log.info("Pénalité marquée avec succès pour l'abonné ID: {}", abonneId);
    }

    /**
     * Incrémente le nombre d'abonnements consécutifs
     */
    @Transactional
    public void incrementerAbonnementsConsecutifs(Long abonneId) {
        log.info("Incrémentation des abonnements consécutifs pour l'abonné ID: {}", abonneId);

        Abonne abonne = findAbonneById(abonneId);
        abonne.setNombreAbonnementsConsecutifs(
                abonne.getNombreAbonnementsConsecutifs() + 1
        );

        abonneRepository.save(abonne);
        verifierEtMettreAJourStatutFidele(abonne);

        log.info("Abonnements consécutifs incrémentés: {}",
                abonne.getNombreAbonnementsConsecutifs());
    }

    /**
     * Désactive un abonné
     */
    @Transactional
    public void desactiverAbonne(Long id) {
        log.info("Désactivation de l'abonné ID: {}", id);

        Abonne abonne = findAbonneById(id);
        abonne.setActif(false);
        abonneRepository.save(abonne);

        log.info("Abonné désactivé avec succès, ID: {}", id);
    }

    /**
     * Recherche des abonnés par nom ou prénom
     */
    @Transactional(readOnly = true)
    public List<Abonne> searchAbonnes(String keyword) {
        log.info("Recherche d'abonnés avec le mot-clé: {}", keyword);
        return abonneRepository.searchByNomOrPrenom(keyword);
    }

    /**
     * Vérifie et met à jour le statut fidèle d'un abonné
     */
    private void verifierEtMettreAJourStatutFidele(Abonne abonne) {
        boolean estFidele = abonne.peutEtreFidele();

        if (estFidele != abonne.getEstFidele()) {
            abonne.setEstFidele(estFidele);
            abonneRepository.save(abonne);

            log.info("Statut fidèle mis à jour pour l'abonné ID: {}, nouveau statut: {}",
                    abonne.getId(), estFidele);

            // Publier événement Kafka
            publierChangementStatutFidele(abonne);
        }
    }

    /**
     * Trouve un abonné par ID ou lance une exception
     */
    private Abonne findAbonneById(Long id) {
        return abonneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Abonné non trouvé avec l'ID: " + id));
    }

    /**
     * Publie un événement de création d'abonné
     */
    private void publierAbonneCree(Abonne abonne) {
        AbonneCreeEvent event = AbonneCreeEvent.builder()
                .abonneId(abonne.getId())
                .email(abonne.getEmail())
                .nomComplet(abonne.getNomComplet())
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_ABONNE_CREE, event);
        log.info("Événement 'abonné créé' publié pour ID: {}", abonne.getId());
    }

    /**
     * Publie un événement de changement de statut fidèle
     */
    private void publierChangementStatutFidele(Abonne abonne) {
        AbonneStatutFideleEvent event = AbonneStatutFideleEvent.builder()
                .abonneId(abonne.getId())
                .estFidele(abonne.getEstFidele())
                .nombreAbonnementsConsecutifs(abonne.getNombreAbonnementsConsecutifs())
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_STATUT_FIDELE, event);
        log.info("Événement 'statut fidèle changé' publié pour ID: {}, statut: {}",
                abonne.getId(), abonne.getEstFidele());
    }
}