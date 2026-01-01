
 import java.util.Random;
import java.util.Scanner;

public class Number_Guess 
{
   
    public static void main(String[] args) 
    {

        Random random = new Random();
    
        Scanner scanner = new Scanner(System.in);

        int guess = 0;
        int tries = 0;
        int min = 1;
        int max = 100;

        int answer = random.nextInt(max - min + 1) + min;

        System.out.println("----- NUMBER GUESSING GAME -----");

        do 
        {
            System.out.print("Guess a number between " + min + " - " + max + ": ");
            guess = scanner.nextInt();
            tries++;

            if (guess < answer) 
                {
                System.out.println("TOO LOW");
                }

            else if (guess > answer) 
                {
                System.out.println("TOO HIGH");
                } 

            else {
                System.out.println("CORRECT");
                 }

        }
         while (guess != answer);

        System.out.println("The Answer is " + answer);
        System.out.println("It Took You " + tries + " Tries");

        scanner.close();
    }
}

    

