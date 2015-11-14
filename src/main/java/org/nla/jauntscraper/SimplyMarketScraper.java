package org.nla.jauntscraper;

import com.jaunt.*;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SimplyMarketScraper {

    private static final String baseUrl = "http://www.livraison.simplymarket.fr/";

    private static final String[] tabloVignetteClasses = new String[]{
            "RST_VERTFONCE", "RST_BLEUCLAIR", "RST_ORANGE", "RST_MARRONCLAIR",
            "RST_BLEUCYAN", "RST_JAUNE", "RST_VIOLETFONCE", "RST_VERT",
            "RST_BEIGE"};

    private String advisedTabloVignetteClass = null;

    private final List<Produit> produits = new ArrayList<>();

    private final List<Rayon> rayons = new ArrayList<>();

    private final UserAgent userAgent;

    public SimplyMarketScraper() throws JauntException {
        this.userAgent = new UserAgent();
        this.userAgent.visit(baseUrl);
    }

    public List<Produit> getProduits() {
        return produits;
    }

    private List<Produit> getProduits(Famille famille) throws Exception {
        List<Produit> products = new ArrayList<>();

        try {
            // add products of page 1
            products.addAll(getProduitsSinglePage(famille, famille.getUrl()));

            Element navigationElement = userAgent.doc
                    .findFirst("<td class='navCentre alignright'>");

            // add products of subsequent pages
            Elements productPages = navigationElement.findEach("<a>");
            for (Element productPage : productPages) {
                if (!productPage.getText().equals("1")) {
                    String nextUrl = productPage.getAt("href");
                    products.addAll(getProduitsSinglePage(famille, nextUrl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // do nothing and continue
        }

        return products;
    }

    private List<Produit> getProduitsSinglePage(Famille famille, String url)
            throws Exception {

        List<Produit> products = null;
        if (advisedTabloVignetteClass != null) {
            products = getProduitsSinglePage(famille, url,
                    advisedTabloVignetteClass);
            if (!products.isEmpty()) {
                return products;
            }
        }

        for (String tabloVignetteClass : tabloVignetteClasses) {
            products = getProduitsSinglePage(famille, url, tabloVignetteClass);
            if (!products.isEmpty()) {
                advisedTabloVignetteClass = tabloVignetteClass;
                return products;
            }
        }

        return products;
    }

    private List<Produit> getProduitsSinglePage(Famille famille, String url,
                                                String tabloVignetteClass) throws Exception {
        List<Produit> products = new ArrayList<>();
        userAgent.visit(url);

        storeHtml(url);

        Elements productElements = userAgent.doc
                .findEach("<div class='tabloVignette " + tabloVignetteClass
                        + "'>");

        for (Element productElement : productElements) {
            String productLabel = productElement.findFirst(
                    "<img class=produit>").getAt("alt");

            String productPrice = productElement
                    .findFirst("<div class='prix " + tabloVignetteClass + "'>")
                    .getText().trim();

            Produit produit = new Produit(famille, productLabel,
                    productPrice.split("&")[0]);
            products.add(produit);
        }

        return products;
    }

    private List<Famille> identifyFamilles(SousRayon sousRayon)
            throws JauntException {
        List<Famille> familles = new ArrayList<>();

        // base section url
        userAgent.visit(sousRayon.getUrl());
        if (userAgent.response.getStatus() != 200) {
            throw new RuntimeException("Unable to go to url, status = "
                    + userAgent.response.getStatus());
        }

        Element selectFamilles;
        try {
            selectFamilles = userAgent.doc.findFirst("<select id=idFamille>");
        } catch (NotFound e) {
            return familles;
        }

        Elements options = selectFamilles.getEach("<option>");
        for (Element option : options) {
            if (!option.getText().equals("tout afficher")
                    && !option.getAt("value").equals("")) {

                Famille famille = new Famille(option.getText(), sousRayon,
                        baseUrl + option.getAt("value"));

                familles.add(famille);
                // System.out.println(option.getText() + ": " +
                // option.getAt("value"));
            }
        }

        return familles;

    }

    private void identifyRayons() throws JauntException {
        Elements elements = userAgent.doc.findFirst("<div class=diaListing>")
                .getEach("<div>");

        for (Element element : elements) {
            // System.out.println("Element =>"
            // + element.getChildElements().get(0).getAt("href"));
            rayons.add(new Rayon(element.getChildElements().get(0)
                    .getChildElements().get(0).getAt("alt"), element
                    .getChildElements().get(0).getAt("href")));
        }
    }

    private List<SousRayon> identifySousRayons(Rayon rayon)
            throws JauntException {
        List<SousRayon> sousRayons = new ArrayList<>();

        // base section url
        userAgent.visit(rayon.getUrl());
        if (userAgent.response.getStatus() != 200) {
            throw new RuntimeException("Unable to go to url, status = "
                    + userAgent.response.getStatus());
        }

        Element selectSousRayon = userAgent.doc
                .findFirst("<select id=idSousRayon>");

        Elements options = selectSousRayon.getEach("<option>");
        for (Element option : options) {
            if (!option.getText().equals("&nbsp;")) {
                sousRayons.add(new SousRayon(option.getText(), rayon, baseUrl
                        + option.getAt("value")));
            }
        }

        return sousRayons;
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

    public void scrape() throws Exception {
        identifyRayons();
        for (int i = 5; i < rayons.size(); ++i) {
            Rayon rayon = rayons.get(i);

            System.out.println("R " + rayon);
            List<SousRayon> sousRayons = identifySousRayons(rayon);

            for (int j = 1; j < sousRayons.size(); ++j) {
                System.out.println("\tSR " + sousRayons.get(j));
                List<Famille> familles = identifyFamilles(sousRayons.get(j));

                for (int k = 1; k < familles.size(); ++k) {
                    System.out.println("\t\tF " + familles.get(k));
                    produits.addAll(getProduits(familles.get(k)));

                    for (Produit produit : produits) {
                        System.out.println("\t\t\tP " + produit);
                    }
                }
            }
        }
    }

    private void storeHtml(String url) throws Exception {
        Files.write(
                Paths.get("outputs\\"
                        + url.replace("/", "_").replace(":", "_")
                        .replace("?", "_").replace("=", "-")),
                userAgent.doc.innerHTML().getBytes());
    }
}
