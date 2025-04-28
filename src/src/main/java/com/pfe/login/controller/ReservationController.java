package com.pfe.login.controller;

import com.pfe.login.model.Reservation;
import com.pfe.login.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200") // Pour Angular
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Créer une nouvelle réservation
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    // Obtenir toutes les réservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Mettre à jour une réservation
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservation);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // confirmer une réservation
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirmReservation(
            @PathVariable Long id,
            @RequestParam String email) {
        Reservation confirmedReservation = reservationService.confirmReservation(id, email);
        return new ResponseEntity<>(confirmedReservation, HttpStatus.OK);
    }
    // Annuler une réservation
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Reservation canceledReservation = reservationService.cancelReservation(id);
        return new ResponseEntity<>(canceledReservation, HttpStatus.OK);
    }

    // Obtenir les réservations par statut
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(
            @PathVariable Reservation.ReservationStatus status) {
        List<Reservation> reservations = reservationService.getReservationsByStatus(status);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir les réservations par constructeur
    @GetMapping("/constructeur/{constructeur}")
    public ResponseEntity<List<Reservation>> getReservationsByConstructeur(
            @PathVariable String constructeur) {
        List<Reservation> reservations = reservationService.getReservationsByConstructeur(constructeur);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir les réservations par catégorie
    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<Reservation>> getReservationsByCategorie(
            @PathVariable String categorie) {
        List<Reservation> reservations = reservationService.getReservationsByCategorie(categorie);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
