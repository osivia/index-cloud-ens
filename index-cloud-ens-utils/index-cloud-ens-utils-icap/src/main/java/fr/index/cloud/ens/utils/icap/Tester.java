package fr.index.cloud.ens.utils.icap;

import java.io.IOException;

public class Tester {
    public static void main(String[] args)
    {

            
            
            String[] files = new String[]{
                    "C:\\datas\\conversion\\cloud.properties",          
                    "C:\\datas\\conversion\\full.properties", 
                    "C:\\datas\\conversion\\distribution.zip",
                    "C:\\datas\\conversion\\fakevirus.bat",
            
//                 "C:\\datas\\fakevirus.bat",
//                 "C:\\datas\\distribution.zip",
//                 "C:\\datas\\cloud.properties"
            };
            
            for(String file : files) {
                try {
                    System.out.print(file + ": ");
                    ICAP icap = new ICAP("localhost",1344,"avscan");                    
                    boolean result = icap.scanFile(file);
                    icap.close();
                    System.out.println(result == true ? "Clean" : "Infected");
                } catch (ICAPException ex) {
                    System.err.println("Could not scan file " + file + ": " + ex.getMessage());
                } catch (IOException ex) {
                    System.err.println("IO error occurred when scanning file " + file + ": " + ex.getMessage());
                }
            }


        
   }
}
