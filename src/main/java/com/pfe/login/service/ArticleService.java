package com.pfe.login.service;

import com.pfe.login.model.Article;
import com.pfe.login.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public List<Article> getArticlesByConstructeur(String constructeur) {
        return articleRepository.findByConstructeur(constructeur);
    }

    public List<Article> getArticlesByCategorie(String categorie) {
        return articleRepository.findByCategorie(categorie);
    }

    public List<Article> getOutOfStockArticles() {
        return articleRepository.findByQuantiteLessThanEqual(0);
    }

    public List<Article> getLowStockArticles() {
        return articleRepository.findByQuantiteGreaterThanAndQuantiteLessThanEqual(0, 5);
    }

    public List<Article> getEndOfSaleArticles() {
        LocalDate today = LocalDate.now();
        return articleRepository.findByEndOfSaleBeforeAndEndOfSaleNotNull(today);
    }

    public List<Article> getEndOfSupportArticles() {
        LocalDate today = LocalDate.now();
        return articleRepository.findByEndOfSupportBeforeAndEndOfSupportNotNull(today);
    }

    public void checkAndUpdateArticleStatus() {

        System.out.println("Vérification des stocks effectuée.");
    }
}