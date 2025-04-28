package com.pfe.login.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifiant;

    @Column(name = "password")
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "nom_utilisateur")
    private String nomUtilisateur;

    @Column(name = "id_role", nullable = false)
    private Integer idRole;

    @Column(name = "statut")
    private String statut;
    @Column(name = "avatarUrl")
    private String avatarUrl;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifiant='" + identifiant + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", idRole=" + idRole +
                ", statut='" + statut + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
    public boolean isActif() {
        return "Actif".equals(this.statut);
    }
}