package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //Hint: Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //Hint: Generate random string of letters of length n
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < n; i += 1) {
            randomString.append(CHARACTERS[rand.nextInt(0, CHARACTERS.length)]);
        }
        return randomString.toString();
    }

    public void drawFrame(String s) {
        // Hint: Take the string and display it in the center of the screen

        // When we want to change what is displayed, firstly we clear the entire screen.
        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Hint: If game is not over, display relevant game information at the top of the screen
        int midWidth = width / 2;
        int midHeight = height / 2;
        if (!gameOver) {
            Font gameInfoFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(gameInfoFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        // Redraws everything we want to show up
        Font bigBoldFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigBoldFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Hint: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i += 1) {
            String letterToDraw = letters.substring(i, i + 1); // draw to ith letter to the screen
            drawFrame(letterToDraw);
            StdDraw.pause(1000); // each character be visible for 1 second
            drawFrame("");
            StdDraw.pause(500); // 0.5 seconds break between characters where the screen is blank
        }
    }

    public String solicitNCharsInput(int n) {
        // Hint: Read n letters of player input
        String input = "";
        drawFrame(input);
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char keyToDraw = StdDraw.nextKeyTyped();
            input += String.valueOf(keyToDraw);
            drawFrame(input);
        }
        return input;
    }

    public void startGame() {
        // Hint: Set any relevant variables before the game starts
        gameOver = false;
        playerTurn = false;
        round = 1;

        // Hint: Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round " + round + "! Good luck!");
            StdDraw.pause(1500);
            String targetString = generateRandomString(round);
            flashSequence(targetString);

            playerTurn = true;
            String userInput = solicitNCharsInput(round);
            if (!userInput.equals(targetString)) {
                gameOver = true;
                drawFrame("Game Over! Final level: " + round);
            } else {
                round += 1;
                drawFrame("Correct! Well done.");
                StdDraw.pause(1500);
            }
        }
    }
}
