package com.jmunoz.footballobs.config;

import com.jmunoz.footballobs.actuator.FootballCustomEndpoint;
import com.jmunoz.footballobs.loader.FileLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FootballConfiguration {

    @Value("${football.folder}")
    private String folder;

    @Bean
    public FileLoader fileLoader() throws IOException {
        FileLoader fileLoader = new FileLoader(folder);
        return fileLoader;
    }

    @Bean
    public FootballCustomEndpoint footballCustomEndpoint(FileLoader fileLoader) {
        return new FootballCustomEndpoint(fileLoader);
    }
}
