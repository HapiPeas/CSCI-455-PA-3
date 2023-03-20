
public class VisibleFieldTester {
    public static void main(String[] args) {

         ;
    }

    private static void testOne() {
         boolean[][] smallMineField =
                 {      {false, false, false, false},
                         {true, false, false, false},
                         {false, true, true, false},
                         {false, true, false, true}};

         MineField testMineField = new MineField(smallMineField);

         VisibleField testVisibleField = new VisibleField(testMineField);

         System.out.println("Minefield");
         System.out.println(testMineField);
         System.out.println(testVisibleField);

         System.out.println("Uncover [0, 3]");
         testVisibleField.uncover(0,3);
         System.out.println(testVisibleField);
         System.out.println("Is Game Over? : "+ testVisibleField.isGameOver());

         System.out.println("Uncover [0, 0]");
         testVisibleField.uncover(0,0);
         System.out.println(testVisibleField);
         System.out.println("Is Game Over? : "+ testVisibleField.isGameOver());

         System.out.println("Reset Display");
         testVisibleField.resetGameDisplay();
         System.out.println(testVisibleField);
         System.out.println("Is Game Over? : " + testVisibleField.isGameOver());

         System.out.println("Uncover [2, 0]");
         testVisibleField.uncover(2,0);
         System.out.println(testVisibleField);
         System.out.println("Is Game Over? : "+ testVisibleField.isGameOver());

         /*
        testVisibleField.cycleGuess(0,0);
        System.out.println(testVisibleField);
        System.out.println("Is Game Over? : "+ testVisibleField.isGameOver());
        */

         System.out.println("Uncover a mine");
         testVisibleField.uncover(1,0);
         System.out.println(testVisibleField);
         System.out.println("Is Game Over? : "+ testVisibleField.isGameOver());
    }

    private static void testWin() {
         boolean[][] smallMineField =
                 {      {false, false, false, false},
                         {true, false, false, false},
                         {false, true, true, false},
                         {false, true, false, true}};
         MineField testMineField = new MineField(smallMineField);
         VisibleField testVisibleField = new VisibleField(testMineField);

         System.out.println(testVisibleField);
         testVisibleField.uncover(0,0);
         testVisibleField.uncover(0, 1);
         testVisibleField.uncover(0, 2);
         testVisibleField.uncover(0, 3);

         System.out.println(testVisibleField);
         testVisibleField.uncover(1,1);
         testVisibleField.uncover(1, 2);
         testVisibleField.uncover(1, 3);

         System.out.println(testVisibleField);
         testVisibleField.uncover(2,0);
         testVisibleField.uncover(2,3);
         System.out.println(testVisibleField.isGameOver());

         System.out.println(testVisibleField);
         testVisibleField.uncover(3, 0);
         testVisibleField.uncover(3,2);

         System.out.println(testVisibleField);

         System.out.println(testVisibleField.isUncovered(0,0));
         System.out.println(testVisibleField.isGameOver());
    }
}
