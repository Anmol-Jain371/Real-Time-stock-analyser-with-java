package com.marketengine.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;      // ← ADD THIS
import java.util.ArrayList; // ← ADD THIS
import java.util.Collections; // ← ADD THIS

public class SimpleChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public List<Double> priceHistory = new ArrayList<>(); // ← FIXED
    private String stockSymbol = "";
    
    public void setStock(String symbol, List<Double> historicalPrices) {
        this.stockSymbol = symbol;
        this.priceHistory = new ArrayList<>(historicalPrices);
        repaint();
    }
    
    public void addPrice(double price) {
        priceHistory.add(price);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (priceHistory.isEmpty()) {
            g2.drawString("Select a stock to see analysis", getWidth()/2 - 60, getHeight()/2);
            return;
        }
        
        // Title
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(stockSymbol + " - Price Analysis & Prediction", 20, 30);
        
        // Draw grid
        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= 10; i++) {
            int x = 50 + i * (getWidth()-100) / 10;
            int y = 50 + i * (getHeight()-100) / 10;
            g2.drawLine(x, 50, x, getHeight()-50);
            g2.drawLine(50, y, getWidth()-50, y);
        }
        
        // Calculate min/max for scaling
        double maxPrice = Collections.max(priceHistory);
        double minPrice = Collections.min(priceHistory);
        double range = maxPrice - minPrice;
        if (range == 0) range = 1;
        
        // Draw historical prices
        if (priceHistory.size() > 1) {
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            
            for (int i = 1; i < priceHistory.size(); i++) {
                double prev = priceHistory.get(i-1);
                double curr = priceHistory.get(i);
                
                int x1 = 50 + (i-1) * (getWidth()-100) / Math.max(priceHistory.size()-1, 1);
                int y1 = getHeight()-50 - (int)(((prev - minPrice) / range) * (getHeight()-100));
                
                int x2 = 50 + i * (getWidth()-100) / Math.max(priceHistory.size()-1, 1);
                int y2 = getHeight()-50 - (int)(((curr - minPrice) / range) * (getHeight()-100));
                
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        
        // Draw prediction
        if (priceHistory.size() > 5) {
            // Simple moving average prediction
            double last5Avg = priceHistory.subList(priceHistory.size()-5, priceHistory.size())
                .stream().mapToDouble(Double::doubleValue).average().orElse(0);
            
            double prediction = predictNextPrice(priceHistory);
            String trend = getTrend(priceHistory);
            
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            
            int lastX = getWidth() - 50;
            int lastY = getHeight()-50 - (int)(((last5Avg - minPrice) / range) * (getHeight()-100));
            int predX = getWidth() - 30;
            int predY = getHeight()-50 - (int)(((prediction - minPrice) / range) * (getHeight()-100));
            
            g2.drawLine(lastX, lastY, predX, predY);
            
            // Prediction text
            g2.setColor(Color.RED);
            g2.drawString("Prediction: " + trend, getWidth()-150, 80);
            g2.drawString("Next: ₹" + String.format("%.2f", prediction), getWidth()-150, 100);
        }
        
        // Draw axis labels
        g2.setColor(Color.BLACK);
        g2.drawString("Time →", getWidth() - 40, getHeight() - 20);
        g2.drawString("↑ Price", 10, 60);
        g2.drawString(String.format("Max: ₹%.2f", maxPrice), getWidth()-100, 40);
        g2.drawString(String.format("Min: ₹%.2f", minPrice), getWidth()-100, getHeight()-10);
    }
    
    private double predictNextPrice(List<Double> prices) {
        if (prices.size() < 2) return prices.get(0);
        
        // Simple linear regression
        double n = prices.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += prices.get(i);
            sumXY += i * prices.get(i);
            sumX2 += i * i;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        return prices.get(prices.size()-1) + slope;
    }
    
    private String getTrend(List<Double> prices) {
        if (prices.size() < 5) return "NEUTRAL";
        
        double first = prices.get(0);
        double last = prices.get(prices.size()-1);
        double change = ((last - first) / first) * 100;
        
        if (change > 5) return "STRONG BULL ↗";
        if (change > 0) return "BULLISH ↗";
        if (change < -5) return "STRONG BEAR ↘";
        if (change < 0) return "BEARISH ↘";
        return "NEUTRAL →";
    }
}