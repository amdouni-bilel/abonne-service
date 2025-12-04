package org.bibliotheque.abonneservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bibliotheque.abonneservice.event.AbonnePenaliteEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service consommateur Kafka pour les événements liés aux abonnés
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final AbonneService abonneService;

    /**
     * Écoute les événements de pénalité provenant du service d'emprunt
     */
    @KafkaListener(
            topics = "penalite-topic",
            groupId = "abonne-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePenaliteEvent(org.bibliotheque.abonneservice.event.AbonnePenaliteEvent event) {
        try {
            log.info("Réception d'un événement de pénalité pour l'abonné ID: {}, pénalité: {}",
                    event.getAbonneId(), event.getADesPenalites());

            abonneService.marquerPenalite(
                    event.getAbonneId(),
                    event.getADesPenalites(),
                    event.getRaison()
            );

            log.info("Événement de pénalité traité avec succès pour l'abonné ID: {}",
                    event.getAbonneId());
        } catch (Exception e) {
            log.error("Erreur lors du traitement de l'événement de pénalité pour l'abonné ID: {}",
                    event.getAbonneId(), e);
        }
    }

    /**
     * Écoute les demandes d'incrémentation d'abonnements consécutifs
     */
    @KafkaListener(
            topics = "increment-abonnement-topic",
            groupId = "abonne-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleIncrementAbonnement(Long abonneId) {
        try {
            log.info("Réception d'une demande d'incrémentation pour l'abonné ID: {}", abonneId);

            abonneService.incrementerAbonnementsConsecutifs(abonneId);

            log.info("Incrémentation traitée avec succès pour l'abonné ID: {}", abonneId);
        } catch (Exception e) {
            log.error("Erreur lors de l'incrémentation pour l'abonné ID: {}", abonneId, e);
        }
    }
}