package com.pfe.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destinataires", nullable = false)
    private String recipients;

    @Column(name = "sujet", nullable = false)
    private String subject;

    @Column(name = "Temps_programmé", nullable = false)
    private LocalDateTime scheduleTime;

    @Column(name = "inclure_out_of_stock")
    private boolean includeOutOfStock;

    @Column(name = "inclure_low_stock")
    private boolean includeLowStock;

    @Column(name = "inclure_end_of_sale")
    private boolean includeEndOfSale;

    @Column(name = "inclure_end_of_support")
    private boolean includeEndOfSupport;

    @Column(name = "envoyé")
    private boolean isSent;

    @Column(name = "Date_de_création")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}