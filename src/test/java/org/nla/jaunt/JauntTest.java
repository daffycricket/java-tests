package org.nla.jaunt;

import com.jaunt.*;
import com.jaunt.component.Form;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class JauntTest {

    private static final String authenticationUrl = "http://www.livraison.simplymarket.fr/authentification.php";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws ResponseException, JauntException {
        UserAgent userAgent = new UserAgent();
        userAgent.visit("http://www.livraison.simplymarket.fr/index.php");

        Form authForm = null;
        List<Form> forms = userAgent.doc.getForms();
        for (Form form : forms) {
            if (form.getRequest().getUrl().equals(authenticationUrl)) {
                authForm = form;
                break;
            }
        }

        authForm = userAgent.doc.getForm(0);
        authForm.setTextField("CLI_FACEMAIL_LOG", "daffycricket@yahoo.fr");
        authForm.setPassword("CLI_PASSWORD_LOG", "c@rybou1");
        authForm.submit("OK");
        System.out.println(userAgent.getLocation());
        // document / blocHaut / menu / diaContainer / diaListing + group
        // alternX

        Elements elements = userAgent.doc.findFirst("<div class=diaListing>")
                .getEach("<div>");

        List<String> urls = new ArrayList<>();
        for (Element element : elements) {
            System.out.println("Element =>"
                    + element.getChildElements().get(0).getAt("href"));
            urls.add(element.getChildElements().get(0).getAt("href"));
        }

        // Elements elements = userAgent.doc
        // .findEvery("<div class=groupe altern[0-9]>");
        // System.out.println("search results:\n" + elements.innerHTML());

        // location (url)
        fail("Not yet implemented");
    }
}