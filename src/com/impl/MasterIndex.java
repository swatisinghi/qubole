package com.impl;

import com.background.DanglingFileManager;

import java.util.Map;
import java.util.TreeMap;

/**
 * Singleton class which has the master index.
 *
 * Master index is a map
 * key: fileId
 * value: fileName, File Data CheckSum, No of references to the file
 *
 */
public class MasterIndex {

    private Map<Long, Node> masterMap;

    public Map<Long, Node> getMasterMap() {
        return masterMap;
    }

    private static MasterIndex masterIndex;

    private MasterIndex() {
        this.masterMap = new TreeMap<Long, Node> ();
    }

    public void printIndex() {
        for(Long file : masterMap.keySet()) {
            System.out.println("FileId = " + file + " File name = " + masterMap.get(file).getFileName() +
                    " File data checksum = " + masterMap.get(file).getDataCheckSum() + " Count of references = " + masterMap.get(file).getCount());
        }
    }

    public static MasterIndex getInstance() {
        if(masterIndex == null) {
            synchronized (MasterIndex.class) {
                if(masterIndex == null) {
                    masterIndex = new MasterIndex();
                }
            }
        }
        return masterIndex;
    }

    /*
     * Main methods
     * indexFile
     * getFile
     * deleteFile
     */

    public void indexFile(long fileId) {
        Node node = new Node(String.valueOf(fileId), 1);
        masterMap.put(fileId, node);
    }

    public String getFile(long id) {
        return masterMap.get(id).getFileName();
    }

    public void deleteFile(long id) {
        if(masterMap.containsKey(id)){
            synchronized (MasterIndex.class){
                if(masterMap.containsKey(id)){
                    Node node = masterMap.get(id);
                    int count = node.getCount();

                    if(count > 0){
                        node.setCount(count - 1);

                        masterMap.remove(id);
                        // No references left to the file so adding the persisted file for future deletion
                        if(node.getCount() == 0) {
                            String fileName = node.getFileName();
                            DanglingFileManager.getInstance().getDanglingFileList().add(fileName);
                        }
                    }

                    System.out.println("Master Index after file deletion");
                    MasterIndex.getInstance().printIndex();
                }
            }
        } else {
          System.out.println("File with id: " + id + " doesn't exists");
        }
    }
}
