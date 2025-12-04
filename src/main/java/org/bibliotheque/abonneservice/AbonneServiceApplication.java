package org.bibliotheque.abonneservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AbonneServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbonneServiceApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Abonné Service démarré avec succès!");
        System.out.println("Port: 8081");
        System.out.println("Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("===========================================");
    }

}
