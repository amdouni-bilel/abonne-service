package org.bibliotheque.abonneservice.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Événement de pénalité d'un abonné
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonnePenaliteEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long abonneId;
    private Boolean aDesPenalites;
    private String raison;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public AbonnePenaliteEvent(Long abonneId, Boolean aDesPenalites) {
        this.abonneId = abonneId;
        this.aDesPenalites = aDesPenalites;
        this.timestamp = LocalDateTime.now();
    }
}



