package com.marketengine.threads;

import com.marketengine.engine.RealTimeMarketEngine;

public class PriceUpdateThread extends Thread {
    private RealTimeMarketEngine engine;
    
    public PriceUpdateThread(RealTimeMarketEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
               
                engine.simulatePriceChange();  
                
                Thread.sleep(3000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}