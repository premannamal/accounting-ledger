import java.util.*;
import java.io.*;

public class AccountingLedger {

    public static void main(String[] args) throws IOException {
        FileReader fileReader;
        FileWriter fileWriter;
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
                txn.add(new Transaction(date, time, description, vendor, amount));
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
                    ledgerMenu(txn);
                    break;
                case 'X':
                    break;
            }
        } while (command != 'X');

        try {
            fileWriter = new FileWriter("transactions.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Write the changes to the file
        BufferedWriter bufWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < txn.size(); i = i + 1) {
            /* use CURRENT instead of txn, because TXN is the list of transactions
               and current will GET the transaction at position j of the txn list */
            Transaction current = txn.get(i);
            bufWriter.write(current.getDate() + "|" + current.getTime() + "|" + current.getDescription() + "|" + current.getVendor() + "|" + current.getAmount());
        }
        bufWriter.close();
    }

    public static void ledgerMenu(ArrayList<Transaction> txn) {
        char command;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Ledger Sub Menu");
            System.out.println(" A) All");
            System.out.println(" D) Deposits");
            System.out.println(" P) Payments");
            System.out.println(" R) Reports");
            System.out.println(" H) Main Menu");
            System.out.print("Enter your selection:");
            command = scanner.next().charAt(0);

            switch (command) {
                case 'A':
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
                    }
                    break;
                case 'D':
                    break;
                case 'P':
                    break;
                case 'R':
                    break;
                case 'H':
                    break;
            }
        } while (command != 'H');
    }

}

