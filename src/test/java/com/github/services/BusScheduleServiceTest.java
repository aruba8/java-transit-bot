package com.github.services;

import com.github.transitbot.api.services.BusScheduleService;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.JsonBody;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class BusScheduleServiceTest {

    private BusScheduleService scheduleService;
    private ClientAndProxy proxy;
    private ClientAndServer mockServer;
    private String tJson;

    @Before
    public void setUp() throws Exception {
        scheduleService = new BusScheduleService();
        InputStream isEmpty = getClass().getResourceAsStream("/stops-schedule.json");
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
    public void jsonTest() throws Exception {
        mockServer.when(request().withMethod("GET"))
                .respond(response().withStatusCode(200).withBody(JsonBody.json(tJson)));
        Assert.assertFalse(scheduleService.getBusSchedulesByStopNumber("60596").isEmpty());
    }

}
