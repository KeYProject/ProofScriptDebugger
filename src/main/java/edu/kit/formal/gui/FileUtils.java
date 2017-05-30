package edu.kit.formal.gui;

import java.io.*;

/**
 * Created by sarah on 5/29/17.
 */
public class FileUtils {

    public static BufferedReader readFile(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return br;
    }

}
