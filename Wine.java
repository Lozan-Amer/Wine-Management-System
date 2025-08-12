package com.example.winejavasql;

public class Wine {
    private double fixedAcidity;
    private double volatileAcidity;
    private double citricAcid;
    private double residualSugar;
    private double chlorides;
    private double freeSulfurDioxide;
    private double totalSulfurDioxide;
    private double density;
    private double pH;
    private double sulphates;
    private double alcohol;
    private String quality;
    private String color;

    //  בנאי מלא לכל הנתונים
    public Wine(double fixedAcidity, double volatileAcidity, double citricAcid, double residualSugar,
                double chlorides, double freeSulfurDioxide, double totalSulfurDioxide, double density,
                double pH, double sulphates, double alcohol, String quality, String color) {
        this.fixedAcidity = fixedAcidity;
        this.volatileAcidity = volatileAcidity;
        this.citricAcid = citricAcid;
        this.residualSugar = residualSugar;
        this.chlorides = chlorides;
        this.freeSulfurDioxide = freeSulfurDioxide;
        this.totalSulfurDioxide = totalSulfurDioxide;
        this.density = density;
        this.pH = pH;
        this.sulphates = sulphates;
        this.alcohol = alcohol;
        this.quality = quality != null ? quality : "N/A";
        this.color = color != null ? color : "N/A";
    }

    public double getFixedAcidity() { return fixedAcidity; }
    public double getVolatileAcidity() { return volatileAcidity; }
    public double getCitricAcid() { return citricAcid; }
    public double getResidualSugar() { return residualSugar; }
    public double getChlorides() { return chlorides; }
    public double getFreeSulfurDioxide() { return freeSulfurDioxide; }
    public double getTotalSulfurDioxide() { return totalSulfurDioxide; }
    public double getDensity() { return density; }
    public double getPH() { return pH; }
    public double getSulphates() { return sulphates; }
    public double getAlcohol() { return alcohol; }

    public String getQuality() {
        return quality;
    }

    public String getColor() {
        return color;
    }
}
