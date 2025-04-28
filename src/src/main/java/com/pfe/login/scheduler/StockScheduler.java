package com.pfe.login.scheduler;

import com.pfe.login.model.Article;
import com.pfe.login.service.ArticleService;
import com.pfe.login.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockScheduler {
    private final ArticleService articleService;
    private final NotificationService notificationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Scheduled(cron = "0 0 8 * * *")
    public void checkStockLevels() {
        System.out.println("La tâche de vérification des stocks a démarré !");

        // Récupérer tous les articles à vérifier
        List<Article> outOfStockArticles = articleService.getOutOfStockArticles();
        List<Article> lowStockArticles = articleService.getLowStockArticles();
        List<Article> endOfSaleArticles = articleService.getEndOfSaleArticles();
        List<Article> endOfSupportArticles = articleService.getEndOfSupportArticles();

        // Vérifier s'il y a des articles à signaler
        boolean hasArticlesToReport = !outOfStockArticles.isEmpty() || !lowStockArticles.isEmpty() ||
                !endOfSaleArticles.isEmpty() || !endOfSupportArticles.isEmpty();

        if (hasArticlesToReport) {
            // Créer le contenu de l'email
            String emailContent = createSimpleHtmlEmail(outOfStockArticles, lowStockArticles,
                    endOfSaleArticles, endOfSupportArticles);

            // Envoyer l'email
            notificationService.sendEmail(
                    "raismoez7@gmail.com,meriamdaadaa8@gmail.com",
                    "Rapport de Stock  " ,
                    emailContent
            );
            System.out.println("Email de rapport envoyé");
        } else {
            System.out.println("Aucun problème de stock à signaler");
        }
    }

    // Méthode simple pour créer un email HTML basique mais attrayant
    private String createSimpleHtmlEmail(List<Article> outOfStock, List<Article> lowStock,
                                         List<Article> endOfSale, List<Article> endOfSupport) {
        StringBuilder html = new StringBuilder();

        // Style et en-tête simples
        html.append("<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto;'>");
        html.append("<h1 style='color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px;'>")
                .append("Rapport de Stock")
                .append("</h1>");

        // Résumé simple
        html.append("<div style='background-color: #f8f9fa; padding: 15px; margin-bottom: 20px; border-radius: 5px;'>");
        html.append("<p><strong>Résumé:</strong></p>");
        html.append("<ul>");
        html.append("<li>Articles en rupture: <strong>").append(outOfStock.size()).append("</strong></li>");
        html.append("<li>Articles à faible stock: <strong>").append(lowStock.size()).append("</strong></li>");
        html.append("<li>Articles en fin de vente: <strong>").append(endOfSale.size()).append("</strong></li>");
        html.append("<li>Articles en fin de support: <strong>").append(endOfSupport.size()).append("</strong></li>");
        html.append("</ul>");
        html.append("</div>");

        // Articles en rupture de stock
        if (!outOfStock.isEmpty()) {
            html.append("<h2 style='color: #e74c3c;'>Articles en rupture de stock</h2>");
            html.append("<table style='width: 100%; border-collapse: collapse;'>");
            html.append("<tr style='background-color: #f2f2f2;'>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Article</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Constructeur</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Catégorie</th>");
            html.append("</tr>");

            for (Article article : outOfStock) {
                html.append("<tr>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getArticle()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getConstructeur()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getCategorie()).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
            html.append("<br>");
        }

        // Articles à faible stock
        if (!lowStock.isEmpty()) {
            html.append("<h2 style='color: #f39c12;'>Articles à faible stock</h2>");
            html.append("<table style='width: 100%; border-collapse: collapse;'>");
            html.append("<tr style='background-color: #f2f2f2;'>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Article</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Constructeur</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Catégorie</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Quantité</th>");
            html.append("</tr>");

            for (Article article : lowStock) {
                html.append("<tr>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getArticle()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getConstructeur()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getCategorie()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getQuantite()).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
            html.append("<br>");
        }

        // Articles en fin de vente et en fin de support (avec un style similaire)
        addEndOfLifeSection(html, "Articles en End of sale", "#3498db", endOfSale, "EndOfSale");
        addEndOfLifeSection(html, "Articles en End of support", "#9b59b6", endOfSupport, "EndOfSupport");



        return html.toString();
    }

    // Méthode d'aide pour les sections "fin de vente/support"
    private void addEndOfLifeSection(StringBuilder html, String title, String color, List<Article> articles, String dateField) {
        if (!articles.isEmpty()) {
            html.append("<h2 style='color: ").append(color).append(";'>").append(title).append("</h2>");
            html.append("<table style='width: 100%; border-collapse: collapse;'>");
            html.append("<tr style='background-color: #f2f2f2;'>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Article</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Constructeur</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Catégorie</th>");
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>End Of Sale</th>");
            html.append("</tr>");

            for (Article article : articles) {
                html.append("<tr>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getArticle()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getConstructeur()).append("</td>");
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getCategorie()).append("</td>");

                // Accès dynamique à la date de fin selon le type et formatage
                LocalDate date;
                if (dateField.equals("EndOfSale")) {
                    date = article.getEndOfSale();
                } else {
                    date = article.getEndOfSupport();
                }
                String formattedDate = date.format(formatter);
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(formattedDate).append("</td>");

                html.append("</tr>");
            }

            html.append("</table>");
            html.append("<br>");
        }
    }
}