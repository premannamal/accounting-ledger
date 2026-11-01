import java.util.*;
import java.io.*;

public class AccountingLedger {
    public static void main(String[] args) throws IOException {
        FileReader fileReader;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Transaction> txn = new ArrayList<Transaction>();

        //Open the file for reading
        try {
            fileReader = new FileReader("transactions.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Read the file
        BufferedReader bufReader = new BufferedReader(fileReader);

        // skip header
        bufReader.readLine();

        //Split the line into tokens and save them in appropriate variables
        String input;
        while ((input = bufReader.readLine()) != null) {
            String[] tokens = input.split("\\|");
            if (tokens.length >= 5) {
                String date = tokens[0];
                String time = tokens[1];
                String description = tokens[2];
                String vendor = tokens[3];
                float amount = Float.parseFloat(tokens[4]);
                //System.out.println(date +";"+ time +";"+ description +";"+ vendor +";"+ amount);

                //Add the transaction record read into txn array list
                txn.add(new Transaction(date,time,description,vendor,amount));
            }
        }
        bufReader.close();

        //Main Menu
        char command;
        do {
            System.out.println("Accounting Ledger");
            System.out.println(" D) Add Deposit");
            System.out.println(" P) Make Payment (Debit)");
            System.out.println(" L) Ledger");
            System.out.println(" X) Exit");
            System.out.print("Enter your selection:");
            command = scanner.next().charAt(0);

            switch (command) {
                case 'D':
                    break;
                case 'P':
                    break;
                case 'L':
                    //displayLedgerScreen(txn);
                    break;
                case 'X':
                    break;
            }
        } while (command != 'X');

    }
}