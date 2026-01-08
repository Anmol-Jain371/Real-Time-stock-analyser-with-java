package com.marketengine.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.marketengine.engine.MarketListener;
import com.marketengine.engine.RealTimeMarketEngine;
import com.marketengine.models.Alert;
import com.marketengine.models.Stock;
import com.marketengine.models.HistoricalPrice;
import com.marketengine.prediction.StockPredictor;
import com.marketengine.prediction.PredictionResult;
import com.marketengine.ui.TimeSeriesChartPanel;
import com.marketengine.threads.PriceUpdateThread;
import com.marketengine.api.StockAPIService;
import java.util.List;

public class Dashboard extends JFrame implements MarketListener {
    private static final long serialVersionUID = 1L;
    
    private RealTimeMarketEngine engine;
    private PriceUpdateThread updateThread;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;
    private JTextField symbolInput;
    private JTextField priceInput;
    private JButton setAlertButton;

    public Dashboard() {
        ThemeManager.applyModernTheme();
        
        engine = new RealTimeMarketEngine();
        engine.addListener(this);
        
        setTitle(" Stock Alert Pro - Real-Time Market Intelligence");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("STOCK ALERT PRO");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subTitle = new JLabel("Real-Time Market Monitoring & Prediction System");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(220, 220, 220));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(subTitle, BorderLayout.EAST);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        tabbedPane.addTab(" Live Market", createMarketPanel());
        tabbedPane.addTab(" Analysis", createAnalysisPanel());
        tabbedPane.addTab(" Alerts", createAlertPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        
        JLabel footerLabel = new JLabel("© 2024 Stock Alert Pro | Real-time data from Alpha Vantage API");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(footerLabel);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        addSampleStocks();
        
        updateThread = new PriceUpdateThread(engine);
        updateThread.start();
        
        setVisible(true);
    }

    private void addSampleStocks() {
        System.out.println("=== DEBUG: Starting addSampleStocks() ===");
        
        StockAPIService api = new StockAPIService();
        String[] symbols = {"RELIANCE", "TCS", "INFY", "HDFCBANK", "ICICIBANK", "SBIN", 
                           "WIPRO", "AXISBANK", "KOTAKBANK", "BAJFINANCE"};
        
        for (String symbol : symbols) {
            Stock stock = api.getStockData(symbol);
            if (stock != null) {
                System.out.println("DEBUG: Adding " + symbol + " = " + stock.getCurrentPrice());
                
                engine.addStock(stock);
                stockTableModel.addRow(new Object[]{
                    symbol, 
                    String.format("₹%.2f", stock.getCurrentPrice()),
                    "0.00%", 
                    "No"
                });
            }
        }
        
        System.out.println("DEBUG: Total stocks: " + engine.getStocks().size());
        System.out.println("DEBUG: Table rows: " + stockTableModel.getRowCount());
    }

    private void createAlert() {
        try {
            String symbol = symbolInput.getText().trim().toUpperCase();
            double targetPrice = Double.parseDouble(priceInput.getText().trim());

            if (symbol.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a stock symbol.");
                return;
            }

            engine.addAlert(new Alert(symbol, targetPrice));
            JOptionPane.showMessageDialog(this, "Alert set for " + symbol + " at ₹" + targetPrice);
            
            for (int i = 0; i < stockTableModel.getRowCount(); i++) {
                if (stockTableModel.getValueAt(i, 0).equals(symbol)) {
                    stockTableModel.setValueAt("YES", i, 3);
                    break;
                }
            }
            
            symbolInput.setText("");
            priceInput.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price! Enter a valid number.");
        }
    }

    @Override
    public void onPriceUpdate(Stock stock) {
        SwingUtilities.invokeLater(() -> {
            boolean found = false;
            for (int i = 0; i < stockTableModel.getRowCount(); i++) {
                if (stockTableModel.getValueAt(i, 0).equals(stock.getSymbol())) {
                    stockTableModel.setValueAt(String.format("₹%.2f", stock.getCurrentPrice()), i, 1);
                    
                    double change = (Math.random() * 4) - 2; // -2% to +2%
                    stockTableModel.setValueAt(String.format("%.2f%%", change), i, 2);
                    
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                stockTableModel.addRow(new Object[]{
                    stock.getSymbol(), 
                    String.format("₹%.2f", stock.getCurrentPrice()),
                    "0.00%", 
                    "No"
                });
            }
        });
    }

    @Override
    public void onAlertTriggered(Alert alert) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                "ALERT TRIGGERED!\n" + alert.getStockSymbol() + " reached target price ₹" + alert.getTargetPrice(),
                "Price Alert", 
                JOptionPane.WARNING_MESSAGE
            );
        });
    }

    private JPanel createMarketPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // this is for the search bar on the giu
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField searchField = new JTextField(20);
        JTextField priceField = new JTextField(8);
        JButton searchButton = new JButton("Add Stock");
        
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBox.add(new JLabel("Add New Stock:"));
        searchBox.add(searchField);
        searchBox.add(new JLabel("Custom Price (optional):"));
        searchBox.add(priceField);
        searchBox.add(searchButton);
        
        topPanel.add(searchBox, BorderLayout.WEST);
        
        searchButton.addActionListener(e -> {
            String symbol = searchField.getText().trim().toUpperCase();
            String priceText = priceField.getText().trim();
            
            if (!symbol.isEmpty()) {
                if (priceText.isEmpty()) {
                    StockAPIService api = new StockAPIService();
                    Stock stock = api.getStockData(symbol);
                    if (stock != null) {
                        engine.addStock(stock);
                        stockTableModel.addRow(new Object[]{
                            symbol,
                            String.format("₹%.2f", stock.getCurrentPrice()),
                            "0.00%",
                            "No"
                        });
                        searchField.setText("");
                        priceField.setText("");
                    }
                } else {
                    try {
                        double customPrice = Double.parseDouble(priceText);
                        Stock stock = new Stock(symbol, customPrice);
                        engine.addStock(stock);
                        stockTableModel.addRow(new Object[]{
                            symbol,
                            String.format("₹%.2f", customPrice),
                            "0.00%",
                            "No"
                        });
                        searchField.setText("");
                        priceField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid price format! Enter numbers only.");
                    }
                }
            }
        });
        
        // CENTER: Stock table
        String[] columns = {"Symbol", "Current Price", "Change", "Alert Set"};
        stockTableModel = new DefaultTableModel(columns, 0);
        stockTable = new JTable(stockTableModel) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        // Style the table
        stockTable.setRowHeight(30);
        stockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        stockTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        stockTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        stockTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        stockTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JPanel alertPanel = new JPanel(new FlowLayout());
        alertPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        symbolInput = new JTextField(10);
        priceInput = new JTextField(10);
        setAlertButton = new JButton("Set Alert");
        setAlertButton.addActionListener(e -> createAlert());
        
        alertPanel.add(new JLabel("Set Alert:"));
        alertPanel.add(symbolInput);
        alertPanel.add(new JLabel("Target Price:"));
        alertPanel.add(priceInput);
        alertPanel.add(setAlertButton);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        panel.add(alertPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField symbolField = new JTextField(10);
        JButton analyzeButton = new JButton("Analyze & Predict");
        JLabel resultLabel = new JLabel("<html><div style='width:300px;'>Enter stock symbol and click Analyze</div></html>");
        
        String[] sampleSymbols = {"RELIANCE", "TCS", "INFY", "HDFCBANK", "ICICIBANK"};
        JComboBox<String> sampleBox = new JComboBox<>(sampleSymbols);
        sampleBox.addActionListener(e -> {
            symbolField.setText((String)sampleBox.getSelectedItem());
        });
        
        controlPanel.add(new JLabel("Stock Symbol:"));
        controlPanel.add(symbolField);
        controlPanel.add(new JLabel("Quick Pick:"));
        controlPanel.add(sampleBox);
        controlPanel.add(analyzeButton);
        
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("Analysis Results"));
        resultPanel.add(resultLabel);
        
        TimeSeriesChartPanel timeSeriesChart = new TimeSeriesChartPanel();
        timeSeriesChart.setBackground(Color.WHITE);
        timeSeriesChart.setPreferredSize(new Dimension(700, 300));
        
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createTitledBorder("Historical Price Chart"));
        chartContainer.add(timeSeriesChart, BorderLayout.CENTER);
        
        analyzeButton.addActionListener(e -> {
            String symbol = symbolField.getText().trim().toUpperCase();
            if (!symbol.isEmpty()) {
                analyzeButton.setText("Analyzing...");
                analyzeButton.setEnabled(false);
                resultLabel.setText("<html>Analyzing " + symbol + "...</html>");
                
                new Thread(() -> {
                    try {
                        // Get historical data
                        StockAPIService api = new StockAPIService();
                        List<HistoricalPrice> history = api.getHistoricalData(symbol, 30);
                        
                        SwingUtilities.invokeLater(() -> {
                            timeSeriesChart.setHistoricalData(symbol, history);
                        });
                        
                        StockPredictor predictor = new StockPredictor();
                        PredictionResult result = predictor.predictNextTwo(history);
                        
                        SwingUtilities.invokeLater(() -> {
                            if (result != null) {
                                boolean isUpward = "UPWARD".equals(result.getTrend());
                                double trendValue = isUpward ? result.getNext1Price() - 100 : -(result.getNext1Price() - 100);
                                
                                String resultText = String.format(
                                    "<html><div style='width:350px;'>" +
                                    "<b><font size='4'>%s PREDICTION ANALYSIS</font></b><br><hr>" +
                                    "<b>Next Period Forecast:</b> ₹%.2f<br>" +
                                    "<b>Following Period:</b> ₹%.2f<br>" +
                                    "<b>Trend Direction:</b> <span style='color:%s;'><b>%s</b></span><br>" +
                                    "<b>Market Volatility:</b> %.2f%%<br>" +
                                    "<b>Prediction Confidence:</b> %.1f%%<br><br>" +
                                    "<b style='color:%s;font-size:14px;'>RECOMMENDATION: %s</b><br>" +
                                    "</div></html>",
                                    symbol,
                                    result.getNext1Price(),
                                    result.getNext2Price(),
                                    isUpward ? "green" : "red",
                                    isUpward ? "BULLISH ↗" : "BEARISH ↘",
                                    result.getVolatility(),
                                    result.getConfidence(),
                                    result.getRecommendation().equals("BUY") ? "green" : 
                                    result.getRecommendation().equals("SELL") ? "red" : "orange",
                                    result.getRecommendation()
                                );
                                resultLabel.setText(resultText);
                            } else {
                                resultLabel.setText("<html><b style='color:red;'>Error:</b> Unable to generate prediction</html>");
                            }
                            
                            analyzeButton.setText("Analyze & Predict");
                            analyzeButton.setEnabled(true);
                        });
                        
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            resultLabel.setText("<html><b style='color:red;'>Error:</b> " + ex.getMessage() + "</html>");
                            analyzeButton.setText("Analyze & Predict");
                            analyzeButton.setEnabled(true);
                        });
                    }
                }).start();
            }
        });
        
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(controlPanel, BorderLayout.NORTH);
        topSection.add(chartContainer, BorderLayout.CENTER);
        
        panel.add(topSection, BorderLayout.CENTER);
        panel.add(resultPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createAlertPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        DefaultListModel<String> alertListModel = new DefaultListModel<>();
        JList<String> alertList = new JList<>(alertListModel);
        alertList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        alertList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value.toString().contains("[TRIGGERED]")) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (value.toString().contains("[ACTIVE]")) {
                    c.setForeground(new Color(0, 128, 0)); // Green
                }
                
                return c;
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("🔄 Refresh Alerts");
        JButton clearButton = new JButton("🗑️ Clear All");
        
        refreshButton.addActionListener(e -> {
            alertListModel.clear();
            if (engine.getAlerts().isEmpty()) {
                alertListModel.addElement("No alerts set. Add alerts in Market tab.");
            } else {
                for (Alert alert : engine.getAlerts()) {
                    String status = alert.isTriggered() ? "[TRIGGERED]" : "[ACTIVE]";
                    String color = alert.isTriggered() ? "red" : "green";
                    alertListModel.addElement(String.format(
                        "<html><b>%s</b> → Target: ₹%.2f <span style='color:%s;'>%s</span></html>",
                        alert.getStockSymbol(), 
                        alert.getTargetPrice(),
                        color,
                        status
                    ));
                }
            }
        });
        
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,  "Clear all alerts?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                alertListModel.clear();
                engine.getAlerts().clear();
                alertListModel.addElement("All alerts cleared.");
                
                for (int i = 0; i < stockTableModel.getRowCount(); i++) {
                    stockTableModel.setValueAt("No", i, 3);
                }
            }
        });
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        
        JLabel headerLabel = new JLabel(" Active Alerts", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(alertList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshButton.doClick();
        
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Dashboard();
        });
    }
}