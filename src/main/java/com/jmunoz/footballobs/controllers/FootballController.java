package com.jmunoz.footballobs.controllers;

import com.jmunoz.footballobs.loader.FileLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/football")
public class FootballController {

    private FileLoader fileLoader;

    public FootballController(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    @GetMapping
    public List<String> getTeams() {
        return fileLoader.getTeams();
    }
}
