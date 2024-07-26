#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

int random(int min, int max){
	bool isFirst = true;
	if(isFirst){
		srand(static_cast<unsigned int>(time(0)));
	}
	return min+rand() % (max-min+1);
}

int dice(){
	return random(1,6);
}

int main(){
	int ladder[] = {1, 20, 21, 38, 50, 90};
	int snake[] = {10, 12, 22, 34, 55, 60, 77, 89, 99};
	int board = 100;
	int maxPlayer = 2;
	bool gameOver = false;
	int current[maxPlayer]={0};
	int player = 0;

	while(!gameOver){
		system("cls");
		cout<<"Snake and Ladder Game!!"<<endl;
		cout<<"Current Player "<< player+1 <<endl;
		cout<<"=================="<<endl;
		for(int i{}; i<maxPlayer; ++i){
			cout<<"Player "<<i+1<<" is at position: "<<current[i]<<endl;
		}
		cout<<"=================="<<endl;

		cout<<"Player "<<player+1<<" rolled a "<<dice()<<endl;
		current[player] += dice();
		if(current[player]>=board){
			cout<<"Player "<<player+1<<" wins!"<<endl;
			gameOver = true;
		}

		for(int i{}; i<sizeof(ladder)/sizeof(ladder[0]); ++i){
			if(current[player] == ladder[i]){
				cout<<"Player "<<player+1<<" climbed a ladder "<<dice()<<endl;
				current[player] += dice();
			}
		}

		for(int i{}; i<sizeof(snake)/sizeof(snake[0]); ++i){
			if(current[player] == snake[i]){
				cout<<"Player "<<player+1<<" is bitten by snake "<<dice()<<endl;
				current[player] -= 2*dice();
			}
		}	

		player = (player+1)%maxPlayer;
		system("pause");	
	}	
}