package org.bibliotheque.abonneservice.event;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonneStatutFideleEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long abonneId;
    private Boolean estFidele;
    private Integer nombreAbonnementsConsecutifs;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public AbonneStatutFideleEvent(Long abonneId, Boolean estFidele, Integer nombreAbonnementsConsecutifs) {
        this.abonneId = abonneId;
        this.estFidele = estFidele;
        this.nombreAbonnementsConsecutifs = nombreAbonnementsConsecutifs;
        this.timestamp = LocalDateTime.now();
    }
}
