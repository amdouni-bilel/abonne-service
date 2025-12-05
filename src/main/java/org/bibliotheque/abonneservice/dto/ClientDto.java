package org.bibliotheque.abonneservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
        private String firstname;
        private String lastname;
        private String phone;
        private String email;


}
