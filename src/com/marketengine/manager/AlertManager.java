package com.marketengine.manager; // only one job and its to alert that that if its goooan trigger or what ?

import com.marketengine.models.Alert;
import com.marketengine.models.Stock;
import java.util.*;

public class AlertManager {
    private Map<String, List<Alert>> stockAlerts = new HashMap<>();
    private Set<Alert> triggeredAlerts = new HashSet<>();
    
    public void addAlert(Alert alert) {
        String symbol = alert.getStockSymbol();
        if (!stockAlerts.containsKey(symbol)) {
            stockAlerts.put(symbol, new ArrayList<>());
        }
        stockAlerts.get(symbol).add(alert);
    }
    
    public List<Alert> checkAlerts(Stock stock) {
        List<Alert> triggered = new ArrayList<>();
        List<Alert> alerts = stockAlerts.getOrDefault(stock.getSymbol(), new ArrayList<>());
        
        for (Alert alert : alerts) {
            if (alert.shouldTrigger(stock.getCurrentPrice())) {
                triggered.add(alert);
                triggeredAlerts.add(alert);
            }
        }
        return triggered;
    }
}