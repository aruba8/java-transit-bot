package com.github.services;


import com.github.transitbot.api.services.StopService;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;

public class StopServiceTest {

    StopService stopService = new StopService();
    String tJson = null;

    @Before
    public void setUp() throws Exception {
        InputStream is = getClass().getResourceAsStream("/stop-resp.json");
        tJson = IOUtils.toString(is, Charset.defaultCharset());
    }


    @Test
    public void jsonTest() throws Exception {
        Assert.assertEquals(stopService.getStopNameByStopNumber("10556"), "Westbound Portage at Wall");
    }


}
