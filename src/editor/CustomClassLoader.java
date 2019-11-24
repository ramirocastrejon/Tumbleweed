package editor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private String projPath;

    public CustomClassLoader(ClassLoader p)
    {
        super(p);
    }

    public Class findClass(String name) throws ClassNotFoundException
    {
        try
        {
            Path classPath = Paths.get(name);
            byte[] classData = Files.readAllBytes(classPath);
            return defineClass(null, classData,0,classData.length);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public void setProjPath(String projPath){
        this.projPath = projPath;
    }

    // Automatically compile source as necessary when looking class files
    public Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Class clas = null;
        try {
            clas = findClass(projPath + "\\" + name + ".class");
            Method[] methods = clas.getDeclaredMethods();
            System.out.println("Method(s) Called: ");
            for (int i = 0; i < methods.length; i++) {
                System.out.println("\t" + methods[i].toString());
            }
        }
        catch (Exception nsfe) {
        }

        //System.out.println("Loading Class: " + clas);

        String fileStub = name.replace('.', '/');

        String javaFilename = fileStub+".java";
        String classFilename = fileStub+".class";
        //System.out.println(javaFilename);
        // System.out.println(classFilename);

        File javaFile = new File(javaFilename);
        File classFile = new File(classFilename);

        if (javaFile.exists() &&
                (!classFile.exists() || javaFile.lastModified() > classFile.lastModified())) {
            try {
                if (!compile(javaFilename) || !classFile.exists()) {
                    throw new ClassNotFoundException("Compile Failed: " + javaFilename);
                }
            } catch (Exception e) {
                throw new ClassNotFoundException(e.toString());
            }
        }

        try {
            byte raw[] = getBytes(classFilename);

            clas = defineClass(null, raw, 0, raw.length);

        } catch (IOException ie) {

        }

        if (clas == null) {
            clas = findSystemClass(name);
        }

        if (resolve && clas != null) {
            resolveClass(clas);
        }

        if (clas == null) {
            throw new ClassNotFoundException(name);
        }

        return clas;
    }

    private byte[] getBytes(String filename) throws IOException {
        // Find out the length of the file
        File file = new File(filename);
        long len = file.length();

        byte[] raw = new byte[(int) len];

        FileInputStream fs = new FileInputStream(file);

        // Read all of it into the array
        int r = fs.read(raw);
        if (r != len) {
            throw new IOException("Can't read all");
        }
        fs.close();
        return raw;
    }

    private boolean compile(String javaFile) {
        System.out.println("Compiling ..." + javaFile);

        // Start up the compiler
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("javac" + javaFile);
            p.waitFor();
        }
        catch (InterruptedException ie) {
            System.out.println("Failed compilation: " + ie.getMessage());
        }
        catch (Exception e) { }

        int ret = p.exitValue();

        return ret == 0;
    }


}