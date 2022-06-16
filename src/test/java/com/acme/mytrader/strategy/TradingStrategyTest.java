package com.acme.mytrader.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.acme.mytrader.execution.ExecutionService;

import com.acme.mytrader.price.PriceListener;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    TradingStrategy tradingStrategy;
    @Mock
    ExecutionService tradeExecutionService;

    @Before
    public void init() {
        tradingStrategy = new TradingStrategy(tradeExecutionService);
    }

    @Test
    public void testAutoBuyForSuccessfulBuy() {
        SecurityDTO input = new SecurityDTO("IBM", 50.00, 10);
        tradingStrategy.autoBuy(input);
        Mockito.verify(tradeExecutionService, times(1))
                .buy(anyString(), anyDouble(), anyInt());

    }


    @Test
    public void testAutoBuyForNotSuccessfulBuy() {
        SecurityDTO input = new SecurityDTO("APPLE", 50.00, 10);
        tradingStrategy.autoBuy(input);
        verifyZeroInteractions(tradeExecutionService);
    }
}
