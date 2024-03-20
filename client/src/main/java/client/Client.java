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
            System.out.println("1. ");
            System.out.println("2. ");
            System.out.println("3. ");
            System.out.println("4. ");
            String line = scanner.nextLine();
            switch (line) {
                case "1":

            }
        }
    }
    public void help() {

    }
    public void quit() {

    }
    public void login() {

    }
    public void register() {
        
    }
}
