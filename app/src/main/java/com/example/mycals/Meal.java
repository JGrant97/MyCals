package com.example.mycals;

class Meal {
    String label;
    String carbs;
    String cals;
    String fibre;
    String salt;
    String fat;
    String protein;
    String sugar;

    public Meal(String label, String carbs, String cals, String fibre, String salt, String fat, String protein, String sugar) {
        this.label = label;
        this.carbs = carbs;
        this.cals = cals;
        this.fibre = fibre;
        this.salt = salt;
        this.fat = fat;
        this.protein = protein;
        this.sugar = sugar;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getCals() {
        return cals;
    }

    public void setCals(String cals) {
        this.cals = cals;
    }

    public String getFibre() {
        return fibre;
    }

    public void setFibre(String fibre) {
        this.fibre = fibre;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }
}
