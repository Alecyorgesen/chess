package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import response.ListGamesResponse;
import ui.ChessBoardPrinter;

import java.util.List;
import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade();
    AuthData authData = null;
    ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter();
    ChessBoard chessBoard = new ChessBoard();

    public void run() {
        beforeLoginLoop();
    }

    private void beforeLoginLoop() {
        Boolean shouldLoop = true;
        while (shouldLoop) {
            System.out.println("Welcome to Chess!!! Please select an option by choosing the appropriate number:");
            System.out.println(" ");
            System.out.println("1. Help.");
            System.out.println("2. Quit.");
            System.out.println("3. Login existing user.");
            System.out.println("4. Register a new user.");
            String line = scanner.nextLine();
            switch (line) {
                case "1":
                case "help":
                    help();
                    continue;
                case "2":
                case "quit":
                    System.out.println("Bye! Have a good one! Come back soon!");
                    shouldLoop = false;
                    continue;
                case "3":
                case "login":
                    login();
                    continue;
                case "4":
                case "register":
                    register();
                    continue;
                default:
                    System.out.println("Enter 'help', 'quit', 'login', or 'register'.");
                    System.out.println("Or enter the adjacent number.");
                    System.out.println();
            }
        }
    }
    private void menuScreenLoop() {
        Boolean shouldLoop = true;
        while (shouldLoop) {
            System.out.println("Welcome to Chess!!! Please select an option by choosing the appropriate number:");
            System.out.println();
            System.out.println("1. Help.");
            System.out.println("2. Logout.");
            System.out.println("3. Create Game.");
            System.out.println("4. List Games.");
            System.out.println("5. Join Game.");
            System.out.println("6. Join Observer.");
            String line = scanner.nextLine();
            switch (line) {
                case "1":
                case "help":
                    helpMenu();
                    continue;
                case "2":
                case "logout":
                    logout();
                    shouldLoop = false;
                    continue;
                case "3":
                case "create game":
                    createGame();
                    continue;
                case "4":
                case "list games":
                    listGames();
                    continue;
                case "5":
                case "join game":
                    joinGame();
                    continue;
                case "6":
                case "join observer":
                    joinObserver();
                default:
                    System.out.println("Enter 'help', 'logout', 'create game', 'list games', 'join game', or 'join observer'.");
                    System.out.println("Or enter the adjacent number.");
                    System.out.println();
            }
        }
    }
    private void help() {
        System.out.println("The actions that you can take are:");
        System.out.println("Help: you just did this. But for future reference, you can press 1 or 'help' to make it work.");
        System.out.println("Quit: 'quit' or 2 shuts turns off the application. (Please don't go yet!!!)");
        System.out.println("Login: type 'login' or 3 if you would like to, well, login! You'll need your username and password.");
        System.out.println("Register: type 'register' or 4 to create a new user. I'll be waiting for your awesomeness to join my game!");
        System.out.println();
    }
    private void login() {
        System.out.println("Please enter your username");
        String username = scanner.nextLine();
        System.out.println("What's your password?");
        String password = scanner.nextLine();
        authData = serverFacade.login(username, password);
        if (authData != null) {
            menuScreenLoop();
            return;
        }
        System.out.println("User or password incorrect.");
    }
    private void register() {
        System.out.println("Please enter a new username:");
        String username = scanner.nextLine();
        System.out.println("Now enter your password:");
        String password = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        authData = serverFacade.register(username,password,email);
        menuScreenLoop();
    }
    private void helpMenu() {
        System.out.println("The actions that you can take are:");
        System.out.println("Help: you just did this. But for future reference, you can press 1 or 'help' to make it work.");
        System.out.println("Logout: 'logout' or 2 signs you out, and you'll have to log back in again to play.");
        System.out.println("Create Game: type 'create game' or 3 to create a new game! You'll have to input a name for the game.");
        System.out.println("List Games: type 'list games' or 4 to get a list of the games.");
        System.out.println("Join Game: type 'join game' or 5 to get a list of the games that you can join.");
        System.out.println("Join Observer: type 'join observer' or 6 to join a game just to watch and not to play.");
        System.out.println();
    }
    private void logout() {
        System.out.println("Bye! Join again sometime!");
        System.out.println();
        serverFacade.logout(authData);
    }
    private void createGame() {
        System.out.println("Please enter the name of the game:");
        String gameName = scanner.nextLine();
        serverFacade.createGame(authData, gameName);
        System.out.println(gameName + " created! Use 'join game' to play!");
        System.out.println();
    }
    private void listGames() {
        ListGamesResponse listGamesResponse = serverFacade.listGames(authData);
        if (listGamesResponse != null) {
            System.out.println("List of games:");
            List<GameData> listOfChessGames = listGamesResponse.games();
            for (int i = 0; i < listOfChessGames.size(); i++) {
                GameData gameData = listOfChessGames.get(i);
                System.out.println(i+1 + ": " + gameData.gameName() + ", White: " + gameData.whiteUsername() + ", Black: " + gameData.blackUsername());
            }
        }
        System.out.println();
    }
    private void joinGame() {

    }
    private void joinObserver() {

    }
}
