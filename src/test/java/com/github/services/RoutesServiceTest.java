package com.github.services;

import com.github.transitbot.api.services.RoutesService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoutesServiceTest {
    RoutesService routesService;

    @Before
    public void setUp() throws Exception {
        routesService = new RoutesService();
    }


    @Test
    public void jsonTest() throws Exception {
        Assert.assertFalse(routesService.getRoutesByStopNumber("10556").isEmpty());
    }
}
