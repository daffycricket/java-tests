package org.nla.jauntscraper;

public class Produit {

    private Famille famille;

    private String label;

    private String price;

    public Produit(Famille famille, String label, String price) {
        this.famille = famille;
        this.label = label;
        this.price = price;
    }

    public String getFamille() {
        return famille.getLabel();
    }

    public String getLabel() {
        return label;
    }

    public String getPrice() {
        return price;
    }

    public String getRayon() {
        return famille.getSousRayon().getRayon().getLabel();
    }

    public String getSousRayon() {
        return famille.getSousRayon().getLabel();
    }

    @Override
    public String toString() {
        return "Produit [label=" + label + ", price=" + price + ", rayon="
                + getRayon() + ", sousRayon=" + getSousRayon() + ", famille="
                + famille + "]";
    }

}
