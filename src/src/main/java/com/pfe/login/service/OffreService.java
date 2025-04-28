package com.pfe.login.service;

import com.pfe.login.model.Offre;
import com.pfe.login.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OffreService {

    private final OffreRepository offreRepository;

    @Autowired
    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    public List<Offre> getAllOffers() {
        return offreRepository.findAll();
    }

    public Optional<Offre> getOfferById(Long id) {
        return offreRepository.findById(id);
    }

    public List<Offre> searchOffers(String query) {
        return offreRepository.searchOffers(query);
    }

    @Transactional
    public Offre createOffer(Offre offer) {
        return offreRepository.save(offer);
    }

    @Transactional
    public Optional<Offre> updateOffer(Long id, Offre offerDetails) {
        return offreRepository.findById(id)
                .map(existingOffer -> {
                    existingOffer.setTitle(offerDetails.getTitle());
                    existingOffer.setDescription(offerDetails.getDescription());
                    existingOffer.setImageUrl(offerDetails.getImageUrl());
                    existingOffer.setDetailsDescription(offerDetails.getDetailsDescription());
                    existingOffer.setFeatures(offerDetails.getFeatures());
                    existingOffer.setObjectives(offerDetails.getObjectives());
                    existingOffer.setPrix(offerDetails.getPrix());
                    existingOffer.setPaymentOptions(offerDetails.getPaymentOptions());
                    existingOffer.setSubscriptionChannels(offerDetails.getSubscriptionChannels());
                    return offreRepository.save(existingOffer);
                });
    }

    @Transactional
    public boolean deleteOffer(Long id) {
        if (offreRepository.existsById(id)) {
            offreRepository.deleteById(id);
            return true;
        }
        return false;
    }
}