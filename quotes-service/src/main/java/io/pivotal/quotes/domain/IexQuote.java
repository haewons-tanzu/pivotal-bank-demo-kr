package io.pivotal.quotes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexQuote {
    private String symbol;
    private String companyName;
    private String primaryExchange;
    private String calculationPrice;
    private BigDecimal open;
    private Long openTime;
    private BigDecimal close;
    private Long closeTime;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal latestPrice;
    private String latestSource;
    private String latestTime;
    private Long latestUpdate;
    private BigDecimal latestVolume;
    private BigDecimal iexRealtimePrice;
    private String iexRealtimeSize;
    private String iexLastUpdated;
    private BigDecimal delayedPrice;
    private Long delayedPriceTime;
    private String extendedPrice;
    private String extendedChange;
    private BigDecimal extendedChangePercent;
    private Long extendedPriceTime;
    private String previousClose;
    private BigDecimal previousVolume;
    private BigDecimal change;
    private BigDecimal changePercent;
    private String volume;
    private BigDecimal iexMarketPercent;
    private String iexVolume;
    private BigDecimal avgTotalVolume;
    private BigDecimal iexBidPrice;
    private String iexBidSize;
    private BigDecimal iexAskPrice;
    private String iexAskSize;
    private BigDecimal marketCap;
    private String peRatio;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal ytdChange;
    private Long lastTradeTime;
    private String isUSMarketOpen;
}