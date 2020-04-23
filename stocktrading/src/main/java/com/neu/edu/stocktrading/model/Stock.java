package com.neu.edu.stocktrading.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock
{
    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column
    private String stockName;

    @Column
    private String stockSymbol;
    
    @Column
    private double currentPrice;
    
    @Column
    private String change;
    
    @Column
    private double buyingPrice;
    
    @Column
    private double sellingPrice;
    
    @Column
    private double high52;
    
    @Column
    private double low52;
    
    @Column
    private String marketType;
    

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return this.stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getCurrentPrice() {
        return this.currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getChange() {
        return this.change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    

    public double getBuyingPrice() {
        return this.buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return this.sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getHigh52() {
        return this.high52;
    }

    public void setHigh52(double high52) {
        this.high52 = high52;
    }

    public double getLow52() {
        return this.low52;
    }

    public void setLow52(double low52) {
        this.low52 = low52;
    }

    public String getMarketType() {
        return this.marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", stockName='" + getStockName() + "'" +
            ", stockSymbol='" + getStockSymbol() + "'" +
            ", currentPrice='" + getCurrentPrice() + "'" +
            ", change='" + getChange() + "'" +
            ", buyingPrice='" + getBuyingPrice() + "'" +
            ", sellingPrice='" + getSellingPrice() + "'" +
            ", high52='" + getHigh52() + "'" +
            ", low52='" + getLow52() + "'" +
            ", marketType='" + getMarketType() + "'" +
            "}";
    }


}