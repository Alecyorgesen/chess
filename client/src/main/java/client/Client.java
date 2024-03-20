package client;

import java.util.Scanner;

public class Client {

    public void run() {
        beforeLoginLoop();
    }

    private void beforeLoginLoop() {
        Scanner scanner = new Scanner(System.in);
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
                    help();
                    continue;
                case "2":
                    quit();
                    continue;
                case "3":
                    login();
                    continue;
                case "4":
                    register();
                    continue;
            }
        }
    }
    private void help() {
        System.out.println("SAY BOOO (help)");
    }
    private void quit() {
        System.out.println("quit");
    }
    private void login() {
        System.out.println("login");
    }
    private void register() {
        System.out.println("register!");
    }
}
