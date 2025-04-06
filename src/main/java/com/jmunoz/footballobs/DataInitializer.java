package com.jmunoz.footballobs;

import com.jmunoz.footballobs.loader.FileLoader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    private FileLoader fileLoader;

    public DataInitializer(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fileLoader.loadFile();
    }
}
