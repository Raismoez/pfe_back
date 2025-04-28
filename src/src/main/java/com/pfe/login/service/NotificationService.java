package com.pfe.login.service;

import com.pfe.login.model.Article;
import com.pfe.login.model.Notification;
import com.pfe.login.repository.ScheduledNotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final ArticleService articleService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ----- Partie Email simple -----
    public void sendEmail(String recipients, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipients.split(","));
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Échec de l'envoi de l'email: " + e.getMessage(), e);
        }
    }

    // ----- Partie Notification programmée -----
    public Notification scheduleNotification(Notification notification) {
        return scheduledNotificationRepository.save(notification);
    }

    public List<Notification> getAllPendingNotifications() {
        return scheduledNotificationRepository.findByIsSentFalse();
    }

    public void cancelNotification(Long id) {
        scheduledNotificationRepository.deleteById(id);
    }

    public Notification saveNotification(Notification notification) {
        return scheduledNotificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000) // Vérifier toutes les minutes
    public void processScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notificationsToSend =
                scheduledNotificationRepository.findByScheduleTimeLessThanEqualAndIsSentFalse(now);

        for (Notification notification : notificationsToSend) {
            try {
                sendScheduledNotification(notification);
                notification.setSent(true);
                scheduledNotificationRepository.save(notification);
            } catch (Exception e) {
                // Log l'erreur mais continuer avec les autres notifications
                System.err.println("Erreur lors de l'envoi de la notification programmée #" + notification.getId() + ": " + e.getMessage());
            }
        }
    }

    private void sendScheduledNotification(Notification notification) {
        // Récupérer les articles selon les critères sélectionnés
        List<Article> outOfStockArticles = notification.isIncludeOutOfStock() ?
                articleService.getOutOfStockArticles() : List.of();

        List<Article> lowStockArticles = notification.isIncludeLowStock() ?
                articleService.getLowStockArticles() : List.of();

        List<Article> endOfSaleArticles = notification.isIncludeEndOfSale() ?
                articleService.getEndOfSaleArticles() : List.of();

        List<Article> endOfSupportArticles = notification.isIncludeEndOfSupport() ?
                articleService.getEndOfSupportArticles() : List.of();

        // Vérifier s'il y a des articles à signaler
        boolean hasArticlesToReport = !outOfStockArticles.isEmpty() || !lowStockArticles.isEmpty() ||
                !endOfSaleArticles.isEmpty() || !endOfSupportArticles.isEmpty();

        if (hasArticlesToReport) {
            // Créer le contenu HTML de l'email
            String emailContent = createHtmlEmail(outOfStockArticles, lowStockArticles,
                    endOfSaleArticles, endOfSupportArticles);

            // Envoyer l'email
            sendEmail(notification.getRecipients(), notification.getSubject(), emailContent);
        }
    }

    // Méthode publique pour créer le contenu HTML d'email (accessible depuis le contrôleur)
    public String createHtmlEmail(List<Article> outOfStock, List<Article> lowStock,
                                  List<Article> endOfSale, List<Article> endOfSupport) {
        StringBuilder html = new StringBuilder();

        // Style et en-tête améliorés
        html.append("<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto;'>");
        html.append("<h1 style='color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px;'>")
                .append("Rapport de Stock")
                .append("</h1>");

        // Résumé
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
            html.append(buildTableHTML(outOfStock, false, null));
        }

        // Articles à faible stock
        if (!lowStock.isEmpty()) {
            html.append("<h2 style='color: #f39c12;'>Articles à faible stock</h2>");
            html.append(buildTableHTML(lowStock, true, null));
        }

        // Articles en fin de vente et en fin de support
        if (!endOfSale.isEmpty()) {
            html.append("<h2 style='color: #3498db;'>Articles en End of sale</h2>");
            html.append(buildTableHTML(endOfSale, false, "EndOfSale"));
        }

        if (!endOfSupport.isEmpty()) {
            html.append("<h2 style='color: #9b59b6;'>Articles en End of support</h2>");
            html.append(buildTableHTML(endOfSupport, false, "EndOfSupport"));
        }

        html.append("</div>");
        return html.toString();
    }

    // Méthode unifiée pour construire les tableaux HTML
    private String buildTableHTML(List<Article> articles, boolean includeQuantity, String dateFieldType) {
        StringBuilder html = new StringBuilder();
        html.append("<table style='width: 100%; border-collapse: collapse;'>");
        html.append("<tr style='background-color: #f2f2f2;'>");
        html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Article</th>");
        html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Constructeur</th>");
        html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Catégorie</th>");

        if (includeQuantity) {
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Quantité</th>");
        }

        if (dateFieldType != null) {
            html.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Date</th>");
        }

        html.append("</tr>");

        for (Article article : articles) {
            html.append("<tr>");
            html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getArticle()).append("</td>");
            html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getConstructeur()).append("</td>");
            html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getCategorie()).append("</td>");

            if (includeQuantity) {
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(article.getQuantite()).append("</td>");
            }

            if (dateFieldType != null) {
                LocalDate date = dateFieldType.equals("EndOfSale") ? article.getEndOfSale() : article.getEndOfSupport();
                String formattedDate = date != null ? date.format(formatter) : "N/A";
                html.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(formattedDate).append("</td>");
            }

            html.append("</tr>");
        }

        html.append("</table>");
        html.append("<br>");
        return html.toString();
    }
}