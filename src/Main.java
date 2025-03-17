import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("What would you like to encrypt?");

        // Use Scanner to read input from the console
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter input: ");
        String input = scanner.nextLine(); // Reads a whole line from the console

        System.out.println(Encrypt.encrypt(input)); // Prints the user input
    }
}