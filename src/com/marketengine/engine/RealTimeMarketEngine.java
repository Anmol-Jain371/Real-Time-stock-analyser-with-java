package com.marketengine.engine; // htis is the main thing that runs the 	things

import com.marketengine.models.Stock;
import com.marketengine.models.Alert;

public class RealTimeMarketEngine extends MarketEngine {
    
    public void simulatePriceChange() {
        for (Stock stock : stocks) {
            double newPrice = stock.getCurrentPrice() * (0.99 + Math.random() * 0.02);
            stock.setCurrentPrice(newPrice);
            notifyListeners(stock);
            checkAlerts(stock);
        }
    }
    
    private void checkAlerts(Stock stock) {
        for (Alert alert : alerts) {
            if (alert.getStockSymbol().equals(stock.getSymbol()) && 
                !alert.isTriggered() && 
                stock.getCurrentPrice() >= alert.getTargetPrice()) {
                
                alert.setTriggered(true);
                for (MarketListener listener : listeners) {
                    listener.onAlertTriggered(alert);  
                }
            }
        }
    }
}