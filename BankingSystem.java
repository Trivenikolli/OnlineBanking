import java.util.*;

class Account {
    int id;
    String name;
    double balance;
    LinkedList<String> transactionHistory;

    Account(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactionHistory = new LinkedList<>();
    }
}

class Loan {
    int loanId;
    int accountId;
    double amount;
    int tenure; // in months
    double monthlyEMI;

    Loan(int loanId, int accountId, double amount, int tenure, double monthlyEMI) {
        this.loanId = loanId;
        this.accountId = accountId;
        this.amount = amount;
        this.tenure = tenure;
        this.monthlyEMI = monthlyEMI;
    }
}

public class BankingSystem {
    LinkedList<Account> accounts = new LinkedList<>();
    Stack<String> transactionStack = new Stack<>();
    Queue<Loan> loanQueue = new LinkedList<>();
    int loanCounter = 1;

    public static void main(String[] args) {
        BankingSystem bank = new BankingSystem();
        bank.runBankingSystem();
    }

    public void runBankingSystem() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Online Banking Simulator ---");
            System.out.println("1. Create Account");
            System.out.println("2. View Balance");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Apply for Loan");
            System.out.println("7. Process Loans");
            System.out.println("8. View Transaction History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    viewBalance(scanner);
                    break;
                case 3:
                    depositMoney(scanner);
                    break;
                case 4:
                    withdrawMoney(scanner);
                    break;
                case 5:
                    transferMoney(scanner);
                    break;
                case 6:
                    applyForLoan(scanner);
                    break;
                case 7:
                    processLoans();
                    break;
                case 8:
                    viewTransactionHistory(scanner);
                    break;
                case 9:
                    System.out.println("Exiting... Thank you for using the Online Banking Simulator!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createAccount(Scanner scanner) {
        System.out.print("Enter your name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();
        System.out.print("Enter initial deposit amount: ");
        double balance = scanner.nextDouble();
        int id = accounts.size() + 1;
        accounts.add(new Account(id, name, balance));
        System.out.println("Account created successfully! Your Account ID is: " + id);
    }

    private void viewBalance(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int id = scanner.nextInt();
        Account account = findAccount(id);
        if (account != null) {
            System.out.println("Your current balance is: ₹" + account.balance);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void depositMoney(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int id = scanner.nextInt();
        Account account = findAccount(id);
        if (account != null) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            account.balance += amount;
            String transaction = "Deposited ₹" + amount;
            account.transactionHistory.add(transaction);
            transactionStack.push(transaction);
            System.out.println("Deposit successful! New balance: ₹" + account.balance);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void withdrawMoney(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int id = scanner.nextInt();
        Account account = findAccount(id);
        if (account != null) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            if (amount <= account.balance) {
                account.balance -= amount;
                String transaction = "Withdrew ₹" + amount;
                account.transactionHistory.add(transaction);
                transactionStack.push(transaction);
                System.out.println("Withdrawal successful! New balance: ₹" + account.balance);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private void transferMoney(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int senderId = scanner.nextInt();
        Account sender = findAccount(senderId);
        if (sender != null) {
            System.out.print("Enter Receiver Account ID: ");
            int receiverId = scanner.nextInt();
            Account receiver = findAccount(receiverId);
            if (receiver != null) {
                System.out.print("Enter amount to transfer: ");
                double amount = scanner.nextDouble();
                if (amount <= sender.balance) {
                    sender.balance -= amount;
                    receiver.balance += amount;
                    String transaction = "Transferred ₹" + amount + " to Account ID " + receiverId;
                    sender.transactionHistory.add(transaction);
                    transactionStack.push(transaction);
                    System.out.println("Transfer successful! New balance: ₹" + sender.balance);
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Receiver Account not found.");
            }
        } else {
            System.out.println("Sender Account not found.");
        }
    }

    private void applyForLoan(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int id = scanner.nextInt();
        Account account = findAccount(id);
        if (account != null) {
            System.out.print("Enter loan amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter tenure (in months): ");
            int tenure = scanner.nextInt();
            double monthlyEMI = (amount + (amount * 0.05 * tenure / 12)) / tenure;
            loanQueue.add(new Loan(loanCounter++, id, amount, tenure, monthlyEMI));
            System.out.println("Loan application submitted. It will be processed shortly.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private void processLoans() {
        if (!loanQueue.isEmpty()) {
            Loan loan = loanQueue.poll();
            System.out.println("Processing Loan ID " + loan.loanId + " for Account ID " + loan.accountId);
            System.out.println("Loan Amount: ₹" + loan.amount + ", Monthly EMI: ₹" + loan.monthlyEMI);
        } else {
            System.out.println("No loans to process.");
        }
    }

    private void viewTransactionHistory(Scanner scanner) {
        System.out.print("Enter your Account ID: ");
        int id = scanner.nextInt();
        Account account = findAccount(id);
        if (account != null) {
            System.out.println("Transaction History:");
            for (String transaction : account.transactionHistory) {
                System.out.println(transaction);
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private Account findAccount(int id) {
        for (Account account : accounts) {
            if (account.id == id) {
                return account;
            }
        }
        return null;
    }
}
