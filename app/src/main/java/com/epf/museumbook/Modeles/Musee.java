package com.epf.museumbook.Modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Musee {

    private int rank;

    @SerializedName("adresse")
    @Expose
    private String adresse;
    @SerializedName("cp")
    @Expose
    private String cp;
    @SerializedName("dept")
    @Expose
    private String dept;
    @SerializedName("ferme")
    @Expose
    private boolean ferme;
    @SerializedName("fermeture_annuelle")
    @Expose
    private String fermetureAnnuelle;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("periode_ouverture")
    @Expose
    private String periodeOuverture;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("site_web")
    @Expose
    private String siteWeb;
    @SerializedName("ville")
    @Expose
    private String ville;

    /**
     * No args constructor for use in serialization
     *
     */
    public Musee() {
    }

    /**
     *
     * @param region
     * @param id
     * @param ferme
     * @param cp
     * @param adresse
     * @param ville
     * @param periodeOuverture
     * @param siteWeb
     * @param dept
     * @param fermetureAnnuelle
     * @param nom
     */
    public Musee(int rank, String adresse, String cp, String dept, boolean ferme, String fermetureAnnuelle, String id, String nom, String periodeOuverture, String region, String siteWeb, String ville) {
        super();
        this.rank = rank;
        this.adresse = adresse;
        this.cp = cp;
        this.dept = dept;
        this.ferme = ferme;
        this.fermetureAnnuelle = fermetureAnnuelle;
        this.id = id;
        this.nom = nom;
        this.periodeOuverture = periodeOuverture;
        this.region = region;
        this.siteWeb = siteWeb;
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public boolean isFerme() {
        return ferme;
    }

    public void setFerme(boolean ferme) {
        this.ferme = ferme;
    }

    public String getFermetureAnnuelle() {
        return fermetureAnnuelle;
    }

    public void setFermetureAnnuelle(String fermetureAnnuelle) {
        this.fermetureAnnuelle = fermetureAnnuelle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPeriodeOuverture() {
        return periodeOuverture;
    }

    public void setPeriodeOuverture(String periodeOuverture) {
        this.periodeOuverture = periodeOuverture;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

}