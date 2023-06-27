package org.js;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DataFilter {
    private static JLabel resultLabel;
    public DataFilter(JLabel resultLabel) {
        this.resultLabel = resultLabel;
    }
    public static void getLaptopsFilteredByScreenResolution(String selectedScreenResolution) {
        try {
            String urlString = "http://localhost:8080/api/laptops/filterLaptopsByScreenResolution?screenResolution=" + selectedScreenResolution;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Odczytanie odpowiedzi serwera
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String jsonResponse = response.toString();
            connection.disconnect();
            String message="Liczba laptopów z wybraną rozdzielczością ekranu: "+jsonResponse;
            resultLabel.setText(message);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static void getLaptopFilteredByManufacturer(String selectedManufacturer){
        try{
            String urlString="http://localhost:8080/api/laptops/filterLaptopsByManufacturer?manufacturer=" + selectedManufacturer;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Odczytanie odpowiedzi serwera
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String jsonResponse = response.toString();
            connection.disconnect();
            String message="Liczba laptopów  z wybranym producentem: "+jsonResponse;
            resultLabel.setText(message);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void getLaptopsFilteredByMatrixType(String selectedMatrixType, DefaultTableModel model){
        try{
            String urlString="http://localhost:8080/api/laptops/filterLaptopsByMatrixType?matrixType=" + selectedMatrixType;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Odczytanie odpowiedzi serwera
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String jsonResponse = response.toString();
            connection.disconnect();
            model.setRowCount(0);
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> list = mapper.readValue(jsonResponse, List.class);
            for (Map<String, Object> item : list) {
                Object[] rowData = new Object[]{
                        item.get("id"),
                        item.get("nazwaProducenta"),
                        item.get("przekatnaEkranu"),
                        item.get("rozdzielczosc"),
                        item.get("rodzajEkranu"),
                        item.get("czyDotykowy"),
                        item.get("nazwaProcesora"),
                        item.get("liczbaRdzeni"),
                        item.get("taktowanie"),
                        item.get("pamiecRam"),
                        item.get("pojemnoscDysku"),
                        item.get("rodzajDysku"),
                        item.get("nazwaUg"),
                        item.get("pamiecUg"),
                        item.get("nazwaSo"),
                        item.get("rodzajNapedu")
                };
                model.addRow(rowData);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
