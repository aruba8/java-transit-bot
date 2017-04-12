package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Coordinates;
import com.mashape.unirest.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class LocationServiceTest {
    private LocationsService locationsService;
    private ClientAndProxy proxy;
    private ClientAndServer mockServer;
    private String tJson;

    @Before
    public void setUp() throws Exception {
        locationsService = new LocationsService();
        InputStream isEmpty = getClass().getResourceAsStream("/locations-resp.json");
        tJson = IOUtils.toString(isEmpty, Charset.defaultCharset());
        mockServer = startClientAndServer(7777);
        proxy = startClientAndProxy(7778);

    }

    @After
    public void stopProxy() {
        proxy.stop();
        mockServer.stop();
    }

    @Test
    public void getLocationsByCoordinatesTest() {
        mockServer.when(request().withMethod("GET"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(tJson)));

        Coordinates coordinates = new Coordinates("-97.138", "49.895");
        Assert.assertNotNull(locationsService.getLocationsByCoordinates(coordinates));
    }

    @Test
    public void getAddressesWithNameFromLocationTest() {
        mockServer.when(request().withMethod("GET"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(tJson)));

        Coordinates coordinates = new Coordinates("-97.138", "49.895");
        HttpResponse response = locationsService.getLocationsByCoordinates(coordinates);
        Set locationsWithName = locationsService.getAddressesWithNameFromLocation(response);
        Assert.assertEquals(2, locationsWithName.size());
    }


}