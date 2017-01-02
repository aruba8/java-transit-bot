package com.github.services;

import com.github.transitbot.api.services.BusScheduleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BusScheduleServiceTest {

    BusScheduleService scheduleService;

    @Before
    public void setUp() throws Exception {
        scheduleService = new BusScheduleService();
    }


    @Test
    public void jsonTest() throws Exception {
        Assert.assertFalse(scheduleService.getBusSchedulesByStopNumber("60596").isEmpty());
    }

}
