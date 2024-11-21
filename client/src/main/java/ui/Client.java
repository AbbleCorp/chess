package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import model.*;
import network.ResponseException;
import network.ServerFacade;
import websocket.messages.*;
import websocket.messages.ErrorMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client implements ServerMessageObserver {
    private String authToken;
    private final ServerFacade serverFacade;
    private final Map<Integer, GameData> gameList = new HashMap<>();
    private String playerColor;
    private GameData currentGame;

    public Client() throws ResponseException {
        serverFacade = new ServerFacade(this);
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
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (username.contains(" ") || password.contains(" ")) {
            System.out.println("Please enter a valid username and password without spaces.");
            preLoginMenu();
        }
        try {
            LoginResult result = serverFacade.login(new LoginRequest(username, password));
            //login attempt may fail, if so return error message and call prelogin menu again
            if (result != null) {
                authToken = result.authToken();
                System.out.println("Login successful.");
                postLoginMenu();
            }
        } catch (Exception e) {
            String message = "";
            if (e.getMessage().equals("failure: 401")) {
                message = "unauthorized";
            }
            System.out.println("Login failed: " + message);
        }
        preLoginMenu();
    }


    private void register() {
        System.out.print("Username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        if (username.contains(" ") || password.contains(" ") || email.contains(" ")) {
            System.out.println("Please enter a valid username, password, and email without spaces.");
            preLoginMenu();
        }
        try {
            LoginResult result = serverFacade.register(new RegisterRequest(username, password, email));
            if (result != null) {
                authToken = result.authToken();
                System.out.println("Registration successful.");
                postLoginMenu();
            }
        } catch (Exception e) {
            String message = "";
            if (e.getMessage().equals("failure: 403")) {
                message = "Username already taken.";
            } else if (e.getMessage().equals("failure: 400")) {
                message = "Please enter a valid username, password, and email.";
            }
            System.out.println("Registration failed: " + message);
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
        System.out.println();
        preLoginMenu();
    }

    private void postLoginMenu() {
        String[] options = {"1 - Create Game", "2 - Play Game", "3 - Observe Game",
                "4 - List Games", "5 - Logout", "6 - Help"};
        for (String opt : options) {
            System.out.println(opt);
        }
        System.out.print("Enter a menu option: ");
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
        }
    }

    private void createGame() {
        System.out.print("Enter name for new game: ");
        Scanner scanner = new Scanner(System.in);
        String gameName = scanner.next();
        try {
            serverFacade.createGame(new CreateGameRequest(authToken, gameName));
            System.out.println("Game created successfully.");
            postLoginMenu();
        } catch (Exception e) {
            if (e.getMessage().equals("failure: 401")) {
                System.out.print("Game creation failed: unauthorized.");
                postLoginMenu();
            }
        }
    }

    private void printBoards() {
        ChessBoard game = new ChessBoard();
        game.resetBoard();
        Board board = new Board(game.getBoard());
        board.drawBoard("WHITE");
        board.drawBoard("BLACK");
        postLoginMenu();
    }

    private void validGame(Integer gameId) {
        if (gameId == null) {
            System.out.println("Please enter a valid game number");
        }
        if (gameList.isEmpty()) {
            System.out.println("Game list is empty. Please create a game first.");
            postLoginMenu();
        } else if (!gameList.containsKey(gameId)) {
            System.out.println("Invalid game number. Please enter the number of an existing game.");
            postLoginMenu();
        }
    }

    private boolean isInt(String id) {
        if (id == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(id);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void playGame() {
        System.out.print("Enter the number of the game you would like to join: ");
        Scanner scanner = new Scanner(System.in);
        String gameIdString = scanner.next();
        Integer gameId = null;
        if (isInt(gameIdString)) {
            gameId = Integer.parseInt(gameIdString);
        } else {
            System.out.println("Please enter a valid game number.");
            postLoginMenu();
        }
        validGame(gameId);
        System.out.println("1 - White");
        System.out.println("2 - Black");
        System.out.print("Enter the number of the color you would like to play as: ");
        String colorNum = scanner.next();
        String playerColor = null;
        if (colorNum.equals("1")) {
            playerColor = "WHITE";
        } else if (colorNum.equals("2")) {
            playerColor = "BLACK";
        } else {
            System.out.println("Please enter a valid number corresponding to either White or Black.");
            postLoginMenu();
        }
        try {
            serverFacade.joinGame(new JoinGameRequest(authToken, playerColor, gameId));
            currentGame = gameList.get(gameId);
            System.out.println("You have joined " + gameList.get(gameId).gameName() + " as " + playerColor + ".");
            this.playerColor = playerColor;
            gamePlayMenu();
        } catch (Exception e) {
            if (e.getMessage().contains("401")) {
                System.out.println("Failed to join game: unauthorized.");
            } else if (e.getMessage().contains("403")) {
                System.out.println("That color is already taken, please choose a different color or game.");
            } else if (e.getMessage().contains("400")) {
                System.out.println("Something went wrong, please try again with valid inputs.");
            }
            postLoginMenu();
        }
        printBoards();
    }


    private void observeGame() {
        System.out.print("Enter the number of the game you would like to observe: ");
        Scanner scanner = new Scanner(System.in);
        Integer gameId = Integer.parseInt(scanner.next());
        validGame(gameId);
        System.out.println("You are now observing " + gameList.get(gameId).gameName() + ".");
        printBoards();
    }

    private void listGames() {
        try {
            ListGamesResult result = serverFacade.listGames(new ListGamesRequest(authToken));
            gameList.clear();
            int i = 1;
            for (GameData game : result.games()) {
                gameList.put(i, game);
                i++;
            }
            System.out.println("Games: ");
            if (gameList.isEmpty()) {
                System.out.println("No games found.");
                postLoginMenu();
            }
            for (int key : gameList.keySet()) {
                printGameInfo(key);
            }
            postLoginMenu();
        } catch (Exception e) {
            if (e.getMessage().equals("failure: 401")) {
                System.out.print("Failed to list games: unauthorized.");
                postLoginMenu();
            }
        }
    }

    private void printGameInfo(int key) {
        String white = "No player";
        String black = "No player";
        if (gameList.get(key).whiteUsername() != null) {
            white = gameList.get(key).whiteUsername();
        }
        if (gameList.get(key).blackUsername() != null) {
            black = gameList.get(key).blackUsername();
        }
        System.out.println(key + " : " + gameList.get(key).gameName());
        System.out.println("White Player: " + white);
        System.out.println("Black Player: " + black);
    }


    private void logout() {
        try {
            serverFacade.logout(new LogoutRequest(authToken));
            authToken = null;
            System.out.println("Logout successful.");
            preLoginMenu();
        } catch (Exception e) {
            if (e.getMessage().equals("failure: 401")) {
                System.out.print("Logout failed: unauthorized.");
                postLoginMenu();
            }
        }
    }


    private void helpPostLogin() {
        String[] info = {"1 - Create Game: Create a new chess game.", "2 - Play Game: Join an existing chess game.",
                "3 - Observe Game: Observe an ongoing chess game.",
                "4 - List Games: View all existing chess games.", "5 - Logout: Logout and return to main menu",
                "6 - Help: See this message again."};
        for (String i : info) {
            System.out.println(i);
        }
        System.out.println();
        postLoginMenu();
    }

    private void gamePlayMenu() {
        String[] options = {"1 - Redraw Chessboard", "2 - Make Move", "3 - Highlight Legal Moves",
                "4 - Leave", "5 - Resign", "6 - Help"};
        for (String opt : options) {
            System.out.println(opt);
        }
        System.out.print("Enter a menu option: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        switch (input) {
            case ("1") -> redrawChessboard();
            case ("2") -> makeMove();
            case ("3") -> highlightLegalMoves();
            case ("4") -> leaveGame();
            case ("5") -> resign();
            case ("6") -> helpGamePlay();
            case null, default -> {
                System.out.println("Please enter a valid menu option.");
                postLoginMenu();
            }
        }
    }

    private void redrawChessboard() {
        Board board = new Board(currentGame.game().getBoard().getBoard());
        board.drawBoard(playerColor);
    }

    private void makeMove() {
        //TODO: implement
    }

    private ChessPosition parsePosition(String coord) {
        String alphaChar = coord.substring(0, 1);
        int intChar = Integer.parseInt(coord.substring(1));
        if (!"abcdefgh".contains(alphaChar) || (intChar < 0) || (intChar > 8)) {
            System.out.println("Please enter valid coordinates.");
            gamePlayMenu();
        }
        int x = 0;
        switch (alphaChar) {
            case "a" -> x = 1;
            case "b" -> x = 2;
            case "c" -> x = 3;
            case "d" -> x = 4;
            case "e" -> x = 5;
            case "f" -> x = 6;
            case "g" -> x = 7;
            case "h" -> x = 8;
        }
        return new ChessPosition(x, intChar);
    }

    private void highlightLegalMoves() {
        System.out.print("Enter the coordinates of the piece you would like to see legal moves for: ");
        Scanner scanner = new Scanner(System.in);
        String coord = scanner.next();
        ChessPosition pos = parsePosition(coord);
        //TODO: implement
    }

    private void leaveGame() {
        //TODO: implement
    }

    private void resign() {
        //TODO: implement
    }

    private void helpGamePlay() {
        String[] info = {"1 - Redraw Chessboard: Redraw the chessboard to view current state of game.", "2 - Make " +
                "Move: Enter coordinates of piece to move, followed by ending position of piece.", "3 - Highlight Legal Moves: See all legal moves " +
                "for the selected piece.", "4 - Leave: Leave the game to come back to later.", "5 - Resign: Forfeit " +
                "the game.", "6 - Help: See this message again."};
        for (String i : info) {
            System.out.println(i);
        }
        System.out.println();
        gamePlayMenu();
    }

    private void displayNotification(String message) {
        //TODO: implement
    }

    private void displayError(String message) {
        //TODO: implement
    }

    private void loadGame(GameData game) {
        //TODO: implement - what does this do?
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }

    }


}