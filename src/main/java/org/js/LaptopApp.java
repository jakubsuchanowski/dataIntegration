package org.js;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import static org.js.DataFilter.*;

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


    public LaptopApp() {
        setTitle("Integracja systemów - Jakub Suchanowski");
        setSize(1000, 400);

        // Tworzenie tabeli
        String[] columnNames = {"id","Nazwa producenta", "Przekątna ekranu", "Rozdzielczość", "Rodzaj ekranu",
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
        exportButton = new JButton("Zapisz dane do pliku TXT");
        importFromXmlButton = new JButton("Wczytaj dane z pliku XML");
        exportToXmlButton = new JButton("Zapisz dane do pliku XML");
        importDatabaseButton = new JButton("Wczytaj z bazy");
        exportDatabaseButton = new JButton("Zapisz do bazy");

        JComboBox<String> screenResolutionComboBox = new JComboBox<>(new String[]{"1920x1080","1366x768","1280x800","1600x900","null"});
        JButton screenResolutionButton = new JButton("Liczba laptopów z wybraną rozdzielczością");

        JComboBox<String> manufacturerComboBox = new JComboBox<>(new String[]{"Dell", "Asus", "Fujitsu","Huawei", "MSI","Samsung", "Sony"});
        JButton manufacturerButton = new JButton("Liczba laptopów z wybranej firmy");

        JComboBox<String> matrixTypeComboBox = new JComboBox<>(new String[]{"blyszczaca", "matowa", "null"});
        JButton matrixTypeButton = new JButton("Laptopy o wybranej matrycy");


        JButton addNewRecordButton = new JButton("Dodaj laptop");
        JButton deleteRecordButton = new JButton("Usuń laptop");
        JButton editRecordButton = new JButton("Edytuj laptop");

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TxtData.ImportTextData(model);
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TxtData.ExportTextData(model);
            }
        });

        importFromXmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlData.importFromXml(model);
            }
        });

        exportToXmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XmlData.exportToXml(model);
            }
        });

        importDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DbData.importDataFromDatabase(model);
            }
        });

        exportDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DbData.exportDataToDatabase(model);
            }
        });

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
                getLaptopsFilteredByMatrixType(selectedMatrixType, model);
            }
        });

        addNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrudData.addNewRecord(model);
            }
        });

        deleteRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrudData.deleteRecord(model);
            }
        });

        editRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrudData.updateRecord(model);
            }
        });

        //do rest
        resultLabel=new JLabel();
        DataFilter dataFilter = new DataFilter(resultLabel);
        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);

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

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5,1));
        rightPanel.add(addNewRecordButton);
        rightPanel.add(deleteRecordButton);
        rightPanel.add(editRecordButton);

        add(rightPanel, BorderLayout.EAST);
    }
}



