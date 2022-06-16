package com.acme.mytrader.strategy;


import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.execution.TradeExecutionService;
import com.acme.mytrader.price.BuyPriceListener;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */

@Getter
public class TradingStrategy {


    private final ExecutionService tradeExecutionService;
    private static final List<String> SECURITIES = Arrays.asList("IBM", "GOOGLE");

    private static final double RANGE_MIN = 1.00;
    private static final double RANGE_MAX = 100.00;


    public TradingStrategy(ExecutionService tradeExecutionService) {
        this.tradeExecutionService = tradeExecutionService;
    }

    public void autoBuy(SecurityDTO request) {
        BuyPriceListener priceListener = new BuyPriceListener(request.getSecurity(), request.getPriceThreshold(), request.getVolume(),
                tradeExecutionService, false);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            String security = SECURITIES.get(rand.nextInt(SECURITIES.size()));
            double price = RANGE_MIN + (RANGE_MAX - RANGE_MIN) * rand.nextDouble();
            priceListener.priceUpdate(security, price);
        }
    }


    //This is a demo test
    public static void main(String[] args) {
        TradingStrategy tradingStrategy = new TradingStrategy(new TradeExecutionService());
        final SecurityDTO ibm = SecurityDTO.builder().security("IBM").priceThreshold(55.00).volume(100)
                .build();
        tradingStrategy.autoBuy(ibm);


    }


}

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
class SecurityDTO {

    private final String security;
    private final double priceThreshold;
    private final int volume;
}
