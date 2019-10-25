package fr.index.cloud.ens.utils.icap;

import java.io.IOException;

public class Tester {
    public static void main(String[] args)
    {
        try{
            ICAP icap = new ICAP("localhost",1344,"avscan");
            
            String[] files = new String[]{
                    "C:\\datas\\cloud_fr.properties",                    
                    "C:\\datas\\fakevirus.bat",
                 "C:\\datas\\cloud.properties"                
//                 "C:\\datas\\fakevirus.bat",
//                 "C:\\datas\\distribution.zip",
//                 "C:\\datas\\cloud.properties"
            };
            
            for(String file : files) {
                try {
                    System.out.print(file + ": ");
                    boolean result = icap.scanFile(file);
                    System.out.println(result == true ? "Clean" : "Infected");
                } catch (ICAPException ex) {
                    System.err.println("Could not scan file " + file + ": " + ex.getMessage());
                } catch (IOException ex) {
                    System.err.println("IO error occurred when scanning file " + file + ": " + ex.getMessage());
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        catch(ICAPException e){
            System.out.println(e.getMessage());
        }
        
   }
}
