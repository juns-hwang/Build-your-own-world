package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.Key;
import java.util.HashMap;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private Random rand;
    private final TETile[][] world = new TETile[WIDTH][HEIGHT];
    private long seed1;
    private boolean canType = true;
    private int playerPositionX;
    private int playerPositionY;
    private int doorX;
    private int doorY;
    private int questionX1;
    private int questionX2;
    private int questionY2;
    private int questionX3;
    private int questionY3;
    private int questionX4;
    private int questionY4;
    private boolean isCorrect = false;
    private boolean gameOver = true;
    private boolean isEncounter = false;
    private static int keyCount = 0;
    private static int score = 0;
    private String saveInput;
    private String q1 = "TRUE OF FALSE - There are 7 colors in a rainbow";
    private String q2 = "TRUE OR FALSE - Franklin is depicted on US 20 Dollar Bill.";
    private String q3 = "TRUE OR FALSE - An eggplant is a vegetable. Answer true or false.";
    private static String avatarName;
    private static char characterP;
    private static TETile PLAYER;

    public Engine() {
        StdDraw.setCanvasSize(WIDTH * 16,HEIGHT * 16 + 5);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + 5);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        //DISPLAY MAIN MENU
        menu();
        //Start playing Game
        playGame();

    }
    public void menu() {
        StdDraw.clear(Color.black);
        Font font = new Font("Arial", Font.ITALIC, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 14, "CS61B: THE GAME");
        Font menuFont = new Font("Arial", Font.ITALIC, 24);
        StdDraw.setFont(menuFont);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(WIDTH / 2, HEIGHT - 24, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 26, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 28, "Quit (Q)");
        StdDraw.text(WIDTH/2, HEIGHT - 30, "Name Avatar (A)");
        StdDraw.text(WIDTH/2, HEIGHT - 32, "Choose Avatar (X)");
        StdDraw.show();

        Font gameFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(gameFont);
        while (canType) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_X)) {
                chooseAvatar();
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                nameAvatar();
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
                newGame();
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_L)) {
                loadGame();
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                canType = false;
                System.exit(0);
            }
        }
    }
    public String tileAtMouse() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        TETile mouse = world[(int) mouseX][(int) mouseY];
        return mouse.description();
    }
    public void playGame() {
        canType = true;
        String Q = "";
        while (canType) {
            Font hud = new Font("Arial", Font.BOLD, 35);
            Font hud2 = new Font("Arial", Font.BOLD, 25);
            Font hud3 = new Font("Arial", Font.BOLD, 20);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(hud);
            if (keyCount == 0) {
                StdDraw.text(WIDTH / 2, HEIGHT + 2.5, "Enter KeyRoom ▒ to Obtain Key!");
            }
            else if(keyCount == 1) {
                StdDraw.text(WIDTH / 2, HEIGHT + 2.5, "You've Unlocked the Exit! ▒");
            }
            else {
                StdDraw.text(WIDTH / 2, HEIGHT + 2.5, "You have " + keyCount + " keys!");
            }
            StdDraw.setFont(hud2);
            StdDraw.text(WIDTH/6, HEIGHT + 3.5, "Inventory");
            StdDraw.text(WIDTH/6, HEIGHT + 3, "_________");
            if (avatarName != null) {
                StdDraw.text(WIDTH - 10, HEIGHT + 2, avatarName.substring(1));
            }
            StdDraw.setFont(hud3);
            StdDraw.text(WIDTH/6, HEIGHT + 1, "Key (" + keyCount + ")");
            StdDraw.text(WIDTH / 2, HEIGHT, "If no more room, you lost! press :Q");
            if (StdDraw.isMousePressed()) {
                StdDraw.text(WIDTH / 8 - 5, HEIGHT + 2, tileAtMouse());
            }
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char move = StdDraw.nextKeyTyped();
                if (move == 'w') {
                    moveUp();
                    saveInput += move;
                }
                if (move == 's') {
                    moveDown();
                    saveInput += move;
                }
                if (move == 'a') {
                    moveLeft();
                    saveInput += move;
                }
                if (move == 'd') {
                    moveRight();
                    saveInput += move;
                }
                if (Q.contains(":")) {
                    save(saveInput);
                    System.exit(0);
                }
                Q += move;
                ter.renderFrame(world);
            }
        }
        if (gameOver) {
            StdDraw.pause(1000);
            StdDraw.clear(Color.GRAY);
            Font menuFont = new Font("Arial", Font.BOLD, 50);
            StdDraw.setFont(menuFont);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Victory!");
            Font dFont = new Font("Arial", Font.BOLD, 30);
            StdDraw.setFont(dFont);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "New Game (Enter)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Quit (Q)");
            StdDraw.show();
            canType = true;
            while (canType) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                    canType = false;
                    System.exit(0);
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                    canType = false;
                    seed1 +=1;
                    keyCount = 0;
                    isCorrect = false;
                    isEncounter = false;
                    interactWithInputString("N" + seed1 + "S");
                    playGame();
                }
            }
        }
        if (isEncounter) {
            int e = rand.nextInt(3);
            if (e == 0) {
                encountered1();
            }
            else if (e == 1) {
                encountered2();
            }
            else {
                encountered3();
            }
            StdDraw.show();
            StdDraw.pause(1500);
            ter.initialize(WIDTH,HEIGHT + 5);
            ter.renderFrame(world);
            playGame();
        }
    }
    public void moveUp() {
        if (world[playerPositionX][playerPositionY + 1] == Tileset.FLOWER){
            world[playerPositionX][playerPositionY + 1] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionY += 1;
        }
        else if (world[playerPositionX][playerPositionY + 1] == Tileset.LOCKED_DOOR) {
            if (isCorrect) {
                world[playerPositionX][playerPositionY + 1] = PLAYER;
                world[playerPositionX][playerPositionY] = Tileset.FLOWER;
                playerPositionY += 1;
                canType = false;
                gameOver = true;
            }
        }
        else if (world[playerPositionX][playerPositionY + 1] == Tileset.SAND) {
            world[playerPositionX][playerPositionY + 1] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionY += 1;
            canType = false;
            isEncounter = true;
        }
    }
    public void moveDown() {
        if (world[playerPositionX][playerPositionY - 1] == Tileset.FLOWER) {
            world[playerPositionX][playerPositionY - 1] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionY -= 1;
        }
        else if (world[playerPositionX][playerPositionY - 1] == Tileset.LOCKED_DOOR) {
            if (isCorrect) {
                world[playerPositionX][playerPositionY - 1] = PLAYER;
                world[playerPositionX][playerPositionY] = Tileset.FLOWER;
                playerPositionY -= 1;
                canType = false;
                gameOver = true;
            }
        }
        else if (world[playerPositionX][playerPositionY - 1] == Tileset.SAND) {
            world[playerPositionX][playerPositionY - 1] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionY -= 1;
            canType = false;
            isEncounter = true;
        }
    }
    public void moveLeft() {
        if (world[playerPositionX - 1][playerPositionY] == Tileset.FLOWER) {
            world[playerPositionX - 1][playerPositionY] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionX -= 1;
        }
        else if (world[playerPositionX - 1][playerPositionY] == Tileset.LOCKED_DOOR) {
            if (isCorrect) {
                world[playerPositionX - 1][playerPositionY] = PLAYER;
                world[playerPositionX][playerPositionY] = Tileset.FLOWER;
                playerPositionX -= 1;
                canType = false;
                gameOver = true;
            }
        }
        else if (world[playerPositionX - 1][playerPositionY] == Tileset.SAND) {
            world[playerPositionX - 1][playerPositionY] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionX -= 1;
            canType = false;
            isEncounter = true;
        }
    }
    public void moveRight() {
        if (world[playerPositionX + 1][playerPositionY] == Tileset.FLOWER) {
            world[playerPositionX + 1][playerPositionY] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionX += 1;
        }
        else if (world[playerPositionX + 1][playerPositionY] == Tileset.LOCKED_DOOR) {
            if (isCorrect) {
                world[playerPositionX + 1][playerPositionY] = PLAYER;
                world[playerPositionX][playerPositionY] = Tileset.FLOWER;
                playerPositionX += 1;
                canType = false;
                gameOver = true;
            }
        }
        else if (world[playerPositionX + 1][playerPositionY] == Tileset.SAND) {
            world[playerPositionX + 1][playerPositionY] = PLAYER;
            world[playerPositionX][playerPositionY] = Tileset.FLOWER;
            playerPositionX += 1;
            canType = false;
            isEncounter = true;
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        input = input.toUpperCase();
        int start = input.indexOf("N") + 1;
        int end = input.indexOf("S");
        int L = input.indexOf("L");
        if (input.contains("N") && input.contains("S")) {
            seed1 = Long.parseLong(input.substring(start, end));
            this.rand = new Random(seed1);
            generateMap();
        }
        for (int i = end + 1; i < input.length(); i++) {
            if (input.charAt(i) == ':'){
                if (input.charAt(i + 1) == 'Q') {
                    save(saveInput.substring(0,saveInput.length()-2));
                    System.exit(0);
                }
            }
            if (input.charAt(i) == 'W'){
                moveUp();
            }
            if (input.charAt(i) == 'A'){
                moveLeft();
            }
            if (input.charAt(i) == 'S'){
                moveDown();
            }
            if (input.charAt(i) == 'D'){
                moveRight();
            }
        }
        if (input.contains("L")) {
            for (int i = L + 1; i < input.length(); i++) {
                saveInput += input.charAt(i);
                if (input.charAt(i) == ':') {
                    if (input.charAt(i + 1) == 'Q') {
                        save(saveInput.substring(0, saveInput.length() - 2));
                        System.exit(0);
                    }
                }
                if (input.charAt(i) == 'W') {
                    moveUp();
                }
                if (input.charAt(i) == 'A') {
                    moveLeft();
                }
                if (input.charAt(i) == 'S') {
                    moveDown();
                }
                if (input.charAt(i) == 'D') {
                    moveRight();
                }
            }
        }
        gameOver = false;
        TETile[][] finalWorldFrame = world;
        ter.initialize(WIDTH,HEIGHT + 5);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    /**
     * public static void main(String[] args) {
     * Engine newGame = new Engine();
     * newGame.world = newGame.interactWithInputString(args[1]);
     * TERenderer ter = new TERenderer();
     * ter.initialize(WIDTH, HEIGHT);
     * ter.renderFrame(newGame.world);
     * }
     */
    public void generateMap() {
        paintNothing();
        randomRoom();
        areaRoomInside();
        areaRoom();
    }
    public void paintNothing() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    //Makes one room sized L * H at positionX and positionY
    public void makeRoom(int L, int H, int positionX, int positionY) {

        int boundaryX = L + positionX;
        int boundaryY = H + positionY;

        if (boundaryX > WIDTH || boundaryY > HEIGHT) {
            return;
        } else if (L < 4 || H < 4) {
            return;
        } else {
            //Paint wall part of the room
            for (int x = positionX; x < positionX + L; x += 1) {
                for (int y = positionY; y < positionY + H; y += 1) {
                    if (world[x][y] != Tileset.NOTHING) {
                        if (world[x][y] != Tileset.WALL) {
                            world[x][y] = Tileset.FLOWER;
                        }
                    } else {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }

            //Paint inside part of the room
            for (int x = positionX + 1; x < positionX + L - 1; x += 1) {
                for (int y = positionY + 1; y < positionY + H - 1; y += 1) {
                    world[x][y] = Tileset.FLOWER;
                }
            }
        }
    }
    public void randomRoom() {
        this.rand = new Random(seed1);

        for (int i = 0; i < 5; i++) {
            int l = rand.nextInt(11, 13);
            int h = rand.nextInt(11,13);

            int x = rand.nextInt(0,WIDTH - l);
            int y = rand.nextInt(0,HEIGHT - h);

            int a = rand.nextInt(1,9);
            makeRoom(l,h,x,y,a);
        }
    }

    //MakeRoom at given area (1-8)
    public void makeRoom(int L, int H, int positionX, int positionY, int area) {
        makeRoom(L, H, positionX, positionY);
        if (area == 1) {
            this.rand = new Random(seed1);
            int a = rand.nextInt(0, 3);
            if (a == 1) {
                makeHallWay(0, WIDTH - (positionX + L),
                        positionX + L - 1, positionY + H / 2);
            } else if (a == 2) {
                makeHallWay2(1, positionY,
                        positionX + L / 2 + 1, positionY + 1);
            } else {
                makeHallWay(0, WIDTH - (positionX + L),
                        positionX + L - 1, positionY + H / 2);
                makeHallWay2(1, positionY,
                        positionX + L / 2 + 1, positionY + 1);
            }
        }
        if (area == 5) {
            makeHallWay(0, WIDTH - (positionX + L),
                    positionX + L - 1, positionY + H / 2);
        }

        if (area == 4) {
            makeHallWay2(1, positionY,
                    positionX + L / 2 + 1, positionY + 1);
        }
        if (area == 2) {
            this.rand = new Random(seed1);
            int a = rand.nextInt(0, 3);
            if (a == 1) {
                makeHallWay(0, WIDTH - positionX - L,
                        positionX + L - 1, positionY + 1);
            } else if (a == 2) {
                makeHallWay2(1, positionY,
                        (WIDTH - positionX - L + 5), positionY + 2);
            } else {
                makeHallWay(0, WIDTH - positionX - L,
                        positionX + L - 1, positionY + 1);
                makeHallWay2(1, positionY,
                        (WIDTH - positionX - L + 5), positionY + 2);
            }
            this.rand = new Random(seed1);
            int length = rand.nextInt(positionY / 2, positionY);
            makeHallWay2(1, length, positionX + L / 2, positionY + 1);
        }
        if (area == 3) {
            makeHallWay2(1, positionY, positionX + L / 2, positionY + 1);
        }
        if (area == 6) {
            makeHallWay(0, WIDTH - positionX - L, positionX + L, positionY + H / 2);
        }
    }

    public void areaRoom() {
        //LENGTH WIDTH OF ROOMS
        this.rand = new Random(seed1);
        int lh = rand.nextInt(12, 20);
        int lh2 = rand.nextInt(8, 12);
        int lh3 = rand.nextInt(12, 20);
        int lh4 = rand.nextInt(8, 12);
        int exitR = rand.nextInt(3);
        //SPLIT AREAS
        //
        //AREA 1: X = 0 ~ (WIDTH/4 - lh), Y = Height/2 ~ (Height - lh2)
        this.rand = new Random(seed1);
        int x1 = rand.nextInt(0, WIDTH / 4 - lh);
        int y1 = rand.nextInt(HEIGHT / 2 + HEIGHT / 8, HEIGHT - lh2);
        makeRoom(lh, lh2, x1, y1, 1);
        int a1 = rand.nextInt(x1 +1 , x1 + lh - 2);
        int a2 = rand.nextInt(y1 + 1, y1 + lh2 - 2);
        //
        //WHERE THE AVATAR STARTS
        world[a1][a2] = PLAYER;
        playerPositionX = a1;
        playerPositionY = a2;

        //
        //AREA 4: X = 3W/4 ~ (WIDTH - lh), Y = Height/2 ~ (Height - lh2)
        this.rand = new Random(seed1);
        int x4 = rand.nextInt((3 * WIDTH) / 4, WIDTH - lh);
        int y4 = rand.nextInt(HEIGHT / 2 + HEIGHT / 8, HEIGHT - lh2);
        makeRoom(lh3, lh2, x4, y4, 4);
        if (exitR == 0) {
            int a4 = rand.nextInt(x4 + 1, x4 + lh3 - 2);
            int b4 = rand.nextInt(y4 + 1, y4 + lh2 - 2);
            world[a4][b4] = Tileset.LOCKED_DOOR;
            doorX = a4;
            doorY = b4;
        }
        //
        //AREA 5: X = 0 ~ (WIDTH/4 - lh), Y = 0 ~ (Height/2 - lh2)
        this.rand = new Random(seed1);
        int x5 = rand.nextInt(0, (WIDTH / 4) - lh);
        int y5 = rand.nextInt(0, (HEIGHT / 2) - lh2);
        makeRoom(lh3, lh4, x5, y5, 5);
        if (exitR == 1) {
            int a5 = rand.nextInt(x5 + 1, x5 + lh3 - 2);
            int b5 = rand.nextInt(y5 + 1, y5 + lh4 - 2);
            world[a5][b5] = Tileset.LOCKED_DOOR;
            doorX = a5;
            doorY = b5;
        }
        //
        //AREA 8: X = 3W/4 ~ (WIDTH - lh), Y = 0 ~ (Height/2 - lh2)
        this.rand = new Random(seed1);
        int x8 = rand.nextInt((3 * WIDTH / 4), WIDTH - lh);
        int y8 = rand.nextInt(0, HEIGHT / 2 - lh2);
        makeRoom(lh, lh4, x8, y8, 8);
        if (exitR == 2) {
            int a8 = rand.nextInt(x8 + 1, x8 + lh - 2);
            int b8 = rand.nextInt(y8 + 1, y8 + lh2 - 2);
            world[a8][b8] = Tileset.LOCKED_DOOR;
            doorX = a8;
            doorY = b8;
        }
    }
    public void areaRoomInside() {
        //LENGTH WIDTH OF ROOMS
        this.rand = new Random(seed1);
        int lh = rand.nextInt(12, 20);
        int lh2 = rand.nextInt(8, 12);
        int lh3 = rand.nextInt(12, 20);
        int lh4 = rand.nextInt(8, 12);
        //
        //AREA 2: X = Width/4 ~ (Width/2 - lh), Y = Height/2 ~ (Height - lh2)
        this.rand = new Random(seed1);
        int x2 = rand.nextInt(WIDTH / 4, (WIDTH / 2 - lh));
        int y2 = rand.nextInt(HEIGHT / 2, HEIGHT - lh2);
        makeRoom(lh, lh2, x2, y2, 2);
        int a2 = rand.nextInt(x2 + 1, x2 + lh - 2);
        int b2 = rand.nextInt(y2 + 1, y2 + lh2 - 2);
        world[a2][b2] = Tileset.SAND;
        questionX1 = a2;
        //
        //AREA 3: X = Width/2 ~ (3Width/4), Y = Height/2 ~ (Height - lh2)
        this.rand = new Random(seed1);
        int x3 = rand.nextInt(WIDTH / 2, 3 * WIDTH / 4);
        int y3 = rand.nextInt(HEIGHT / 2, HEIGHT - lh2);
        makeRoom(lh3, lh4, x3, y3, 3);
        int a3 = rand.nextInt(x3 + 1, x3 + lh3 - 2);
        int b3 = rand.nextInt(y3 + 1, y3 + lh4 - 2);
        world[a3][b3] = Tileset.SAND;
        questionX2 = a3;
        questionY2 = b3;
        //
        //AREA 6: X = Width/4 ~ (Width/2 - lh), Y = 0 ~ (HEIGHT/2) - lh2
        this.rand = new Random(seed1);
        int x6 = rand.nextInt(WIDTH / 4, (WIDTH / 2 - lh));
        int y6 = rand.nextInt(0, (HEIGHT / 2) - lh2);
        makeRoom(lh, lh4, x6, y6, 6);
        int a6 = rand.nextInt(x6 + 1, x6 + lh - 2);
        int b6 = rand.nextInt(y6 + 1, y6 + lh4 - 2);
        world[a6][b6] = Tileset.SAND;
        questionX3 = a6;
        questionY3 = b6;
        //
        //AREA 7: X = Width/2 ~ (3Width/4),Y = 0 ~ (HEIGHT/2) - lh2
        int x7 = rand.nextInt(WIDTH / 2, 3 * WIDTH / 4);
        int y7 = rand.nextInt(0, (HEIGHT / 2) - lh2);
        makeRoom(lh3, lh2, x7, y7, 7);
        int a7 = rand.nextInt(x7 + 1, x7 + lh3 - 2);
        int b7 = rand.nextInt(y7 + 1, y7 + lh2 - 2);
        world[a7][b7] = Tileset.SAND;
        questionX4 = a7;
        questionY4 = b7;
    }

    //Makes a hallway of length, L at positionX and positionY
    //Is a horizontal if r == 0, and vertical if r == 1
    public void makeHallWay(int r, int L, int positionX, int positionY) {
        if (r == 0) {
            makeHW1(L, positionX, positionY);
        }
        if (r == 1) {
            makeHW2(L, positionX, positionY);
        }
    }

    //HALLWAY THAT GOES OPPOSITE DIRECTION
    //STARTS AT X,Y AND GOES DOWN L DISTANCE AT MOST X, Y DISTANCES.
    public void makeHallWay2(int r, int L, int positionX, int positionY) {
        if (r == 0) {
            if (positionX < L || positionY < 3) {
                return;
            }
            for (int x = positionX - 1; x > positionX - L - 1; x--) {
                for (int y = positionY - 1; y > positionY - 2; y--) {
                    if (world[x][y] != Tileset.NOTHING) {
                        if (world[x][y] != Tileset.WALL) {
                            world[x][y] = Tileset.FLOWER;
                        }
                    } else {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
            for (int x = positionX - 1; x > positionX - L - 1; x--) {
                for (int y = positionY - 2; y > positionY - 3; y--) {
                    world[x][y] = Tileset.FLOWER;
                }
            }
            world[positionX - 1][positionY - 2] = Tileset.WALL;
            world[positionX - L][positionY - 2] = Tileset.WALL;
            for (int x = positionX - 1; x > positionX - L - 1; x--) {
                for (int y = positionY - 3; y > positionY - 4; y--) {
                    if (world[x][y] != Tileset.NOTHING) {
                        if (world[x][y] != Tileset.WALL) {
                            world[x][y] = Tileset.FLOWER;
                        }
                    } else {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
        if (r == 1) {
            if (positionY < L || positionX < 3) {
                return;
            }
            for (int x = positionX - 1; x > positionX - 2; x--) {
                for (int y = positionY - 1; y > positionY - L - 1; y--) {
                    if (world[x][y] != Tileset.NOTHING) {
                        if (world[x][y] != Tileset.WALL) {
                            world[x][y] = Tileset.FLOWER;
                        }
                    } else {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
            for (int x = positionX - 2; x > positionX - 3; x--) {
                for (int y = positionY - 1; y > positionY - L - 1; y--) {
                    world[x][y] = Tileset.FLOWER;
                }
            }
            world[positionX - 2][positionY - 1] = Tileset.FLOWER;
            world[positionX - 2][positionY - L] = Tileset.WALL;
            for (int x = positionX - 3; x > positionX - 4; x--) {
                for (int y = positionY - 1; y > positionY - L - 1; y--) {
                    if (world[x][y] != Tileset.NOTHING) {
                        if (world[x][y] != Tileset.WALL) {
                            world[x][y] = Tileset.FLOWER;
                        }
                    } else {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void makeHW1(int L, int positionX, int positionY) {
        if (L + positionX > WIDTH || 3 + positionY > HEIGHT) {
            return;
        }
        for (int x = positionX; x < positionX + L; x++) {
            for (int y = positionY; y < positionY + 1; y++) {
                if (world[x][y] != Tileset.NOTHING) {
                    if (world[x][y] != Tileset.WALL) {
                        world[x][y] = Tileset.FLOWER;
                    }
                } else {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
        for (int x = positionX + 1; x < positionX + L - 1; x++) {
            for (int y = positionY; y < positionY + 1; y++) {
                world[x][y + 1] = Tileset.FLOWER;
            }
        }
        world[positionX][positionY + 1] = Tileset.FLOWER;
        world[positionX + L - 1][positionY + 1] = Tileset.WALL;
        for (int x = positionX; x < positionX + L; x++) {
            for (int y = positionY; y < positionY + 1; y++) {
                if (world[x][y + 2] != Tileset.NOTHING) {
                    if (world[x][y + 2] != Tileset.WALL) {
                        world[x][y + 2] = Tileset.FLOWER;
                    }
                } else {
                    world[x][y + 2] = Tileset.WALL;
                }
            }
        }
    }

    public void makeHW2(int L, int positionX, int positionY) {
        if (L + positionY > HEIGHT || 3 + positionX > WIDTH) {
            return;
        }
        for (int x = positionX; x < positionX + 1; x++) {
            for (int y = positionY; y < positionY + L; y++) {
                if (world[x][y] != Tileset.NOTHING) {
                    if (world[x][y] != Tileset.WALL) {
                        world[x][y] = Tileset.FLOWER;
                    }
                } else {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
        for (int x = positionX; x < positionX + 1; x++) {
            for (int y = positionY; y < positionY + L; y++) {
                world[x + 1][y] = Tileset.FLOWER;
            }
        }
        world[positionX + 1][positionY] = Tileset.FLOWER;
        world[positionX + 1][positionY + L - 1] = Tileset.WALL;
        for (int x = positionX; x < positionX + 1; x++) {
            for (int y = positionY; y < positionY + L; y++) {
                if (world[x + 2][y] != Tileset.NOTHING) {
                    if (world[x + 2][y] != Tileset.WALL) {
                        world[x + 2][y] = Tileset.FLOWER;
                    }
                } else {
                    world[x + 2][y] = Tileset.WALL;
                }
            }
        }
    }
    private static String load() {
        File file = new File("./save_date.txt");
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                return (String) objectInputStream.readObject();
            } catch (FileNotFoundException | ClassNotFoundException excp) {
                throw new IllegalArgumentException(excp.getMessage());
            } catch (IOException excp) {
                System.out.println(excp);
                System.exit(0);
            }
        }
        return "";
    }

    private static void save(String record) {
        File file = new File("./save_date.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(record);
        } catch (FileNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        } catch (IOException excp) {
            System.out.println(excp);
            System.exit(0);
        }
    }
    public void chooseAvatar() {
        canType = false;
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "Choose Your Avatar");
        StdDraw.text(WIDTH/2 - 5, HEIGHT - 15, "▲");
        StdDraw.text(WIDTH/2, HEIGHT- 15, "♠");
        StdDraw.text(WIDTH/2 + 5, HEIGHT - 15, "≈");
        StdDraw.text(WIDTH/2 - 5, HEIGHT - 20, "1");
        StdDraw.text(WIDTH/2, HEIGHT - 20, "2");
        StdDraw.text(WIDTH/2 + 5, HEIGHT - 20, "3");
        StdDraw.show();
        canType = true;
        while (canType) {
            if (StdDraw.hasNextKeyTyped()) {
                char x = StdDraw.nextKeyTyped();
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "Your Character is: " + characterP);
                    StdDraw.show();
                    StdDraw.pause(1000);
                    menu();
                }
                else {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 5, "Choose Your Avatar, then Press Enter");
                    StdDraw.text(WIDTH/2 - 5, HEIGHT - 15, "▲");
                    StdDraw.text(WIDTH/2, HEIGHT- 15, "♠");
                    StdDraw.text(WIDTH/2 + 5, HEIGHT - 15, "≈");
                    StdDraw.text(WIDTH/2 - 5, HEIGHT - 20, "1");
                    StdDraw.text(WIDTH/2, HEIGHT - 20, "2");
                    StdDraw.text(WIDTH/2 + 5, HEIGHT - 20, "3");
                    StdDraw.show();
                    if (x == '1') {
                        characterP = '▲';
                        PLAYER = new TETile(characterP, Color.WHITE, Color.GRAY, "you");
                    } else if (x == '2') {
                        characterP = '♠';
                        PLAYER = new TETile(characterP, Color.WHITE, Color.GRAY, "you");
                    } else if (x == '3') {
                        characterP = '≈';
                        PLAYER = new TETile(characterP, Color.WHITE, Color.GRAY, "you");
                    }
                }
            }
        }
    }
    public void nameAvatar() {
        canType = false;
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 15, "Name Your Avatar");
        StdDraw.show();
        canType = true;
        avatarName = "";
        while (canType) {
            if (StdDraw.hasNextKeyTyped()) {
                char x = StdDraw.nextKeyTyped();
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "Name = " + avatarName.substring(1));
                    StdDraw.show();
                    StdDraw.pause(1000);
                    menu();
                }
                else {
                    avatarName += x;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "Name Your Avatar, then Enter");
                    StdDraw.text(WIDTH / 2, HEIGHT - 25, avatarName.substring(1));
                    StdDraw.show();
                }
            }
        }
    }
    public void newGame() {
        if (characterP != '▲' && characterP != '♠' && characterP != '≈') {
            characterP = 'X';
            PLAYER = new TETile(characterP, Color.WHITE, Color.GRAY, "you");
        }
        canType = false;
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 15, "Please Enter a Seed and Movement");
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Please 'Enter' to Load");
        StdDraw.show();
        canType = true;
        String input = "";
        while (canType) {
            if (StdDraw.hasNextKeyTyped()) {
                char num = StdDraw.nextKeyTyped();
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                    canType = false;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "LOADING WORLD: " + input);
                    StdDraw.text(WIDTH / 2, HEIGHT - 25, input);
                    StdDraw.show();
                    StdDraw.pause(1500);
                    saveInput = input;
                    interactWithInputString(input);
                }
                else {
                    input += num;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "Please Enter a Seed, then press s");
                    StdDraw.text(WIDTH / 2, HEIGHT - 25, input);
                    StdDraw.show();
                }
            }
        }
    }
    public void loadGame() {
        if (characterP != '▲' && characterP != '♠' && characterP != '≈') {
            characterP = 'X';
            PLAYER = new TETile(characterP, Color.WHITE, Color.GRAY, "you");
        }
        canType = false;
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 15, "Please Enter a Movement");
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Please 'Enter' to Load");
        StdDraw.show();
        canType = true;
        String inputL = "";
        while (canType) {
            if (StdDraw.hasNextKeyTyped()) {
                char num = StdDraw.nextKeyTyped();
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                    canType = false;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "LOADING WORLD: " + inputL);
                    StdDraw.text(WIDTH / 2, HEIGHT - 25, inputL);
                    StdDraw.show();
                    StdDraw.pause(1500);
                    String loadInput = load();
                    loadInput += inputL.substring(1);
                    saveInput = loadInput;
                    interactWithInputString(loadInput);
                }
                else {
                    inputL += num;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT - 15, "Please Enter Movement");
                    StdDraw.text(WIDTH / 2, HEIGHT - 25, inputL);
                    StdDraw.show();
                }
            }
        }
    }
    public void encountered1() {
        StdDraw.pause(1000);
        StdDraw.clear(Color.GRAY);
        Font menuFont = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Answer correctly to obtain the key!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Type in lowercase!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Press 'Enter' once done!");
        StdDraw.show();
        StdDraw.pause(4000);
        StdDraw.clear(Color.GRAY);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, q1);
        StdDraw.show();
        String input1 = "";
        canType = true;
        while (canType) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                canType = false;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char answer = StdDraw.nextKeyTyped();
                input1 += answer;
                StdDraw.clear(Color.GRAY);
                StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, q1);
                StdDraw.text(WIDTH / 2, HEIGHT/2, input1);
                StdDraw.show();
            }
        }
        if (input1.length() == 6) {
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Incorrect!");
        }
        else {
            keyCount += 1;
            isCorrect = true;
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Correct! Obtained a 'KEY'!");
        }
    }
    public void encountered2() {
        StdDraw.pause(1000);
        StdDraw.clear(Color.GRAY);
        Font menuFont = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Answer correctly to obtain the key!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Type in lowercase!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Press 'Enter' once done!");
        StdDraw.show();
        StdDraw.pause(4000);
        StdDraw.clear(Color.GRAY);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, q2);
        StdDraw.show();
        String input2 = "";
        canType = true;
        while (canType) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                canType = false;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char answer = StdDraw.nextKeyTyped();
                input2 += answer;
                StdDraw.clear(Color.GRAY);
                StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, q2);
                StdDraw.text(WIDTH / 2, HEIGHT/2, input2);
                StdDraw.show();
            }
        }
        if (input2.length() == 4) {
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Incorrect!");
        }
        else {
            keyCount += 1;
            isCorrect = true;
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Correct! Obtained a 'KEY'!");
        }
    }
    public void encountered3() {
        StdDraw.pause(1000);
        StdDraw.clear(Color.GRAY);
        Font menuFont = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Answer correctly to obtain the key!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Type in lowercase!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Press 'Enter' once done!");
        StdDraw.show();
        StdDraw.pause(4000);
        StdDraw.clear(Color.GRAY);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, q3);
        StdDraw.show();
        String input4 = "";
        canType = true;
        while (canType) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                canType = false;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char answer = StdDraw.nextKeyTyped();
                input4 += answer;
                StdDraw.clear(Color.GRAY);
                StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, q3);
                StdDraw.text(WIDTH / 2, HEIGHT/2, input4);
                StdDraw.show();
            }
        }
        if (input4.length() == 4) {
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Incorrect!");
        }
        else {
            keyCount += 1;
            isCorrect = true;
            StdDraw.clear(Color.GRAY);
            StdDraw.text(WIDTH / 2, HEIGHT /2 + 10, "Correct! Obtained a 'KEY'!");
        }
    }
}