package com.marketengine.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Nifty50List {
    public static List<String> loadSymbols() {
        List<String> symbols = new ArrayList<>();
        
        try {
            // Try absolute path from project root
            String filePath = "src/com/marketengine/data/nifty50.csv";
            System.out.println("DEBUG: Trying path: " + filePath);
            
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            System.out.println("DEBUG: CSV loaded successfully!");
            
            String line;
            boolean firstLine = true;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    System.out.println("DEBUG: Skipping header: " + line);
                    firstLine = false;
                    continue;
                }
                System.out.println("DEBUG: Line " + (++count) + ": " + line);
                symbols.add(line.trim());
            }
            br.close();
            
            System.out.println("DEBUG: Success! Loaded " + symbols.size() + " Nifty50 stocks");
            
        } catch (Exception e) {
            System.out.println("DEBUG: ERROR: " + e.getMessage());
            System.out.println("DEBUG: Using fallback symbols");
            // Fallback
            symbols.add("RELIANCE");
            symbols.add("TCS");
            symbols.add("INFY");
        }
        
        return symbols;
    }
	}