package com.pfe.login.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "offers")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "details_description", columnDefinition = "TEXT")
    private String detailsDescription;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "features", columnDefinition = "text[]")
    private List<String> features = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "objectives", columnDefinition = "text[]")
    private List<String> objectives = new ArrayList<>();

    @Column(name = "prix")
    private String prix;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "payment_options", columnDefinition = "text[]")
    private List<String> paymentOptions = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "subscription_channels", columnDefinition = "text[]")
    private List<String> subscriptionChannels = new ArrayList<>();

    @Column(name = "offre_type")
    private String offreType;



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailsDescription() {
        return detailsDescription;
    }

    public void setDetailsDescription(String detailsDescription) {
        this.detailsDescription = detailsDescription;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public List<String> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<String> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<String> getSubscriptionChannels() {
        return subscriptionChannels;
    }

    public void setSubscriptionChannels(List<String> subscriptionChannels) {
        this.subscriptionChannels = subscriptionChannels;
    }

    public String getOffreType() {
        return offreType;
    }

    public void setOffreType(String offreType) {
        this.offreType = offreType;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", detailsDescription='" + detailsDescription + '\'' +
                ", features=" + features +
                ", objectives=" + objectives +
                ", prix='" + prix + '\'' +
                ", paymentOptions=" + paymentOptions +
                ", subscriptionChannels=" + subscriptionChannels +
                ", offreType='" + offreType + '\'' +
                '}';
    }
}