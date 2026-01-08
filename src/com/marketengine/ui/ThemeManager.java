package com.marketengine.ui;

import java.awt.*;
import javax.swing.*;

public class ThemeManager {
    public static void applyModernTheme() {
        try {
            // SIMPLE theme - no Nimbus issues
            Color primaryColor = new Color(41, 128, 185);
            Color backgroundColor = new Color(236, 240, 241);
            
            UIManager.put("Panel.background", backgroundColor);
            UIManager.put("Button.background", primaryColor);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.selectionBackground", new Color(52, 152, 219));
            
        } catch (Exception e) {
            System.out.println("Theme error, using default");
        }
    }
}