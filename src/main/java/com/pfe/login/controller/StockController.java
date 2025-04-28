package com.pfe.login.controller;

import com.pfe.login.model.Article;
import com.pfe.login.repository.ArticleRepository;
import com.pfe.login.service.ArticleService;
import com.pfe.login.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final ArticleService articleService;
    private final NotificationService notificationService;
    private final ArticleRepository articleRepository;

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/articles/constructeur/{constructeur}")
    public ResponseEntity<List<Article>> getArticlesByConstructeur(@PathVariable String constructeur) {
        return ResponseEntity.ok(articleService.getArticlesByConstructeur(constructeur));
    }

    @GetMapping("/articles/categorie/{categorie}")
    public ResponseEntity<List<Article>> getArticlesByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(articleService.getArticlesByCategorie(categorie));
    }

    @GetMapping("/articles/out-of-stock")
    public ResponseEntity<List<Article>> getOutOfStockArticles() {
        return ResponseEntity.ok(articleService.getOutOfStockArticles());
    }

    @GetMapping("/articles/low-stock")
    public ResponseEntity<List<Article>> getLowStockArticles() {
        return ResponseEntity.ok(articleService.getLowStockArticles());
    }

    @GetMapping("/articles/end-of-sale")
    public ResponseEntity<List<Article>> getEndOfSaleArticles() {
        return ResponseEntity.ok(articleService.getEndOfSaleArticles());
    }

    @GetMapping("/articles/end-of-support")
    public ResponseEntity<List<Article>> getEndOfSupportArticles() {
        return ResponseEntity.ok(articleService.getEndOfSupportArticles());
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody Article article) {
        return ResponseEntity.ok(articleService.saveArticle(article));
    }
    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        // Vérifier si l'article existe
        if (!articleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        article.setId(id); // S'assurer que l'ID est défini correctement
        Article updatedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        // Vérifier si l'article existe
        if (!articleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        articleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/send-email")
    public ResponseEntity<String> sendEmail() {
        // Récupérer juste les articles en rupture de stock
        List<Article> outOfStockArticles = articleService.getOutOfStockArticles();

        String emailContent = "";
        if (!outOfStockArticles.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("ARTICLES EN RUPTURE DE STOCK:\n");
            for (Article a : outOfStockArticles) {
                sb.append("- ").append(a.getArticle()).append(" (").append(a.getConstructeur()).append(")\n");
            }
            emailContent = sb.toString();
        }

        // Envoyer le mail
        notificationService.sendEmail(
                "raismoez7@gmail.com",
                "Rapport de stock",
                emailContent.isEmpty() ? "Aucun article en rupture de stock." : emailContent
        );

        return ResponseEntity.ok("E-mail  envoyé !");
    }
}