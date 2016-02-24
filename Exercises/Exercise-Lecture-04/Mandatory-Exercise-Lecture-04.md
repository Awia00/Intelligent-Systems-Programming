# Mandatory Exercise Lecture 04 - Tic-Tac-Toe
## Task 1

    function MiniMax-Descision(state) return action
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

Note that this solution only works for the game given in the assignment. We start with a throw

## Task 2
a)
See the attached image "Simple-TTT-GameTree.png" to see the GameTree

b)
On the game tree "Simple-TTT-GameTree.png" the evaluation of a ExpectMiniMax is run and the highest utility, 0.5, is acheived by placing the x in the first coloumn second row. 
So that is the decision for MAX's first move if the coin flipped to HEAD.