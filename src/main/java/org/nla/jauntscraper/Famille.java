package org.nla.jauntscraper;

public class Famille {

    private String label;

    private SousRayon sousRayon;

    private String url;

    public Famille(String label, SousRayon sousRayon, String url) {
        super();
        this.label = label;
        this.sousRayon = sousRayon;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public SousRayon getSousRayon() {
        return sousRayon;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Famille [label=" + label + ", sousRayon=" + sousRayon
                + ", url=" + url + "]";
    }
}