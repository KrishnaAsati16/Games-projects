import java.util.Scanner;

public class BankProgram {
     static Scanner scanner = new Scanner(System.in);

   public static void main(String[] args) {
       // Scanner scanner = new Scanner(System.in);
        double balance = 0;
        boolean isRunning = true;
        int choice;

        // Display menu to the user

        while (isRunning) {
            System.out.println("***************");
            System.out.println("BANKING PROGRAM");
            System.out.println("***************");
            System.out.println("1.Show Balance:");
            System.out.println("2.Deposit:");
            System.out.println("3.Withdraw:");
            System.out.println("4.Exit:");

            // Get In Process In User Choice

            System.out.println("Enter your choice(1-4): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> showBalance(balance);
                case 2 -> balance += deposit();
                case 3 -> balance = balance-withdraw(balance);
                case 4 -> isRunning = false;
                default -> System.out.println("Invalid choice.");
            }
        }

       System.out.println("********************");
       System.out.println("Thanking You ! Having a nice day");
       System.out.println("********************");

        scanner.close();

    }
    // Show balance

    static void showBalance(double balance){
        System.out.println("***************");
        System.out.printf("Balance: $%.2f%n", balance);
    }

    // Deposit

    static double deposit(){
       double amount;

        System.out.println("Enter An Amount To Be Deposited:");
//        Scanner scanner = new Scanner(System.in);
        amount = scanner.nextDouble();

        if(amount <0)
        {
            System.out.println("Amount can be negative");
            return 0;
        }
        else
        {
         return amount;
        }

    }

    // withdraw

    static  double withdraw(double balance){
       double amount;

        System.out.print("Enter Amount To Be Drawn:");
        amount = scanner.nextDouble();

        if(amount > balance)
        {
            System.out.println("Insufficient funds");
            return 0;
        }
        else if(amount<0) {
            System.out.println("Amount can't be negative");
            return 0;
        }
        else {
            return amount;
        }

    }
    
}
