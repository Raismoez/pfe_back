package com.pfe.login.controller;

import com.pfe.login.model.User;
import com.pfe.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String identifiant = loginRequest.get("identifiant");
        String password = loginRequest.get("password");

        // Validation des champs
        if (identifiant == null || identifiant.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Identifiant est requis"));
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Mot de passe est requis"));
        }

        // Recherche de l'utilisateur
        Optional<User> userOptional = userService.findByIdentifiant(identifiant);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilisateur introuvable"));
        }

        User user = userOptional.get();

        // Vérification du statut de l'utilisateur
        if (!user.isActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Accès refusé. Votre compte est inactif."));
        }

        // Vérification du mot de passe (avec BCrypt)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Identifiant ou mot de passe incorrect"));
        }

        // Réponse en cas de succès avec l'idRole inclus
        return ResponseEntity.ok().body(Map.of(
                "message", "Connexion réussie",
                "userId", user.getId().toString(),
                "idRole", user.getIdRole(),
                "user", Map.of(
                        "nomUtilisateur", user.getNomUtilisateur(),
                        "email", user.getEmail()
                )
        ));
    }
}