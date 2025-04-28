package com.pfe.login.service;

import com.pfe.login.model.User;
import com.pfe.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Méthodes existantes
    public Optional<User> findByIdentifiant(String identifiant) {
        return userRepository.findByIdentifiant(identifiant);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String query) {
        String queryLowerCase = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(user ->
                        (user.getNomUtilisateur() != null && user.getNomUtilisateur().toLowerCase().contains(queryLowerCase)) ||
                                (user.getEmail() != null && user.getEmail().toLowerCase().contains(queryLowerCase)))
                .collect(Collectors.toList());
    }

    public boolean resetPassword(String identifiant, String nouveauMotDePasse) {
        Optional<User> userOptional = findByIdentifiant(identifiant);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(nouveauMotDePasse));
            saveUser(user);
            return true;
        }
        return false;
    }

    // Nouvelles méthodes pour gérer le statut

    // Activer un utilisateur
    public boolean activerUtilisateur(Long userId) {
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatut("ACTIF");
            saveUser(user);
            return true;
        }
        return false;
    }

    // Désactiver un utilisateur
    public boolean desactiverUtilisateur(Long userId) {
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatut("INACTIF");
            saveUser(user);
            return true;
        }
        return false;
    }

    // Vérifier si un utilisateur est actif
    public boolean isUtilisateurActif(Long userId) {
        Optional<User> userOptional = findUserById(userId);
        return userOptional.map(User::isActif).orElse(false);
    }

    // Changer le statut d'un utilisateur
    public boolean changerStatutUtilisateur(Long userId, String nouveauStatut) {
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatut(nouveauStatut);
            saveUser(user);
            return true;
        }
        return false;
    }

    // Récupérer tous les utilisateurs actifs
    public List<User> findAllUsersActifs() {
        return userRepository.findAll().stream()
                .filter(User::isActif)
                .collect(Collectors.toList());
    }

    // Récupérer tous les utilisateurs inactifs
    public List<User> findAllUsersInactifs() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isActif())
                .collect(Collectors.toList());
    }
}