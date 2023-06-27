package org.js;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbData {

    private static Connection connection;
    private final static String databaseUrl = "jdbc:mysql://localhost:3306/is";
    private final static String username = "root";
    private final static String password = "root";
    private static int newRecordsCount = 0;
    private static int duplicateRecordsCount = 0;
    public static void importDataFromDatabase(DefaultTableModel model) {
        try {
            connection = DriverManager.getConnection(databaseUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT id, nazwa_producenta, przekatna_ekranu, rozdzielczosc, rodzaj_ekranu, czy_dotykowy, nazwa_procesora, liczba_rdzeni, taktowanie, pamiec_ram, pojemnosc_dysku, rodzaj_dysku, nazwa_ug, pamiec_ug, nazwa_so, rodzaj_napedu FROM laptop");
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

    public static void exportDataToDatabase(DefaultTableModel model) {
        try {
            connection = DriverManager.getConnection(databaseUrl, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("Delete From laptop");
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder values = new StringBuilder();
//                values.append("'").append(i + 1).append("',");
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
}
