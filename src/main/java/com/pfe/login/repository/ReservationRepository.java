package com.pfe.login.repository;

import com.pfe.login.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Trouver les réservations par nom de projet
    List<Reservation> findByNomDuProjetContainingIgnoreCase(String nomDuProjet);

    // Trouver les réservations par constructeur
    List<Reservation> findByConstructeurIgnoreCase(String constructeur);

    // Trouver les réservations par catégorie
    List<Reservation> findByCategorieIgnoreCase(String categorie);

    // Trouver les réservations par article
    List<Reservation> findByArticleContainingIgnoreCase(String article);

    // Trouver les réservations par date de réservation
    List<Reservation> findByDateReservation(LocalDate dateReservation);

    // Trouver les réservations par statut
    List<Reservation> findByStatus(Reservation.ReservationStatus status);

    // Trouver les réservations entre deux dates
    List<Reservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
}


