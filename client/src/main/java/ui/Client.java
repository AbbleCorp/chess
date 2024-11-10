package ui;

import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import network.ServerFacade;

import java.util.Scanner;

public class Client {
    private String authToken;
    private ServerFacade serverFacade;

    public Client() {
        serverFacade = new ServerFacade();
    }

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

    private void postLoginMenu() {

    }


    private void login() {
        System.out.print("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        LoginResult result = serverFacade.login(new LoginRequest(username,password));
        //login attempt may fail, if so return error message and call prelogin menu again
        if (result != null) {
            authToken = result.authToken();
            postLoginMenu(); }
        else {
            System.out.println("Login failed");
        preLoginMenu(); }
    }

    private void register() {
        System.out.print("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        LoginResult result = serverFacade.register(new RegisterRequest(username,password,email));
        if (result != null) {
            authToken = result.authToken();
            postLoginMenu(); }
        else {
            System.out.print("Registration failed");
            preLoginMenu(); }
    }

    private void quit() {
        System.exit(0);
    }

    private void help() {
        System.out.print("called help function");
        preLoginMenu();
    }
}