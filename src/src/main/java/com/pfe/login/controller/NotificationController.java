package com.pfe.login.controller;

import com.pfe.login.model.Article;
import com.pfe.login.model.Notification;
import com.pfe.login.service.ArticleService;
import com.pfe.login.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    private final ArticleService articleService;
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, Object> request) {
        try {
            // Extraction des valeurs
            String recipients = (String) request.get("recipients");
            String subject = (String) request.get("subject");
            boolean sendNow = (boolean) request.get("sendNow");

            // Types de notifications à inclure
            @SuppressWarnings("unchecked")
            Map<String, Boolean> notificationTypes = (Map<String, Boolean>) request.get("notificationTypes");

            boolean includeOutOfStock = Boolean.TRUE.equals(notificationTypes.get("outOfStock"));
            boolean includeLowStock = Boolean.TRUE.equals(notificationTypes.get("lowStock"));
            boolean includeEndOfSale = Boolean.TRUE.equals(notificationTypes.get("endOfSale"));
            boolean includeEndOfSupport = Boolean.TRUE.equals(notificationTypes.get("endOfSupport"));

            // Obtenir les articles selon les critères
            List<Article> outOfStockArticles = includeOutOfStock ? articleService.getOutOfStockArticles() : List.of();
            List<Article> lowStockArticles = includeLowStock ? articleService.getLowStockArticles() : List.of();
            List<Article> endOfSaleArticles = includeEndOfSale ? articleService.getEndOfSaleArticles() : List.of();
            List<Article> endOfSupportArticles = includeEndOfSupport ? articleService.getEndOfSupportArticles() : List.of();

            // Vérifier s'il y a des articles à signaler
            boolean hasArticlesToReport = !outOfStockArticles.isEmpty() || !lowStockArticles.isEmpty() ||
                    !endOfSaleArticles.isEmpty() || !endOfSupportArticles.isEmpty();

            if (hasArticlesToReport) {
                if (sendNow) {
                    // Créer le contenu HTML de l'email
                    String emailContent = notificationService.createHtmlEmail(outOfStockArticles, lowStockArticles,
                            endOfSaleArticles, endOfSupportArticles);

                    // Envoyer l'email immédiatement
                    notificationService.sendEmail(recipients, subject, emailContent);

                    // Créer et sauvegarder aussi une notification dans la base de données
                    Notification notification = new Notification();
                    notification.setRecipients(recipients);
                    notification.setSubject(subject);
                    notification.setScheduleTime(LocalDateTime.now()); // Heure actuelle
                    notification.setIncludeOutOfStock(includeOutOfStock);
                    notification.setIncludeLowStock(includeLowStock);
                    notification.setIncludeEndOfSale(includeEndOfSale);
                    notification.setIncludeEndOfSupport(includeEndOfSupport);
                    notification.setSent(true); // Déjà envoyé

                    // Sauvegarder la notification
                    notificationService.saveNotification(notification);

                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Email envoyé avec succès");
                    return ResponseEntity.ok(response);
                } else {
                    String scheduleTimeStr = (String) request.get("scheduleTime");
                    LocalDateTime scheduleTime = LocalDateTime.parse(scheduleTimeStr);

                    Notification notification = new Notification();
                    notification.setRecipients(recipients);
                    notification.setSubject(subject);
                    notification.setScheduleTime(scheduleTime);
                    notification.setIncludeOutOfStock(includeOutOfStock);
                    notification.setIncludeLowStock(includeLowStock);
                    notification.setIncludeEndOfSale(includeEndOfSale);
                    notification.setIncludeEndOfSupport(includeEndOfSupport);
                    notification.setSent(false);

                    notificationService.scheduleNotification(notification);

                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Email programmé avec succès");
                    return ResponseEntity.ok(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "warning");
                response.put("message", "Aucun article à signaler");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<Notification>> getScheduledNotifications() {
        return ResponseEntity.ok(notificationService.getAllPendingNotifications());
    }

    @DeleteMapping("/scheduled/{id}")
    public ResponseEntity<Void> cancelScheduledNotification(@PathVariable Long id) {
        notificationService.cancelNotification(id);
        return ResponseEntity.noContent().build();
    }
}