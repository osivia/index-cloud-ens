package fr.index.cloud.ens.utils.conversion;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestCloud {

    public static void main(String[] args) {
        File file = new File("c:\\datas");
        URL[] urls;
        try {
            urls = new URL[]{file.toURI().toURL()};

        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle rb = ResourceBundle.getBundle("cloud", Locale.FRENCH, loader);
        String test = rb.getString("TRASH_EMPTY_TRASH_MESSAGE_WARNING");
        System.out.println( test);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
