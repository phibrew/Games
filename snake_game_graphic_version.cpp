#include <SFML/Graphics.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>
#include <iostream>
#include <vector>

using namespace std;

#define Max_Length 1000

const char Dir_Up = 'U';
const char Dir_Down = 'D';
const char Dir_Left = 'L';
const char Dir_Right = 'R';

struct Point {
    sf::Vector2i position;
    Point() {}
    Point(int x, int y) {
        position.x = x;
        position.y = y;
    }
};

class Snake {
private:
    int length;
    char direc;
public:
    vector<Point> body;
    Snake(int x, int y) {
        length = 1;
        body.push_back(Point(x, y));
        direc = Dir_Right;
    }

    int getLength() {
        return length;
    }

    void changeDirection(char newDirec) {
        if (newDirec == Dir_Up && direc != Dir_Down) {
            direc = newDirec;
        }
        else if (newDirec == Dir_Down && direc != Dir_Up) {
            direc = newDirec;
        }
        else if (newDirec == Dir_Left && direc != Dir_Right) {
            direc = newDirec;
        }
        else if (newDirec == Dir_Right && direc != Dir_Left) {
            direc = newDirec;
        }
    }
    bool move(Point food) {
        for (int i = length - 1; i > 0; --i) {
            body[i] = body[i - 1];
        }
        switch (direc) {
        case Dir_Up:
            body[0].position.y -= 1;
            break;
        case Dir_Down:
            body[0].position.y += 1;
            break;
        case Dir_Left:
            body[0].position.x -= 1;
            break;
        case Dir_Right:
            body[0].position.x += 1;
            break;
        }

        for (int i = 1; i < length; i++) {
            if (body[0].position == body[i].position)
                return false;
        }

        if (food.position == body[0].position) {
            body.push_back(Point(body[length - 1].position.x, body[length - 1].position.y));
            length++;
        }

        return true;
    }
};

class Board {
private:
    Snake* snake;
    const char snakeBody = ':';
    Point food;
    const char Food = '0';
    int score;
    int consoleWidth;
    int consoleHeight;
    sf::RenderWindow window;
    sf::RectangleShape snakeShape;
    sf::RectangleShape foodShape;
public:
    Board(int width, int height) : consoleWidth(width), consoleHeight(height), window(sf::VideoMode(width * 10, height * 10), "Snake Game") {
        spawnFood();
        snake = new Snake(width / 2, height / 2);
        score = 0;
        snakeShape.setSize(sf::Vector2f(10.f, 10.f));
        foodShape.setSize(sf::Vector2f(10.f, 10.f));
        foodShape.setFillColor(sf::Color::Red);
    }
    ~Board() {
        delete snake;
    }

    int getScore() {
        return score;
    }
    void spawnFood() {
        int x = rand() % consoleWidth;
        int y = rand() % consoleHeight;
        food = Point(x, y);
    }

    void displayCurrentScore() {
        cout << "Current Score: " << score << endl;
    }

    void draw() {
        window.clear();
        for (int i = 0; i < snake->getLength(); i++) {
            snakeShape.setPosition(snake->body[i].position.x * 10, snake->body[i].position.y * 10);
            window.draw(snakeShape);
        }
        foodShape.setPosition(food.position.x * 10, food.position.y * 10);
        window.draw(foodShape);
        window.display();
    }

    bool update() {
        bool isAlive = snake->move(food);
        if (isAlive == false) {
            return false;
        }
        if (food.position == snake->body[0].position) {
            score++;
            spawnFood();
        }
        return true;
    }

    void getInput() {
        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed)
                window.close();
            if (event.type == sf::Event::KeyPressed) {
                if (event.key.code == sf::Keyboard::W) {
                    snake->changeDirection(Dir_Up);
                }
                else if (event.key.code == sf::Keyboard::A) {
                    snake->changeDirection(Dir_Left);
                }
                else if (event.key.code == sf::Keyboard::S) {
                    snake->changeDirection(Dir_Down);
                }
                else if (event.key.code == sf::Keyboard::D) {
                    snake->changeDirection(Dir_Right);
                }
            }
        }
    }

    bool isWindowOpen() {
        return window.isOpen();
    }
};

int main() {
    srand(time(0));
    int consoleWidth = 800;
    int consoleHeight = 600;
    Board* board = new Board(consoleWidth / 10, consoleHeight / 10);
    while (board->isWindowOpen() && board->update()) {
        board->getInput();
        board->draw();
        sf::sleep(sf::milliseconds(100));
    }
    cout << "Game Over!!!" << endl;
    cout << "Final Score is: " << board->getScore() << endl;
    delete board;
    return 0;
}

