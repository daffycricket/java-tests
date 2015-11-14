package org.nla.jauntscraper;

public class SousRayon {

    private String label;

    private Rayon rayon;

    private String url;

    public SousRayon(String label, Rayon rayon, String url) {
        super();
        this.label = label;
        this.rayon = rayon;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public Rayon getRayon() {
        return rayon;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "SousRayon [label=" + label + ", rayon=" + rayon + ", url="
                + url + "]";
    }

}
