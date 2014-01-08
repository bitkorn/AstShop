/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.callshop4u.zeug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author sportsbar
 */
public class TariffListConverter {

    private static Logger logger = Logger.getRootLogger();

    /**
     * www.pbx-network.de rates.csv
     */
    public static String convertPbxnetwork(File origin) {
        BufferedReader br;
        String line;
        String[] spalten;
        String[] prefixe;

        String originPath = origin.getAbsolutePath();
        String goalPath = "";
        if(originPath.endsWith(".csv")) {
            goalPath = originPath.substring(0, originPath.length() - 4) + "_new.csv";
        } else {
            goalPath = originPath + "_out.csv";
        }
        
        File fileOut = new File(goalPath); // Ausgabefile
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileOut);
        } catch (IOException e1) {
            logger.error(e1.getMessage());
            return "Fehler: Details in astshop.log";
        }

        StringBuilder sb = new StringBuilder();
        String tmp = "";
        try {
            br = new BufferedReader(new FileReader(origin)); // Eingabefile
            while ((line = br.readLine()) != null) { // die ganze Datei durchgehen
                //System.out.println(line);
                spalten = line.split(";");
                prefixe = spalten[0].split(","); // erste Spalte mit den Vorwahlen splitten
                for (int i = 0; prefixe.length > i; i++) { // so oft wie Vorwahlen in einer Zeile
                    prefixe[i] = prefixe[i].replace(" ", ""); // Leerzeichen in Vorwahlen loeschen
                    spalten[1] = spalten[1].replace("\"", ""); // Gaensefuesse in zweiter Spalte loeschen
                    tmp = prefixe[i] + ";" + spalten[1] + ";" + spalten[4].replace(",", ".") + ";" + spalten[5].replace(",", ".") + "\r\n";
                    sb.append(tmp);
                }
            }
            br.close();
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "Fehler: Details in astshop.log";
        }
        return "success";
    }
}