package com.marketengine.models; // this is for the stocks detaill and all 

public class Stock {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePercent;
    
    public Stock(String symbol, String name, double price, double change, double changePercent) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
    }
    
    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.name = symbol;
        this.price = price;
        this.change = 0.0;
        this.changePercent = 0.0;
    }
    
    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getCurrentPrice() {
        return price;
    }
    
    public void setCurrentPrice(double price) {
        this.price = price;
    }
    
    public double getChange() {
        return change;
    }
    
    public void setChange(double change) {
        this.change = change;
    }
    
    public double getChangePercent() {
        return changePercent;
    }
    
    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f (%.2f%%)", symbol, price, changePercent);
    }
}