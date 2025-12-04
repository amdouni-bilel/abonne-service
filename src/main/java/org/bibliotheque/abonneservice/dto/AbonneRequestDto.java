package org.bibliotheque.abonneservice.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les requêtes de création/modification d'abonné
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonneRequestDto {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotNull(message = "L'âge est obligatoire")
    @Min(value = 12, message = "L'âge minimum est 12 ans")
    @Max(value = 120, message = "L'âge maximum est 120 ans")
    private Integer age;

    @NotBlank(message = "Le genre est obligatoire")
    @Pattern(regexp = "HOMME|FEMME|AUTRE", message = "Le genre doit être HOMME, FEMME ou AUTRE")
    private String genre;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{8,20}$", message = "Le numéro de téléphone doit être valide")
    private String numeroTelephone;
}
