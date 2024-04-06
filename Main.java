import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

class BankAccount {
    private String accNo;
    private String name;
    protected float balance;

    public BankAccount(String accNo, String name) {
        this.accNo = accNo;
        this.name = name;
        this.balance = 0;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public void deposit(float amount) {
        if (amount > 0) {
            balance += amount;
            JOptionPane.showMessageDialog(null, "Deposit successful.");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid amount for deposit.");
        }
    }

    public void withdraw(float amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            JOptionPane.showMessageDialog(null, "Withdrawal successful.");
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance or invalid amount for withdrawal.");
        }
    }

    public void transferFunds(BankAccount recipient, float amount) {
        if (amount > 0 && balance >= amount) {
            withdraw(amount);
            recipient.deposit(amount);
            JOptionPane.showMessageDialog(null, "Transfer successful.");
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance or invalid amount for transfer.");
        }
    }
}

class SavingsAccount extends BankAccount {
    private final float PENALTY = 50;
    private final int MIN_BALANCE = 500;

    public SavingsAccount(String accNo, String name) {
        super(accNo, name);
    }

    @Override
    public void withdraw(float amount) {
        if (balance - amount < MIN_BALANCE) {
            JOptionPane.showMessageDialog(null, "Penalty of " + PENALTY + " applied for going below minimum balance.");
            balance -= PENALTY;
        }
        super.withdraw(amount);
    }
}

class CurrentAccount extends BankAccount {
    private final float OVERDRAFT_LIMIT = 1000;

    public CurrentAccount(String accNo, String name) {
        super(accNo, name);
    }

    @Override
    public void withdraw(float amount) {
        if (balance + OVERDRAFT_LIMIT >= amount) {
            super.withdraw(amount);
        } else {
            JOptionPane.showMessageDialog(null, "Withdrawal amount exceeds overdraft limit.");
        }
    }
}

public class Main extends JFrame {
    private Map<String, BankAccount> accounts;
    private JTextArea accountDetailsTextArea;

    public Main() {
        accounts = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banking System");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Welcome to the Banking System!");
        label.setBounds(100, 20, 350, 30);
        panel.add(label);

        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.setBounds(50, 70, 150, 30);
        createAccountBtn.addActionListener(e -> createAccount());
        panel.add(createAccountBtn);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(220, 70, 100, 30);
        depositBtn.addActionListener(e -> deposit());
        panel.add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(50, 120, 150, 30);
        withdrawBtn.addActionListener(e -> withdraw());
        panel.add(withdrawBtn);

        JButton transferBtn = new JButton("Transfer Funds");
        transferBtn.setBounds(220, 120, 150, 30);
        transferBtn.addActionListener(e -> transferFunds());
        panel.add(transferBtn);

        JButton displayAccountBtn = new JButton("Display Account Details");
        displayAccountBtn.setBounds(50, 170, 200, 30);
        displayAccountBtn.addActionListener(e -> displayAccountDetails());
        panel.add(displayAccountBtn);

        accountDetailsTextArea = new JTextArea();
        accountDetailsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(accountDetailsTextArea);
        scrollPane.setBounds(50, 220, 300, 200);
        panel.add(scrollPane);

        add(panel);
    }

    private void createAccount() {
        String accNo = JOptionPane.showInputDialog("Enter account number:");
        String name = JOptionPane.showInputDialog("Enter customer name:");
        String accTypeStr = JOptionPane.showInputDialog("Select account type (1 for Savings, 2 for Current):");
        try {
            int accType = Integer.parseInt(accTypeStr);
            if (accType == 1) {
                accounts.put(accNo, new SavingsAccount(accNo, name));
            } else if (accType == 2) {
                accounts.put(accNo, new CurrentAccount(accNo, name));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid account type.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input for account type.");
        }
    }

    private void deposit() {
        String accNo = JOptionPane.showInputDialog("Enter account number:");
        if (accounts.containsKey(accNo)) {
            String amountStr = JOptionPane.showInputDialog("Enter deposit amount:");
            try {
                float amount = Float.parseFloat(amountStr);
                accounts.get(accNo).deposit(amount);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for deposit amount.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    private void withdraw() {
        String accNo = JOptionPane.showInputDialog("Enter account number:");
        if (accounts.containsKey(accNo)) {
            String amountStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
            try {
                float amount = Float.parseFloat(amountStr);
                accounts.get(accNo).withdraw(amount);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for withdrawal amount.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    private void transferFunds() {
        String senderAccNo = JOptionPane.showInputDialog("Enter sender account number:");
        if (accounts.containsKey(senderAccNo)) {
            String recipientAccNo = JOptionPane.showInputDialog("Enter recipient account number:");
            if (accounts.containsKey(recipientAccNo)) {
                String amountStr = JOptionPane.showInputDialog("Enter transfer amount:");
                try {
                    float amount = Float.parseFloat(amountStr);
                    accounts.get(senderAccNo).transferFunds(accounts.get(recipientAccNo), amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input for transfer amount.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Recipient account not found.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sender account not found.");
        }
    }

    private void displayAccountDetails() {
        String accNo = JOptionPane.showInputDialog("Enter account number:");
        if (accounts.containsKey(accNo)) {
            BankAccount account = accounts.get(accNo);
            String details = "Account Number: " + account.getAccNo() + "\n"
                    + "Username: " + account.getName() + "\n"
                    + "Balance: " + account.getBalance();
            accountDetailsTextArea.setText(details);
        } else {
            JOptionPane.showMessageDialog(null,"Account not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}

