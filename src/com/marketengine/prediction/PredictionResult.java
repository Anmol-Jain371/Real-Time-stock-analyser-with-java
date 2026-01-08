package com.marketengine.prediction; // predicting and forcasting .

public class PredictionResult {
    private String symbol;
    private double predictedPrice;
    private double confidence;
    private double next1Price;
    private double next2Price;
    private String trend;
    private double volatility;
    private String recommendation;
    
    public PredictionResult(String symbol, double predictedPrice, double confidence,
                          double next1Price, double next2Price, String trend,
                          double volatility, String recommendation) {
        this.symbol = symbol;
        this.predictedPrice = predictedPrice;
        this.confidence = confidence;
        this.next1Price = next1Price;
        this.next2Price = next2Price;
        this.trend = trend;
        this.volatility = volatility;
        this.recommendation = recommendation;
    }
    
    public String getSymbol() { return symbol; }
    public double getPredictedPrice() { return predictedPrice; }
    public double getConfidence() { return confidence; }
    public double getNext1Price() { return next1Price; }
    public double getNext2Price() { return next2Price; }
    public String getTrend() { return trend; }
    public double getVolatility() { return volatility; }
    public String getRecommendation() { return recommendation; }
}