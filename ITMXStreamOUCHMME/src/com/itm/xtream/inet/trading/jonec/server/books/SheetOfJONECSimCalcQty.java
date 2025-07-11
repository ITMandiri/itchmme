/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.books;

/**
 *
 * @author fredy
 */
public class SheetOfJONECSimCalcQty {
    
    private final long orderToken;
    
    private long qtyOrder = 0;
    private long qtyMatch = 0;
    private String jatsOrderNo = ""; 
    private String oriJatsOrderNo = "";
    private int orderStatus = 0;
    private String brokerRef = "";
    private String oriBrokerRef = "";
    private long lastOuchSeq = 0;
    private long lastJatsTradeNo = 0;
    
    public SheetOfJONECSimCalcQty(long orderToken){
        this.orderToken = orderToken;
    }
    
    public long getOrderToken() {
        return orderToken;
    }

    public long getQtyOrder() {
        return qtyOrder;
    }

    public void setQtyOrder(long qtyOrder) {
        this.qtyOrder = qtyOrder;
    }

    public long getQtyMatch() {
        return qtyMatch;
    }

    public void setQtyMatch(long qtyMatch) {
        this.qtyMatch = qtyMatch;
    }
    
    public void addQtyMatch(long qtyMatch) {
        this.qtyMatch += qtyMatch;
    }

    public long getQtyLeave() {
        return qtyOrder - qtyMatch;
    }

    public String getJatsOrderNo() {
        return jatsOrderNo;
    }

    public void setJatsOrderNo(String jatsOrderNo) {
        this.jatsOrderNo = jatsOrderNo;
    }

    public String getOriJatsOrderNo() {
        return oriJatsOrderNo;
    }

    public void setOriJatsOrderNo(String oriJatsOrderNo) {
        this.oriJatsOrderNo = oriJatsOrderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBrokerRef() {
        return brokerRef;
    }

    public void setBrokerRef(String brokerRef) {
        this.brokerRef = brokerRef;
    }

    public String getOriBrokerRef() {
        return oriBrokerRef;
    }

    public void setOriBrokerRef(String oriBrokerRef) {
        this.oriBrokerRef = oriBrokerRef;
    }

    public long getLastOuchSeq() {
        return lastOuchSeq;
    }

    public void setLastOuchSeq(long lastOuchSeq) {
        this.lastOuchSeq = lastOuchSeq;
    }

    public long getLastJatsTradeNo() {
        return lastJatsTradeNo;
    }

    public void setLastJatsTradeNo(long lastJatsTradeNo) {
        if (lastJatsTradeNo <= this.lastJatsTradeNo) return; //. tidak boleh mundur
        this.lastJatsTradeNo = lastJatsTradeNo;
    }

}
