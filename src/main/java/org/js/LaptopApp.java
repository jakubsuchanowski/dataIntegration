package org.js;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.JsonArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class LaptopApp extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton importButton;
    private JButton exportButton;
    private JButton importFromXmlButton;
    private JButton exportToXmlButton;

    private JButton importDatabaseButton;
    private JButton exportDatabaseButton;
    private JLabel resultLabel;

    private Connection connection;
    private final static String databaseUrl = "jdbc:mysql://localhost:3306/is";
    private final static String username = "root";
    private final static String password = "root";



    public LaptopApp() {
        setTitle("Integracja systemów - Jakub Suchanowski");
        setSize(1000, 400);

        // Tworzenie tabeli
        String[] columnNames = {"Nazwa producenta", "Przekątna ekranu", "Rozdzielczość", "Rodzaj ekranu",
                "Czy dotykowy", "Nazwa procesora", "Liczba rdzeni", "Taktowanie MHz", "Pamięć RAM", "Pojemność dysku",
                "Rodzaj dysku", "Nazwa układu graficznego", "Pamięć układu graficznego", "Nazwa SO", "Rodzaj napędu"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Umożliwienie edycji komórek w tabeli
            }
        };
        table = new JTable(model);

        // Dodawanie przycisków
        importButton = new JButton("Wczytaj dane z pliku TXT");
        importButton.addActionListener(new ImportButtonListener());
        exportButton = new JButton("Zapisz dane do pliku TXT");
        exportButton.addActionListener(new ExportButtonListener());

        importFromXmlButton = new JButton("Wczytaj dane z pliku XML");
        importFromXmlButton.addActionListener(new ImportFromXMLButtonListener());

        exportToXmlButton = new JButton("Zapisz dane do pliku XML");
        exportToXmlButton.addActionListener(new ExportToXMLButtonListener());

        importDatabaseButton = new JButton("Wczytaj z bazy");
        exportDatabaseButton = new JButton("Zapisz do bazy");



        importDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importDataFromDatabase();
            }
        });

        exportDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportDataToDatabase();
            }
        });


        //do rest
        resultLabel=new JLabel();
        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);

        JComboBox<String> screenResolutionComboBox = new JComboBox<>(new String[]{"1920x1080","1366x768","1280x800","1600x900","null"});
        JButton screenResolutionButton = new JButton("Liczba laptopów z wybraną rozdzielczością");

        JComboBox<String> manufacturerComboBox = new JComboBox<>(new String[]{"Dell", "Asus", "Fujitsu","Huawei", "MSI","Samsung", "Sony"});
        JButton manufacturerButton = new JButton("Liczba laptopów z wybranej firmy");

        JComboBox<String> matrixTypeComboBox = new JComboBox<>(new String[]{"blyszczaca", "matowa", "null"});
        JButton matrixTypeButton = new JButton("Laptopy o wybranej matrycy");

        manufacturerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedManufacturer = (String) manufacturerComboBox.getSelectedItem();
                getLaptopFilteredByManufacturer(selectedManufacturer);
            }
        });
        screenResolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedScreenResolution = (String) screenResolutionComboBox.getSelectedItem();
                getLaptopsFilteredByScreenResolution(selectedScreenResolution);
            }
        });

        matrixTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMatrixType = (String) matrixTypeComboBox.getSelectedItem();
                getLaptopsFilteredByMatrixType(selectedMatrixType);
            }
        });

        JButton addNewRecordButton = new JButton("Dodaj laptop");
        addNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



        // Dodawanie tabeli i przycisków do okna
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel(new GridLayout(2,1));
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        row1.add(importButton);
        row1.add(exportButton);
        row1.add(importFromXmlButton);
        row1.add(exportToXmlButton);
        row1.add(importDatabaseButton);
        row1.add(exportDatabaseButton);
        add(scrollPane, BorderLayout.CENTER);
        row2.add(screenResolutionComboBox);
        row2.add(screenResolutionButton);
        row2.add(manufacturerComboBox);
        row2.add(manufacturerButton);
        row2.add(matrixTypeComboBox);
        row2.add(matrixTypeButton);
        buttonPanel.add(row1);
        buttonPanel.add(row2);
        add(buttonPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.SOUTH);

        JPanel editPanel = new JPanel(new FlowLayout(3));
        add(editPanel, BorderLayout.EAST);
        editPanel.add(addNewRecordButton);



    }

    private class ImportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    model.setRowCount(0);
//                    File file = new File("C:\\Users\\kubas\\Documents\\katalog.txt");
                    // Odczytanie danych z pliku i dodanie ich do tabeli
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(";");
                        model.addRow(data);
                    }
                    br.close();
                    JOptionPane.showMessageDialog(null, "Dane zaimportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ExportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
//                File file = new File("C:\\Users\\kubas\\Documents\\katalog2.txt");

                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            bw.write(model.getValueAt(i, j) + ";");
                        }
                        bw.newLine();
                    }
                    bw.close();
                    JOptionPane.showMessageDialog(null, "Dane wyeksportowane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private class ImportFromXMLButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    model.setRowCount(0);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(selectedFile);
                    doc.getDocumentElement().normalize();
                    NodeList laptopNodes = doc.getElementsByTagName("laptop");
                    for (int i = 0; i < laptopNodes.getLength(); i++) {
                        Element laptopElement = (Element) laptopNodes.item(i);
                        String manufacturer = getElementValue(laptopElement, "manufacturer");
                        Element screenElement = (Element) laptopElement.getElementsByTagName("screen").item(0);
                        String touch = screenElement.getAttribute("touch");
                        String size = getElementValue(screenElement, "size");
                        String resolution = getElementValue(screenElement, "resolution");
                        String screenType = getElementValue(screenElement, "type");
                        Element processorElement = (Element) laptopElement.getElementsByTagName("processor").item(0);
                        String processorName = getElementValue(processorElement, "name");
                        String cores = getElementValue(processorElement, "physical_cores");
                        String speed = getElementValue(processorElement, "clock_speed");
                        String ram = getElementValue(laptopElement, "ram");
                        Element discElement = (Element) laptopElement.getElementsByTagName("disc").item(0);
                        String discType = discElement.getAttribute("type");
                        String storage = getElementValue(discElement, "storage");
                        Element graphicCardElement = (Element) laptopElement.getElementsByTagName("graphic_card").item(0);
                        String graphicCardName = getElementValue(graphicCardElement, "name");
                        String graphicCardMemory = getElementValue(graphicCardElement, "memory");
                        String os = getElementValue(laptopElement, "os");
                        String discReader = getElementValue(laptopElement, "disc_reader");


                        String[] parts = {manufacturer, size, resolution,
                                screenType, touch, processorName, cores, speed,
                                ram, storage, discType, graphicCardName, graphicCardMemory,
                                os, discReader};
                        model.addRow(parts);
                    }

                    JOptionPane.showMessageDialog(null, "Dane zaimportowane");

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private class ExportToXMLButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.newDocument();
                    Element laptopsElement = doc.createElement("laptops");
                    laptopsElement.setAttribute("moddate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
                    doc.appendChild(laptopsElement);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Element laptopElement = doc.createElement("laptop");
                        laptopElement.setAttribute("id", String.valueOf(i + 1));

                        Element manufacturerElement = doc.createElement("manufacturer");
                        manufacturerElement.setTextContent(model.getValueAt(i, 0).toString());
                        laptopElement.appendChild(manufacturerElement);

                        Element screenElement = doc.createElement("screen");
                        screenElement.setAttribute("touch", model.getValueAt(i, 4).toString());

                        Element sizeElement = doc.createElement("size");
                        sizeElement.setTextContent(model.getValueAt(i, 1).toString());
                        screenElement.appendChild(sizeElement);

                        Element resolutionElement = doc.createElement("resolution");
                        resolutionElement.setTextContent(model.getValueAt(i, 2).toString());
                        screenElement.appendChild(resolutionElement);

                        Element typeElement = doc.createElement("type");
                        typeElement.setTextContent(model.getValueAt(i, 3).toString());
                        screenElement.appendChild(typeElement);
                        laptopElement.appendChild(screenElement);

                        Element processorElement = doc.createElement("processor");

                        Element processorNameElement = doc.createElement("name");
                        processorNameElement.setTextContent(model.getValueAt(i, 5).toString());
                        processorElement.appendChild(processorNameElement);

                        Element physcialCoresElement = doc.createElement("physical_cores");
                        physcialCoresElement.setTextContent(model.getValueAt(i, 6).toString());
                        processorElement.appendChild(physcialCoresElement);

                        Element clockSpeedElement = doc.createElement("clock_speed");
                        clockSpeedElement.setTextContent(model.getValueAt(i, 7).toString());
                        processorElement.appendChild(clockSpeedElement);
                        laptopElement.appendChild(processorElement);


                        Element ramElement = doc.createElement("ram");
                        ramElement.setTextContent(model.getValueAt(i, 8).toString());

                        laptopElement.appendChild(ramElement);

                        Element discElement = doc.createElement("disc");
                        discElement.setAttribute("type", model.getValueAt(i, 10).toString());

                        Element storageElement = doc.createElement("storage");
                        storageElement.setTextContent(model.getValueAt(i, 9).toString());
                        discElement.appendChild(storageElement);

                        laptopElement.appendChild(discElement);

                        Element graphicCardElement = doc.createElement("graphic_card");

                        Element nameCardElement = doc.createElement("name");
                        nameCardElement.setTextContent(model.getValueAt(i, 11).toString());
                        graphicCardElement.appendChild(nameCardElement);

                        Element memoryElement = doc.createElement("memory");
                        memoryElement.setTextContent(model.getValueAt(i, 12).toString());
                        graphicCardElement.appendChild(memoryElement);

                        laptopElement.appendChild(graphicCardElement);

                        Element osElement = doc.createElement("os");
                        if (model.getValueAt(i, 13) != null) {
                            osElement.setTextContent(model.getValueAt(i, 13).toString());
                        } else osElement.setTextContent("brak");
                        laptopElement.appendChild(osElement);

                        Element readerElement = doc.createElement("disc_reader");
                        if (model.getValueAt(i, 14) != null) {
                            readerElement.setTextContent(model.getValueAt(i, 14).toString());
                        } else readerElement.setTextContent("brak");
                        laptopElement.appendChild(readerElement);

                        laptopsElement.appendChild(laptopElement);
                    }
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(doc);
                    StreamResult streamResult = new StreamResult(selectedFile);
                    transformer.transform(source, streamResult);
                    JOptionPane.showMessageDialog(null, "Dane wyeksportowane");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private String getElementValue(Element element, String name) {
        return element.getElementsByTagName(name).item(0).getTextContent();
    }

    public static void main(String[] args) {
        LaptopApp laptopApp = new LaptopApp();
        laptopApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        laptopApp.setVisible(true);
    }


    //    ---------databse---------
    private int newRecordsCount = 0;
    private int duplicateRecordsCount = 0;
    private void importDataFromDatabase() {
        try {
            connection = DriverManager.getConnection(databaseUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT nazwa_producenta, przekatna_ekranu, rozdzielczosc, rodzaj_ekranu, czy_dotykowy, nazwa_procesora, liczba_rdzeni, taktowanie, pamiec_ram, pojemnosc_dysku, rodzaj_dysku, nazwa_ug, pamiec_ug, nazwa_so, rodzaj_napedu FROM laptop");
            ResultSet resultSet = statement.executeQuery();


            List<String[]> dataFromDatabase = new ArrayList<>();

            while (resultSet.next()) {
                String[] data = new String[model.getColumnCount()];
                for (int i = 0; i < model.getColumnCount(); i++) {
                    data[i] = resultSet.getString(i + 1);
                }
                dataFromDatabase.add(data);

            }
            for (String[] data : dataFromDatabase) {
                boolean isDuplicate = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    boolean isSameRecord = true;
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if (!data[j].equals(model.getValueAt(i, j))) {
                            isSameRecord = false;
                            break;
                        }
                    }
                    if (isSameRecord) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (isDuplicate) {
                    duplicateRecordsCount++;
                } else {
                    newRecordsCount++;
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

            model.setRowCount(0);
            for (String[] data : dataFromDatabase) {
                model.addRow(data);
            }
            String infoMessage = "Liczba nowych rekordów: " + newRecordsCount + " Liczba znalezionych duplikatów: " + duplicateRecordsCount;
            duplicateRecordsCount=0;
            newRecordsCount=0;
            JOptionPane.showMessageDialog(null, "Dane wczytane z bazy danych");
            JOptionPane.showMessageDialog(null,infoMessage);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void exportDataToDatabase() {
        try {
            connection = DriverManager.getConnection(databaseUrl, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("Delete From laptop");
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder values = new StringBuilder();
                values.append("'").append(i + 1).append("',");
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (model.getValueAt(i, j) != "") {
                        values.append("'").append(model.getValueAt(i, j)).append("',");
                    } else {
                        values.append("'null',");
                    }
                }
                values.deleteCharAt(values.length() - 1);
                String insertQuery = "INSERT INTO `laptop` (`id`, `nazwa_producenta`, `przekatna_ekranu`, `rozdzielczosc`, " +
                        "`rodzaj_ekranu`, `czy_dotykowy`, `nazwa_procesora`, `liczba_rdzeni`, `taktowanie`, `pamiec_ram`, " +
                        "`pojemnosc_dysku`, `rodzaj_dysku`, `nazwa_ug`,`pamiec_ug`, `nazwa_so`, `rodzaj_napedu`)" + "VALUES(" + values + ")";
                statement.executeUpdate(insertQuery);
            }
            newRecordsCount = 0;
            duplicateRecordsCount = 0;
            statement.close();
            connection.close();
            JOptionPane.showMessageDialog(null, "Dane zapisane do bazy danych");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getLaptopsFilteredByScreenResolution(String selectedScreenResolution) {
        try {
            // Tworzenie połączenia HTTP GET do odpowiedniego endpointu serwera
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
    public void getLaptopFilteredByManufacturer(String selectedManufacturer){
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

    public void getLaptopsFilteredByMatrixType(String selectedMatrixType){
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


    public void addNewRecord(){
        try {
            String urlString = "http://localhost:8080/api/laptops/addNewRecord";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }


}



