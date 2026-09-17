package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameWindow extends JPanel{
    GameWindow(Vector screenscale, Vector gamegrid, int appleCount){
        //Run init function on object creation
        init(screenscale, gamegrid, appleCount);
    }

    //Constants for the game scaling
    Vector SCREENSCALE;
    Vector GAMEGRID;
    Vector GAMESCALE;

    //4 colors based on gameboy color palette
    Color white = new Color(162, 196, 16);
    Color light = new Color(139, 172, 15);
    Color dark = new Color(48, 98, 48);
    Color black = new Color(15, 56, 15);

    //Game positioning in the window
    Vector gameCorner;
    Vector gameSize;

    //Tracks the score, apples remaining and the starting apples
    int totalScore;
    int appleCount;
    int initialAppleCount;

    //Flips color when dead
    boolean colorFlip = false;

    //Font to use in game
    Font pixelatedFont;

    //Label to display the score
    JLabel textLabel = new JLabel("Hello, World!");

    //grid to hold all game data
    GameGrid grid;

    //RNG
    Random rand;

    //Game conditions
    boolean endCondition;
    boolean addSegment;

    //Snake data container, stores snake segments as vectors
    ArrayList<Vector> snake;

    //Runs on object creation, called when resetting the game as well
    public void init(Vector screenscale, Vector gamegrid, int appleCount){
        //Set the local vars to the input vars
        SCREENSCALE = screenscale;
        GAMEGRID = gamegrid;
        //Cap y scale at 50
        GAMEGRID.intX = Math.min(GAMEGRID.intY, 50);
        //Limit the X scale to something that will fit on the screen
        GAMEGRID.intX = Math.min(GAMEGRID.intX, (int) (GAMEGRID.intY * 1.333));
        this.appleCount = appleCount;
        this.initialAppleCount = appleCount;

        //Load Determination font, fallback on the arial font if can't find
        try {
            //Load the font file
            File fontFile = new File("SnakeGame/Fonts/determination.ttf");
            pixelatedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            //Register the font in the local Graphics Environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelatedFont);

            //Set font to size and font type
            textLabel.setFont(pixelatedFont.deriveFont(36f));

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback font if the file fails to load
            textLabel.setFont(new Font("Arial", Font.BOLD, 36));
        }

        //Create a blank grid
        grid = new GameGrid(GAMEGRID);

        //Calculate scale of game squared based on the fixed y constraint (Occupies 7/8 of the screen vertically)
        GAMESCALE = new Vector((SCREENSCALE.intY * 7 / 8) / GAMEGRID.intY);

        //Calculate the top left corner based on the fixed y and the square size
        gameCorner = new Vector((SCREENSCALE.intX - GAMESCALE.intX * GAMEGRID.intX) / 2, SCREENSCALE.intY / 16);

        //Find the size of the window that will hold the game
        gameSize = new Vector(GAMESCALE.intX * GAMEGRID.intX, GAMESCALE.intY * GAMEGRID.intY);

        //Set up the label
        this.setLayout(null);
        this.textLabel.setBounds(SCREENSCALE.intX / 32, SCREENSCALE.intY / 64, SCREENSCALE.intX, SCREENSCALE.intY / 20);
        this.add(textLabel, BorderLayout.CENTER);

        //Random number generator
        rand = new Random();

        //Condition for ending
        endCondition = false;

        //Condition for adding one segment to the snake
        addSegment = false;

        //Snake init
        snake = new ArrayList<Vector>();
        snake.add(new Vector(2, GAMEGRID.intY / 2));
        snake.add(new Vector(1, GAMEGRID.intY / 2));
        snake.add(new Vector(0, GAMEGRID.intY / 2));

        //Container for score and the label that holds the display for it
        totalScore = 0;
        textLabel.setText("Score: 0");

        //Add first apples, draw snake for first time
        for(int i = 0; i < appleCount; i++) {
            //Add amount of apples the user wants
            addApple();
        }
        redrawSnake();
    }

    //Add all the current data for the snake to the game grid, allowing it to be drawn.
    public void redrawSnake(){
        for(int i = 0; i < snake.size(); i++){
            grid.setData(snake.get(i), 1);
        }
    }

    //Move the snake by one unit, update data, graph, and check for loseconditions.
    public void updateSnake(Vector move){
        //Check for collision with the wall
        checkWall(snake.get(0), move);

        //Check for collision with self if still alive
        if(!endCondition){endCondition = checkSelf(snake.get(0), move);}

        //Check for collision with apple
        checkApple(snake.get(0), move);

        //If still alive, move
        if(!endCondition) {
            //Remove old segment unless the snake needs to grow
            if(!addSegment) {
                //Remove the old snake tail graphically
                grid.setData(snake.get(snake.size() - 1), 0);
                //Remove old snake tail from data
                snake.remove(snake.size() - 1);
            }
            else{
                //If the snake ate an apple, add a new one, increment the score and the display for the score.
                addApple();
                totalScore++;
                textLabel.setText("Score: " + totalScore);
                addSegment = false;
            }
            //Add new snake head at next position
            snake.add(0, Vector.add(snake.get(0), move));
            //Add the new snake head graphically
            grid.setData(snake.get(0), 1);
        }
    }

    //Check snake collision with the wall
    public void checkWall(Vector position, Vector offset){
        //Calculate new position from the povided values
        Vector checkPos = Vector.add(position, offset);
        //Check wall collision
        if(checkPos.intX < 0 || checkPos.intY < 0 || checkPos.intX >= GAMEGRID.intX || checkPos.intY >= GAMEGRID.intY){
            endCondition = true;
        }
    }

    //Check snake collision with itself
    public boolean checkSelf(Vector position, Vector offset){
        //Calculate new position from the provided values
        Vector checkPos = Vector.add(position, offset);
        //Check self collision on all segments
        System.out.println(snake.size());
        for (int i = 1; i < snake.size(); i++) {
            if (checkPos.intX.equals(snake.get(i).intX) && checkPos.intY.equals(snake.get(i).intY)) {
                return true;
            }
        }
        return false;
    }

    //Check snake collision with apples
    public void checkApple(Vector position, Vector offset){
        if(!endCondition) {
            //Calculate new position from the povided values
            Vector checkPos = Vector.add(position, offset);
            //Check if new segment is on an apple
            if (grid.getData(checkPos) == 2) {
                addSegment = true;
            }
        }
    }

    //Add an apple at a random position that is not occupied by the snake
    public void addApple(){
        //If there is open space
        if((int) grid.scale.area() - snake.size() - appleCount + 1 > 0) {
            //Get random number that corresponds to space not occupied by the snake or apples
            int randPos = rand.nextInt((int) grid.scale.area() - snake.size() - appleCount + 1);
            //Make sure snake is properly registered in the grid
            redrawSnake();
            //Scan all spots, associate the randomly chosen square with an open x, y pair
            for (int i = 0; i < (int) grid.scale.area(); i++) {
                //If space is not occupied, increment the empty space counter. This counts the empty spaces to place the apple.
                if (grid.getData(new Vector(i % grid.scale.intX, i / grid.scale.intX)) == 0) {
                    //If at the random position, add the apple. (will be an empty space if incrementing to the point from the start.)
                    if (randPos == 0) {
                        grid.setData(new Vector(i % grid.scale.intX, i / grid.scale.intX), 2);
                        break;
                    }
                    randPos -= 1;
                }
            }
        }
        else{
            //Remove one apple from the amount to draw if there is no space to place one
            appleCount -= 1;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //Set background to light if alive, dark if dead
        g.setColor(endCondition ? dark : light);
        g.fillRect(0, 0, SCREENSCALE.intX, SCREENSCALE.intY);

        //Iterate over every x, y pair in GAMEGRID
        for(int x = 0; x < GAMEGRID.intX; x++){
            for(int y = 0; y < GAMEGRID.intY; y++){
                //Draw any apples as red squares
                if(grid.getData(x, y) == 2){
                    g.setColor(light);
                }
                //Draw any snake segments as white if alive, black or white if dead depending on time
                if(grid.getData(x, y) == 1){
                    g.setColor(endCondition && colorFlip ? black : white);

                }
                //Draw any blank spaces as a checkerboard
                if(grid.getData(x, y) == 0){
                    g.setColor((x+ y) % 2 == 0 ? black : dark);
                }
                //Draw the square
                g.fillRect(GAMESCALE.intX * x + gameCorner.intX, GAMESCALE.intY * y + gameCorner.intY, GAMESCALE.intX, GAMESCALE.intY);
            }
        }
        //Flip color if dead
        if(endCondition) {
            colorFlip = !colorFlip;
        }

    }
}
