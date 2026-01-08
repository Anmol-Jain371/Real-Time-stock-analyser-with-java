package com.marketengine.engine; // this is like the structure of the whole parts and all 

import java.util.ArrayList;
import java.util.List;
import com.marketengine.models.Stock;
import com.marketengine.models.Alert;

public class MarketEngine {
    protected List<Stock> stocks = new ArrayList<>();
    protected List<Alert> alerts = new ArrayList<>();
    protected List<MarketListener> listeners = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }

    public void addListener(MarketListener listener) {
        listeners.add(listener);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    protected void notifyListeners(Stock stock) {
        for (MarketListener listener : listeners) {
            listener.onPriceUpdate(stock);
        }
    }
}