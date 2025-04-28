package com.pfe.login.controller;

import com.pfe.login.model.Offre;
import com.pfe.login.service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*") // Pour permettre les requêtes cross-origin depuis votre frontend Angular
public class OffreController {

    private final OffreService offreService;

    @Autowired
    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    @GetMapping
    public ResponseEntity<List<Offre>> getAllOffers() {
        List<Offre> offers = offreService.getAllOffers();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOfferById(@PathVariable Long id) {
        return offreService.getOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/search")
    public ResponseEntity<List<Offre>> searchOffers(@RequestParam String query) {
        List<Offre> offers = offreService.searchOffers(query);
        return ResponseEntity.ok(offers);
    }

    @PostMapping
    public ResponseEntity<Offre> createOffer(@RequestBody Offre offer) {
        Offre createdOffer = offreService.createOffer(offer);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offre> updateOffer(@PathVariable Long id, @RequestBody Offre offerDetails) {
        return offreService.updateOffer(id, offerDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOffer(@PathVariable Long id) {
        boolean deleted = offreService.deleteOffer(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", deleted);

        if (deleted) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}