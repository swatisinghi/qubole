package com.background;

import com.utility.Utility;
import java.util.LinkedList;

/**
 * Singleton class which contains a list of all the file names that
 * are scheduled for deletion. A process runs in the background
 * which reads from the list and deletes the files from the file system.
 */

public class DanglingFileManager {

    private LinkedList<String> danglingFileList;

    public LinkedList<String> getDanglingFileList() {
        return danglingFileList;
    }

    private static DanglingFileManager danglingFileManager;

    private DanglingFileManager() {
        this.danglingFileList = new LinkedList<String>();
    }

    public static DanglingFileManager getInstance() {
        if(danglingFileManager == null) {
            synchronized (DanglingFileManager.class) {
                if(danglingFileManager == null) {
                    danglingFileManager = new DanglingFileManager();
                }
            }
        }

        return danglingFileManager;
    }

    public void purge() {
        Utility util = new Utility();
        for (int i = 0; i < danglingFileList.size(); ++i) {
            System.out.println("Removing file " + danglingFileList.get(i));
            util.deleteData(danglingFileList.remove(i));
        }
    }
}
