package com.neu.edu.stocktrading.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "transaction")
public class Transaction
{
    public Transaction()
    {

    }
    
    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date timestampdate;

    @Column
    private double totalPrice;

    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER, mappedBy = "transaction")
    private Set<Trade> allTrades;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable=false)
    private User user;


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestampdate() {
        return this.timestampdate;
    }

    public void setTimestampdate(Date timestampdate) {
        this.timestampdate = timestampdate;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<Trade> getAllTrades() {
        return this.allTrades;
    }

    public void setAllTrades(Set<Trade> allTrades) {
        this.allTrades = allTrades;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", timestampdate='" + getTimestampdate() + "'" +
            ", totalPrice='" + getTotalPrice() + "'" +
            "}";
    }


    
    
}