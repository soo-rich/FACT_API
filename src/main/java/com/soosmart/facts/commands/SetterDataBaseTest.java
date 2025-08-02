package com.soosmart.facts.commands;

import com.soosmart.facts.entity.Article;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.repository.ClientDAO;
import com.soosmart.facts.repository.ProjetDAO;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class SetterDataBaseTest {
    private final ArticleDAO articleDAO;
    private final ClientDAO clientDAO;
    private final ProjetDAO projetDAO;

    @ShellMethod(key = "db-article", value = "ajout des valeur de test article")
    public String articledatabaseadd(int range) {
        for (int i = 0; i < range; i++) {
            this.articleDAO.save(Article.builder()
                    .libelle("Article" + i)
                    .prix_unitaire(1000f *i)
                    .build());
            System.out.println("Save Article "+i);
        }
        return "Save Article";
    }

    @ShellMethod(key = "db-projet", value = "ajout des valeur de test projet")
    public String projetdatabaseadd(int range) {
        for (int i = 0; i < range; i++) {
            Client client = this.clientDAO.save(Client.builder()
                    .lieu("Lieu_" + i)
                    .nom("Nom_" + i)
                    .sigle("Sigle_" + i)
                    .telephone("90 00 00 0" + i)
                    .potentiel(i >= 5 && i <= 10)
                    .build());
            this.projetDAO.save(
                    Projet.builder()
                            .client(client)
                            .description("Description_" + i)
                            .offre(i >= 5 && i <= 10)
                            .projetType("Type_" + i)
                            .build()
            );
            System.out.println("Save Client et Projet "+i);
        }
        return "Save Projet et Client";
    }


}
