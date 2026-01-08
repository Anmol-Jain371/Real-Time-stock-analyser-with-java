package com.marketengine.models; // this is the one which will analyse the historical data and 

public class HistoricalPrice {
    private String symbol;
    private double price;
    private String date;
    
    public HistoricalPrice(String symbol, double price, String date) {
        this.symbol = symbol;
        this.price = price;
        this.date = date;
    }
    
    // Getters and setters...
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public String getDate() { return date; }
    
    
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setPrice(double price) { this.price = price; }
    public void setDate(String date) { this.date = date; }
}