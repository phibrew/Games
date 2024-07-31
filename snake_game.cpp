#include <bits/stdc++.h>
#include <unistd.h>
#include <termios.h>
#include <sys/ioctl.h>
using namespace std;

#define Max_Length 1000

const char Dir_Up = 'U';
const char Dir_Down = 'D';
const char Dir_Left = 'L';
const char Dir_Right = 'R';

int consoleWidth, consoleHeight;

void initScreen(){
    struct winsize w;
    ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
    consoleHeight = w.ws_row;
    consoleWidth = w.ws_col;
}

struct Point{
    int xCord;
    int yCord;
    Point(){}
    Point (int x, int y){
        xCord = x;
        yCord = y;        
    }
};

class Snake{
private:
    int length;
    char direc;
public:
    Point body[Max_Length];
    Snake(int x, int y){
        length = 1;
        body[0] = Point(x,y);
        direc = Dir_Right;
    }    

    int getLength(){
        return length;
    }

    void changeDirection(char newDirec){
        if(newDirec == Dir_Up && direc != Dir_Down){
            direc = newDirec;
        }
        else if(newDirec == Dir_Down && direc != Dir_Up){
            direc = newDirec;
        }
        else if(newDirec == Dir_Left && direc != Dir_Right){
            direc = newDirec;
        }
        else if(newDirec == Dir_Right && direc != Dir_Left){
            direc = newDirec;
        }
    }
    bool move(Point food){
        for(int i= length-1; i>0; --i){
            body[i] = body[i-1];
        }
        switch(direc){
            int val;
            case Dir_Up:
                val = body[0].yCord;
                body[0].yCord = val-1;
                break;
            case Dir_Down:
                val = body[0].yCord;
                body[0].yCord = val+1;
                break;
            case Dir_Left:
                val = body[0].xCord;
                body[0].xCord = val-1;
                break;
            case Dir_Right:
                val = body[0].xCord;
                body[0].xCord = val+1;
                break;
        }

        for(int i{1}; i<length; i++){
            if(body[0].xCord == body[i].xCord && body[0].yCord == body[i].yCord)
                return false;
        }    

        if(food.xCord == body[0].xCord && food.yCord == body[0].yCord){
            body[length] = Point(body[length-1].xCord, body[length-1].yCord);
            length++;
        }

        return true;
    }
};

class Board {
private:
    Snake *snake;
    const char snakeBody = '0';
    Point food;
    const char Food = '0';
    int score;
public:
    Board(){
        spawnFood();
        snake = new Snake(10, 10);
        score = 0;
    }
    ~Board(){
        delete snake;
    }

    int getScore(){
        return score;
    }
    void spawnFood(){
        int x = rand()%consoleWidth;
        int y = rand()%consoleHeight;    
        food = Point(x, y);
    }

    void displayCurrentScore(){
        gotoxy(consoleWidth/2, 0);
        cout<<"Current Score: "<<score;
    }
    void gotoxy(int x, int y){
        printf("\033[%d;%dH", y+1, x+1);
    }
    
    void draw(){
        system("clear");
        for(int i{}; i<snake->getLength(); i++){
            gotoxy(snake->body[i].xCord, snake->body[i].yCord);
            cout<<snakeBody;
        }
        gotoxy(food.xCord, food.yCord);
        cout<<Food;
    }

    bool update(){
        bool isAlive = snake->move(food);
        if(isAlive == false){
            return false;
        }
        if(food.xCord == snake->body[0].xCord && food.yCord == snake->body[0].yCord){
            score++;
            spawnFood();
        }
        return true;
    }
    void getInput(){
        struct termios oldt, newt;
        int ch;
        tcgetattr( STDIN_FILENO, &oldt );
        newt = oldt;
        newt.c_lflag &= ~( ICANON | ECHO );
        tcsetattr( STDIN_FILENO, TCSANOW, &newt );
        ch = getchar();
        tcsetattr( STDIN_FILENO, TCSANOW, &oldt );
        
        if(ch=='w' || ch=='W'){
            snake->changeDirection(Dir_Up);
        }
        else if(ch=='a' || ch=='A'){
            snake->changeDirection(Dir_Left);
        }
        else if(ch=='s' || ch=='S'){
            snake->changeDirection(Dir_Down);
        }
        else if(ch=='d' || ch=='D'){
            snake->changeDirection(Dir_Right);
        }
    }
};

int main(){
    srand(time(0));
    initScreen();
    Board *board = new Board();
    while(board->update()){
        board->getInput();
        board->draw();
        usleep(100000);
    }
    cout<<"Game Over!!!"<<endl;
    cout<<"Final Score is: "<<board->getScore();
    return 0;
}
