package com.github.utils;

import com.github.transitbot.utils.GoogleApiRequestSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class GoogleApiRequestSenderTest {

    GoogleApiRequestSender requestSender = new GoogleApiRequestSender();

    private ClientAndProxy proxy;
    private ClientAndServer mockServer;

    @Before
    public void startProxy() {
//        mockServer = new MockServerClient("localhost", 7777);

        mockServer = startClientAndServer(7777);
        proxy = startClientAndProxy(7778);
        mockServer.when(request().withMethod("GET").withPath("/maps/api/geocode/json"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json("[{ \"test\": \"XXX%%%XXX\"}]")));

    }

    @After
    public void stopProxy() {
        proxy.stop();
        mockServer.stop();
    }

    @Test
    public void sendRequestTest() throws Exception {
        requestSender.sendRequest("http://localhost:7777/maps/api/geocode/json", "", null);
    }
}
