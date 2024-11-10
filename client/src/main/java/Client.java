import java.util.Scanner;

public class Client {
    private String authToken;

    public void preLoginMenu() {
        String[] options = {"1 - Login", "2 - Register", "3 - Help", "4 - Quit"};
        for (String opt : options) {
            System.out.println(opt);
        }
        System.out.print("Enter a menu option: ");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.next();
    switch (input) {
        case ("1") -> login();
        case ("2") -> register();
        case ("3") -> help();
        case ("4") -> quit();
        case null, default -> {
            System.out.println("Please enter a valid menu option.");
            preLoginMenu();
        }
    }
    }



    private void login() {
        System.out.print("called login function");
        preLoginMenu();
    }

    private void register() {
        System.out.print("called register function");
        preLoginMenu();
    }

    private void quit() {
        System.exit(0);
    }

    private void help() {
        System.out.print("called help function");
        preLoginMenu();
    }
}