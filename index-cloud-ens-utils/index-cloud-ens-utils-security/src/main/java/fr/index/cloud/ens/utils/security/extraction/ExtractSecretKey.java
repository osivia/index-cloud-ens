package fr.index.cloud.ens.utils.security.extraction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

import javax.crypto.SecretKey;

/**
 * The Class ExtractSecretKey.
 */
public class ExtractSecretKey {

    public static void main(String[] args) throws Exception {
        final String fileName = args[0];
        final String alias = args[1];
        final char[] storepass = args[2].toCharArray();
        final char[] keypass = args[3].toCharArray();
        final String fileOutput = args[4];
        
        if( args.length != 5) {
            System.out.println("ExtractSecretKey srcstore alias storepass keypass deststore");
        }

        KeyStore ksinput = KeyStore.getInstance("JCEKS");

        try (FileInputStream fis = new FileInputStream(fileName)) {

            ksinput.load(fis, storepass);
            SecretKey secretKey = (SecretKey) ksinput.getKey(alias, keypass);

            KeyStore output = KeyStore.getInstance("JCEKS");
            output.load(null, storepass);
            output.setKeyEntry(alias, secretKey, keypass, null);
            FileOutputStream fos = new FileOutputStream(fileOutput);
            output.store(fos, storepass);
            fos.close();
        }
    }


}
