package com.pfe.login.repository;

import com.pfe.login.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.quantite > 0 ORDER BY a.quantite ASC")
    List<Article> findStockItems();

    @Query("SELECT COUNT(a) FROM Article a")
    int countTotalArticles();

    @Query("SELECT SUM(a.quantite) FROM Article a")
    int calculateTotalQuantity();

    @Query("SELECT COUNT(a) FROM Article a WHERE a.quantite < 20")
    int countLowStockItems();

    @Query("SELECT COUNT(a) FROM Article a WHERE a.endOfSale BETWEEN :today AND :threeMonthsLater")
    int countExpiringItems(LocalDate today, LocalDate threeMonthsLater);

    @Query("SELECT SUM(a.quantite) FROM Article a WHERE a.quantite > 0")
    int calculateTotalEntries();

    @Query("SELECT COALESCE(SUM(ABS(a.quantite)), 0) FROM Article a WHERE a.quantite < 0")
    int calculateTotalExits();;

    @Query("SELECT a.categorie AS category, SUM(a.quantite) AS quantity " +
            "FROM Article a GROUP BY a.categorie")
    List<Object[]> getCategoryDistribution();

    @Query("SELECT a.constructeur AS manufacturer, SUM(a.quantite) AS quantity " +
            "FROM Article a GROUP BY a.constructeur")
    List<Object[]> getManufacturerDistribution();

    @Query("SELECT CONCAT(YEAR(a.date), '-', MONTH(a.date)) AS month, " +
            "SUM(a.quantite) AS totalQuantity " +
            "FROM Article a GROUP BY YEAR(a.date), MONTH(a.date) " +
            "ORDER BY YEAR(a.date), MONTH(a.date)")
    List<Object[]> getStockEvolution();

    @Query("SELECT CONCAT(YEAR(a.date), '-', MONTH(a.date)) AS month, " +
            "SUM(CASE WHEN a.quantite > 0 THEN a.quantite ELSE 0 END) AS totalEntries, " +
            "SUM(CASE WHEN a.quantite < 0 THEN ABS(a.quantite) ELSE 0 END) AS totalExits " +
            "FROM Article a GROUP BY YEAR(a.date), MONTH(a.date) " +
            "ORDER BY YEAR(a.date), MONTH(a.date)")
    List<Object[]> getEntriesAndExitsEvolution();
}