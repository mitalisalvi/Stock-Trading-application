package com.neu.edu.stocktrading.model;

import java.util.Objects;

public class StockAPIBean
{

    private String symbol;
    private String exchange;
    private String name;
    private String dayCode;
    private String serverTimestamp;
    private String mode;
    private String lastPrice;
    private String tradeTimestamp;
    private String netChange;
    private String percentChange;
    private String unitCode;
    private String open;
    private String high = "0.0";
    private String low = "0.0";
    private String close;
    private String flag;
    private String volume;
    private String fiftyTwoWkHigh = "0.0";
    private String fiftyTwoWkHighDate;
    private String fiftyTwoWkLow = "0.0";
    private String fiftyTwoWkLowDate;


    public StockAPIBean() {
    }

    public StockAPIBean(String symbol, String exchange, String name, String dayCode, String serverTimestamp, String mode, String lastPrice, String tradeTimestamp, String netChange, String percentChange, String unitCode, String open, String high, String low, String close, String flag, String volume, String fiftyTwoWkHigh, String fiftyTwoWkHighDate, String fiftyTwoWkLow, String fiftyTwoWkLowDate) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.name = name;
        this.dayCode = dayCode;
        this.serverTimestamp = serverTimestamp;
        this.mode = mode;
        this.lastPrice = lastPrice;
        this.tradeTimestamp = tradeTimestamp;
        this.netChange = netChange;
        this.percentChange = percentChange;
        this.unitCode = unitCode;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.flag = flag;
        this.volume = volume;
        this.fiftyTwoWkHigh = fiftyTwoWkHigh;
        this.fiftyTwoWkHighDate = fiftyTwoWkHighDate;
        this.fiftyTwoWkLow = fiftyTwoWkLow;
        this.fiftyTwoWkLowDate = fiftyTwoWkLowDate;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayCode() {
        return this.dayCode;
    }

    public void setDayCode(String dayCode) {
        this.dayCode = dayCode;
    }

    public String getServerTimestamp() {
        return this.serverTimestamp;
    }

    public void setServerTimestamp(String serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLastPrice() {
        return this.lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getTradeTimestamp() {
        return this.tradeTimestamp;
    }

    public void setTradeTimestamp(String tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }

    public String getNetChange() {
        return this.netChange;
    }

    public void setNetChange(String netChange) {
        this.netChange = netChange;
    }

    public String getPercentChange() {
        return this.percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getOpen() {
        return this.open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return this.high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return this.low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return this.close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVolume() {
        return this.volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFiftyTwoWkHigh() {
        return this.fiftyTwoWkHigh;
    }

    public void setFiftyTwoWkHigh(String fiftyTwoWkHigh) {
        this.fiftyTwoWkHigh = fiftyTwoWkHigh;
    }

    public String getFiftyTwoWkHighDate() {
        return this.fiftyTwoWkHighDate;
    }

    public void setFiftyTwoWkHighDate(String fiftyTwoWkHighDate) {
        this.fiftyTwoWkHighDate = fiftyTwoWkHighDate;
    }

    public String getFiftyTwoWkLow() {
        return this.fiftyTwoWkLow;
    }

    public void setFiftyTwoWkLow(String fiftyTwoWkLow) {
        this.fiftyTwoWkLow = fiftyTwoWkLow;
    }

    public String getFiftyTwoWkLowDate() {
        return this.fiftyTwoWkLowDate;
    }

    public void setFiftyTwoWkLowDate(String fiftyTwoWkLowDate) {
        this.fiftyTwoWkLowDate = fiftyTwoWkLowDate;
    }

    public StockAPIBean symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public StockAPIBean exchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public StockAPIBean name(String name) {
        this.name = name;
        return this;
    }

    public StockAPIBean dayCode(String dayCode) {
        this.dayCode = dayCode;
        return this;
    }

    public StockAPIBean serverTimestamp(String serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
        return this;
    }

    public StockAPIBean mode(String mode) {
        this.mode = mode;
        return this;
    }

    public StockAPIBean lastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public StockAPIBean tradeTimestamp(String tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
        return this;
    }

    public StockAPIBean netChange(String netChange) {
        this.netChange = netChange;
        return this;
    }

    public StockAPIBean percentChange(String percentChange) {
        this.percentChange = percentChange;
        return this;
    }

    public StockAPIBean unitCode(String unitCode) {
        this.unitCode = unitCode;
        return this;
    }

    public StockAPIBean open(String open) {
        this.open = open;
        return this;
    }

    public StockAPIBean high(String high) {
        this.high = high;
        return this;
    }

    public StockAPIBean low(String low) {
        this.low = low;
        return this;
    }

    public StockAPIBean close(String close) {
        this.close = close;
        return this;
    }

    public StockAPIBean flag(String flag) {
        this.flag = flag;
        return this;
    }

    public StockAPIBean volume(String volume) {
        this.volume = volume;
        return this;
    }

    public StockAPIBean fiftyTwoWkHigh(String fiftyTwoWkHigh) {
        this.fiftyTwoWkHigh = fiftyTwoWkHigh;
        return this;
    }

    public StockAPIBean fiftyTwoWkHighDate(String fiftyTwoWkHighDate) {
        this.fiftyTwoWkHighDate = fiftyTwoWkHighDate;
        return this;
    }

    public StockAPIBean fiftyTwoWkLow(String fiftyTwoWkLow) {
        this.fiftyTwoWkLow = fiftyTwoWkLow;
        return this;
    }

    public StockAPIBean fiftyTwoWkLowDate(String fiftyTwoWkLowDate) {
        this.fiftyTwoWkLowDate = fiftyTwoWkLowDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StockAPIBean)) {
            return false;
        }
        StockAPIBean stockAPIBean = (StockAPIBean) o;
        return Objects.equals(symbol, stockAPIBean.symbol) && Objects.equals(exchange, stockAPIBean.exchange) && Objects.equals(name, stockAPIBean.name) && Objects.equals(dayCode, stockAPIBean.dayCode) && Objects.equals(serverTimestamp, stockAPIBean.serverTimestamp) && Objects.equals(mode, stockAPIBean.mode) && Objects.equals(lastPrice, stockAPIBean.lastPrice) && Objects.equals(tradeTimestamp, stockAPIBean.tradeTimestamp) && Objects.equals(netChange, stockAPIBean.netChange) && Objects.equals(percentChange, stockAPIBean.percentChange) && Objects.equals(unitCode, stockAPIBean.unitCode) && Objects.equals(open, stockAPIBean.open) && Objects.equals(high, stockAPIBean.high) && Objects.equals(low, stockAPIBean.low) && Objects.equals(close, stockAPIBean.close) && Objects.equals(flag, stockAPIBean.flag) && Objects.equals(volume, stockAPIBean.volume) && Objects.equals(fiftyTwoWkHigh, stockAPIBean.fiftyTwoWkHigh) && Objects.equals(fiftyTwoWkHighDate, stockAPIBean.fiftyTwoWkHighDate) && Objects.equals(fiftyTwoWkLow, stockAPIBean.fiftyTwoWkLow) && Objects.equals(fiftyTwoWkLowDate, stockAPIBean.fiftyTwoWkLowDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, exchange, name, dayCode, serverTimestamp, mode, lastPrice, tradeTimestamp, netChange, percentChange, unitCode, open, high, low, close, flag, volume, fiftyTwoWkHigh, fiftyTwoWkHighDate, fiftyTwoWkLow, fiftyTwoWkLowDate);
    }

    @Override
    public String toString() {
        return "{" +
            " symbol='" + getSymbol() + "'" +
            ", exchange='" + getExchange() + "'" +
            ", name='" + getName() + "'" +
            ", dayCode='" + getDayCode() + "'" +
            ", serverTimestamp='" + getServerTimestamp() + "'" +
            ", mode='" + getMode() + "'" +
            ", lastPrice='" + getLastPrice() + "'" +
            ", tradeTimestamp='" + getTradeTimestamp() + "'" +
            ", netChange='" + getNetChange() + "'" +
            ", percentChange='" + getPercentChange() + "'" +
            ", unitCode='" + getUnitCode() + "'" +
            ", open='" + getOpen() + "'" +
            ", high='" + getHigh() + "'" +
            ", low='" + getLow() + "'" +
            ", close='" + getClose() + "'" +
            ", flag='" + getFlag() + "'" +
            ", volume='" + getVolume() + "'" +
            ", fiftyTwoWkHigh='" + getFiftyTwoWkHigh() + "'" +
            ", fiftyTwoWkHighDate='" + getFiftyTwoWkHighDate() + "'" +
            ", fiftyTwoWkLow='" + getFiftyTwoWkLow() + "'" +
            ", fiftyTwoWkLowDate='" + getFiftyTwoWkLowDate() + "'" +
            "}";
    }




      
}