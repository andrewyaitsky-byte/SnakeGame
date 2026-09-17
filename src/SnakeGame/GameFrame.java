package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class GameFrame extends JFrame {

    //Container for the window
    GameWindow window;

    //Screen size
    Vector SCREENSCALE = new Vector(1000, 800);
    //Game size
    Vector GAMEGRID;

    //Time Variables
    long lastTime = 0;
    long currentTime = 0;
    double deltaTime = 0;
    long lastFrameTime = 0;

    //Key handling
    Vector lastDirection = new Vector(1, 0);
    int xVel = 0;
    int yVel = 0;
    int[] keysPressed = new int[4];

    //Flag to reset the game
    boolean resetGame = false;
    //Flag to open the menu
    boolean openMenu = false;
    //Checks if the user changed any values in the settings menu
    boolean valuesChanged = false;

    //Sets the game speed, modulates the dt to run each frame
    int gameSpeed;

    //Constructor for the frame object
    GameFrame(int apples, Vector gridSize, int gameSpeed) {
        //Set game vars
        GAMEGRID = new Vector(gridSize.intX, gridSize.intY);
        this.gameSpeed = gameSpeed;
        //Set up the window
        this.window = new GameWindow(SCREENSCALE, GAMEGRID, apples);
        this.add(this.window);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(SCREENSCALE.intX, SCREENSCALE.intY));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        //Needs to be true to capture key inputs
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        //Add keyboard detection
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Store which keys are currently pressed down into an array
                switch(e.getKeyCode()){
                    case KeyEvent.VK_RIGHT:
                        keysPressed[0] = 1;
                        break;

                    case KeyEvent.VK_LEFT:
                        keysPressed[2] = -1;
                        break;

                    case KeyEvent.VK_UP:
                        keysPressed[1] = -1;
                        break;

                    case KeyEvent.VK_DOWN:
                        keysPressed[3] = 1;
                        break;

                    case KeyEvent.VK_R:
                        resetGame = true;
                        break;

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }

    public void loop() {
        //Check the reset flag before any game logic
        if (resetGame) {
            //Reset all parameters to base
            window.init(SCREENSCALE, GAMEGRID, window.initialAppleCount);
            resetGame = false;
            lastDirection = new Vector(1, 0);
            keysPressed = new int[4];
        }

        //Calculate deltaTime, using the previous recorded time and the current time.
        currentTime = System.nanoTime();
        //Convert elapsed time to seconds from nanoseconds
        deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
        lastTime = currentTime;

        //Run Game Logic at 60fps * the speed modifier
        if ((currentTime - lastFrameTime) > 16666666 * (-.3 * gameSpeed + 30)) {
            //Save the time that the last frame was drawn
            lastFrameTime = currentTime;

            //Manage distance needed to move, negating opposing inputs
            xVel = keysPressed[0] + keysPressed[2]; //-1 is left, 1 is right
            yVel = keysPressed[1] + keysPressed[3]; //1 is down, -1 is up

            //Set the snake's next movement direction based on held inputs, x takes priority over y. (Falls through if no input currently pressed)
            if (xVel != 0) {
                lastDirection = new Vector(xVel, 0);
            }
            else if (yVel != 0) {
                lastDirection = new Vector(0, yVel);
            }

            //Move the snake based on last movement direction or the current held direction
            window.updateSnake(lastDirection);

            //Draw to screen
            window.repaint();

            //Reset held inputs, allowing user to change directions
            Arrays.fill(keysPressed, 0);
        }
    }
}
