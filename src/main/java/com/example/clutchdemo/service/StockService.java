package com.example.clutchdemo.service;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

@Service
public class StockService {
    private  Map<String,Stock> stockMap=new HashMap<>();


    public Optional<Stock> retrieveStock(String stockName) throws IOException {
       if(stockMap.get(stockName.toUpperCase().trim())!=null){
           return Optional.of(stockMap.get(stockName.toUpperCase().trim()));
       }
        return Optional.empty();
    }

    public Optional<Stock> deleteStock(String stockName) throws IOException{
        Stock valueStock=this.stockMap.remove(stockName.toUpperCase().trim());
        if(valueStock==null){
            return Optional.empty();
        }
       return  Optional.of(valueStock);
    }


    public Optional<Stock> addToWatch(String stockName) throws IOException {
        Stock stock=YahooFinance.get(stockName);
        if(stock!=null){
            this.stockMap.put(stockName.trim().toUpperCase(),stock);
            return Optional.of(stock);
        }
        return Optional.empty();
    }

    public List<Stock> refreshStocks() throws IOException {
        this.stockMap.values().stream().forEach(stock-> {
            try {
                stock.setQuote(stock.getQuote(true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return new ArrayList<Stock>(this.stockMap.values());
    }


}
