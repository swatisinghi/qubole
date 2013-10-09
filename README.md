A file system is used to store the data that is entered. A master index is used to store the unique id that is returned as a result of the put operation against the file name, file data check sum and number of references to the file.

The checksum and the references is used to implement the de-duplication and the lazy deletion of the files in the background, as a result of which only one copy of data is stored in the file system until all the references to the file are deleted, and all the ids corresponding to a given data point to the same file stored in the file system.

