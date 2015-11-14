package org.nla.jauntscraper;

public class Rayon {

    private String label;

    private String url;

    public Rayon(String label, String url) {
        super();
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Rayon [label=" + label + ", url=" + url + "]";
    }

}
