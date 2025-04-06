package com.jmunoz.footballobs.actuator;

import com.jmunoz.footballobs.loader.FileLoader;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import java.io.IOException;

// Define el endpoint de Actuator personalizado.
@Endpoint(id = "football")
public class FootballCustomEndpoint {
    private FileLoader fileLoader;

    public FootballCustomEndpoint(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    // Recuperamos la versión del fichero en uso
    @ReadOperation
    public String getFileVersion() {
        return fileLoader.getFileName();
    }

    // Refrescamos el fichero
    @WriteOperation
    public void refreshFile() {
        try {
            fileLoader.loadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
