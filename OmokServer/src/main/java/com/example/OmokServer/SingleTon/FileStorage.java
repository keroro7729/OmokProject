package com.example.OmokServer.SingleTon;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileStorage {

    private static FileStorage instance = null;
    private final String PATH = "C:\\.omok\\";
    private FileStorage(){
        File directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
        }
    }
    public static synchronized  FileStorage getInstance() {
        if(instance == null)
            instance = new FileStorage();
        return instance;
    }

    public void save(String fileName, String data) throws IOException {
        File file = new File(PATH + fileName);
        // 만약 파일이 없다면 생성
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(data);
        }
    }

    public String read(String fileName) throws IOException {
        File file = new File(PATH + fileName);

        if(!file.exists()){
            throw new FileNotFoundException(fileName+" doesnt exist");
        }

        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public void append(String fileName, String data) throws IOException {
        File file = new File(PATH + fileName);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.newLine();
            writer.write(data);
        }
    }
}
