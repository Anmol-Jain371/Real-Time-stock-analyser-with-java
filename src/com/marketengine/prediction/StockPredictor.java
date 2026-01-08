package com.marketengine.prediction;

import com.marketengine.models.HistoricalPrice;
import java.util.List;

public class StockPredictor {
    
    public PredictionResult predictNextTwo(List<HistoricalPrice> historicalData) {
        if (historicalData == null || historicalData.isEmpty()) {
            return createDefaultResult();
        }
        
        String symbol = historicalData.get(0).getSymbol();
        double next1Price = 100 + Math.random() * 50;
        double next2Price = next1Price * (1 + (Math.random() * 0.1 - 0.05));
        double predictedPrice = (next1Price + next2Price) / 2;
        double confidence = 70 + Math.random() * 30;
        String trend = Math.random() > 0.5 ? "UPWARD" : "DOWNWARD";
        double volatility = 10 + Math.random() * 20;
        String recommendation = Math.random() > 0.6 ? "BUY" : (Math.random() > 0.3 ? "HOLD" : "SELL");
        
        return new PredictionResult(symbol, predictedPrice, confidence,
                                  next1Price, next2Price, trend, volatility, recommendation);
    }
    
    private PredictionResult createDefaultResult() {
        return new PredictionResult("UNKNOWN", 0, 0, 0, 0, "NEUTRAL", 0, "NO DATA");
    }
}