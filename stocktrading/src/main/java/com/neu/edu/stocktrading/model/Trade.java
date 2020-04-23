package com.neu.edu.stocktrading.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class Trade
{
    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private Stock stock;

    @Column
    private double individualPrice;

    @Column
    private String transId;

    @Column
    private int quantity;

    @Column
    private String buySell;


    @ManyToOne(fetch = FetchType.EAGER , cascade=CascadeType.ALL)
    private Transaction transaction;





    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBuySell() {
        return this.buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the stock
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(Stock stock) {
        this.stock = stock;
    }

    /**
     * @return the transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * @param transaction the transaction to set
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * @return the individualPrice
     */
    public double getIndividualPrice() {
        return individualPrice;
    }

    /**
     * @param individualPrice the individualPrice to set
     */
    public void setIndividualPrice(double individualPrice) {
        this.individualPrice = individualPrice;
    }

    /**
     * @return the transId
     */
    public String getTransId() {
        return transId;
    }

    /**
     * @param transId the transId to set
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }




}