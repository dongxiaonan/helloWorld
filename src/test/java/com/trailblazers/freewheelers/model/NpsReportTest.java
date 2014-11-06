package com.trailblazers.freewheelers.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NpsReportTest {
    @Test
    public void shouldReturn50PercentWhenAnyMethodIsCalled() throws Exception {
        NpsReport npsReport = new NpsReport(100,100,100,200);

        assertThat(npsReport.getDetractorsPercentage(), is(50d));
        assertThat(npsReport.getNpsScore(), is(0.0));
        assertThat(npsReport.getPassivesPercentage(), is(50d));
        assertThat(npsReport.getPromotersPercentage(), is(50d));

    }

}