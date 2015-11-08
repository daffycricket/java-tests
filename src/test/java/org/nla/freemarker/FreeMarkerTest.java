package org.nla.freemarker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class FreeMarkerTest {

    protected Configuration configuration;

    @Before
    public void setUp() {
        try {
            configuration = new Configuration(Configuration.VERSION_2_3_21);
            //configuration.setDirectoryForTemplateLoading(new File("/templates"));
            configuration.setClassForTemplateLoading(this.getClass(), "/");
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_21));
        } catch (Exception ioe) {
            throw new RuntimeException(ioe);
        }
    }


    @Test
    public void useTemplateWithListOfElements() {

        try {
            // build model
            Map<String, Object> metaDashboard = new HashMap<String, Object>();
            List<Map<String, Object>> environments = new ArrayList<Map<String, Object>>();

            Map<String, Object> envInt1 = new HashMap<String, Object>();
            envInt1.put("env", "INT1");
            envInt1.put("envName", "Integration");

            Map<String, Object> envUat1 = new HashMap<String, Object>();
            envUat1.put("env", "UAT1");
            envUat1.put("envName", "Recette");

            metaDashboard.put("name", "WebMap meta dashboard");
            metaDashboard.put("environments", environments);
            environments.add(envInt1);
            environments.add(envUat1);

            // get template
            Template template = configuration.getTemplate("kibana_Introduction.template.json");

            // merge template + model
            //Writer out = new OutputStreamWriter(System.out);
            Writer out = new StringWriter();
            template.process(metaDashboard, out);

            assertThat(out.toString(), containsString("UAT1"));
            assertThat(out.toString(), containsString("INT1"));
            assertThat(out.toString(), containsString("Recette"));
            assertThat(out.toString(), containsString("Integration"));
        } catch (IOException|TemplateException ex) {
            throw new RuntimeException(ex);
        }
    }
}