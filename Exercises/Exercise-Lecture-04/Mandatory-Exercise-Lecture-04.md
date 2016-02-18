# Mandatory Exercise Lecture 04 - Tic-Tac-Toe
Assume that MAX and MIN play with circle and cross tokens, respectively. The game is played using a coin
with 50% chance of landing on each side. The rules are as follows:
1. Initially the board is empty.

2. A player starts his turn by flipping the coin.

3. If the coin shows heads, the player can place a token on positions marked H, otherwise the player must place a token on the position marked T.

4. The game terminates when:

    a) a player is unable to place a token, because there are no empty positions left with the right mark, or
    
    b) a player wins the game.
    
5. A player wins the game, if the player has two tokens on any of the 5 pair of positions connected by a dashed line.

6. The utility of the game is 1if MAX wins, ‐1 if Min wins, and 0 otherwise.  

## Task 1
Function: MiniMax-Descision is the same

    function MiniMax-Descision(state) return action¨
        Chance-Max(state)

    function Max-Value(state) return a utility value
        if Terminal-Test(state) then return Utility(state)
        
        v <- inf*-1
        foreach a in Action(state) do
            v <- Max(v,Chance-Min(Result(state,a))
        return v

    function Min-Value(state) return a utility value
        if Terminal-Test(state) then return Utility(state)
        v <- inf
        foreach a in Action(state) do
            v <- Min(v,Chance-Max(Result(state,a))
        return v

    function Chance-Min(state) 
        return (Min-Value(Result(state, head)), Min-Value(Result(state, tails)))*0.5

    function Chance-Max(state) 
        return (Max-Value(Result(state, head)), Max-Value(Result(state, tails)))*0.5

## Task 2
