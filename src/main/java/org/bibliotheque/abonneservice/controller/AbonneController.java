package org.bibliotheque.abonneservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bibliotheque.abonneservice.entity.Abonne;
import org.bibliotheque.abonneservice.service.AbonneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des abonnés
 */
@RestController
@RequestMapping("/api/abonnes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Abonnés", description = "API de gestion des abonnés de la bibliothèque")
public class AbonneController {

    private final AbonneService abonneService;

    @Operation(summary = "Créer un nouvel abonné",
            description = "Crée un nouvel abonné dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Abonné créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Email ou téléphone déjà existant")
    })
    @PostMapping
    public ResponseEntity<Abonne> creerAbonne(@Valid @RequestBody Abonne abonne) {
        log.info("Requête de création d'abonné reçue: {}", abonne.getEmail());
        Abonne created = abonneService.creerAbonne(abonne);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Obtenir un abonné par ID",
            description = "Récupère les informations d'un abonné spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abonné trouvé"),
            @ApiResponse(responseCode = "404", description = "Abonné non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Abonne> getAbonne(
            @Parameter(description = "ID de l'abonné")
            @PathVariable Long id) {
        log.info("Requête de récupération de l'abonné ID: {}", id);
        Abonne abonne = abonneService.getAbonne(id);
        return ResponseEntity.ok(abonne);
    }

    @Operation(summary = "Lister tous les abonnés",
            description = "Récupère la liste de tous les abonnés")
    @GetMapping
    public ResponseEntity<List<Abonne>> getAllAbonnes() {
        log.info("Requête de récupération de tous les abonnés");
        List<Abonne> abonnes = abonneService.getAllAbonnes();
        return ResponseEntity.ok(abonnes);
    }

    @Operation(summary = "Lister les abonnés actifs",
            description = "Récupère la liste des abonnés actifs uniquement")
    @GetMapping("/actifs")
    public ResponseEntity<List<Abonne>> getAbonnesActifs() {
        log.info("Requête de récupération des abonnés actifs");
        List<Abonne> abonnes = abonneService.getAbonnesActifs();
        return ResponseEntity.ok(abonnes);
    }

    @Operation(summary = "Lister les abonnés fidèles",
            description = "Récupère la liste des abonnés ayant le statut fidèle")
    @GetMapping("/fideles")
    public ResponseEntity<List<Abonne>> getAbonnesFideles() {
        log.info("Requête de récupération des abonnés fidèles");
        List<Abonne> abonnes = abonneService.getAbonnesFideles();
        return ResponseEntity.ok(abonnes);
    }

    @Operation(summary = "Mettre à jour un abonné",
            description = "Met à jour les informations d'un abonné existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abonné mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Abonné non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Abonne> updateAbonne(
            @PathVariable Long id,
            @Valid @RequestBody Abonne abonne) {
        log.info("Requête de mise à jour de l'abonné ID: {}", id);
        Abonne updated = abonneService.updateAbonne(id, abonne);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Marquer une pénalité",
            description = "Marque ou retire une pénalité à un abonné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalité mise à jour"),
            @ApiResponse(responseCode = "404", description = "Abonné non trouvé")
    })
    @PutMapping("/{id}/penalite")
    public ResponseEntity<Map<String, String>> marquerPenalite(
            @PathVariable Long id,
            @RequestParam Boolean hasPenalty,
            @RequestParam(required = false, defaultValue = "") String raison) {
        log.info("Requête de marquage de pénalité pour l'abonné ID: {}, pénalité: {}",
                id, hasPenalty);

        abonneService.marquerPenalite(id, hasPenalty, raison);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pénalité mise à jour avec succès");
        response.put("abonneId", id.toString());
        response.put("hasPenalty", hasPenalty.toString());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Désactiver un abonné",
            description = "Désactive un abonné dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abonné désactivé"),
            @ApiResponse(responseCode = "404", description = "Abonné non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> desactiverAbonne(@PathVariable Long id) {
        log.info("Requête de désactivation de l'abonné ID: {}", id);
        abonneService.desactiverAbonne(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Abonné désactivé avec succès");
        response.put("abonneId", id.toString());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechercher des abonnés",
            description = "Recherche des abonnés par nom ou prénom")
    @GetMapping("/search")
    public ResponseEntity<List<Abonne>> searchAbonnes(
            @Parameter(description = "Mot-clé de recherche")
            @RequestParam String keyword) {
        log.info("Requête de recherche d'abonnés avec le mot-clé: {}", keyword);
        List<Abonne> abonnes = abonneService.searchAbonnes(keyword);
        return ResponseEntity.ok(abonnes);
    }

    @Operation(summary = "Health check",
            description = "Vérifie que le service est opérationnel")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Abonné Service");
        response.put("database", "MySQL");
        return ResponseEntity.ok(response);
    }
}