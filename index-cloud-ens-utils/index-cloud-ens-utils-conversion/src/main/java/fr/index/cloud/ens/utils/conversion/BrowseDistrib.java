package fr.index.cloud.ens.utils.conversion;

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

public class BrowseDistrib {

    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    private static List<String> prefixsToRemove = Arrays.asList(new String[]{"fr.index.cloud.ens.portal.directory-", "org.osivia.services.directory.group-",
            "org.osivia.services.directory.person-", "org.osivia.services-", "org.osivia.services.workspace-"});

    private static List<String> prefixsToIgnore = Arrays.asList(new String[]{"osivia-portal-custom-services", "toutatice-portail-cms-nuxeo-web",
            "index-cloud-ens-portal-ws/CONVERSION_ADMIN", "index-cloud-ens-portal-ws/APPLICATION", "osivia-services-procedure", "osivia-services-statistics",
            "osivia-services-contact", "osivia-services-workspace-sharing", "osivia-services-workspace-participants", "osivia-services-calendar",
            "osivia-services-editor-helpers", "osivia-services-pad", "osivia-services-workspace-file-browser/FILE_BROWSER_ADMIN", "osivia-services-versions",
            "osivia-services-forum", "osivia-services-workspace-creation", "osivia-services-workspace-edition", "osivia-services-workspace-member-management",
            "osivia-services-workspace-local-group-management", "osivia-services-workspace-acl-management", "osivia-services-faq", "osivia-services-onlyoffice",
            "osivia-services-directory-person-management", "osivia-services-directory-group-card", "osivia-services-directory-group-management",
            "portal-core-modules", "osivia-services-tasks", "index-cloud-ens-portal-initializer", "osivia-services-workspace-map", "osivia-services-taskbar",
            "osivia-services-document-creation", "osivia-services-directory-group-creation", "osivia-services-directory-user-settings",
            "index-cloud-ens-portal-highest-levels", "osivia-services-cgu-portail"});

    private static List<String> sectionsToIgnore = Arrays.asList(new String[]{"osivia-services-widgets/WEB-INF/classes/issued_fr.properties","osivia-services-widgets/WEB-INF/classes/Resource_fr.properties"});

    public static final String SNAPSHOT = "-SNAPSHOT";

    public static  String directory = null;
    
    public static ArrayList<String> lines = null;

    public static boolean applyFilter = true;


    public static void transfer(InputStream in, OutputStream out, int buffer) throws IOException {
        byte[] read = new byte[buffer]; // Your buffer size.
        while (0 < (buffer = in.read(read)))
            out.write(read, 0, buffer);
    }


    public static File stream2file(InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            transfer(in, out, 100000);
        }
        return tempFile;
    }


    private static void addLine(String line) {
        lines.add(line);
    }


    private static void addLine(int index, String line) {
        lines.add(index, line);
    }

    public static void extract(String path, String appName) throws Exception {


        ZipFile zipFile = null;

        try {

            zipFile = new ZipFile(path);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();


                if (entry.getName().endsWith(".ear") || entry.getName().endsWith(".sar") || entry.getName().endsWith(".war")) {

                    File file = stream2file(zipFile.getInputStream(entry));

                    String application = entry.getName();

                    int index = application.lastIndexOf("/");
                    if (index != -1)
                        application = application.substring(index);

                    if (application.startsWith("/"))
                        application = application.substring(1);

                    for (String prefix : prefixsToRemove) {
                        if (application.startsWith(prefix)) {
                            application = application.substring(prefix.length());
                        }
                    }

                    int extension = application.lastIndexOf(".");
                    if (extension != -1)
                        application = application.substring(0, extension);

                    /* Version */

                    if (application.endsWith(SNAPSHOT))
                        application = application.substring(0, application.length() - SNAPSHOT.length());

                    int versionIndex = application.lastIndexOf("-");
                    if (application.substring(versionIndex).startsWith("-RC"))
                        application = application.substring(0, versionIndex);

                    versionIndex = application.lastIndexOf("-");
                    application = application.substring(0, versionIndex);

                    boolean ignore = false;
                    for (String prefix : prefixsToIgnore) {
                        if (application.startsWith(prefix)) {
                            ignore = true;
                        }
                    }

                    if (!applyFilter || !ignore)
                        extract(file.getPath(), application);
                }

                if (entry.getName().endsWith("_fr.properties")) {

                    if (!applyFilter || !appName.equals("portal-core-modules")) {


                        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

                        try {
                            String line = null;

                            while ((line = br.readLine()) != null) {

                                String section = appName + "/" + entry.getName();


                                boolean ignore = false;
                                if (applyFilter) {
                                    for (String prefix : sectionsToIgnore) {
                                        if (section.startsWith(prefix)) {
                                            ignore = true;
                                        }
                                    }
                                }

                                if (!ignore) {


                                    // Search end of section
                                    int iEndSection = getSection("/sync/end/" + section);
                                    if (iEndSection == -1) {
                                        System.out.println("insert section " + section);
                                        addLine("##### /sync/start/" + section);
                                        addLine("##### /sync/end/" + section);
                                    }


                                    iEndSection = getSection("/sync/end/" + section);
                                    if (iEndSection == -1) {
                                        throw new Exception("can't insert section " + section);
                                    }

                                    int iBeginSection = getSection("/sync/start/" + section);
                                    if (iBeginSection == -1) {
                                        throw new Exception("can't fin section begin " + section);
                                    }

                                    // search
                                    String toSearch = null;
                                    int iEquals = line.indexOf("=");
                                    if (iEquals != -1) {
                                        toSearch = line.substring(0, iEquals).replaceAll(" ", "");

                                        boolean found = false;

                                        // Find if the value has already be inserted

                                        for (int iLine = 0; iLine < lines.size(); iLine++) {
                                            String curLine = lines.get(iLine);
                                            iEquals = curLine.indexOf("=");
                                            if (iEquals != -1) {
                                                String curSearch = curLine.substring(0, iEquals).replaceAll(" ", "");
                                                if (curSearch.equals(toSearch))
                                                    found = true;
                                            }
                                        }


                                        if (!found) {
                                            // Add value
                                            ignore = false;
                                            if (applyFilter) {
                                                for (String prefix : prefixsToIgnore) {
                                                    if ((appName + "/" + line).startsWith(prefix)) {
                                                        ignore = true;
                                                    }
                                                }
                                            }

                                            if (!ignore) {
                                                iEndSection = getSection("/sync/end/" + section);

                                                String lineToInsert = line;
                                                while (lineToInsert.endsWith("\\")) {
                                                    lineToInsert = lineToInsert.substring(0, lineToInsert.lastIndexOf("\\"));
                                                    line = br.readLine();
                                                    if (line != null) {
                                                        lineToInsert += line;
                                                    }
                                                }

                                                addLine(iEndSection, decode(lineToInsert));


                                            }
                                        }

                                    }
                                }
                            }

                        } finally {
                            br.close();
                        }


                    }
                }

            }
        } finally {
            zipFile.close();
        }


    }


    private static String decode(String lineToInsert) {

        lineToInsert = lineToInsert.replaceAll("\\\\u00E9", "é");
        lineToInsert = lineToInsert.replaceAll("\\\\u00C9", "E");
        lineToInsert = lineToInsert.replaceAll("\\\\u00E8", "è");
        lineToInsert = lineToInsert.replaceAll("\\\\u00EA", "ê");
        lineToInsert = lineToInsert.replaceAll("\\\\u00ea", "ê");
        lineToInsert = lineToInsert.replaceAll("\\\\u2026", " ");
        lineToInsert = lineToInsert.replaceAll("\\\\u202F", " ");
        lineToInsert = lineToInsert.replaceAll("\\\\u00A0", " ");        
        lineToInsert = lineToInsert.replaceAll("\\\\u00AB", "'");
        lineToInsert = lineToInsert.replaceAll("\\\\u00BB", "'");
        lineToInsert = lineToInsert.replaceAll("\\\\u00E0", "à");

        lineToInsert = lineToInsert.replaceAll("''", "'");

        return lineToInsert;
    }


    private static int getSection(String sectionName) {
        int iSection = 0;
        int returnSection = -1;
        for (String curLine : lines) {
            if (curLine.indexOf(sectionName) != -1)
                returnSection = iSection;
            else
                iSection++;
        }
        return returnSection;
    }

    public static void main(String[] args) {
        
        if( args.length == 0)   {
            System.out.println( "java fr.index.cloud.ens.utils.conversion.BrowseDistrib work-directory");
            System.exit(1);
        }
        
        directory = args[0];
        
        try {
            lines = new ArrayList<String>();
            applyFilter = false;
            performMerge();


            lines = new ArrayList<String>();
            applyFilter = true;
            performMerge();


            /* Check */
            for (int iLine = 0; iLine < lines.size(); iLine++) {
                String line = lines.get(iLine);
                int iEquals = line.indexOf("=");
                if (iEquals != -1) {
                    String toSearch = line.substring(0, iEquals).replaceAll(" ", "");
                    // Find if the value has already be inserted
                    for (int iLine2 = 0; iLine2 < lines.size(); iLine2++) {
                        if (iLine2 != iLine) {
                            String curLine = lines.get(iLine2);
                            iEquals = curLine.indexOf("=");
                            if (iEquals != -1) {
                                String curSearch = curLine.substring(0, iEquals).replaceAll(" ", "");
                                if (curSearch.equals(toSearch)) {
                                    System.out.println(" multiple values for " + toSearch);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static void performMerge() throws  Exception {
        /* Read input */

        String fileName = directory+File.separator+"cloud.properties";
        if (!applyFilter)
            fileName = directory+File.separator+"full.properties";


        if (applyFilter) {
            File tempFile = new File(fileName);

            boolean exists = tempFile.exists();
            if (exists) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile),"UTF-8"));
                try {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }

                } finally {
                    br.close();
                }
            }
        }

        extract(directory+File.separator+"distribution.zip", "portal-core-modules");


        
        PrintWriter out = new PrintWriter( new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
        for (String line : lines) {
            out.println(line);
        }
        out.close();
        
    }


}
