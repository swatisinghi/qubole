package com.impl;

/***
 * Value in the master index is of type node.
 */
public class Node {

    private String fileName;
    private int count;
    private int dataCheckSum;

    public Node(String fileName, int count) {
        this.fileName = fileName;
        this.count = count;
    }

    public String getFileName() {
        return fileName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDataCheckSum() {
        return dataCheckSum;
    }

    public void setDataCheckSum(int dataCheckSum) {
        this.dataCheckSum = dataCheckSum;
    }




    public boolean equals(Node node) {
        return fileName.equals(node.fileName);
    }


}


