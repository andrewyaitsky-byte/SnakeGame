package SnakeGame;
//Andrew Yaitsky
//Snake Game
//9/15/26


import java.util.Scanner;

public class RunGame {
    public static void main(String[] args) {
        //Create a new scanner to capture user input
        Scanner inp = new Scanner(System.in);

        //Create variables to store user input
        int apples = 1;
        Vector gridSize = new Vector(16);
        int gameSpeed = 75;

        //Into message
        System.out.println("Arrow keys to change direction, R to reset. Pressing the direction your previous body segment is is failure.");

        //Ask the user if they want a custom game
        System.out.println("Do you want to customize the game? y-n");
        String userInput = inp.nextLine();
        //If the user inputs anything starting with "y"
        if(userInput.substring(0,1).equals("y")) {

            //See how many apples the user wants
            System.out.println("How many apples to start? 1 - 25");
            apples = inp.nextInt();

            //Ask the user for a width and height for the game
            System.out.println("Initial grid width? 5 - 20");
            gridSize.intX = inp.nextInt();
            System.out.println("Initial grid height? 5 - 20");
            gridSize.intY = inp.nextInt();

            System.out.println("How fast should the game be? (Slowest) 0 - 100 (Fastest)");
            gameSpeed = inp.nextInt();
        }

        //Create the game and run it until the user closes the window
        GameFrame frame = new GameFrame(apples, gridSize, gameSpeed);
        while (true) {
            frame.loop();
        }
    }
}
