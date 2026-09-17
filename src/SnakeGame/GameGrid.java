package SnakeGame;

public class GameGrid {
    GameGrid(){}
    //Create grid based on vector scale
    GameGrid(Vector scale){
        this.scale = scale;
        data = new int[this.scale.intX][this.scale.intY];
    }

    //Containers for the scale of the grid and the grid's data
    Vector scale;
    int[][] data;

    //Set the data at a given integer vector
    void setData(Vector point, int value){ data[point.intX][point.intY] = value;}

    //Return data held at integer vector
    int getData(Vector point){
        return data[(int) point.intX][point.intY];
    }

    //Return data held at x, y pair
    int getData(int x, int y){return data[x][y];}
}
