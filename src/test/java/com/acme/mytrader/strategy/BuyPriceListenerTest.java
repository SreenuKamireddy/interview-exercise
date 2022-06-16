package com.acme.mytrader.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.BuyPriceListener;
import com.acme.mytrader.price.PriceListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuyPriceListenerTest {

    BuyPriceListener buyPriceListener;
    @Mock
    ExecutionService executionService;

    @Before
    public void init() {
        buyPriceListener = new BuyPriceListener("IBM", 50.00, 100, executionService,
                false);
    }

    @Test
    public void testInitializeStateForBuyPriceListener() {

        assertThat(buyPriceListener.getSecurity()).isEqualTo("IBM");
        assertThat(buyPriceListener.getTriggerLevel()).isEqualTo(50.00);
        assertThat(buyPriceListener.getQuantityToPurchase()).isEqualTo(100);
        assertThat(buyPriceListener.isTradeExecuted()).isFalse();
    }

    @Test
    public void testBuy_whenThresholdIsMet() {

        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

        buyPriceListener.priceUpdate("IBM", 25.00);

        verify(executionService, times(1))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        assertThat(acString.getValue()).as("Should be IBM ")
                .isEqualTo("IBM");
        assertThat(acDouble.getValue()).as("Should be the value less than 50.00, that is 25.00")
                .isEqualTo(25.00);
        assertThat(acInteger.getValue()).as("Should be the volume purchased").isEqualTo(100);
        assertThat(buyPriceListener.isTradeExecuted())
                .as("Should be the trade is successfully executed").isTrue();
    }

    @Test
    public void testShouldNotBuy_whenThresholdIsNotMet() {
        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);


        buyPriceListener.priceUpdate("IBM", 55.00);

        verify(executionService, times(0))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        assertThat(buyPriceListener.isTradeExecuted())
                .as("Should be the trade is not successfully executed").isFalse();
    }

    @Test
    public void testShouldNotBuy_whenSecurityIsDifferent() {

        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);


        buyPriceListener.priceUpdate("IBM", 55.00);

        verify(executionService, times(0))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        assertThat(buyPriceListener.isTradeExecuted())
                .as("Should be the trade is not successfully executed").isFalse();
    }

    @Test
    public void testGivenSeveralPriceUpdates_whenTradeIsAlreadyExecucted_shouldBuyOnlyOnce() {

        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

        buyPriceListener.priceUpdate("IBM", 25.00);
        buyPriceListener.priceUpdate("IBM", 10.00);
        buyPriceListener.priceUpdate("IBM", 35.00);

        verify(executionService, times(1))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        assertThat(acString.getValue()).as("Should be IBM ")
                .isEqualTo("IBM");
        assertThat(acDouble.getValue()).as("Should be the value less than 50.00, that is 25.00")
                .isEqualTo(25.00);
        assertThat(acInteger.getValue()).as("Should be the volume purchased").isEqualTo(100);
        assertThat(buyPriceListener.isTradeExecuted())
                .as("Should be the trade is successfully executed").isTrue();
    }
}
