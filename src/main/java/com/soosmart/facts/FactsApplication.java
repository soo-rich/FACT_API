package com.soosmart.facts;

import com.soosmart.facts.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
public class FactsApplication implements CommandLineRunner {

    private UtilisateurService utilisateurService;

    public static void main(String[] args) {
        SpringApplication.run(FactsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            utilisateurService.createSuprerAdmin(
                    "admin@admin.com",
                    "admin",
                    "admin"
            );
        } catch (Exception e) {
            System.out.println("Super admin already exists");
        }
    }
}
