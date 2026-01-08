package com.marketengine.models; // this model is for the alerting message pop up when the price is hit 

public class Alert {
    private String stockSymbol;
    private double targetPrice;
    private boolean triggered;
    
    public Alert(String stockSymbol, double targetPrice) {
        this.stockSymbol = stockSymbol;
        this.targetPrice = targetPrice;
        this.triggered = false;
    }
    
    public boolean shouldTrigger(double currentPrice) {
        return currentPrice >= targetPrice;
    }
    
    public double getTargetPrice() {
        return targetPrice;
    }
    
    public String getStockSymbol() { return stockSymbol; }
    public boolean isTriggered() { return triggered; }
    public void setTriggered(boolean triggered) { this.triggered = triggered; }
}