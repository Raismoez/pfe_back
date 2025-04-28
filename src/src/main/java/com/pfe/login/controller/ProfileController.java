package com.pfe.login.controller;

import com.pfe.login.model.User;
import com.pfe.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Définir le répertoire de stockage des images
    private final String UPLOAD_DIR = "uploads/avatars/";

    @GetMapping("/{identifiant}")
    public ResponseEntity<?> getUserProfile(@PathVariable String identifiant) {
        Optional<User> userOptional = userService.findByIdentifiant(identifiant);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Utilisateur introuvable"));
        }

        User user = userOptional.get();
        // Ne pas inclure le mot de passe dans la réponse
        return ResponseEntity.ok(Map.of(
                "identifiant", user.getIdentifiant(),
                "email", user.getEmail(),
                "nomUtilisateur", user.getNomUtilisateur(),
                "idRole", user.getIdRole(),
                "statut", user.getStatut(),
                "avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : ""
        ));
    }

    @PutMapping("/update/{identifiant}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String identifiant, @RequestBody Map<String, String> profileRequest) {
        Optional<User> userOptional = userService.findByIdentifiant(identifiant);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Utilisateur introuvable"));
        }

        User user = userOptional.get();

        // Mise à jour des champs si présents dans la requête
        if (profileRequest.containsKey("email")) {
            user.setEmail(profileRequest.get("email"));
        }

        if (profileRequest.containsKey("nomUtilisateur")) {
            user.setNomUtilisateur(profileRequest.get("nomUtilisateur"));
        }

        // Mise à jour de l'avatar si présent
        if (profileRequest.containsKey("avatarUrl")) {
            user.setAvatarUrl(profileRequest.get("avatarUrl"));
        }

        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
    }

    @PostMapping("/upload-avatar/{identifiant}")
    public ResponseEntity<?> uploadAvatar(@PathVariable String identifiant, @RequestParam("avatar") MultipartFile file) {
        try {
            Optional<User> userOptional = userService.findByIdentifiant(identifiant);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Utilisateur introuvable"));
            }

            User user = userOptional.get();

            // Créer le répertoire s'il n'existe pas
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom de fichier unique pour éviter les conflits
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Chemin complet du fichier
            Path filePath = uploadPath.resolve(newFilename);



            // Sauvegarder le fichier
            Files.copy(file.getInputStream(), filePath);

            // Mettre à jour l'URL de l'avatar dans la base de données
            String avatarUrl = "/api/profile/avatars/" + newFilename;
            user.setAvatarUrl(avatarUrl);
            userService.saveUser(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Avatar mis à jour avec succès",
                    "avatarUrl", avatarUrl
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de l'enregistrement de l'avatar: " + e.getMessage()));
        }
    }

    // Endpoint pour récupérer les images d'avatar
    @GetMapping("/avatars/{filename}")
    public ResponseEntity<?> getAvatar(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            byte[] imageBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Type", Files.probeContentType(filePath))
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Avatar introuvable"));
        }
    }

    @PostMapping("/change-password/{identifiant}")
    public ResponseEntity<?> changePassword(@PathVariable String identifiant, @RequestBody Map<String, String> passwordRequest) {
        String currentPassword = passwordRequest.get("currentPassword");
        String newPassword = passwordRequest.get("newPassword");
        String confirmPassword = passwordRequest.get("confirmPassword");

        // Vérification des champs requis
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Mot de passe actuel est requis"));
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Nouveau mot de passe est requis"));
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Confirmation du mot de passe est requise"));
        }

        // Vérification que les deux nouveaux mots de passe correspondent
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Les mots de passe ne correspondent pas"));
        }

        // Recherche de l'utilisateur
        Optional<User> userOptional = userService.findByIdentifiant(identifiant);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Utilisateur introuvable"));
        }

        User user = userOptional.get();

        // Vérification du mot de passe actuel avec passwordEncoder
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Mot de passe actuel incorrect"));
        }

        // Mise à jour du mot de passe avec encodage
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }
}