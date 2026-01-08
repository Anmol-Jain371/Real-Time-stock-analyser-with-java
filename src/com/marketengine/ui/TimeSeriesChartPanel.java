package com.marketengine.ui;

import com.marketengine.models.HistoricalPrice;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.List;

public class TimeSeriesChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private String symbol;
    private List<HistoricalPrice> historicalData;
    
    public void setHistoricalData(String symbol, List<HistoricalPrice> historicalData) {
        this.symbol = symbol;
        this.historicalData = historicalData;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (historicalData == null || historicalData.isEmpty()) {
            g.drawString("No historical data available", 50, 50);
            return;
        }
        
        // Simple chart drawing
        int width = getWidth();
        int height = getHeight();
        g.drawString("Chart for: " + symbol, 10, 20);
        
        // Draw simple line chart
        int x = 50;
        int prevY = -1;
        
        for (int i = 0; i < historicalData.size(); i++) {
            HistoricalPrice hp = historicalData.get(i);
            int y = height - 50 - (int)(hp.getPrice() % 100);
            
            if (prevY != -1) {
                g.drawLine(x - 20, prevY, x, y);
            }
            
            g.fillOval(x - 2, y - 2, 4, 4);
            prevY = y;
            x += 20;
        }
    }
}