package com.qubole;

import com.impl.MasterIndex;
import com.utility.DataHandler;
import com.utility.Utility;

import java.io.IOException;
import java.util.Scanner;


public class Qubole {

    private Scanner in;
    private DataHandler dataHandler;

    Qubole() {
        this.in = new Scanner(System.in);
        this.dataHandler = new DataHandler();
    }

    /**
     * Menu
     * 1.Put
     * 2.Get
     * 3.Delete
     * Q.Exit
     */
    private void menu() {

        Utility util = new Utility();

        // Back ground task to run de duplication
        util.scheduleTaskDeDup();

        // Back ground task to lazily delete the files
        util.scheduleTaskPurgeDanglingFiles();

        try {
            //
            System.out.println();
            System.out.println("Menu");
            System.out.println("1.Put");
            System.out.println("2.Get");
            System.out.println("3.Delete");
            System.out.println("Q.Exit");

            // TODO : Should be removed
            MasterIndex index = MasterIndex.getInstance();
            String input = in.nextLine();

            if (input.equals("1")) {
                System.out.println("Enter the data");
                String data = in.nextLine();
                long id = dataHandler.put(data);

                System.out.println("Data inserted with id: " + id);
                System.out.println("Master Index after data addition");
                index.printIndex();

            } else if (input.equals("2")) {

                System.out.println("Enter the id");
                long id = Long.parseLong(in.nextLine());
                System.out.println(dataHandler.get(id));

            } else if (input.equals("3")) {

                System.out.println("Enter the id");
                long id = Long.parseLong(in.nextLine());
                System.out.println("Deleting with id: " + id);
                dataHandler.delete(id);


            } else if (input.equals("Q")) {
                System.exit(0);
            } else {
                System.out.println("Invalid option !!");
            }
        } catch (IOException e) {
            System.out.println("Problem while working with File System. Please try again.");
            System.out.println();
        } finally {
            menu();
        }
    }

    public static void main(String args[]) {
        Qubole qubole = new Qubole();
        qubole.menu();
    }
}
