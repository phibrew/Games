import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x; 
        int y;
        
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    //snake 
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    int tileSize = 10;
    //JPanel frame
    int boardWidth; 
    int boardHeight;
    int score = 0;

    //Now to make snake move, we need the loading to happen 
    Timer gameLoop;
    int velocityX, velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);// this allows the keyboard clicks...

        //snake body and head
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        //food
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        // Grid
        // for(int i=0; i<boardHeight/tileSize; ++i){
        //     // (x1, y1, x2, y2)
        //     g.drawLine(0, i*tileSize, boardWidth, i*tileSize);//this draws the horizontal lines
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);//this draws the vertical lines
        // }

        // Food
        g.setColor(Color.red);
        // g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        for(int i = 0; i<snakeBody.size(); ++i){
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize-14, tileSize);
        }
        else{
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-14, tileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake Body
        for(int i=snakeBody.size()-1; i>=0; --i){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over condition
        for(int i=0; i<snakeBody.size(); ++i){
            Tile snakePart = snakeBody.get(i);
            //collision
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if(snakeHead.x*tileSize < 0){
            snakeHead.x = boardWidth/tileSize;
        }
        if(snakeHead.x*tileSize > boardWidth){
            snakeHead.x = 0;
        }
        if(snakeHead.y*tileSize < 0) {
            snakeHead.y = boardHeight/tileSize;
        }
        if(snakeHead.y*tileSize > boardHeight){
            snakeHead.y = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }
}
