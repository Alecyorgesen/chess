package client;

import chess.ChessBoard;
import ui.ChessBoardPrinter;

import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade();
    ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter();
    ChessBoard chessBoard = new ChessBoard();

    public void run() {
        beforeLoginLoop();
    }

    private void beforeLoginLoop() {
        while (true) {
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
                    quit();
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

    }
    private void help() {
        System.out.println("The actions that you can take are:");
        System.out.println("Help: you just did this. But for future reference, you can press 1 or 'help' to make it work.");
        System.out.println("Quit: 'quit' or 2 shuts turns off the application. (Please don't go yet!!!)");
        System.out.println("Login: type 'login' or 3 if you would like to, well, login! You'll need your username and password.");
        System.out.println("Register: type 'register' or 4 to create a new user. I'll be waiting for your awesomeness to join my game!");
        System.out.println();
    }
    private void quit() {
        System.out.println("quit");
    }
    private void login() {
        System.out.println("login");
        String line = scanner.nextLine();

    }
    private void register() {
        System.out.println("Please enter a new username:");
        String username = scanner.nextLine();
        System.out.println("Now enter your password:");
        String password = scanner.nextLine();
        System.out.println("Pass in your email:");
        String email = scanner.nextLine();

    }
}
