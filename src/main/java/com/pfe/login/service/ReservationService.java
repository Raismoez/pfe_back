package com.pfe.login.service;

import com.pfe.login.model.Reservation;
import com.pfe.login.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, JavaMailSender javaMailSender) {
        this.reservationRepository = reservationRepository;
        this.javaMailSender = javaMailSender;
    }

    // Méthode pour envoyer un e-mail de confirmation
    public void sendConfirmationEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("meriamdaadaa8@gmail.com");  // Adresse de l'expéditeur
            message.setTo("mariem.daadaa@esen.tn"); // Destinataire
            message.setSubject(subject);  // Sujet
            message.setText(body);  // Corps du message

            System.out.println("Tentative d'envoi d'un e-mail à: " + to + " avec le sujet: " + subject);
            javaMailSender.send(message);
            System.out.println("E-mail envoyé avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Créer une nouvelle réservation
    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus(Reservation.ReservationStatus.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }

    // Obtenir toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Obtenir une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Mettre à jour une réservation
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable avec l'ID: " + id));

        existingReservation.setNomDuProjet(reservationDetails.getNomDuProjet());
        existingReservation.setConstructeur(reservationDetails.getConstructeur());
        existingReservation.setCategorie(reservationDetails.getCategorie());
        existingReservation.setArticle(reservationDetails.getArticle());
        existingReservation.setDateReservation(reservationDetails.getDateReservation());
        existingReservation.setStatus(reservationDetails.getStatus());

        return reservationRepository.save(existingReservation);
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable avec l'ID: " + id));

        reservationRepository.delete(reservation);
    }


    // Confirmer une réservation et envoyer un e-mail
    public Reservation confirmReservation(Long id, String agentEmail) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable avec l'ID: " + id));

        reservation.setStatus(Reservation.ReservationStatus.CONFIRMEE);
        reservation = reservationRepository.save(reservation);

        String emailBody = "Bonjour,\n\n" +
                "Votre réservation pour le projet \"" + reservation.getNomDuProjet() + "\" a été confirmée.\n\n" +
                "Détails de la réservation :\n" +
                "- Projet : " + reservation.getNomDuProjet() + "\n" +
                "- Constructeur : " + reservation.getConstructeur() + "\n" +
                "- Catégorie : " + reservation.getCategorie() + "\n" +
                "- Article : " + reservation.getArticle() + "\n" +
                "- Date : " + reservation.getDateReservation() + "\n\n" +
                "Cordialement";

        sendConfirmationEmail(agentEmail, "Confirmation de réservation - " + reservation.getNomDuProjet(), emailBody);

        return reservation;
    }

    // Annuler une réservation et envoyer un e-mail
    public Reservation cancelReservation(Long id, String agentEmail) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable avec l'ID: " + id));

        reservation.setStatus(Reservation.ReservationStatus.ANNULEE);
        reservation = reservationRepository.save(reservation);

        // Créer un message d'e-mail détaillé pour l'annulation
        String emailBody = "Bonjour,\n\n" +
                "Votre réservation pour le projet \"" + reservation.getNomDuProjet() + "\" a été annulée.\n\n" +
                "Détails de la réservation :\n" +
                "- Projet : " + reservation.getNomDuProjet() + "\n" +
                "- Constructeur : " + reservation.getConstructeur() + "\n" +
                "- Catégorie : " + reservation.getCategorie() + "\n" +
                "- Article : " + reservation.getArticle() + "\n" +
                "- Date : " + reservation.getDateReservation() + "\n\n" +
                "Cordialement";

        sendConfirmationEmail(agentEmail, "Annulation de réservation - " + reservation.getNomDuProjet(), emailBody);

        return reservation;
    }



    // Trouver les réservations par statut
    public List<Reservation> getReservationsByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
}