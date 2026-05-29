package com.notzed.aiproject.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class StockTools {

    @Tool(description = "Get the market price of the stock")
    public String getStockPrice(String ticker){
        return switch (ticker.toUpperCase()){
            case "AAPL" -> "$182.50";
            case "TSLA" -> "$245.30";
            case "GOOGL" -> "$175.20";
            case "MSFT" -> "$415.80";
            default -> "Price not available for " + ticker;
        };
    }

    @Tool(description = "Buy the stocks")
    public String buyStock(String ticker, int quantity){
        String priceStr = getStockPrice(ticker).replace("$", "");
        double price = Double.parseDouble(priceStr);
        double total = price * quantity;
        return String.format("Order placed: Bought %d shares of %s at $%.2f each. Total: $%.2f",
                quantity, ticker.toUpperCase(), price, total);
    }

}
