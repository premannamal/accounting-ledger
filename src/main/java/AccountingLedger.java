import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.*;

public class AccountingLedger {

    public static void main(String[] args) {
        //throws IOException
        FileReader fileReader;
        FileWriter fileWriter;
        boolean isFileWrite = false;

        Scanner scanner = new Scanner(System.in);
        ArrayList<Transaction> txn = new ArrayList<Transaction>();

        //Open the file for reading
        try {
            fileReader = new FileReader("transactions.csv");
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found");
            return;
        }

        //Read the file
        BufferedReader bufReader = new BufferedReader(fileReader);
        String input;

        // skip header & read the first line
        try {
            bufReader.readLine();
            input = bufReader.readLine();
        } catch (IOException e) {
            System.out.println("Error in reading the line from buffer");
            return;
        }


        //Split the line into tokens and save them in appropriate variables
        while (input != null) {
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

                try {
                    input = bufReader.readLine();
                } catch (IOException e) {
                    System.out.println("Error in reading the line from buffer");
                    return;
                }
            }
        }

        try {
            bufReader.close();
        } catch (IOException e) {
            System.out.println("Error in closing the buffer reader");
            return;
        }


        //Main Menu
        char command;
        do {
            System.out.println();
            System.out.println("Accounting Ledger");
            System.out.println(" D) Add Deposit");
            System.out.println(" P) Make Payment (Debit)");
            System.out.println(" L) Ledger");
            System.out.println(" X) Exit");
            System.out.print("Enter your selection:");
            command = scanner.next().toUpperCase().charAt(0);

            switch (command) {
                case 'D':
                    generateDeposits(txn);
                    isFileWrite = true;
                    break;
                case 'P':
                    generatePayments(txn);
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

        BufferedWriter bufWriter;
        //Write to the file only if Deposits or payments are added.
        if (isFileWrite) {
            try {
                fileWriter = new FileWriter("transactions.csv");
                bufWriter = new BufferedWriter(fileWriter);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                return;
            } catch (IOException f) {
                System.out.println("IO Exceptipon");
                return;
            }

            //for (int i = 0; i < txn.size(); i = i + 1) {
            //use CURRENT instead of txn, because TXN is the list of transactions
            //and current will GET the transaction at position j of the txn list
            for (Transaction current : txn) {
                //Transaction current = txn.get(i);

                try {
                    bufWriter.write(current.getDate() + "|" + current.getTime() + "|" + current.getDescription() + "|" + current.getVendor() + "|" + current.getAmount() + "\n");
                } catch (IOException e) {
                    System.out.println("Error in writing in the file");
                }

            }

            try {
                bufWriter.close();
            } catch (IOException e) {
                System.out.println("Error in closing the file");
            }

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
                    displayAllEntries(txn);
                    break;
                case 'D':
                    displayDeposits(txn);
                    break;
                case 'P':
                    displayPayments(txn);
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

        boolean isAtleastOneTransactionWithVendorMatch = false;

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
                    displayMonthToDate(txn,firstDateStr,monthToDateStr);
                    break;
                case '2':
                    displayPreviousMonth(txn,previousFirstDateStr,previousLastDateStr);
                    break;
                case '3':
                    displayYearToDate(txn,currentYearStr);
                    break;
                case '4':
                    displayPreviousYear(txn,previousYearStr);
                    break;
                case '5':
                    displayByVendorName(txn,isAtleastOneTransactionWithVendorMatch);
                    break;
                case '6':
                    customSearch(txn);
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

    public static void generateDeposits(ArrayList<Transaction> txn) {
        Scanner scanner = new Scanner(System.in);
        String transactionDate, transactionTime;
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //scanner.nextLine();
        //validate date
        System.out.println("Please enter the Deposit transaction date in YYYY-MM-DD format:");
        while ( true ) {
            try {
                transactionDate = scanner.nextLine();
                LocalDate trandate = LocalDate.parse(transactionDate,Formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Date Time format. Please re-enter in YYYY-MM-DD format:");
            }
        }

        //Validate time
        System.out.println("Please enter the Deposit transaction time in HH:MI:SS format:");
        while ( true ) {
            try {
                transactionTime = scanner.nextLine();
                LocalTime tranTime = LocalTime.parse(transactionTime,timeFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Date Time format. Please re-enter in HH:MI:SS format:");
            }
        }

        System.out.println("Please enter the Deposit description:");
        String description = scanner.nextLine();
        System.out.println("Please enter the Depositing vendor name:");
        String vendor = scanner.nextLine();
        System.out.println("Please enter the Deposit amount as a positive number:");
        float amount = scanner.nextFloat();
        while (amount <= 0.0f) {
            System.out.println("Please re-enter the Deposit amount as a positive number:");
            amount = scanner.nextFloat();
        }

        Transaction depositTransaction = new Transaction(transactionDate, transactionTime, description, vendor, amount);
        txn.add(depositTransaction);
    }

    public static void generatePayments(ArrayList<Transaction> txn) {
        Scanner scanner = new Scanner(System.in);
        String paymentTransactionDate, paymentTransactionTime;
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //scanner.nextLine();

        //validate date
        System.out.println("Please enter the payment/debit transaction date in YYYY-MM-DD format:");
        while ( true ) {
            try {
                paymentTransactionDate = scanner.nextLine();
                LocalDate trandate = LocalDate.parse(paymentTransactionDate,Formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Date Time format. Please re-enter the payment/debit transaction in YYYY-MM-DD format:");
            }
        }

        //Validate time
        System.out.println("Please enter the Payment/Debit transaction time in HH:MI:SS format:");
        while ( true ) {
            try {
                paymentTransactionTime = scanner.nextLine();
                LocalTime tranTime = LocalTime.parse(paymentTransactionTime,timeFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Time format. Please re-enter the Payment/Debit transaction time in HH:MI:SS format:");
            }
        }

        System.out.println("Please enter the Payment/Debit description:");
        String paymentDescription = scanner.nextLine();
        System.out.println("Please enter the Payment/Debit vendor name:");
        String paymentVendor = scanner.nextLine();
        System.out.println("Please enter the Payment/Debit amount:");
        float paymentAmount = scanner.nextFloat();
        while (paymentAmount < 0.0f) {
            System.out.println("Please re-enter the payment as positive amount:");
            paymentAmount = scanner.nextFloat();
        }
        Transaction paymentTransaction = new Transaction(paymentTransactionDate, paymentTransactionTime, paymentDescription, paymentVendor, paymentAmount * (-1));
        txn.add(paymentTransaction);
    }

    public static void displayAllEntries(ArrayList<Transaction> txn) {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying all transactions...\n");
        displayHeader();
        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            //System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No entries to display in the file...");
        }

    }

    public static void displayDeposits(ArrayList<Transaction> txn) {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying deposit transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if (t.getAmount() > 0.0) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No Deposits found in the file");
        }
    }

    public static void displayPayments(ArrayList<Transaction> txn)
    {

        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying payment transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if (t.getAmount() < 0.0) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No Payments found in the file");
        }

    }

    public static void displayMonthToDate(ArrayList<Transaction> txn, String firstDateStr, String monthToDateStr)
    {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying month to date transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if ((t.getDate().compareTo(firstDateStr) >= 0) && (t.getDate().compareTo(monthToDateStr) <= 0)) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No month to date transactions found in the file");
        }


    }

    public static void displayPreviousMonth(ArrayList<Transaction> txn, String previousFirstDateStr, String previousLastDateStr) {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying previous month transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if ((t.getDate().compareTo(previousFirstDateStr) >= 0) && (t.getDate().compareTo(previousLastDateStr) <= 0)) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No previous month transactions found in the file");
        }

    }

    public static void displayYearToDate(ArrayList<Transaction> txn, String currentYearStr) {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying year to date transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if (currentYearStr.equals(t.getDate().substring(0, 4))) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No year to date transactions found in the file");
        }

    }

    public static void displayPreviousYear(ArrayList<Transaction> txn, String previousYearStr) {
        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying previous year transactions...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            if (previousYearStr.equals(t.getDate().substring(0, 4))) {
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                //System.out.println(t.getDate() + ";" + t.getTime() + ";" + t.getDescription() + ";" + t.getVendor() + ";" + t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No previous year transactions found in the file");
        }

    }

    public static void displayByVendorName(ArrayList<Transaction> txn, boolean isAtleastOneTransactionWithVendorMatch)
    {
        Scanner scanner = new Scanner(System.in);
        isAtleastOneTransactionWithVendorMatch = false;
        System.out.println("Please enter the vendor name to search the transactions:");
        String vendorName = scanner.nextLine();

        System.out.println("_________________________________________________________________________________");
        System.out.println("Displaying transactions from vendor: " + vendorName + "...\n");
        displayHeader();

        for (int i = 0; i < txn.size(); i = i + 1) {
            Transaction t = txn.get(i);
            //Check if vendorName matches
            if (vendorName.equalsIgnoreCase(t.getVendor())) {
                isAtleastOneTransactionWithVendorMatch = true;
                System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }

        if (txn.size() > 0) {
            System.out.println("______________________________________________________________________________________________________________");
        } else if (txn.size() == 0 )
        {
            System.out.println("No transactions from vendor:" + vendorName);
        }

        if (!isAtleastOneTransactionWithVendorMatch) {
            System.out.println("No transactions from vendor:" + vendorName);
        }
    }



    public static void customSearch(ArrayList<Transaction> txn)
    {
        Scanner scanner = new Scanner(System.in);
        float amount = 0.0f;
        boolean isAnyHit = false;
        String startDate, endDate;
        System.out.println("Custom search sub menu");
        System.out.println("Press ENTER to skip any filter");
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        //validate date
        System.out.println("Please enter the start date in YYYY-MM-DD format:");
        while ( true ) {
            try {
                startDate = scanner.nextLine();
                if ( startDate == null || startDate.isEmpty() )
                {
                    break;
                }
                LocalDate trandate = LocalDate.parse(startDate,Formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Date format. Please re-enter start date in YYYY-MM-DD format:");
            }
        }

        System.out.println("Please enter the end date in YYYY-MM-DD format:");
        while ( true ) {
            try {
                endDate = scanner.nextLine();
                if ( endDate == null || endDate.isEmpty() )
                {
                    break;
                }
                LocalDate trandate = LocalDate.parse(endDate,Formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error in Date format. Please re-enter end date in YYYY-MM-DD format:");
            }
        }

        System.out.println("Please enter the description:");
        String description = scanner.nextLine();
        System.out.println("Please enter the vendor name:");
        String vendor = scanner.nextLine();
                /* The scanner.nextFloat always expects a value which is not null or <CR>
                Hence reading this as String and converting it to float */
        System.out.println("Please enter the amount:");
        String amountString = scanner.nextLine();
        String input = ""; // or from scanner.nextLine()

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
                if (!curr.getDescription().toLowerCase().contains(description.toLowerCase())) {
                    //System.out.println("6 Description");
                    continue;
                }
            }

            if (vendor != null && !vendor.isEmpty()) {
                if (!curr.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
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
                System.out.println("_________________________________________________________________________________");
                System.out.println("Custom search results .....\n");
                displayHeader();
            }

            //System.out.println(curr.getDate() + "|" + curr.getTime() + "|" + curr.getDescription() + "|" + curr.getVendor() + "|" + curr.getAmount());
            System.out.printf("%-10s | %-8s | %-50s | %-20s | $%7.2f\n", curr.getDate(), curr.getTime(), curr.getDescription(), curr.getVendor(), curr.getAmount());
            isAnyHit = true;
        }

        if (!isAnyHit) {
            System.out.println("There are no transactions fulfilling that criteria");
        } else {
            System.out.println("\n______________________________________________________________________________________________________________");;
        }

    }

    public static void displayHeader() {
        System.out.println("______________________________________________________________________________________________________________");
        System.out.printf("%-10s | %-8s | %-50s | %-20s | %-9s", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("\n______________________________________________________________________________________________________________");
    }

}