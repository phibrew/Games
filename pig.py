from random import randint
import turtle

player1 = input("Enter the name of player0: ")
player2 = input("Enter the name of player1: ")
maxscore = int(input("Enter the score to win: "))

scorescards = [0,0]
score = 0
player = 0

dot_positions = [[(0, 0, 'red'), (-100, 100, 'white'), (-100, 0, 'white'), (-100, -100, 'white'), (100, 100, 'white'), 
                  (100, 0, 'white'), (100, -100, 'white')], 
  
                 [(0, 0, 'white'), (-100, 100, 'red'), (-100, 0, 'white'), (-100, -100, 'white'), (100, 100, 'white'), (100, 0, 'white'), 
                  (100, -100, 'red')], 
  
                 [(0, 0, 'red'), (-100, 100, 'red'), (-100, 0, 'white'), (-100, -100, 'white'), (100, 100, 'white'), (100, 0, 'white'), 
                  (100, -100, 'red')], 
  
                 [(0, 0, 'white'), (-100, 100, 'red'), (-100, 0, 'white'), (-100, -100, 'red'), (100, 100, 'red'), (100, 0, 'white'), (100, -
                                                                                                                                       100, 'red')], 
  
                 [(0, 0, 'red'), (-100, 100, 'red'), (-100, 0, 'white'), (-100, -100, 'red'), (100, 100, 'red'), (100, 0, 'white'), (100, -
                                                                                                                                     100, 'red')], 
  
                 [(0, 0, 'white'), (-100, 100, 'red'), (-100, 0, 'red'), (-100, -100, 'red'), (100, 100, 'red'), (100, 0, 'red'), (100, -
                                                                                                                                   100, 'red')]] 

def draw_dice(num):
	for i in range(7):
		dot[i].shape('circle')
		dot[i].color(dot_positions[num-1][i][2])
		dot[i].penup()
		dot[i].goto(dot_positions[num-1][i][0], dot_positions[num-1][i][1])
		dot[i].dot()


d = turtle.Screen()
d.setup(width=300, height = 300)
d.bgcolor('white')
d.title('Dice play')
d.tracer(0)

dot = [turtle.Turtle() for _ in range(7)]

while max(scorescards) < maxscore:
	rolling = input('Player %i: (%i, %i) Rolling? (Y) '
		%(player, scorescards[player], score)).strip().lower() in {'yes', 'y', ''}
	
	if rolling:
		rolled = randint(1,6)

		turtle.clear()
		draw_dice(rolled)

		print('Rolled %i' %rolled)
		if rolled == 1:
			print('Bust! you lose %i, your score remains %i.'
			 %(score, scorescards[player]))
			score, player = 0, (player^1)

		else:
			score += rolled

	else:
		scorescards[player] += score
		print('Your updated score is %i' %scorescards[player])
		if scorescards[player]>=maxscore:
			break;
		score, player = 0, (player^1)


print('\nPlayer %i wins with a score of %i.' %(player, scorescards[player]))
turtle.done()