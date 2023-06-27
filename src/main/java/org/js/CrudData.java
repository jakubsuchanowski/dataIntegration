package org.js;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CrudData extends Component {
    public static void addNewRecord(DefaultTableModel model){
        try {
            JTextField nazwaProdcuntaTextField = new JTextField();
            JTextField przekatnaEkranuTextField = new JTextField();
            JTextField rozdzielczoscjTextField = new JTextField();
            JTextField rodzajEkranujTextField = new JTextField();
            JTextField czyDotykowyTextField = new JTextField();
            JTextField nazwaProcesorajTextField = new JTextField();
            JTextField liczbaRdzeniTextField = new JTextField();
            JTextField taktowanieTextField = new JTextField();
            JTextField pamiecRamTextField = new JTextField();
            JTextField pojemnoscDyskuTextField = new JTextField();
            JTextField rodzajDyskuTextField = new JTextField();
            JTextField nazwaUgTextField = new JTextField();
            JTextField pamiecUgTextField = new JTextField();
            JTextField nazwaSoTextField = new JTextField();
            JTextField rodzajNapeduTextField = new JTextField();

            JPanel editPanel = new JPanel(new GridLayout(15,1));
            editPanel.add(new JLabel("Nazwa producenta: "));
            editPanel.add(nazwaProdcuntaTextField);
            editPanel.add(new JLabel("Przektna ekranu: "));
            editPanel.add(przekatnaEkranuTextField);
            editPanel.add(new JLabel("Rozdzielczosc: "));
            editPanel.add(rozdzielczoscjTextField);
            editPanel.add(new JLabel("Rodzaj ekranu: "));
            editPanel.add(rodzajEkranujTextField);
            editPanel.add(new JLabel("Czy dotykowy: "));
            editPanel.add(czyDotykowyTextField);
            editPanel.add(new JLabel("Nazwa procesora: "));
            editPanel.add(nazwaProcesorajTextField);
            editPanel.add(new JLabel("Liczba rdzeni: "));
            editPanel.add(liczbaRdzeniTextField);
            editPanel.add(new JLabel("Taktowanie: "));
            editPanel.add(taktowanieTextField);
            editPanel.add(new JLabel("Pamieć RAM: "));
            editPanel.add(pamiecRamTextField);
            editPanel.add(new JLabel("Pojemnosc dysku: "));
            editPanel.add(pojemnoscDyskuTextField);
            editPanel.add(new JLabel("Rodzaj dysku: "));
            editPanel.add(rodzajDyskuTextField);
            editPanel.add(new JLabel("Nazwa układu graficznego: "));
            editPanel.add(nazwaUgTextField);
            editPanel.add(new JLabel("Pamięć układu graficznego"));
            editPanel.add(pamiecUgTextField);
            editPanel.add(new JLabel("Nazwa systemu operacyjnego"));
            editPanel.add(nazwaSoTextField);
            editPanel.add(new JLabel("Rodzaj napędu"));
            editPanel.add(rodzajNapeduTextField);
            int result = JOptionPane.showConfirmDialog(null, editPanel, "Edycja rekordu", JOptionPane.OK_CANCEL_OPTION);


            JSONObject newData = new JSONObject();
            if (result == JOptionPane.OK_OPTION) {
                String newValue1 = nazwaProdcuntaTextField.getText();
                String newValue2 = przekatnaEkranuTextField.getText();
                String newValue3 = rozdzielczoscjTextField.getText();
                String newValue4 = rodzajEkranujTextField.getText();
                String newValue5 = czyDotykowyTextField.getText();
                String newValue6 = nazwaProcesorajTextField.getText();
                String newValue7 = liczbaRdzeniTextField.getText();
                String newValue8 = taktowanieTextField.getText();
                String newValue9 = pamiecRamTextField.getText();
                String newValue10 = pojemnoscDyskuTextField.getText();
                String newValue11 = rodzajDyskuTextField.getText();
                String newValue12 = nazwaUgTextField.getText();
                String newValue13 = pamiecUgTextField.getText();
                String newValue14 = nazwaSoTextField.getText();
                String newValue15 = rodzajNapeduTextField.getText();

                newData.put("nazwaProducenta", newValue1);
                newData.put("przekatnaEkranu", newValue2);
                newData.put("rozdzielczosc", newValue3);
                newData.put("rodzajEkranu", newValue4);
                newData.put("czyDotykowy", newValue5);
                newData.put("nazwaProcesora", newValue6);
                newData.put("liczbaRdzeni", newValue7);
                newData.put("taktowanie", newValue8);
                newData.put("pamiecRam", newValue9);
                newData.put("pojemnoscDysku", newValue10);
                newData.put("rodzajDysku", newValue11);
                newData.put("nazwaUg", newValue12);
                newData.put("pamiecUg", newValue13);
                newData.put("nazwaSo", newValue14);
                newData.put("rodzajNapedu", newValue15);


                String urlString = "http://localhost:8080/api/laptops/addNewRecord";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(newData.toString().getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }


                int numberNewRow=model.getRowCount();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Object[] newEmptyRow = new Object[model.getColumnCount()];
                    model.addRow(newEmptyRow);
                    model.setValueAt(newValue1, numberNewRow, 1);
                    model.setValueAt(newValue2, numberNewRow, 2);
                    model.setValueAt(newValue3, numberNewRow, 3);
                    model.setValueAt(newValue4, numberNewRow, 4);
                    model.setValueAt(newValue5, numberNewRow, 5);
                    model.setValueAt(newValue6, numberNewRow, 6);
                    model.setValueAt(newValue7, numberNewRow, 7);
                    model.setValueAt(newValue8, numberNewRow, 8);
                    model.setValueAt(newValue9, numberNewRow, 9);
                    model.setValueAt(newValue10, numberNewRow, 10);
                    model.setValueAt(newValue11, numberNewRow, 11);
                    model.setValueAt(newValue12, numberNewRow, 12);
                    model.setValueAt(newValue13, numberNewRow, 13);
                    model.setValueAt(newValue14, numberNewRow, 14);
                    model.setValueAt(newValue15, numberNewRow, 15);
                }
                JOptionPane.showMessageDialog(null, "Rekord został dodany!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas dodawania rekordu!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void deleteRecord(DefaultTableModel model){
        try {

            String stringId = JOptionPane.showInputDialog("Podaj id wiersza: ");
            int id;
            if(stringId != null && stringId !="") {
                id = Integer.parseInt(stringId);
            }
            else {
                JOptionPane.showMessageDialog(null, "Nie podano id!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String urlString = "http://localhost:8080/api/laptops/deleteRecord/"+id;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Rekord został usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Wystąpił problem podczas usuwania rekordu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            connection.disconnect();

            for(int i=0;i<model.getRowCount();i++)
                if(model.getValueAt(i,0).equals(String.valueOf(id))){
                    model.removeRow(i);
                }
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
    public static void updateRecord(DefaultTableModel model){
        try {
            String stringId = JOptionPane.showInputDialog("Podaj id wiersza: ");
            int id;
            if(stringId != null && stringId !="") {
                id = Integer.parseInt(stringId);
            }
            else {
                JOptionPane.showMessageDialog(null, "Nie podano id!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(model.getRowCount()<=0){
                JOptionPane.showMessageDialog(null, "Brak danych w modelu!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int firstId = Integer.parseInt(model.getValueAt(0,0).toString());
            int lastRowIndex = model.getRowCount()-1;
            int lastId = Integer.parseInt(model.getValueAt(lastRowIndex,0).toString());
            if (id < firstId || id>lastId) {
                JOptionPane.showMessageDialog(null, "Niepoprawne ID rekordu!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selectedRow = -1;
            String[] oldValue = new String[model.getColumnCount()];
            for (int i = 0; i < model.getRowCount(); i++)
                if (model.getValueAt(i, 0).equals(String.valueOf(id))) {
                    selectedRow=i;
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if(model.getValueAt(i,j)==null){oldValue[j]=" ";}
                        oldValue[j] =model.getValueAt(i,j).toString();
                    }
                }

            JTextField nazwaProdcuntaTextField = new JTextField(oldValue[1]);
            JTextField przekatnaEkranuTextField = new JTextField(oldValue[2]);
            JTextField rozdzielczoscjTextField = new JTextField(oldValue[3]);
            JTextField rodzajEkranujTextField = new JTextField(oldValue[4]);
            JTextField czyDotykowyTextField = new JTextField(oldValue[5]);
            JTextField nazwaProcesorajTextField = new JTextField(oldValue[6]);
            JTextField liczbaRdzeniTextField = new JTextField(oldValue[7]);
            JTextField taktowanieTextField = new JTextField(oldValue[8]);
            JTextField pamiecRamTextField = new JTextField(oldValue[9]);
            JTextField pojemnoscDyskuTextField = new JTextField(oldValue[10]);
            JTextField rodzajDyskuTextField = new JTextField(oldValue[11]);
            JTextField nazwaUgTextField = new JTextField(oldValue[12]);
            JTextField pamiecUgTextField = new JTextField(oldValue[13]);
            JTextField nazwaSoTextField = new JTextField(oldValue[14]);
            JTextField rodzajNapeduTextField = new JTextField(oldValue[15]);

            JPanel editPanel = new JPanel(new GridLayout(15,1));
            editPanel.add(new JLabel("Nazwa producenta: "));
            editPanel.add(nazwaProdcuntaTextField);
            editPanel.add(new JLabel("Przektna ekranu: "));
            editPanel.add(przekatnaEkranuTextField);
            editPanel.add(new JLabel("Rozdzielczosc: "));
            editPanel.add(rozdzielczoscjTextField);
            editPanel.add(new JLabel("Rodzaj ekranu: "));
            editPanel.add(rodzajEkranujTextField);
            editPanel.add(new JLabel("Czy dotykowy: "));
            editPanel.add(czyDotykowyTextField);
            editPanel.add(new JLabel("Nazwa procesora: "));
            editPanel.add(nazwaProcesorajTextField);
            editPanel.add(new JLabel("Liczba rdzeni: "));
            editPanel.add(liczbaRdzeniTextField);
            editPanel.add(new JLabel("Taktowanie: "));
            editPanel.add(taktowanieTextField);
            editPanel.add(new JLabel("Pamieć RAM: "));
            editPanel.add(pamiecRamTextField);
            editPanel.add(new JLabel("Pojemnosc dysku: "));
            editPanel.add(pojemnoscDyskuTextField);
            editPanel.add(new JLabel("Rodzaj dysku: "));
            editPanel.add(rodzajDyskuTextField);
            editPanel.add(new JLabel("Nazwa układu graficznego: "));
            editPanel.add(nazwaUgTextField);
            editPanel.add(new JLabel("Pamięć układu graficznego"));
            editPanel.add(pamiecUgTextField);
            editPanel.add(new JLabel("Nazwa systemu operacyjnego"));
            editPanel.add(nazwaSoTextField);
            editPanel.add(new JLabel("Rodzaj napędu"));
            editPanel.add(rodzajNapeduTextField);
            int result = JOptionPane.showConfirmDialog(null, editPanel, "Edycja rekordu", JOptionPane.OK_CANCEL_OPTION);


            JSONObject newData = new JSONObject();
            if (result == JOptionPane.OK_OPTION) {
                String newValue1 = nazwaProdcuntaTextField.getText();
                String newValue2 = przekatnaEkranuTextField.getText();
                String newValue3 = rozdzielczoscjTextField.getText();
                String newValue4 = rodzajEkranujTextField.getText();
                String newValue5 = czyDotykowyTextField.getText();
                String newValue6 = nazwaProcesorajTextField.getText();
                String newValue7 = liczbaRdzeniTextField.getText();
                String newValue8 = taktowanieTextField.getText();
                String newValue9 = pamiecRamTextField.getText();
                String newValue10 = pojemnoscDyskuTextField.getText();
                String newValue11 = rodzajDyskuTextField.getText();
                String newValue12 = nazwaUgTextField.getText();
                String newValue13 = pamiecUgTextField.getText();
                String newValue14 = nazwaSoTextField.getText();
                String newValue15 = rodzajNapeduTextField.getText();

                newData.put("nazwaProducenta", newValue1);
                newData.put("przekatnaEkranu", newValue2);
                newData.put("rozdzielczosc", newValue3);
                newData.put("rodzajEkranu", newValue4);
                newData.put("czyDotykowy", newValue5);
                newData.put("nazwaProcesora", newValue6);
                newData.put("liczbaRdzeni", newValue7);
                newData.put("taktowanie", newValue8);
                newData.put("pamiecRam", newValue9);
                newData.put("pojemnoscDysku", newValue10);
                newData.put("rodzajDysku", newValue11);
                newData.put("nazwaUg", newValue12);
                newData.put("pamiecUg", newValue13);
                newData.put("nazwaSo", newValue14);
                newData.put("rodzajNapedu", newValue15);


                String urlString = "http://localhost:8080/api/laptops/updateRecord/" + id;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(newData.toString().getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }


                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    model.setValueAt(newValue1, selectedRow, 1);
                    model.setValueAt(newValue2, selectedRow, 2);
                    model.setValueAt(newValue3, selectedRow, 3);
                    model.setValueAt(newValue4, selectedRow, 4);
                    model.setValueAt(newValue5, selectedRow, 5);
                    model.setValueAt(newValue6, selectedRow, 6);
                    model.setValueAt(newValue7, selectedRow, 7);
                    model.setValueAt(newValue8, selectedRow, 8);
                    model.setValueAt(newValue9, selectedRow, 9);
                    model.setValueAt(newValue10, selectedRow, 10);
                    model.setValueAt(newValue11, selectedRow, 11);
                    model.setValueAt(newValue12, selectedRow, 12);
                    model.setValueAt(newValue13, selectedRow, 13);
                    model.setValueAt(newValue14, selectedRow, 14);
                    model.setValueAt(newValue15, selectedRow, 15);

                    JOptionPane.showMessageDialog(null, "Rekord został zaktualizowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas aktualizacji rekordu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
