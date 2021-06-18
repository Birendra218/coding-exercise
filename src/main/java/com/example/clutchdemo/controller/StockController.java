package com.example.clutchdemo.controller;

import com.example.clutchdemo.model.ErrorResponse;
import com.example.clutchdemo.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import java.util.Optional;

@RestController
@RequestMapping("stocks")
@ServerEndpoint("/stocks-refresh")
public class StockController {
    private static final String ERR_ADD_STOCK="Unable to add stock, please verify stock name and retry";
    private static final String ERR_STOCK_NOT_IN_WATCHLIST="Stock is not in watch list, please add first to watch list";
    private static final String ERR_STOCK_NOT_ADDED="Stock is not in watch list, please add first to remove from the watch list";


    @Autowired
   StockService stockService;

    @PostMapping("/{stockName}")
    public ResponseEntity<?> addToWatchController(@PathVariable("stockName") String stockName) throws IOException {
           Optional<Stock> optionalStock=this.stockService.addToWatch(stockName);
           if(optionalStock.isPresent()){
               return ResponseEntity.status(HttpStatus.CREATED).body(optionalStock.get());
           }
           return ResponseEntity.badRequest().body(new ErrorResponse(ERR_ADD_STOCK));
    }

    @GetMapping("/{stockName}")
    public ResponseEntity<?> getStock(@PathVariable("stockName") String stockName) throws IOException {
        Optional<Stock> stockOptional = this.stockService.retrieveStock(stockName);
        if(stockOptional.isPresent()) {
            stockOptional.get().print();
            return ResponseEntity.ok(stockOptional.get());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(ERR_STOCK_NOT_IN_WATCHLIST));
    }

    @DeleteMapping("/{stockName}")
    public ResponseEntity<?> deleteStock(@PathVariable("stockName") String stockName) throws IOException {
        Optional<Stock> stockOptional = this.stockService.deleteStock(stockName);
        if(stockOptional.isPresent()) {
            return ResponseEntity.ok(stockOptional.get());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(ERR_STOCK_NOT_ADDED));
    }


    @OnMessage
    public String onMessage(String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this.stockService.refreshStocks());
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
}
