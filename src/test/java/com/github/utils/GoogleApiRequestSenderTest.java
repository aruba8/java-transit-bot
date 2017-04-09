package com.github.utils;

import com.github.transitbot.utils.GoogleApiRequestSender;
import com.mashape.unirest.http.JsonNode;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;
import com.mashape.unirest.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class GoogleApiRequestSenderTest {

    GoogleApiRequestSender requestSender = new GoogleApiRequestSender();

    private ClientAndProxy proxy;
    private ClientAndServer mockServer;
    String tJson;

    @Before
    public void startProxy() throws IOException {
        InputStream is = getClass().getResourceAsStream("/google-api-response.json");
        tJson = IOUtils.toString(is, Charset.defaultCharset());
        mockServer = startClientAndServer(7777);
        proxy = startClientAndProxy(7778);
        mockServer.when(request().withMethod("GET").withPath("/maps/api/geocode/json"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(tJson)));


    }

    @After
    public void stopProxy() {
        proxy.stop();
        mockServer.stop();
    }

    @Test
    public void sendRequestTest() throws Exception {
        HttpResponse<JsonNode> response = requestSender.sendRequest("http://localhost:7777/maps/api/geocode/json", "", null);
        Assert.assertNotNull(response);
    }

    @Test
    public void getCoordinatesFromResponseTest() throws Exception{
        HttpResponse<JsonNode> response = requestSender.sendRequest("http://localhost:7777/maps/api/geocode/json", "", null);

    }
}
