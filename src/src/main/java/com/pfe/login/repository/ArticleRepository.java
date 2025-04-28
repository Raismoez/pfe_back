package com.pfe.login.repository;

import com.pfe.login.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByConstructeur(String constructeur);
    List<Article> findByCategorie(String categorie);
    List<Article> findByQuantiteLessThanEqual(int quantity);
    List<Article> findByQuantiteGreaterThanAndQuantiteLessThanEqual(int min, int max);
    List<Article> findByEndOfSaleBeforeAndEndOfSaleNotNull(LocalDate date);
    List<Article> findByEndOfSupportBeforeAndEndOfSupportNotNull(LocalDate date);
}