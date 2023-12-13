/*
 * James Salzer & Trent Heier
 * COP 3330 OOP
 * GUI Board Project
 * December 2 2023 
 * 
 */
//figure out directory...

//implement interface for randomizer
//implement randomizer

//imports for JFrame and graphic implementation
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.swing.JFrame;

//imports for JButton implementation
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

//imports for Serializable and file reading
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;

import java.util.Random;

abstract class Collectables extends JPanel{
    abstract void DrawCollectable(Graphics g);
}

abstract class Obstacles extends JPanel{
    abstract void DrawObstacles(Graphics g);
}

abstract class Doors{
    abstract void DrawDoor(Graphics g);
}
/**
 * InnerBoardGameGUI
 */
interface RandomObstacle {
    public int[][] generateRandomLayout();
}
class Randomizer implements RandomObstacle{ 

    int [][] layout = new int[32][32];
    //max obstacle walls: 820
    int collectableCoin = 16;
    int collectableTree = 16;
    int collectableTreasure = 16; 
    int obstacleWall = 256;
    int obstacleRandom = 32;


    public int[][] generateRandomLayout(){


        Random random = new Random();
        int entranceY = random.nextInt(1,30);
        int exitY = random.nextInt(1,30);
        layout[0][entranceY]=7;
        layout[31][exitY]=8;

        while((collectableCoin!=0)||(collectableTree!=0)||(collectableTreasure!=0)||(obstacleRandom!=0)){
            int randomIntX = random.nextInt(1,31);
            int randomIntY = random.nextInt(1,31);
            if(layout[randomIntX][randomIntY]==0){
                if(collectableCoin!=0){
                    layout[randomIntX][randomIntY]=1;
                    collectableCoin--;
                }
                else if(collectableTree!=0){
                    layout[randomIntX][randomIntY]=2;
                    collectableTree--;
                }
                else if(collectableTreasure!=0){
                    layout[randomIntX][randomIntY]=3;
                    collectableTreasure--;
                }
                else if(obstacleWall!=0){
                    if(validLocation(randomIntX, randomIntY)!=-1){
                            int valid = validLocation(randomIntX, randomIntY);
                            layout[randomIntX][randomIntY]=valid;
                            obstacleWall--;
                        }
                }
                else if(obstacleRandom!=0){
                    int randomObstacleInt = random.nextInt(5,7);
                    layout[randomIntX][randomIntY]=randomObstacleInt;
                    obstacleRandom--;
                }
            }
        }
        return layout;
    }
    public int validLocation(int row, int col){
        Random rand = new Random();
        int r = rand.nextInt(1,5);
        switch (r) {
            case 1:
                if(layout[row-1][col]!=43){
                    return 41;
                }
                else if(layout[row][col+1]!=44){
                    return 42;
                }
                else if(layout[row+1][col]!=41){
                    return 43;
                }
                else if(layout[row][col-1]!=42){
                    return 44;
                }
            return -1;
            case 2:
                if(layout[row][col+1]!=44){
                    return 42;
                }
                else if(layout[row+1][col]!=41){
                    return 43;
                }
                else if(layout[row][col-1]!=42){
                    return 44;
                }
                else if(layout[row-1][col]!=43){
                    return 41;
                }
            return -1;
            case 3:
                if(layout[row+1][col]!=41){
                    return 43;
                }
                else if(layout[row][col-1]!=42){
                    return 44;
                }
                else if(layout[row-1][col]!=43){
                    return 41;
                }
                else if(layout[row][col+1]!=44){
                    return 42;
                }
            return -1;
            case 4:
                if(layout[row][col-1]!=42){
                    return 44;
                }
                else if(layout[row-1][col]!=43){
                    return 41;
                }
                else if(layout[row][col+1]!=44){
                    return 42;
                }
                else if(layout[row+1][col]!=41){
                    return 43;
                }
            return -1;
        }
        if(layout[row-1][col]!=43){
            return 41;
        }
        else if(layout[row][col+1]!=44){
            return 42;
        }
        else if(layout[row+1][col]!=41){
            return 43;
        }
        else if(layout[row][col-1]!=42){
            return 44;
        }
        return -1;
    }
}

class DrawEnterance extends Doors{
    private int mX;
    private int mY;
    
        public DrawEnterance(int x,int y){
            this.mX=x;
            this.mY=y;
        }
        public void DrawDoor(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.green);
            g2D.fillRect(mX+2,mY+3,16,14);
            g2D.setColor(Color.black);
            g2D.setFont(new Font("Times New Roman", Font.BOLD,12));
            g2D.drawString("Enter >>", mX-48, mY+13);
        }
}
class DrawExit extends Doors {
    private int mX;
    private int mY;
    
        public DrawExit(int x,int y){
            this.mX=x;
            this.mY=y;
        }
    
        public void DrawDoor(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.CYAN);
            g2D.fillRect(mX+2,mY+3,16,14);
            g2D.setColor(Color.black);
            g2D.setFont(new Font("Times New Roman", Font.BOLD,14));
            g2D.drawString("<< Exit", mX+22, mY+13);
        }
}

class DrawOuterWall extends Obstacles{
    private int mX;
    private int mY;
    
        public DrawOuterWall(int x,int y){
            this.mX=x;
            this.mY=y;
        }

    public void DrawObstacles(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(139,69,19));
        g2D.fillRect(mX+2,mY+3,16,14);
        g2D.setColor(Color.black);
        g2D.setFont(new Font("Times New Roman", Font.BOLD,18));
        g2D.drawString("\\/", mX+5, mY+16);
    }
}
class DrawWallVerticle extends Obstacles {
    private int mX;
    private int mY;
    
        public DrawWallVerticle(int x,int y){
            this.mX=x;
            this.mY=y;
        }
    
        public void DrawObstacles(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.red);
            g2D.fillRect(mX+2,mY+3,4,16);
            g2D.setColor(Color.black);
            g2D.setFont(new Font("Times New Roman", Font.BOLD,20));
            //g2D.drawString("|", mX+4, mY+16);
        }

}
class DrawWallHorizontal extends Obstacles {
    private int mX;
    private int mY;
    
        public DrawWallHorizontal(int x,int y){
            this.mX=x;
            this.mY=y;
        }
    
        public void DrawObstacles(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.red);
            g2D.fillRect(mX+2,mY+3,16,3);
            g2D.setColor(Color.black);
            g2D.setFont(new Font("Times New Roman", Font.BOLD,20));
            //g2D.drawString("-", mX+4, mY+16);
        }

}
class DrawMine extends Obstacles {
    private int mX;
    private int mY;
    
        public DrawMine(int x,int y){
            this.mX=x;
            this.mY=y;
        }
    
        public void DrawObstacles(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.gray);
            g2D.fillOval(mX+2,mY+3,16,14);
            g2D.setColor(Color.red);
            g2D.fillOval(mX+7, mY+7, 6, 6);
            g2D.setColor(Color.black);
            g2D.drawOval(mX+2, mY+3, 16, 14);

        }

}
class DrawSpike extends Obstacles {
    private int mX;
    private int mY;
    
        public DrawSpike(int x,int y){
            this.mX=x;
            this.mY=y;
        }
    
        public void DrawObstacles(Graphics g) {
            
            // converts to graphics 2d, easier to work with
            int[] xcord = {mX+2, mX+10, mX+18};
            int[] ycord = {mY+14, mY+4, mY+14};
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.red);
            g2D.fillPolygon(xcord, ycord, 3);
            g2D.setColor(Color.black);

        }
}

class DrawTree extends Collectables{
    private int mX;
    private int mY;
    public DrawTree(int x, int y){
        this.mX=x;
        this.mY=y;
    }
    public void DrawCollectable(Graphics g){
        // converts to graphics 2d, easier to work with
        Graphics2D g2D = (Graphics2D) g;
        // need to get x y cords
        g2D.setColor(Color.orange);
        g2D.fillRect(mX+5, mY+9, 8, 10);
        g2D.setColor(Color.green);
        g2D.fillOval(mX+2, mY, 14, 14);
        g2D.setColor(Color.black);
    }
}
class DrawCoin extends Collectables{
    private int mX;
    private int mY;
    
    public DrawCoin(int x, int y){
            this.mX=x;
            this.mY=y;
        }
    public void DrawCollectable(Graphics g){

        // converts to graphics 2d, easier to work with
        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.yellow);

        // draws a coin with basic outline, need to give x y coords
        g2D.fillOval(mX+3, mY+1, 12, 18);
        g2D.setColor(Color.black);
        g2D.drawOval(mX+3,mY+1,12,18);
        // sets font for dollar sign
        g2D.setFont(new Font("Times New Roman", Font.BOLD,18));
        // draws string, need to replace hardcode nums with x y params + 1 + 18
        // to have sign in middle of all coins
        g2D.drawString("$", mX+4, mY+16);
    }
}
class DrawTreasure extends Collectables{
    private int mX;
    private int mY;

    public DrawTreasure(int x,int y){
        this.mX=x;
        this.mY=y;
    }

    public void DrawCollectable(Graphics g){
        
        // converts to graphics 2d, easier to work with
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.black);
        g2D.fillRect(mX+2,mY+4,15,11);
        g2D.setColor(Color.orange);
        g2D.fillArc(mX+2,mY+2,15,5,0,180);
        g2D.fillRect(mX+6,mY+4,6,11);
        g2D.setColor(Color.black);
    }
}

class BoardGame extends JPanel{
    private int [][] mLayout = new int[32][32];

    public BoardGame(int[][] layout){
        for(int i=0;i<32;i++){
            for(int j=0;j<32;j++){
                this.mLayout[i][j]=layout[i][j];
            }
        }
    }

    public void paintComponent(Graphics g){
        //20 x 20 board Size
        //starting x = 50 maximum x = 690
        //starting y = 20 maximum y = 660
        super.paintComponent(g);
        
        //initializes the begining pixels for each square
        int [] x ={50, 50, 70, 70, 50};
        int [] y ={20, 40, 40, 20, 20};
        //not yet implemented to store positional coord for middle of each square
        //int [][] gridAlignment = new int[32][32];
        
        //length and width of each square
        int posDisplacement=20;
        //used to return x to its starting position on left side of grid
        int returnInitialPos=posDisplacement*32;

        //loops for all 512 squares
        for(int j =0;j<512;j++){
            //at the end of the rows it shifts back to the left and down a square
            if((j!=0)&&(j%16==0)){
                for(int i=0;i<y.length;i++){
                    y[i]+=posDisplacement;
                    x[i]-=returnInitialPos;
                }
            }
            //initializes and draws the first polygon square
            Polygon poly = new Polygon(x,y,5);
            g.drawPolygon(poly);

            //adds increment for next starting x position
            for(int i=0;i<x.length;i++){
                x[i]+=posDisplacement;
            }
            //draws next square
            g.drawPolyline(x,y,5);
            for(int i=0;i<x.length;i++){
                //increments for next starting x position
                x[i]+=posDisplacement;
            }
            
           for(int l = 0;l<32;l++){
                for(int k = 0;k<32;k++){
                    if(((l==0)||(k==0)||(k==31)||(l==31))&&(mLayout[l][k]==0)){
                        if(l==0&&k==0){
                            DrawOuterWall outerWall = new DrawOuterWall(42+(l*20),12+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(l==31&k==0){
                            DrawOuterWall outerWall = new DrawOuterWall(58+(l*20),12+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(l==31&&k==31){
                            DrawOuterWall outerWall = new DrawOuterWall(58+(l*20),28+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(l==0&&k==31){
                            DrawOuterWall outerWall = new DrawOuterWall(42+(l*20),28+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(l>=0&&k==0){
                            DrawOuterWall outerWall = new DrawOuterWall(50+(l*20),10+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(k>=0&&l==31){
                            DrawOuterWall outerWall = new DrawOuterWall(60+(l*20),20+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(l>=0&&k==31){
                             DrawOuterWall outerWall = new DrawOuterWall(50+(l*20),30+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        else if(k>=0&&l==0){
                            DrawOuterWall outerWall = new DrawOuterWall(40+(l*20),20+(k*20));
                            outerWall.DrawObstacles(g);
                        }
                        
                    }
                    switch (mLayout[l][k]) {
                        case 1:
                            DrawTreasure treasure = new DrawTreasure(50+(l*20),20+(k*20));
                            treasure.DrawCollectable(g);
                        break;
                        case 2:
                            DrawCoin coin = new DrawCoin(50+(l*20),20+(k*20));
                            coin.DrawCollectable(g);
                        break;
                        case 3:
                            DrawTree tree = new DrawTree(50+(l*20),20+(k*20));
                            tree.DrawCollectable(g);
                        break;
                        case 41:
                            DrawWallHorizontal northWall = new DrawWallHorizontal(50+(l*20),15+(k*20));
                            northWall.DrawObstacles(g);
                        break;
                        case 42:
                            DrawWallVerticle eastWall = new DrawWallVerticle(66+(l*20),19+(k*20));
                            eastWall.DrawObstacles(g);
                        break;
                        case 43:
                            DrawWallHorizontal southWall = new DrawWallHorizontal(50+(l*20),35+(k*20));
                            southWall.DrawObstacles(g);
                        break;
                        case 44:
                            DrawWallVerticle westWall = new DrawWallVerticle(47+(l*20),19+(k*20));
                            westWall.DrawObstacles(g);
                        break;
                        case 5:
                            DrawMine mine = new DrawMine(50+(l*20),20+(k*20));
                            mine.DrawObstacles(g);
                        break;
                        case 6:
                            DrawSpike spike = new DrawSpike(50+(l*20),20+(k*20));
                            spike.DrawObstacles(g);
                        break;
                        case 7:
                            DrawEnterance enterance = new DrawEnterance(50+(l*20),20+(k*20));
                            enterance.DrawDoor(g);

                        break;
                        case 8:
                            DrawExit exit = new DrawExit(50+(l*20),20+(k*20));
                            exit.DrawDoor(g);
                        break;
                    }
                }
            }
        }  
    }
}

class GameMenu extends JFrame{
    public JButton generate = null;
    public JButton reload = null;
    public Color backgroundColor = Color.LIGHT_GRAY;
    public JPanel menu = null;
    int [][] newLayout = new int[32][32];

    public GameMenu(){
        //gives title to the menu frame
        super("Main Menu");
        
        menu = new JPanel();
        menu.setBackground(backgroundColor);
        //adds button to generate new game board
        generate = new JButton("Generate New Board");
        //adds button to reload previous game board
        reload = new JButton("Reload Previous Board");
        //Actions taken upon selecting "Generate New Board"
        generate.addActionListener(
            new ActionListener() {
                @Override
                //Creates game board.
                public void actionPerformed(ActionEvent e) {
                        //create 2d randomzied array...
                        //containing all obstacles and collectable placements...
                        //call randomizer
                    
                    Randomizer rand = new Randomizer();
                    newLayout = rand.generateRandomLayout();

                    JFrame gameBoard = new JFrame("New Board Game");
                    //this button does not work yet.
                    //will implement serialization for storing file.
                    JButton saveBoard = new JButton("Save Board");

                    gameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


                    BoardGame board = new BoardGame(newLayout);       
                    gameBoard.add(board);
                    
                    
                    gameBoard.setSize(740,760);
                    gameBoard.setVisible(true);
                    gameBoard.setResizable(false);
                    gameBoard.setLocation(400,0);

                    gameBoard.add(saveBoard,BorderLayout.SOUTH);
                    setVisible(false);
                    
                    saveBoard.addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e){
                                SaveBoard newBoard = new SaveBoard();
                                newBoard.openFile();
                                newBoard.addRecod(newLayout);
                                newBoard.closeFile();
                                gameBoard.setVisible(false);
                                setVisible(true);
                            }
                        }
                    );
                }
            }
        );
        reload.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    JFrame gameBoard = new JFrame("Previous Board Game");
                    
                    JButton gameMenu = new JButton("Return to menu");

                    gameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    SaveBoard newBoard = new SaveBoard();
                    newBoard = newBoard.fileIn();
                    for(int i=0;i<32;i++){
                        for(int j=0;j<32;j++){
                            newLayout[j][i]=newBoard.returnValue(j, i);
                        }
                    }

                    BoardGame board = new BoardGame(newLayout);       
                    gameBoard.add(board);
                    
                    
                    gameBoard.setSize(740,760);
                    gameBoard.setVisible(true);
                    gameBoard.setLocation(400,0);
                    gameBoard.setResizable(false);
                    gameBoard.add(gameMenu,BorderLayout.SOUTH);

                    setVisible(false);
                    gameMenu.addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e){
                                GameMenu newGameMenu = new GameMenu();
                                newGameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                gameBoard.setVisible(false);
                                gameBoard.setLocation(400,0);
                            }
                        }
                    );

                }
            }
        );
        //location of buttons and size of panel
        add(generate,BorderLayout.NORTH);
        add(reload,BorderLayout.SOUTH);
        setSize(240,90);
        setResizable(false);
        setLocation(550,100);
        setVisible(true);
    }
}
public class BoardGameGUI{
        public static void main(String args[]){
            //creates menu popup, allows user to generate board or selct from a old save
            GameMenu newGameMenu = new GameMenu();
            newGameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         
    }
}

 class SaveBoard implements Serializable{
    public int[][] mSavedLayout = new int [32][32];

    public static ObjectOutputStream output;

    public int returnValue(int x, int y){
        return mSavedLayout[x][y];
    }
    public static void openFile(){
        try{
            output = new ObjectOutputStream(
                new FileOutputStream("board.ser"));
        } catch (IOException e){
            System.out.println("Unable to locate file!");
        }
    }

    public static void addRecod(int [][] layout){
        SaveBoard board = new SaveBoard();
        for(int i=0;i<32;i++){
            for(int j=0;j<32;j++){
                board.mSavedLayout[j][i]=layout[j][i];
            }
        }
        try{
            output.writeObject(board);
        } catch (IOException ex){
            System.out.println("Error writing object.");
        }
    }
    public static void closeFile(){
        if(output!=null){
            try{
                output.close();
            } catch (IOException ex){
                System.out.println("Error closing file.");
            }
        }
    }
    public static SaveBoard fileIn(){
        try{
            File file = new File("board.ser");
            ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(file));
            SaveBoard savedBoard = (SaveBoard) in.readObject();
            in.close();
            return savedBoard;

        } catch (IOException e){
            System.out.println("No more items to add.");
        } catch (ClassNotFoundException ex){
            System.out.println("Invalid object ty!pe");
        }
        return null;
    }
 }