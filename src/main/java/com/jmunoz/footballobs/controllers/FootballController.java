package com.jmunoz.footballobs.controllers;

import com.jmunoz.footballobs.loader.FileLoader;
import com.jmunoz.footballobs.services.TradingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/football")
public class FootballController {

    private final FileLoader fileLoader;
    private final TradingService tradingService;

    public FootballController(FileLoader fileLoader, TradingService tradingService) {
        this.fileLoader = fileLoader;
        this.tradingService = tradingService;
    }

    @GetMapping
    public List<String> getTeams() {
        return fileLoader.getTeams();
    }

    @PostMapping
    public int tradeCards(@RequestBody int orders) {
        return tradingService.tradeCards(orders);
    }
}
