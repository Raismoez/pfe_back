package com.pfe.login.model;



import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_du_projet", nullable = false)
    private String nomDuProjet;

    @Column(nullable = false)
    private String constructeur;

    @Column(nullable = false)
    private String categorie;

    @Column(nullable = false)
    private String article;

    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    // On pourrait ajouter une relation avec Stock si nécessaire
    // @ManyToOne
    // @JoinColumn(name = "stock_id")
    // private Stock stock;

    // Statut de la réservation (en attente, confirmée, annulée)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.EN_ATTENTE;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDuProjet() {
        return nomDuProjet;
    }

    public void setNomDuProjet(String nomDuProjet) {
        this.nomDuProjet = nomDuProjet;
    }

    public String getConstructeur() {
        return constructeur;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    // Enum pour les statuts possibles
    public enum ReservationStatus {
        EN_ATTENTE,
        CONFIRMEE,
        ANNULEE
    }
}