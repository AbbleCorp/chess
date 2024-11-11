package ui;

import chess.ChessBoard;
import model.*;
import network.ServerFacade;

import java.util.Map;
import java.util.Scanner;

public class Client {
    private String authToken;
    private ServerFacade serverFacade;
    private Map<Integer, GameData> gameList;

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
        case ("3") -> helpPreLogin();
        case ("4") -> quit();
        case null, default -> {
            System.out.println("Please enter a valid menu option.");
            preLoginMenu();
        }
    }
    }




    private void login() {
        System.out.print("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        try {
        LoginResult result = serverFacade.login(new LoginRequest(username,password));
        //login attempt may fail, if so return error message and call prelogin menu again
        if (result != null) {
            authToken = result.authToken();
            postLoginMenu(); } }
        catch (Exception e){
            System.out.println("Login failed"); }
        preLoginMenu(); }


    private void register() {
        System.out.print("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        try {
            LoginResult result = serverFacade.register(new RegisterRequest(username, password, email));
            if (result != null) {
                authToken = result.authToken();
                postLoginMenu();
            }
        }
        catch (Exception e) {
            System.out.println("Registration failed");
        }
        preLoginMenu();
    }

    private void quit() {
        System.exit(0);
    }

    private void helpPreLogin() {
        String[] info = {"1 - Login: Login an existing user and play chess.",
                "2 - Register: Register a new user.",
                "3 - Help:  See this message again.",
                "4 - Quit: Exit the program."};
        for (String i : info) {
            System.out.println(i);
        }
        preLoginMenu();
    }

    private void postLoginMenu() {
        String[] options = {"1 - Create Game", "2 - Play Game", "3 - Observe Game",
                "4 - List Games", "5 - Logout", "6 - Help"};
        for (String opt : options) {
            System.out.println(opt);
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        switch (input) {
            case ("1") -> createGame();
            case ("2") -> playGame();
            case ("3") -> observeGame();
            case ("4") -> listGames();
            case ("5") -> logout();
            case ("6") -> helpPostLogin();
            case null, default -> {
                System.out.println("Please enter a valid menu option.");
                postLoginMenu();
            }
        }}

    private void createGame() {
        System.out.print("Enter name for new game: ");
        Scanner scanner = new Scanner(System.in);
        String gameName = scanner.next();
        serverFacade.createGame(new CreateGameRequest(authToken,gameName));
        System.out.println("Game created successfully.");
        postLoginMenu();
    }


    private void playGame() {
        ChessBoard game = new ChessBoard();
        game.resetBoard();
        Board board = new Board(game.getBoard());
        board.drawBoard("WHITE");
        board.drawBoard("BLACK");
        postLoginMenu();
    }


    private void observeGame() {
        ChessBoard game = new ChessBoard();
        game.resetBoard();
        Board board = new Board(game.getBoard());
        board.drawBoard("WHITE");
        board.drawBoard("BLACK");
        postLoginMenu();

    }

    private void listGames() {
        ListGamesResult result =  serverFacade.listGames(new ListGamesRequest(authToken));
        gameList.clear();
        int i = 1;
        for (GameData game : result.games()) {
            gameList.put(i, game);
            i++;
        }
        System.out.println("Games: ");
        for (int key : gameList.keySet()) {
            System.out.println(key + " : " + gameList.get(key).toString());
        }
    }


    private void logout() {
        serverFacade.logout(new LogoutRequest(authToken));
        authToken = null;
        preLoginMenu();
    }


    private void helpPostLogin() {
        String[] info = {"1 - Create Game: Create a new chess game.", "2 - Play Game: Join an existing chess game.",
                "3 - Observe Game: Observe an ongoing chess game.",
                "4 - List Games: View all existing chess games.", "5 - Logout: Logout and return to main menu",
                "6 - Help: See this message again."};
        for (String i : info) {
            System.out.println(i);
        }
        postLoginMenu();
    }
}