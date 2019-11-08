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

public class Convert {


    private static final String LEVEL_CSV = "level.csv";

    private static final String PORTAL_CONVERSION_CSV = "portal-conversion.csv";

    private static final String SUBJECT_CSV = "subject.csv";

    private static final int COUVERTURE = 95;

    public static String directory = null;

    public static ArrayList<String> lines = null;


    private static void addLine(String line) {
        lines.add(line);
    }


    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("java fr.index.cloud.ens.utils.conversion.vocabulary.Convert work-directory");
            System.exit(1);
        }

        directory = args[0];

        try {
            lines = new ArrayList<String>();

            int nbSubjects = 0;
            int nbLevels = 0;
            int nbConversion = 0;


            /* Conversion file */

            String fileConversionOutputName = directory + File.separator + PORTAL_CONVERSION_CSV;
            PrintWriter conversionOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileConversionOutputName), "UTF-8"));


            String fileOutputName;
            File inputFile;
            PrintWriter out;
            BufferedReader br;

            /* Read input */

            inputFile = open("subject-metamatieres.csv");
            fileOutputName = directory + File.separator + SUBJECT_CSV;

            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), "UTF-8"));

            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "ISO-8859-1"));
            try {
                String line = br.readLine();

                out.println("\"id\",\"label\",\"parent\",\"obsolete\"");


                while ((line = br.readLine()) != null) {
                    String[] toks = line.split(";");
                    if (toks.length != 4)
                        throw new Exception(inputFile.getName() + ": line " + line + " contains " + toks.length + " tokens");
                    out.println("\"" + toks[0] + "\",\"" + toks[1] + "\",\"\",\"0\"");

                    nbSubjects++;
                }

            } finally {
                br.close();
                out.close();
            }


 
            inputFile = open("subject-correspondances.csv");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "ISO-8859-1"));
            try {
                String line = br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] toks = line.split(";");
                    if (toks.length != 3)
                        throw new Exception(inputFile.getName() + ": line " + line + " contains " + toks.length + " tokens");

                    String code = toks[1];
                    if (code.length() == 0)
                        code = toks[0] + "*";

                    conversionOut.println("S;;" + code + ";;" + toks[2]);
                    nbConversion++;
                }

            } finally {
                br.close();
            }

            inputFile = open("subject-libelles.csv");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            try {
                String line = br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] toks = line.split(";");
                    if (toks.length != 6)
                        throw new Exception(inputFile.getName() + ": line " + line + " contains " + toks.length + " tokens");


                    String percent = toks[5];
                    if (!percent.endsWith("%"))
                        throw new Exception(inputFile.getName() + ": line " + line + " doesn't endswith '%'");
                    int couverture = Integer.parseInt(percent.substring(0, percent.length() - 1));

                    if (couverture <= COUVERTURE) {
                        String libelle = toks[3];
                        conversionOut.println("S;;;" + libelle + ";" + toks[0]);
                        nbConversion++;
                    }
                }

            } finally {
                br.close();
            }

            
            inputFile = open("level-meta-niveaux.csv");
            fileOutputName = directory + File.separator + LEVEL_CSV;


            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), "UTF-8"));

            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "ISO-8859-1"));
            try {
                String line = br.readLine();

                out.println("\"id\",\"label\",\"parent\",\"obsolete\"");


                while ((line = br.readLine()) != null) {
                    String[] toks = line.split(";");
                    if (toks.length != 2)
                        throw new Exception(inputFile.getName() + ": line " + line + " contains " + toks.length + " tokens");
                    out.println("\"" + toks[0] + "\",\"" + toks[1] + "\",\"\",\"0\"");
                    nbLevels++;


                    conversionOut.println("L;;;" + toks[1] + ";" + toks[0]);
                    nbConversion++;
                }

            } finally {
                br.close();
                out.close();
            }



            inputFile = open("level-code-niveaux.csv");

            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            try {
                String line = br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] toks = line.split(";");
                    if (toks.length != 2)
                        throw new Exception(inputFile.getName() + ": line " + line + " contains " + toks.length + " tokens");


                    conversionOut.println("L;;" + toks[0] + ";;" + toks[1]);
                    nbConversion++;
                }

            } finally {
                br.close();
            }


            conversionOut.close();

            System.out.println("Traitement ok. ");
            System.out.println(SUBJECT_CSV + " contains " + nbSubjects + " lines");
            System.out.println(LEVEL_CSV + " contains " + nbLevels + " lines");
            System.out.println(PORTAL_CONVERSION_CSV + " contains " + nbConversion + " lines");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static File open(String fileName) throws Exception {

        String completeName = directory + File.separator + fileName;
        File inputFile = new File(completeName);
        if (!inputFile.exists())
            throw new Exception("File " + completeName + " not found");

        return inputFile;

    }

}
