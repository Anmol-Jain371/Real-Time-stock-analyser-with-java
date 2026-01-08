package com.marketengine.api; // this is for the getting the stoclks data from the online api called 

import java.util.ArrayList;
import java.util.List;
import com.marketengine.models.Stock;
import com.marketengine.models.HistoricalPrice;  // Add this import

public class StockAPIService {
    
    public Stock getStockData(String symbol) {
        double price = 100.0 + (Math.random() * 50);
        double change = Math.random() * 10 - 5;
        double changePercent = (change / (price - change)) * 100;
        
        return new Stock(symbol, symbol + " Inc.", price, change, changePercent);
    }
    
    public List<HistoricalPrice> getHistoricalData(String symbol, int days) {
        List<HistoricalPrice> historicalData = new ArrayList<>();
        
        for (int i = days; i > 0; i--) {
            double price = 100.0 + (Math.random() * 50) + (i * 0.5);
            String date = String.format("2025-12-%02d", i);
            historicalData.add(new HistoricalPrice(symbol, price, date));
        }
        
        return historicalData;  
    }
    
    public Stock getMockStockData(String symbol) {
        return getStockData(symbol);  
    }
}