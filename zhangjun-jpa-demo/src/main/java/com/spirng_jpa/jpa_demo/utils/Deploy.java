package com.spirng_jpa.jpa_demo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Created by 佟盟 on 2018/10/18
 */
public class Deploy {
    /**
     * mvn -s F:\.m2\settings.xml
     * deploy:deploy-file
     * -Durl=http://IP:PORT/nexus/content/repositories/thirdpart
     * -DrepositoryId=thirdpart
     * -Dfile=antlr-2.7.2.jar
     * -DpomFile=antlr-2.7.2.pom
     * -Dpackaging=jar
     * -DgeneratePom=false
     * -Dsources=./path/to/artifact-name-1.0-sources.jar
     * -Djavadoc=./path/to/artifact-name-1.0-javadoc.jar
     */
    public static final String BASE_CMD = "cmd /c mvn " +
            "deploy:deploy-file " +
            "-Durl=http://192.168.137.110:8081/nexus/repository/local_repo/ " +
            "-DrepositoryId=local_repo " +
            "-DgeneratePom=false";

    public static final Pattern DATE_PATTERN = Pattern.compile("-[\\d]{8}\\.[\\d]{6}-");

    public static final Runtime CMD = Runtime.getRuntime();

    public static final Writer ERROR;

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);

    static {
        Writer err = null;
        try {
            err = new OutputStreamWriter(new FileOutputStream("deploy-error.log"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        ERROR = err;
    }

    private static void error(String error) {
        try {
            System.err.println(error);
            ERROR.write(error + "\n");
            ERROR.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkArgs(String[] args) {
        if (args.length != 1) {
            System.out.println("用法如： java -jar Deploy D:\\some\\path\\");
            return false;
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println(args[0] + " 目录不存在!");
            return false;
        }
        if (!file.isDirectory()) {
            System.out.println("必须指定为目录!");
            return false;
        }
        return true;
    }

    private static boolean packingIsPom(File pom) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pom)));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains("<packaging>pom</packaging>")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    private static void deployPom(final File pom) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder cmd = new StringBuilder(BASE_CMD);
                cmd.append(" -DpomFile=").append(pom.getName());
                cmd.append(" -Dfile=").append(pom.getName());
                try {
                    final Process proc = CMD.exec(cmd.toString(), null, pom.getParentFile());
                    writeLog(proc);
                } catch (Exception e) {
                    error("上传失败：" + pom.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void deploy(final File pom, final File jar, final File source, final File javadoc) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                StringBuffer cmd = new StringBuffer(BASE_CMD);
                cmd.append(" -DpomFile=").append(pom.getName());
                if (jar != null) {
                    cmd.append(" -Dpackaging=jar -Dfile=").append(jar.getName());
                } else {
                    cmd.append(" -Dfile=").append(pom.getName());
                }
                if (source != null) {
                    cmd.append(" -Dsources=").append(source.getName());
                }
                if (javadoc != null) {
                    cmd.append(" -Djavadoc=").append(javadoc.getName());
                }

                try {
                    final Process proc = CMD.exec(cmd.toString(), null, pom.getParentFile());
                    writeLog(proc);
                } catch (Exception e) {
                    error("上传失败：" + pom.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        });
    }

    private static File[] getLastDir(File[] files) {
        for (int i = files.length - 1; i > -1; i--) {
            try {
                File file = files[i];
                boolean s = Pattern.compile("^[\\d].*").matcher(file.getName()).matches();
                if (s && file.isDirectory()) {
                    return new File[]{file};
                }
            }catch (Exception ignored){
            }
        }
        return files;
    }

    public static void deploy(File[] files) {
        if (files.length == 0) {
            return;
        }
//        files = getLastDir(files);
        if (files[0].isDirectory()) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deploy(file.listFiles());
                }
            }
        } else if (files[0].isFile()) {
            File pom = null;
            File jar = null;
            File source = null;
            File javadoc = null;
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith(".pom")) {
                    pom = file;
                } else if (name.endsWith("-javadoc.jar")) {
                    javadoc = file;
                } else if (name.endsWith("-sources.jar")) {
                    source = file;
                } else if (name.endsWith(".jar")) {
                    jar = file;
                }
            }
            if (pom != null) {
                if (jar != null) {
                    deploy(pom, jar, source, javadoc);
                } else if (packingIsPom(pom)) {
                    deployPom(pom);
                }
            }
        }
    }

    private static void writeLog(Process proc) throws Exception {
        InputStream inputStream = proc.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("\n\n\n=====================================\n");
        while ((line = reader.readLine()) != null) {
            logBuffer.append(Thread.currentThread().getName() + " : " + line + "\n");
        }
        System.out.println(logBuffer);
    }

    public static void main(String[] args) {
        deploy(Objects.requireNonNull(new File(REPOSITORY_DIR).listFiles()));
        EXECUTOR_SERVICE.shutdown();
        try {
            ERROR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String REPOSITORY_DIR = "D:\\mavenRepository";

}
