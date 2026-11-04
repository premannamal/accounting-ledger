# Accounting Ledger Application

An accounting ledger, also known as a general ledger, is the physical or digital record of a company's finances. 

## Description of the Project

Accounting ledger Application is a Java-based command line interface application that tracks all financial transactions for a business or for personal use. 
All transactions in the application are read from and saved to a transaction file named transactions.csv. Each transaction should be saved as a single line with the following format: 
date|time|description|vendor|amount

## Features

- Adds a deposit
- Adds a payment
- Displays all entries in the ledger
- Displays deposit entries into the account 
- Displays the payments
- Generate Month To Date reports
- Generate Previous Month transactions report
- Generate Year To Date transactions report
- Generate Previous year transaction report
- Provides ability to search transactions by vendor
- Provides ability to custom search based on combination of all these attributes: start date, end date, description, vendor and amount. Some of these attributes may be null.

## User Stories

- As a user, I want to add a deposit or payment.
- As a user, I want to see all the ledger entries (or) part of the ledger entries (deposits or payments)
- As a user, I want to generate various reports such as Month To Date, Year To Date, Previous Month, Previous Year
- As a user, I want to search the transactions by Vendor.
- As a user, I want to custom search on a combination of attributes viz. start date, end date, description, vendor and amount.


## Setup

### Prerequisites

- Java 17 (Amazon Corretto), the version used in class.
- IntelliJ IDEA Community Edition: <https://www.jetbrains.com/idea/download/>

### Running the application

1. In IntelliJ: File > Open, pick the project folder, press Open.
2. Wait for IntelliJ to finish indexing (progress bar at the bottom).
3. Open `src/main/java/AccountingLedger.java`.
4. Right-click inside the file and choose Run 'AccountingLedger.main()'.

### Data and configuration

- `transactions.csv` sits at the project root, next to `pom.xml`. The program reads it at start and appends to it when a payment or deposit is entered. The first line is a header: 
date|time|description|vendor|amount

## Technologies Used

- Java 27
- IntelliJ IDEA

## Demo

![Main menu](images/menu.png)

![Monthly total](images/month-total.png)

## A piece of code I am proud of

```java


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

This is the piece of code I am very proud of. Because, this does custom search. If the user enters a value for a field I filter on this field and if the user does not enter a value, I should not filter on 
that field. This is a challenge question that i raised up to.


## Future Work

- Customize the application by giving it an unique app name and starter screen.


## Resources

- Workbook 3a, 3b, 3w

## Author

- **Prem Annamalai**

## Thanks

- Thank you to Mr. Matt C for providing valuable feedback and Gabriel C for sharing a sample README file.
