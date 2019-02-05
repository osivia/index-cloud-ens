package fr.index.cloud.ens.ws;

import java.util.Map;

public class GenericErrors {
    private static Map<String, String> genericErrors = null;
    public static final int ERR_OK = 0;
    public static final int ERR_NOT_FOUND = 101;
    public static final int ERR_FORBIDDEN = 102;
    public static final int ERR_UNKNOWN = 999;

    
    public static String getErrorMsg(int errorCode)   {
        switch (errorCode)  {
            case ERR_NOT_FOUND : return "Not found";
            case ERR_FORBIDDEN : return "Forbidden";
        }
         return null;
    }

}
