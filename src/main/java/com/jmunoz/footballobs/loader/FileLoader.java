package com.jmunoz.footballobs.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileLoader {

    private String fileName;
    private List<String> teams;
    private String folder;

    public FileLoader(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getTeams() {
        return teams;
    }

    // Cargar el fichero y mantener el contenido en memoria
    private void loadFile(String fileName) throws Exception {
        // Simulamos que la carga del fichero lleva 10 sg
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        this.fileName = fileName;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        teams = mapper.readValue(file, new TypeReference<List<String>>() {
        });
    }

    // Méto-do público para cargar el primer fichero que se encuentre en la carpeta
    // que se le pasa al constructor.
    public void loadFile() throws IOException {
        Files.list(Paths.get(folder))
                .filter(Files::isRegularFile)
                .findFirst()
                .ifPresent(file -> {
                    try {
                        loadFile(file.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
