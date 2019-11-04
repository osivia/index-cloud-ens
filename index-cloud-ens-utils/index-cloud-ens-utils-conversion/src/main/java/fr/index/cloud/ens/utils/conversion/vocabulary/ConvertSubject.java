package fr.index.cloud.ens.utils.conversion.vocabulary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConvertSubject {


    public static String directory = null;

    public static ArrayList<String> lines = null;


    private static void addLine(String line) {
        lines.add(line);
    }


    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("java fr.index.cloud.ens.utils.conversion.vocabulary.ConvertSubject work-directory");
            System.exit(1);
        }

        directory = args[0];

        try {
            lines = new ArrayList<String>();


            /* Read input */

            String fileInputName = directory + File.separator + "subject-input-metamatieres.csv";
            String fileOutputName = directory + File.separator + "subject.csv";

            File inputFile = new File(fileInputName);


            boolean exists = inputFile.exists();
            if (exists) {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), "UTF-8"));

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "ISO-8859-1"));
                try {
                    String line = br.readLine();
                    
                    out.println("\"id\",\"label\",\"parent\",\"obsolete\"");
                    
                    
                    while ((line = br.readLine()) != null) {
                        String[] toks = line.split(";");
                        if (toks.length != 4)
                            throw new Exception("line " + line + " contains " + toks.length + " tokens");
                        out.println("\"" + toks[3] + "\",\"" + toks[1] + "\",\"\",\"0\"");
                    }

                } finally {
                    br.close();
                    out.close();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
