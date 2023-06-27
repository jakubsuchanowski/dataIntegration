package org.js;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class TxtData {
    public static void ImportTextData(DefaultTableModel model) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                model.setRowCount(0);
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

    public static void ExportTextData(DefaultTableModel model) {
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
