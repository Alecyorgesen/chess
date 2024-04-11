package client;

import chess.*;
import model.AuthData;
import model.GameData;
import response.ListGamesResponse;
import ui.ChessBoardPrinter;

import java.util.*;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade(8080);
    AuthData authData = null;
    Map<Integer,GameData> mapOfGames = new HashMap<>();
    int gameNumber = 1;
    ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter();
    public static GameData currentGameData = null;
    public static String currentUser = "";
    public static boolean isObserving = false;

    public void run() {
        beforeLoginLoop();
    }

    private void beforeLoginLoop() {
        boolean shouldLoop = true;
        System.out.println("Welcome to Chess!!! Please select an option by choosing the appropriate number:");
        while (shouldLoop) {
            System.out.println();
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
        boolean shouldLoop = true;
        System.out.println("Welcome to Chess!!! Please select an option by choosing the appropriate number:");
        while (shouldLoop) {
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
                    continue;
                default:
                    System.out.println("Enter 'help', 'logout', 'create game', 'list games', 'join game', or 'join observer'.");
                    System.out.println("Or enter the adjacent number.");
            }
        }
    }
    private void help() {
        System.out.println("The actions that you can take are:");
        System.out.println("Help: you just did this. But for future reference, you can press 1 or 'help' to make it work.");
        System.out.println("Quit: 'quit' or 2 shuts turns off the application. (Please don't go yet!!!)");
        System.out.println("Login: type 'login' or 3 if you would like to, well, login! You'll need your username and password.");
        System.out.println("Register: type 'register' or 4 to create a new user. I'll be waiting for your awesomeness to join my game!");
    }
    private void login() {
        System.out.println("Please enter your username");
        String username = scanner.nextLine();
        System.out.println("What's your password?");
        String password = scanner.nextLine();
        authData = serverFacade.login(username, password);
        if (authData != null) {
            setCurrentUser(username);
            menuScreenLoop();
            return;
        }
        System.out.println("User or password incorrect.");
    }
    private void register() {
        System.out.println("Please enter a new username:");
        String username = scanner.nextLine();
        if (username.isEmpty()) {
            System.out.println("Username can't be empty!");
            return;
        }
        System.out.println("Now enter your password:");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("Password can't be empty!");
            return;
        }
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        if (email.isEmpty()) {
            System.out.println("Email can't be empty!");
            return;
        }
        authData = serverFacade.register(username,password,email);
        setCurrentUser(username);
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
    }
    private void listGames() {
        ListGamesResponse listGamesResponse = serverFacade.listGames(authData);
        if (listGamesResponse != null) {
            System.out.println("List of games:");
            List<GameData> listOfChessGames = listGamesResponse.games();
            displayGames(listOfChessGames);
        }
        System.out.println();
    }
    private void displayGames(List<GameData> listOfChessGames) {
        for (var gameData : listOfChessGames) {
            for (int key : mapOfGames.keySet()) {
                if (mapOfGames.get(key).gameID() == gameData.gameID()) {
                    mapOfGames.put(key, gameData);
                }
            }
            if (!mapOfGames.containsValue(gameData)) {
                mapOfGames.put(gameNumber, gameData);
                gameNumber++;
            }
        }
        for (int key : mapOfGames.keySet()) {
            GameData gameData = mapOfGames.get(key);
            System.out.println(key + ": " + gameData.gameName() + ", White: " + gameData.whiteUsername() + ", Black: " + gameData.blackUsername());
        }
    }
    private void joinGame() {
        listGames();
        System.out.println("Please select the number of the game you would like to join.");
        int number = 0;
        try {
            number = Integer.parseInt(scanner.nextLine());
        } catch (Exception ex) {
            System.out.println("Invalid input.");
            return;
        }
        GameData gameData = mapOfGames.get(number);
        if (gameData == null) {
            System.out.println("Invalid input.");
            return;
        }
        int gameID = gameData.gameID();
        System.out.println("Please select the number or color of which team you'd like to join.");
        System.out.println("1. White");
        System.out.println("2. Black");
        String teamColor = scanner.nextLine();
        teamColor = switch (teamColor) {
            case "1", "white" -> "WHITE";
            case "2", "black" -> "BLACK";
            default -> teamColor;
        };
        if (teamColor.equals("WHITE") || teamColor.equals("BLACK")) {
            if (teamColor.equals("WHITE") && gameData.whiteUsername() != null) {
                System.out.println("Already taken!");
                return;
            }
            if (teamColor.equals("BLACK") && gameData.blackUsername() != null) {
                System.out.println("Already taken!");
                return;
            }
            serverFacade.joinGame(authData, gameID, teamColor);
            if (teamColor.equals("WHITE")) {
                insideGameLoop(gameID, ChessGame.TeamColor.WHITE);
            } else {
                insideGameLoop(gameID, ChessGame.TeamColor.BLACK);
            }
            return;
        }
        System.out.println("Invalid input.");
    }
    private void joinObserver() {
        listGames();
        System.out.println("Please select the number of the game you would like to observe.");
        int number;
        try {
            number = Integer.parseInt(scanner.nextLine());
        } catch (Exception ex) {
            System.out.println("Invalid input.");
            return;
        }
        GameData gameData = mapOfGames.get(number);
        if (gameData == null) {
            System.out.println("Invalid input.");
            return;
        }
        int gameID = gameData.gameID();

        observeGameLoop(gameID, ChessGame.TeamColor.WHITE);
    }
// You'll need to add functionality for what you can do if you are observing.
    public void observeGameLoop(int gameID, ChessGame.TeamColor teamColor) {
        WSClient wsClient;
        try {
            wsClient = new WSClient();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        serverFacade.observeGame(wsClient, authData, gameID);
        System.out.println("1. Help");
        System.out.println("2. Redraw Chess Board");
        System.out.println("3. Leave");
        System.out.println("4. Highlight Legal Moves");
        boolean shouldLoop = true;
        while (shouldLoop) {
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                case "help":
                    helpDuringGame();
                    break;
                case "2":
                case "redraw":
                    serverFacade.redrawChessBoard(wsClient, authData, gameID, teamColor);
                    break;
                case "3":
                case "leave":
                    serverFacade.leave(wsClient, authData, gameID);
                    shouldLoop = false;
                    break;
                case "4":
                case "legal moves":
                    highlightLegalMoves(teamColor);
                    break;
                default:
                    System.out.println("Please enter the number of the option you would like to select.");
            }
        }

    }
    public void insideGameLoop(int gameID, ChessGame.TeamColor teamColor) {
        WSClient wsClient;
        try {
            wsClient = new WSClient();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        serverFacade.joinThroughWebSocket(wsClient,authData,gameID,teamColor);
        System.out.println("Please select one of the options below:");
        System.out.println("1. Help");
        System.out.println("2. Redraw Chess Board");
        System.out.println("3. Leave");
        System.out.println("4. Make Move");
        System.out.println("5. Resign");
        System.out.println("6. Highlight Legal Moves");
        boolean shouldLoop = true;
        while (shouldLoop) {
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                case "help":
                    helpDuringGame();
                    break;
                case "2":
                case "redraw":
                    serverFacade.redrawChessBoard(wsClient, authData, gameID, teamColor);
                    break;
                case "3":
                case "leave":
                    serverFacade.leave(wsClient, authData, gameID);
                    shouldLoop = false;
                    break;
                case "4":
                case "move":
                    makeMove(wsClient, authData);
                    break;
                case "5":
                case "resign":
                    serverFacade.resign(wsClient, authData, gameID);
                    break;
                case "6":
                case "legal moves":
                    highlightLegalMoves(teamColor);
                    break;
                default:
                    System.out.println("Please enter the number of the option you would like to select.");
            }
        }
    }
    private void helpDuringGame() {
        System.out.println("Help: I hope you know what help does by now.");
        System.out.println("Redraw Chess Board: This let's you see the current state of the game. 'redraw' or 2.");
        System.out.println("Leave: This lets you leave the game. You can resume whenever you like.");
        System.out.println("Make Move: Select this and enter the coordinates of the piece you would like to move and its destination. To select, type 'move'.");
        System.out.println("Resign: This ends the game. Your opponent automatically wins if you do this option");
        System.out.println("Highlight Legal Moves: This lets you see the moves that you are allowed to make. Type 'legal moves' or 6 to select.");
    }
    synchronized private void makeMove(WSClient wsClient, AuthData authData) {
        System.out.println("Please select the coordinates of the piece you would like to move:");
        String position1 = scanner.nextLine();
        System.out.println("Now enter the location you want to move the piece to:");
        String position2 = scanner.nextLine();
        int row1;
        int column1;
        try {
            row1 = Integer.parseInt(String.valueOf(position1.charAt(1)));
            column1 = switch (String.valueOf(position1.charAt(0))) {
                case "A", "a" -> 1;
                case "B", "b" -> 2;
                case "C", "c" -> 3;
                case "D", "d" -> 4;
                case "E", "e" -> 5;
                case "F", "f" -> 6;
                case "G", "g" -> 7;
                case "H", "h" -> 8;
                default -> 0;
            };
        } catch (Exception exception) {
            System.out.println("Invalid input.");
            return;
        }
        if (column1 == 0 || row1 < 1 || row1 > 8) {
            System.out.println("Invalid Position");
            return;
        }
        int row2;
        int column2;
        try {
            row2 = Integer.parseInt(String.valueOf(position2.charAt(1)));
            column2 = switch (String.valueOf(position2.charAt(0))) {
                case "A", "a" -> 1;
                case "B", "b" -> 2;
                case "C", "c" -> 3;
                case "D", "d" -> 4;
                case "E", "e" -> 5;
                case "F", "f" -> 6;
                case "G", "g" -> 7;
                case "H", "h" -> 8;
                default -> 0;
            };
        } catch (Exception exception) {
            System.out.println("Invalid input.");
            return;
        }
        if (column2 == 0 || row2 < 1 || row2 > 8) {
            System.out.println("Invalid Position");
            return;
        }
        ChessPiece.PieceType promotionPiece = null;
        ChessPosition startPosition = new ChessPosition(row1, column1);
        ChessPosition endPosition = new ChessPosition(row2, column2);
        ChessPiece chessPiece = currentGameData.game().getBoard().getPiece(startPosition);
        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row2 == 8) {
                    promotionPiece = selectPromotionPiece();
                }
            } else {
                if (row2 == 1) {
                    promotionPiece = selectPromotionPiece();
                }
            }
        }
        ChessMove chessMove = new ChessMove(startPosition, endPosition, promotionPiece);
        serverFacade.makeMove(wsClient, authData, currentGameData.gameID(), chessMove);
    }
    private ChessPiece.PieceType selectPromotionPiece() {
        System.out.println("Please select which piece you would like to promote to:");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");
        String input = scanner.nextLine();
        return switch (input) {
            case "1", "queen" -> ChessPiece.PieceType.QUEEN;
            case "2", "rook" -> ChessPiece.PieceType.ROOK;
            case "3", "bishop" -> ChessPiece.PieceType.BISHOP;
            case "4", "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> ChessPiece.PieceType.QUEEN;
        };
    }

    synchronized public void highlightLegalMoves(ChessGame.TeamColor teamColor) {
        System.out.println("Which piece would you like see the moves for?");
        String position1 = scanner.nextLine();
        int row;
        int column;
        try {
            row = Integer.parseInt(String.valueOf(position1.charAt(1)));
            column = switch (String.valueOf(position1.charAt(0))) {
                case "A", "a" -> 1;
                case "B", "b" -> 2;
                case "C", "c" -> 3;
                case "D", "d" -> 4;
                case "E", "e" -> 5;
                case "F", "f" -> 6;
                case "G", "g" -> 7;
                case "H", "h" -> 8;
                default -> 0;
            };
        } catch (Exception ex) {
            System.out.println("Invalid input.");
            return;
        }
        if (column == 0 || row < 1 || row > 8) {
            System.out.println("Invalid Position");
            return;
        }
        ChessPosition chessPosition = new ChessPosition(row, column);
        ChessGame chessGame = currentGameData.game();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            chessBoardPrinter.printBoardFromWhiteSideWithHighlight(currentGameData.game().getBoard(), chessGame, chessPosition);
        } else {
            chessBoardPrinter.printBoardFromBlackSideWithHighlight(currentGameData.game().getBoard(), chessGame, chessPosition);
        }
    }
    public static String getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(String user) {
        currentUser = user;
    }
}
