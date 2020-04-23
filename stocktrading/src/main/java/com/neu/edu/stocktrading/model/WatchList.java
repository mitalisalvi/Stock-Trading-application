package com.neu.edu.stocktrading.model;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "watchlist")
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection (fetch = FetchType.EAGER)
    @MapKeyColumn(name = "stock_id")
    @Column(name = "amount")
    @CollectionTable(name = "trade_stock")
    private Map<Integer, Integer> stockToAmount ;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private User user;

    @Column
    private String stocks;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the stocks
     */
    public String getStocks() {
        return stocks;
    }

    /**
     * @param stocks the stocks to set
     */
    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    /**
     * @return the stockToAmount
     */
    public Map<Integer, Integer> getStockToAmount() {
        return stockToAmount;
    }

    /**
     * @param stockToAmount the stockToAmount to set
     */
    public void setStockToAmount(Map<Integer, Integer> stockToAmount) {
        this.stockToAmount = stockToAmount;
    }

}