package com.background;

import com.impl.MasterIndex;
import com.impl.Node;
import com.utility.Utility;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.TreeMap;

public class DeDup {

    public DeDup() {
    }

    private int calCheckSum(String fileName) {
        Utility utility = new Utility();
        try {
            String fileContent = utility.retrieveData(fileName);
            return fileContent.hashCode();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Background task that calculates the checksum from the file data
     * and populates in the master index which is then used for de-duplication
     */

    public void calCheckSum() {
        Map<Long, Node> map = MasterIndex.getInstance().getMasterMap();

        for (Long fileId : map.keySet()) {

            Node node = map.get(fileId);
            if (node.getDataCheckSum() == 0) {
                int checksum = this.calCheckSum(node.getFileName());
                node.setDataCheckSum(checksum);
            }
        }
    }

    /**
     * Traverses through the master index, keeps only one copy of the file
     * (duplicate copies are identified using the checksum calculated previously),
     * the extra files with the same data are then added to the dangling file list
     * which is then lazily deleted.
     */

    public void deDuplicate() {

        this.calCheckSum(); // Calculate Checksum for the files

        Map<Long, Node> masterMap = MasterIndex.getInstance().getMasterMap();

        synchronized (MasterIndex.class) {
            Map<Integer, Node> dataCheckSumMap = new TreeMap<Integer, Node>();

            for (Long file : masterMap.keySet()) {
                Node node = masterMap.get(file);
                int dataCheckSum = node.getDataCheckSum();

                if (dataCheckSumMap.containsKey(dataCheckSum)) {
                    Node checkSumNode = dataCheckSumMap.get(dataCheckSum);
                    if (!checkSumNode.equals(node)) {
                        DanglingFileManager danglingFileManager = DanglingFileManager.getInstance();
                        danglingFileManager.getDanglingFileList().add(node.getFileName());

                        checkSumNode.setCount(checkSumNode.getCount() + 1);
                        masterMap.put(file, checkSumNode);

                        System.out.println("Master Index after de-duplication");
                        MasterIndex.getInstance().printIndex();
                    }
                } else {
                    dataCheckSumMap.put(dataCheckSum, node);
                }

            }
        }
    }
}
