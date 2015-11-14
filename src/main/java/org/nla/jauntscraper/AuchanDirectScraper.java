package org.nla.jauntscraper;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import java.util.ArrayList;
import java.util.List;

public class AuchanDirectScraper extends BaseScraper {

    private static final String baseUrl = "http://www.auchandirect.fr/";

    private static final String[] tabloVignetteClasses = new String[]{
            "RST_VERTFONCE", "RST_BLEUCLAIR", "RST_ORANGE", "RST_MARRONCLAIR",
            "RST_BLEUCYAN", "RST_JAUNE", "RST_VIOLETFONCE", "RST_VERT",
            "RST_BEIGE"};

    private String advisedTabloVignetteClass = null;

    public AuchanDirectScraper() {
        try {
            this.userAgent.visit("http://www.auchandirect.fr/Accueil");
        } catch (ResponseException e) {
            throw new ScrapingException(e);
        }
    }

    private List<Produit> getProduits(Famille famille) {
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

    private List<Produit> getProduitsSinglePage(Famille famille, String url) {
        List<Produit> localProducts = null;

        try {
            if (advisedTabloVignetteClass != null) {
                localProducts = getProduitsSinglePage(famille, url,
                        advisedTabloVignetteClass);
                if (!localProducts.isEmpty()) {
                    return localProducts;
                }
            }

            for (String tabloVignetteClass : tabloVignetteClasses) {
                localProducts = getProduitsSinglePage(famille, url,
                        tabloVignetteClass);
                if (!localProducts.isEmpty()) {
                    advisedTabloVignetteClass = tabloVignetteClass;
                    return localProducts;
                }
            }
        } catch (Exception e) {
            throw new ScrapingException(e);
        }

        return localProducts;
    }

    private List<Produit> getProduitsSinglePage(Famille famille, String url,
                                                String tabloVignetteClass) {
        List<Produit> products = new ArrayList<>();
        try {
            userAgent.visit(url);

            storeHtml(url);

            Elements productElements = userAgent.doc
                    .findEach("<div class='tabloVignette " + tabloVignetteClass
                            + "'>");

            for (Element productElement : productElements) {
                String productLabel = productElement.findFirst(
                        "<img class=produit>").getAt("alt");

                String productPrice = productElement
                        .findFirst(
                                "<div class='prix " + tabloVignetteClass + "'>")
                        .getText().trim();

                Produit produit = new Produit(famille, productLabel,
                        productPrice.split("&")[0]);
                products.add(produit);
            }
        } catch (Exception e) {
            throw new ScrapingException(e);
        }

        return products;
    }

    private List<Famille> identifyFamilles(SousRayon sousRayon) {
        List<Famille> familles = new ArrayList<>();

        try {
            // base section url
            userAgent.visit(sousRayon.getUrl());
            if (userAgent.response.getStatus() != 200) {
                throw new RuntimeException("Unable to go to url, status = "
                        + userAgent.response.getStatus());
            }

            Element selectFamilles;
            try {
                selectFamilles = userAgent.doc
                        .findFirst("<select id=idFamille>");
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
        } catch (Exception e) {
            throw new ScrapingException(e);
        }

        return familles;

    }

    private void identifyRayons() {
        try {

            Elements elements = userAgent.doc.findFirst(
                    "<ul class='menu menu-horizontal'>").getEach("<li>");

            for (Element element : elements) {
                // System.out.println("Element =>"
                // + element.getChildElements().get(0).getAt("href"));
                rayons.add(new Rayon(element.getChildElements().get(0)
                        .getChildElements().get(0).getAt("alt"), element
                        .getChildElements().get(0).getAt("href")));
            }
        } catch (NotFound e) {
            throw new ScrapingException(e);
        }
    }

    private List<SousRayon> identifySousRayons(Rayon rayon) {
        List<SousRayon> sousRayons = new ArrayList<>();

        try {
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
                    sousRayons.add(new SousRayon(option.getText(), rayon,
                            baseUrl + option.getAt("value")));
                }
            }
        } catch (Exception e) {
            new ScrapingException(e);
        }

        return sousRayons;
    }

    public void scrape() {
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
}
