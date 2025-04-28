package com.pfe.login.controller;

import com.pfe.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String nouveauMotDePasse = resetRequest.get("nouveauMotDePasse");
        String confirmationMotDePasse = resetRequest.get("confirmationMotDePasse");
        String identifiant = resetRequest.get("identifiant");

        // Vérification des champs requis
        if (identifiant == null || identifiant.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Identifiant est requis"));
        }

        // Vérification de la longueur du mot de passe
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Le mot de passe doit contenir au moins 8 caractères."));
        }

        // Vérification que les mots de passe correspondent
        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "La confirmation du mot de passe ne correspond pas."));
        }

        // Réinitialisation du mot de passe
        boolean success = userService.resetPassword(identifiant, nouveauMotDePasse);

        if (success) {
            return ResponseEntity.ok(Map.of("message", "Mot de passe changé avec succès !"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Utilisateur introuvable"));
        }
    }
}