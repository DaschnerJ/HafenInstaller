import com.sun.javaws.Launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Installer {

    public static void main(String[] args)
    {
        Installer installer = new Installer();
        installer.copy();
    }

    private void copy()
    {
        final String path = "sample/folder";
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            System.out.println("Jar is a file!");
            final JarFile jar;
            try {
                jar = new JarFile(jarFile);

                final Enumeration<JarEntry> entries = jar.entries();
                System.out.println("Got jar and all entries.");//gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    JarEntry e = entries.nextElement();
                    final String name = e.getName();
                    System.out.println();
                    System.out.println("Parsing " + name + "...");
                    System.out.println("Info Directory: " + e.isDirectory());
                    System.out.println("Info String: " + e.toString());
                    if (name.startsWith(path + "/")) { //filter according to the path
                        System.out.println(name);
                    }
                    if(e.isDirectory())
                    {
                        if(!e.toString().toLowerCase().startsWith("meta"))
                            new File(getPath()+"/"+e.toString()).mkdirs();
                    }
                    else
                    {
                        if(!e.toString().toLowerCase().startsWith("meta"))
                        {
                            File f;
                            System.out.println("File Length: " + e.toString().split("\\.").length);
                            if(e.toString().split("\\.").length > 1)
                            {
                                if(!e.toString().split("\\.")[e.toString().split("\\.").length-1].endsWith("class"))
                                    f = new java.io.File(getPath() + java.io.File.separator + e.getName());
                                else
                                    f = null;
                            }
                            else
                            {
                               f = new java.io.File(getPath() + java.io.File.separator + e.getName() + ".txt");
                            }
                            if(f != null) {
                                InputStream is = jar.getInputStream(e); // get the input stream
                                FileOutputStream fos = new java.io.FileOutputStream(f);
                                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                    fos.write(is.read());
                                }
                                fos.close();
                                is.close();
                            }
                        }
                    }
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else { // Run with IDE
            System.out.println("Jar is not a file!");
            final URL url = Launcher.class.getResource("/" + path);
            System.out.println("Copying to the directory of: " + url.getPath());
            if (url != null) {
                System.out.println("URL is valid.");
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        System.out.println(app);
                    }
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
            System.out.println("Done.");
        }

        System.out.println("Path of Jar: " + getPath());
    }

    public String getPath()
    {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        return path;
//        try {
//            return new File(Installer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
    }


}
