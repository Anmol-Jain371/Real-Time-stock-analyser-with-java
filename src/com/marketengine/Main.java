package com.marketengine;

import javax.swing.SwingUtilities;
import com.marketengine.ui.Dashboard;

public class Main {
    public static void main(String[] args) {
        System.out.println(" Starting Real-Time Market Alert Engine");
        System.out.println(" Loading Dashboard Interface...");
        System.out.println(" Connecting to Alpha  API...");
        
        
        SwingUtilities.invokeLater(() -> {
            new Dashboard();  
            System.out.println(" Dashboard launched successfully!");
        });
        
        System.out.println("\n APPLICATION FEATURES ");
        System.out.println("1. Real-time stock prices");
        System.out.println("2. Price alert system");
        System.out.println("3. Predictive chart analysis");
        System.out.println("4. REST API integration");
        System.out.println("5. Multi-threaded updates");
    }
}