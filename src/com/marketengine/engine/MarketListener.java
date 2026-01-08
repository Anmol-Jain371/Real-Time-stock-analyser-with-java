package com.marketengine.engine; // this is the one where the interface is used if the dashboard calls 

import com.marketengine.models.Stock;
import com.marketengine.models.Alert;

public interface MarketListener {
    void onPriceUpdate(Stock stock);
    void onAlertTriggered(Alert alert);
}