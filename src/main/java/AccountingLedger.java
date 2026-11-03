import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class AccountingLedger {

    public static void main(String[] args) throws IOException {
        FileReader fileReader;
        FileWriter fileWriter;
        Boolean isFileWrite = false;
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
            System.out.println("");
            System.out.println("Accounting Ledger");
            System.out.println(" D) Add Deposit");
            System.out.println(" P) Make Payment (Debit)");
            System.out.println(" L) Ledger");
            System.out.println(" X) Exit");
            System.out.print("Enter your selection:");
            command = scanner.next().toUpperCase().charAt(0);

            switch (command) {
                case 'D':
                    scanner.nextLine();
                    System.out.println("Please enter the Deposit transaction date in YYYY-MM-DD format:");
                    String transactionDate = scanner.nextLine();
                    System.out.println("Please enter the Deposit transaction time in HH:MI:DD format:");
                    String transactionTime = scanner.nextLine();
                    System.out.println("Please enter the Deposit description:");
                    String description = scanner.nextLine();
                    System.out.println("Please enter the Depositing vendor name:");
                    String vendor = scanner.nextLine();
                    System.out.println("Please enter the Deposit amount as a positive number:");
                    Float amount = scanner.nextFloat();
                    Transaction depositTransaction = new Transaction(transactionDate, transactionTime, description, vendor, amount);
                    txn.add(depositTransaction);
                    isFileWrite = true;
                    break;
                case 'P':
                    scanner.nextLine();
                    System.out.println("Please enter the Payment/Debit transaction date in YYYY-MM-DD format:");
                    String PaymentTransactionDate = scanner.nextLine();
                    System.out.println("Please enter the Payment/Debit transaction time in HH:MI:DD format:");
                    String paymentTransactionTime = scanner.nextLine();
                    System.out.println("Please enter the Payment/Debit description:");
                    String paymentDescription = scanner.nextLine();
                    System.out.println("Please enter the Payment/Debit vendor name:");
                    String paymentVendor = scanner.nextLine();
                    System.out.println("Please enter the Payment/Debit amount as a negative number:");
                    Float paymentAmount = scanner.nextFloat();
                    Transaction paymentTransaction = new Transaction(PaymentTransactionDate, paymentTransactionTime, paymentDescription, paymentVendor, paymentAmount);
                    txn.add(paymentTransaction);
                    isFileWrite = true;
                    break;
                case 'L':
                    ledgerMenu(txn);
                    break;
                case 'X':
                    break;
                default:
                    System.out.println("Invalid Option! Please choose a valid option");
                    System.out.println("");
                    break;
            }
        } while (command != 'X');


        try {
            fileWriter = new FileWriter("transactions.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (isFileWrite) {
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < txn.size(); i = i + 1) {
                //use CURRENT instead of txn, because TXN is the list of transactions
                //and current will GET the transaction at position j of the txn list
                Transaction current = txn.get(i);
                bufWriter.write(current.getDate() + "|" + current.getTime() + "|" + current.getDescription() + "|" + current.getVendor() + "|" + current.getAmount() + "\n");
            }
            bufWriter.close();
        }

    }

    public static void ledgerMenu(ArrayList<Transaction> txn) {
        char command;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("");
            System.out.println("Ledger Sub Menu");
            System.out.println(" A) All");
            System.out.println(" D) Deposits");
            System.out.println(" P) Payments");
            System.out.println(" R) Reports");
            System.out.println(" H) Main Menu");
            System.out.print("Enter your selection:");
            command = scanner.next().toUpperCase().charAt(0);


            switch (command) {
                case 'A':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying all transactions...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                    }
                    break;
                case 'D':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying all deposits...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        if (t.getAmount() > 0.0) {
                            System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
                        }
                    }
                    break;
                case 'P':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying all payments...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        if (t.getAmount() < 0.0) {
                            System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
                        }
                    }
                    break;
                case 'R':
                    reportMenu(txn);
                    break;
                case 'H':
                    break;
                default:
                    System.out.println("Invalid Option! Please choose a valid option");
                    System.out.println("");
                    break;
            }
        } while (command != 'H');
    }

    public static void reportMenu(ArrayList<Transaction> txn) {
        char command;
        Scanner scanner = new Scanner(System.in);
        //Figure out the current months start date and end date
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDate = currentYearMonth.atDay(1);
        LocalDate lastDate = currentYearMonth.atEndOfMonth();
        String firstDateStr = firstDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lastDateStr = lastDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String monthToDateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        //Figure out the previous months start date and end date
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        LocalDate firstDateOfPreviousMonth = previousYearMonth.atDay(1);
        LocalDate lastDateOfPreviousMonth = previousYearMonth.atEndOfMonth();
        String previousFirstDateStr = firstDateOfPreviousMonth.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String previousLastDateStr = lastDateOfPreviousMonth.format(DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate today = LocalDate.now();
        //Get currentYear as a string and store it in currentYearStr
        String currentYearStr = String.valueOf(today.getYear());
        String previousYearStr = String.valueOf(today.getYear() - 1);

        Boolean isAtleastOneTransactionWithVendorMatch = false;

        do {
            System.out.println("");
            System.out.println("Report Sub Menu");
            System.out.println(" 1) Month To Date");
            System.out.println(" 2) Previous Month");
            System.out.println(" 3) Year To Date");
            System.out.println(" 4) Previous Year");
            System.out.println(" 5) Search By Vendor");
            System.out.println(" 6) Custom Search");
            System.out.println(" 0) Back");
            System.out.print("Enter your selection:");
            command = scanner.next().charAt(0);


            switch (command) {
                case '1':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying month to date transactions...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        if ((t.getDate().compareTo(firstDateStr) >= 0) && (t.getDate().compareTo(monthToDateStr) <= 0)) {
                            System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                        }
                    }
                    break;
                case '2':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying previous month transactions...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        if ((t.getDate().compareTo(previousFirstDateStr) >= 0) && (t.getDate().compareTo(previousLastDateStr) <= 0)) {
                            System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
                        }
                    }
                    break;
                case '3':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying year to date transactions...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        //Check if current year is equal to the transaction year
                        if (currentYearStr.equals(t.getDate().substring(0, 4))) {
                            System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                        }
                    }
                    break;
                case '4':
                    System.out.println("_________________________________________");
                    System.out.println("Displaying previous year transactions...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        //Check if current year is equal to the transaction year
                        if (previousYearStr.equals(t.getDate().substring(0, 4))) {
                            System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                        }
                    }
                    break;
                case '5':
                    //prompt the user for vendor name and display all entries for that vendor
                    isAtleastOneTransactionWithVendorMatch = false;
                    System.out.println("Please enter the vendor name to search the transactions:");
                    scanner.nextLine();
                    String vendorName = scanner.nextLine();
                    System.out.println("Searching for vendor name " + vendorName + " ignoring the case ...");
                    for (int i = 0; i < txn.size(); i = i + 1) {
                        Transaction t = txn.get(i);
                        //Check if vendorName matches
                        if (vendorName.equalsIgnoreCase(t.getVendor())) {
                            isAtleastOneTransactionWithVendorMatch = true;
                            System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                        }
                    }
                    if (!isAtleastOneTransactionWithVendorMatch) {
                        System.out.println("No transactions from that vendor ....");
                    }
                    break;
                case '6':
                    scanner.nextLine();
                    //clear screen
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    float amount = 0.0f;
                    boolean isAnyHit = false;
                    System.out.println("Custom Search sub menu");
                    System.out.println("Press ENTER to skip any filter");
                    System.out.println("Please enter the start date in YYYY-MM-DD format:");
                    String startDate = scanner.nextLine();
                    System.out.println("Please enter the end date in YYYY-MM-DD format:");
                    String endDate = scanner.nextLine();
                    System.out.println("Please enter the description:");
                    String description = scanner.nextLine();
                    System.out.println("Please enter the vendor name:");
                    String vendor = scanner.nextLine();
                /* The scanner.nextFloat always expects a value which is not null or <CR>
                Hence reading this as String and converting it to float */
                    System.out.println("Please enter the amount:");
                    String amountString = scanner.nextLine();
                    String input = ""; // or from scanner.nextLine()
                    //clear screen
                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    if (amountString != null && !amountString.trim().isEmpty()) {
                        try {
                            amount = Float.parseFloat(amountString);
                            //System.out.println("Parsed amount: " + amount);
                        } catch (NumberFormatException e) {
                            //System.out.println("The string is not a valid number: " + input);
                        }
                    }

                    //System.out.println("DEBUG MESSAGE" + startDate + "|" + endDate + "|" + description + "|" + vendor + "|" + amountString + "|");
                    ArrayList<Transaction> txnDup = new ArrayList<Transaction>();

                    for (int i = 0; i < txn.size(); i++) {
                        Transaction curr = txn.get(i);

                        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                            if ((curr.getDate().compareTo(startDate) < 0) || (curr.getDate().compareTo(endDate) > 0)) {
                                //System.out.println("6 Date:" + curr.getDate() + startDate + endDate);
                                continue;
                            }
                        }

                        if (description != null && !description.isEmpty()) {
                            if (!curr.getDescription().contains(description)) {
                                //System.out.println("6 Description");
                                continue;
                            }
                        }

                        if (vendor != null && !vendor.isEmpty()) {
                            if (!curr.getVendor().contains(vendor)) {
                                //System.out.println("6 Vendor");
                                continue;
                            }
                        }

                        if (amountString != null && !amountString.isEmpty()) {
                            if (curr.getAmount() != amount) {
                                //System.out.println("6 amount");
                                continue;
                            }
                        }
                        if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty()) &&
                                (description == null || description.isEmpty()) && (vendor == null || vendor.isEmpty()) &&
                                (amountString == null || amountString.isEmpty())) {
                            //System.out.println("All nulls");
                            continue;
                        }

                        txnDup.add(curr);

                        if (isAnyHit == false) {
                            System.out.println("_________________________________________");
                            System.out.println("Transactions fulfilling the criteria are:");
                        }

                        System.out.println(curr.getDate() + "|" + curr.getTime() + "|" + curr.getDescription() + "|" + curr.getVendor() + "|" + curr.getAmount());
                        isAnyHit = true;
                    }

                    if (!isAnyHit) {
                        System.out.println("There are no transactions fulfilling that criteria");
                    } else {
                        System.out.println("_________________________________________");
                    }

                    //for (int i = 0; i < txnDup.size(); i++) {
                    //Transaction t = txnDup.get(i);
                    //---System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
                    //}

                    break;
                case '0':
                    break;
                default:
                    System.out.println("Invalid Option! Please choose a valid option");
                    System.out.println("");
                    break;

            }
        } while (command != '0');
    }

}
