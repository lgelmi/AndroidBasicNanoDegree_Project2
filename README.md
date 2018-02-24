# AndroidBasicNanoDegree_Project2
FightKeeper - An almost game

This project target should be a simple game score counter, as the CourtCounter realized along the course. 
Since this didn't inspired me that much, I preferred to try mimiking an actual game. 
The "Score" counter is represented in this case by the players Health Points.

The main features are:
* Actions can be performed in turns. The first player is choosen randomly at new game. The other has his side disabled.
* Random Fighters selection at game start. Each has its own stats and image. 
  (In an actual game I would also have added different names and sound for each action...)
* Random background selection. I couldn't find many background fitting the application though.
* Each fighter is a subclass of the Fighter class.
* Rotation is prevented: landscape orientation posed a big threat on the layout design stability and from a gameplay point of view made no sense.

Brief rules:
* An attack action will be actually completed once the opponent decide his move.
* A defence action can shield from an attack both before and after the attack is actually performed, leaving a bit more space for tactics.
* Focus allow to improve all other actions efficency. Once used its bonus is completely depleted.
* Healing takes turn to recharge once used, depending on the fighter class.

I'm not completely satisfied by the interface itself, but I'm not sure how to improve it without any designing experience (when in doubt, I prefer to keep it simple).

**N.B.:** I tried using as much free sources (images and sounds) as possible, and adding credits in the code. Still, some 
source are protected by copyright and are viable only for an app demo.  
