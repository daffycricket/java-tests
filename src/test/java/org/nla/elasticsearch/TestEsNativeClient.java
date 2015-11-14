package org.nla.elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class TestEsNativeClient {

    private Settings settings = ImmutableSettings.settingsBuilder()
            .put("cluster.name", "elasticsearch_1.3").build();

    private TransportClient client;

    @Before
    public void setUp() {
        settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch_1.3").build();
        client = new TransportClient(settings);
        client = client.addTransportAddress(new InetSocketTransportAddress(
                "localhost", 9300));
    }

    @Test
    @Ignore
    public void testSearchKibanaDashboards() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("kibana-int").setTypes("dashboard").setSize(10);
        SearchResponse searchResponse = client.search(searchRequestBuilder.request()).actionGet();

        for (SearchHit hit : searchResponse.getHits()) {
            String source = hit.sourceAsString();
            System.out.println(source);
        }
    }

    @Test
    @Ignore
    public void testIndexKibanaDashboard() throws IOException {
        final String indexName = "kibana-int";
        final String typeName = "dashboard";

        String dashboard = new String(Files.readAllBytes(Paths.get("files", "dashboard.json")));
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, typeName, "My own basic dashboard");
        indexRequestBuilder.setSource(dashboard);
        IndexResponse response = indexRequestBuilder.execute().actionGet();
        assertThat(response.isCreated()).isTrue();

        String docId = response.getId();
        DeleteRequestBuilder deleteRequest = client.prepareDelete(indexName, typeName, docId);
        DeleteResponse deleteResponse = deleteRequest.execute().actionGet();
        assertThat(deleteResponse.isFound()).isTrue();
    }

    @Test
    @Ignore
    public void testSearchTerms() {
        QueryBuilder queryBuilder = QueryBuilders.termQuery("summary", "legal");
        SearchRequestBuilder searchRequestBuilder = client
                .prepareSearch("auindex")
                .setQuery(queryBuilder)
                .setTypes("person")
                .setSize(10);
        SearchResponse searchResponse = client.search(searchRequestBuilder.request()).actionGet();

        for (SearchHit hit : searchResponse.getHits()) {
            String source = hit.sourceAsString();
            System.out.println(source);
        }
    }


    @Test
    @Ignore
    public void testIndexJsonDocuments() {
        final String indexName = "jsonindex";
        final String typeName = "person";

        String jsonString;
        jsonString = new Converter()
                .convertXmlFileToJsonString("C:\\Users\\NIL\\Google Drive\\au-profiles\\f\\finegan-kruckemeyer_26_63_110.txt");
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(
                indexName, typeName);
        indexRequestBuilder.setSource(jsonString);
        IndexResponse response = indexRequestBuilder.execute().actionGet();
        String docId = response.getId();
        System.out.println("finegan-kruckemeyer created...");

        File profileDir = new File(
                "C:\\Users\\NIL\\Google Drive\\au-profiles\\f");
        File[] profileFiles = profileDir.listFiles();
        int i = 0;
        for (File profileFile : profileFiles) {
            try {
                String filePath = profileFile.getAbsolutePath();
                jsonString = new Converter()
                        .convertXmlFileToJsonString(filePath);

                // Add document
                indexRequestBuilder = client.prepareIndex(indexName, typeName);
                indexRequestBuilder.setSource(jsonString);
                response = indexRequestBuilder.execute().actionGet();
                docId = response.getId();
                System.out.println(i + " " + filePath);
            } catch (ElasticsearchException e) {
                // e.printStackTrace();
            }
        }

        client.close();
    }

    @Test
    @Ignore
    public void testCreateThenDeleteIndex() throws IOException {
        final String indexName = "indextocreatethendelete";
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin()
                .indices().prepareCreate(indexName);
        CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
        assertThat(createIndexResponse.isAcknowledged()).isTrue();

        DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin()
                .indices().prepareDelete(indexName);
        DeleteIndexResponse deleteIndexResponse = deleteIndexRequestBuilder.execute().actionGet();
        assertThat(deleteIndexResponse.isAcknowledged()).isTrue();
    }
}
