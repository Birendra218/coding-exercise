package com.example.clutchdemo.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockService {
    List<Stock> watchList = new ArrayList<>();

    public Stock getStock(String stockName) throws IOException {
        Stock stock = YahooFinance.get(stockName);
        stock.print();
        return stock;
    }


    public boolean addToWatch(String stockName) throws IOException {
        Stock stock = YahooFinance.get(stockName);
        if (stock == null) return false;
        watchList.add(stock);
        return true;
    }
    public List<Stock> refreshStocks() throws IOException {
        this.watchList.stream().forEach(stock-> {
            try {
                stock.setQuote(stock.getQuote(true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return this.watchList;
    }
}
