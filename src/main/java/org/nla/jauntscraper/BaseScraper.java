package org.nla.jauntscraper;

import com.jaunt.UserAgent;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BaseScraper {

    protected final List<Produit> produits = new ArrayList<>();

    protected final List<Rayon> rayons = new ArrayList<>();

    protected final UserAgent userAgent = new UserAgent();

    public List<Produit> getProduits() {
        return produits;
    }

    public void persist() throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter("produits.csv"), ';');

        for (Produit produit : produits) {
            String[] line = new String[]{produit.getRayon(),
                    produit.getSousRayon(), produit.getFamille(),
                    produit.getLabel(), produit.getPrice()};

            writer.writeNext(line);
        }
        writer.close();
    }

    protected void storeHtml(String url) throws Exception {
        Files.write(
                Paths.get("outputs\\"
                        + url.replace("/", "_").replace(":", "_")
                        .replace("?", "_").replace("=", "-")),
                userAgent.doc.innerHTML().getBytes());
    }

}