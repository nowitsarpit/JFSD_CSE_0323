#include <iostream>
#include <fstream>
#include <vector>
#include <iomanip>
using namespace std;

// -------------------- Class: BankAccount --------------------
class BankAccount {
protected:
    int accountNumber;
    string name;
    string type;
    double balance;

public:
    BankAccount() {}
    BankAccount(int accNo, string n, string t, double bal) {
        accountNumber = accNo;
        name = n;
        type = t;
        balance = bal;
    }

    // Create a new account
    void createAccount() {
        cout << "\nEnter Account Number: ";
        cin >> accountNumber;
        cin.ignore();
        cout << "Enter Account Holder Name: ";
        getline(cin, name);
        cout << "Enter Account Type (Saving/Current): ";
        cin >> type;
        cout << "Enter Initial Deposit: ";
        cin >> balance;
        cout << "\n✅ Account Created Successfully!\n";
    }

    // Display account details
    void showAccount() const {
        cout << "\n-------------------------------------";
        cout << "\nAccount Number: " << accountNumber;
        cout << "\nAccount Holder: " << name;
        cout << "\nAccount Type: " << type;
        cout << "\nBalance: " << balance;
        cout << "\n-------------------------------------\n";
    }

    // Deposit money
    void deposit(double amount) {
        balance += amount;
        cout << "Amount Deposited Successfully!\n";
    }

    // Withdraw money
    void withdraw(double amount) {
        if (amount > balance) {
            cout << "nsufficient Balance!\n";
        } else {
            balance -= amount;
            cout << "ithdrawal Successful!\n";
        }
    }

    // Get balance
    void checkBalance() const {
        cout << "Current Balance: " << balance << "\n";
    }

    // Update account details
    void updateAccount() {
        cout << "Enter new Account Holder Name: ";
        cin.ignore();
        getline(cin, name);
        cout << "Enter new Account Type: ";
        cin >> type;
        cout << "ccount Updated Successfully!\n";
    }

    int getAccountNumber() const { return accountNumber; }
};

// -------------------- Class: BankSystem --------------------
class BankSystem {
private:
    vector<BankAccount> accounts;

public:
    // Create new account
    void newAccount() {
        BankAccount acc;
        acc.createAccount();
        accounts.push_back(acc);
    }

    // Display all accounts
    void displayAllAccounts() {
        if (accounts.empty()) {
            cout << "\nNo Accounts Found!\n";
            return;
        }
        cout << "\n All Accounts:\n";
        for (auto &acc : accounts) {
            acc.showAccount();
        }
    }

    // Search for an account by number
    BankAccount* findAccount(int accNo) {
        for (auto &acc : accounts) {
            if (acc.getAccountNumber() == accNo)
                return &acc;
        }
        return nullptr;
    }

    // Deposit money into account
    void depositMoney() {
        int accNo;
        double amount;
        cout << "Enter Account Number: ";
        cin >> accNo;
        BankAccount* acc = findAccount(accNo);
        if (acc) {
            cout << "Enter Amount to Deposit: ";
            cin >> amount;
            acc->deposit(amount);
        } else {
            cout << "Account Not Found!\n";
        }
    }

    // Withdraw money from account
    void withdrawMoney() {
        int accNo;
        double amount;
        cout << "Enter Account Number: ";
        cin >> accNo;
        BankAccount* acc = findAccount(accNo);
        if (acc) {
            cout << "Enter Amount to Withdraw: ";
            cin >> amount;
            acc->withdraw(amount);
        } else {
            cout << "Account Not Found!\n";
        }
    }

    // Check account balance
    void checkBalance() {
        int accNo;
        cout << "Enter Account Number: ";
        cin >> accNo;
        BankAccount* acc = findAccount(accNo);
        if (acc) {
            acc->checkBalance();
        } else {
            cout << "Account Not Found!\n";
        }
    }

    // Close account
    void closeAccount() {
        int accNo;
        cout << "Enter Account Number to Close: ";
        cin >> accNo;
        for (auto it = accounts.begin(); it != accounts.end(); ++it) {
            if (it->getAccountNumber() == accNo) {
                accounts.erase(it);
                cout << "Account Closed Successfully!\n";
                return;
            }
        }
        cout << "Account Not Found!\n";
    }

    // Update account info
    void updateAccountInfo() {
        int accNo;
        cout << "Enter Account Number: ";
        cin >> accNo;
        BankAccount* acc = findAccount(accNo);
        if (acc) {
            acc->updateAccount();
        } else {
            cout << "Account Not Found!\n";
        }
    }
};

// -------------------- Main Function --------------------
int main() {
    BankSystem bank;
    int choice;

    do {
        cout << "\n==========  BANK MANAGEMENT SYSTEM  ==========\n";
        cout << "1. Create New Account\n";
        cout << "2. Display All Accounts\n";
        cout << "3. Deposit Money\n";
        cout << "4. Withdraw Money\n";
        cout << "5. Check Balance\n";
        cout << "6. Close Account\n";
        cout << "7. Update Account\n";
        cout << "8. Exit\n";
        cout << "==============================================\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1: bank.newAccount(); break;
            case 2: bank.displayAllAccounts(); break;
            case 3: bank.depositMoney(); break;
            case 4: bank.withdrawMoney(); break;
            case 5: bank.checkBalance(); break;
            case 6: bank.closeAccount(); break;
            case 7: bank.updateAccountInfo(); break;
            case 8: cout << " Thank you for using the Bank System!\n"; break;
            default: cout << "Invalid Choice! Try Again.\n";
        }
    } while (choice != 8);

    return 0;
}