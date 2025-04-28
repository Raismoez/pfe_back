package com.pfe.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article")
    private String article;

    @Column(name = "constructeur")
    private String constructeur;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "quantite")
    private int quantite;

    @Column(name = "end_of_sale")
    private LocalDate endOfSale;

    @Column(name = "end_of_support")
    private LocalDate endOfSupport;

    @Column(name = "date_m2")
    private LocalDate dateM2;

    @Column(name = "quantite_m2")
    private Integer quantiteM2;

    @Column(name = "date_m3")
    private LocalDate dateM3;

    @Column(name = "quantite_m3")
    private Integer quantiteM3;

    @Column(name = "date_m4")
    private LocalDate dateM4;

    @Column(name = "quantite_m4")
    private Integer quantiteM4;

    @Column(name = "date_m5")
    private LocalDate dateM5;

    @Column(name = "quantite_m5")
    private Integer quantiteM5;

    @Column(name = "date_m6")
    private LocalDate dateM6;

    @Column(name = "quantite_m6")
    private Integer quantiteM6;
}