package com.soosmart.facts.commands;

import com.soosmart.facts.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class SuperAdminCommand {

    private final UtilisateurService utilisateurService;

    
    //commande recuprer l'email et username et password afin de creer un super admin
    @ShellMethod(key = "super-admin", value="Creer un super admin") //commande pour creer un super admin
    public String superAdmin(String email, String username, String password) {
        this.utilisateurService.createSuprerAdmin(email, username, password);
        return "Super admin cree avec succes";
    }
}
