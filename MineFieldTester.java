// Name: Benson Li
// USC NetID: 5489569472
// CS 455 PA3
// Spring 2023

public class MineFieldTester {
    public static void main(String[] args) {

        // Testing all MineField methods on given 2D array

        // 5x5 array with four total  mines at each corner
        boolean[][] cornerMines = {
                {true, false, false, false, true},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {true, false, false, false, true}
        };

        // Testing 1-arg MineField constructor
        MineField cornerMineField = new MineField(cornerMines);

        // Testing MineField toString method
        System.out.println(cornerMineField);

    }
}
