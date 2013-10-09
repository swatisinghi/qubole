package com.utility;

import com.background.DanglingFileManager;
import com.background.DeDup;

import java.io.*;
import java.util.*;

public class Utility {

    public Utility() {

    }

    /**
     * Generates a unique id for
     * Concatenates the time with a random number
     */

    public long idGenerator() {
        Date date = new Date();
        return Long.valueOf(String.valueOf(date.getTime()).concat(String.valueOf(new Random().nextInt(1000))));
    }

    public long persistData(long fileId, String data) throws IOException {
        // TODO : remove absolute path
        String fileName = String.valueOf(fileId);
        String filePath = fileName;
        File file = new File(filePath);

        //Try until a file is available for creation
        while (file.exists()) {
            fileId++;

            fileName = String.valueOf(fileId);
            filePath = "/Users/swatis/MyWork/qubole-java/".concat(fileName);
            file = new File(filePath);
        }

        file.createNewFile();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(data);
        bw.close();

        return fileId;
    }

    public String retrieveData(String fileName) throws FileNotFoundException {
        String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();

        return content;
    }

    public void deleteData(String fileName) {
        File file = new File(fileName);
        if(file.exists())
            file.delete();
    }

    /**
     * Scheduling background de-duplication and dangling file purge tasks
     */
    public void scheduleTaskDeDup() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DeDup deDup = new DeDup();
                deDup.deDuplicate();
            }
        }, 5000, 5000);
    }

    public void scheduleTaskPurgeDanglingFiles() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (DanglingFileManager.class) {
                    DanglingFileManager danglingFileManager = DanglingFileManager.getInstance();
                    danglingFileManager.purge();
                }
            }
        }, 10000, 10000);
    }

}
