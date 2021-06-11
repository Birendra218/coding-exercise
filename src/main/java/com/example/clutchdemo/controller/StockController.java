package com.example.clutchdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("stocks")
@ServerEndpoint("/stocks-refresh")
public class StockController {

    private static List<Stock> watchList = new ArrayList<>();

    @GetMapping("/{stockName}")
    public boolean addToWatchController(@PathVariable("stockName") String stockName) throws IOException {
        return this.addToWatch(stockName);
    }

    @OnMessage
    public String onMessage(String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this.refreshStocks());
    }

    @OnOpen
    public void onOpen() {
        System.out.println("Client connected");
    }

    @OnClose
    public void onClose() {
        System.out.println("Connection closed");
    }

    @OnError
    public void OnError(Throwable error) {
        error.printStackTrace();

        System.out.println("Connection error");
    }



    public Stock getStock(String stockName) throws IOException {
        Stock stock = YahooFinance.get(stockName);
        stock.print();
        return stock;
    }


    public boolean addToWatch(String stockName) throws IOException {
        Stock stock = YahooFinance.get(stockName);
        if (stock == null) return false;
        this.watchList.add(stock);
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
