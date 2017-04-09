package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Coordinates;
import com.github.transitbot.api.services.exceptions.GoogleApiZeroResultsException;
import com.github.transitbot.utils.GoogleApiRequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class GoogleGeoApiServiceTest {

    private ClientAndProxy proxy;
    private ClientAndServer mockServer;
    String emptyJson;
    String tJson;

    GoogleGeoApiService service = new GoogleGeoApiService();
    GoogleApiRequestSender requestSender = new GoogleApiRequestSender();

    @Before
    public void startProxy() throws IOException {
        InputStream isEmpty = getClass().getResourceAsStream("/google-api-empty.json");
        emptyJson = IOUtils.toString(isEmpty, Charset.defaultCharset());
        InputStream is = getClass().getResourceAsStream("/google-api-response.json");
        tJson = IOUtils.toString(is, Charset.defaultCharset());

        mockServer = startClientAndServer(7777);
        proxy = startClientAndProxy(7778);
    }

    @After
    public void stopProxy() {
        proxy.stop();
        mockServer.stop();
    }

    @Test
    public void getCoordinatesByAddress() throws Exception {
    }

    @Test
    public void getCoordinatesFromResponseTest() throws Exception {
        mockServer.when(request().withMethod("GET").withPath("/maps/api/geocode/json"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(tJson)));

        HttpResponse<JsonNode> response = requestSender.sendRequest("http://localhost:7777/maps/api/geocode/json", "", null);
        Coordinates coordinates = service.getCoordinatesFromResponse(response);
        Assert.assertEquals("49.88040729999999", coordinates.getLatitude());
        Assert.assertEquals("-97.32757319999999", coordinates.getLongitude());
    }

    @Test(expected = GoogleApiZeroResultsException.class)
    public void getCoordinatesFromEmptyResponseTest() throws Exception {
        mockServer.when(request().withMethod("GET").withPath("/maps/api/geocode/json"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(emptyJson)));

        HttpResponse<JsonNode> response = requestSender.sendRequest("http://localhost:7777/maps/api/geocode/json", "", null);
        Coordinates coordinates = service.getCoordinatesFromResponse(response);
    }

}