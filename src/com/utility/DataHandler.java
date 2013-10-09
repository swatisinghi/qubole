package com.utility;

import com.impl.MasterIndex;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataHandler {

    private Utility util;

    public DataHandler() {
        this.util = new Utility();
    }

    /**
     * The file is persisted synchronously  in the file system,
     * a reference of the file id is also added to the master index
     * which is then used for de-duplication in the background
     */
    public long put(String data) throws IOException {

        long fileId = util.idGenerator();

        fileId = util.persistData(fileId, data);

        MasterIndex.getInstance().indexFile(fileId);
        return fileId;
    }

    /**
     * Takes in the fileId gets the corresponding name
     * from the master map and retrieves the data.
     */
    public String get(long id) throws FileNotFoundException {
        String fileName = MasterIndex.getInstance().getFile(id);
        return util.retrieveData(fileName);
    }

    /**
     * The file is deleted only from the master index,
     * the actual file name is added to the dangling file list
     * which is deleted lazily in the background.
     */
    public void delete(long id) {
        MasterIndex.getInstance().deleteFile(id);
    }
}
