package com.pfe.login.repository;

import com.pfe.login.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {
    // Méthode existante pour la recherche
    @Query("SELECT o FROM Offre o WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(o.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Offre> searchOffers(@Param("query") String query);

    // Nouvelle méthode pour filtrer par type d'offre
    List<Offre> findByOffreType(String offreType);
}