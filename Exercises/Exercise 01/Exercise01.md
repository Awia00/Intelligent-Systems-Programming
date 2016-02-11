# Exercise 01

## Exercise 1

### a)
<img src='http://g.gravizo.com/g?
 digraph G {
   1 -> 2;
   1 -> 3;
   2 -> 4;
   2 -> 5;
   3 -> 6;
   3 -> 7;
   4 -> 8;
   4 -> 9;
   5 -> 10;
   5 -> 11;
   6 -> 12;
   6 -> 13;
   7 -> 14;
   7 -> 15;
 }
'/>
If we imagine that all nodes have to be to the right of nodes with lower value we get the possibility to read the numbers from left to right.

### b)
Limit 3:
1,2,4,8,9,5,10,11

Iterative deepening search. Limit starter på 0
1, 1, 2, 3, 1, 2, 4, 5, 3, 6, 7, 1 ,2, 4, 8, 9, 5, 10, 11

### c)
The branching is 1 since only 1 former state goes to another.

### d)
Since the domain is that numbers go to their double 2n and double+1 2n+1 we can divide the goal and all intermideate steps log g times where g is the goal until we get to the initial state.

Such an algorithm could look like this:
    public Stack trace(int goal, int initial)
    {
        Stack<int> traceList = new Stack<int>();
        int step = goal;
        while(step != intial)
        {
            traceList.Push(step);
            step /= 2;
        }
        return list;
    }
    
    
## Exercise 2

### a)
States:
    A disc's size is represented by an integer.
    The entire state is three stacks one for each peg, where the values in the sets are the discs, and these have to be ordered.
    The intersection between two arbitrary stacks should be the empty set
    Also in the state is a stepCost
    

Actions:
    empty stacks return infinite.
    for all kombinations of n and x
        if ((Stack N).Peek() < (Stack X).Peek())
            ((Stack n).Push (Stack x.pop))
        
    Where Stack N and Stack X are different pegs
    
    So an action is two stacks (stack1, stack2), (stack1, stack3), (stack2, stack1) and so on: (fromSet, toSet)
    Max 6 different actions for a given state: since 3 from stacks which can go to 2 other stacks = 3*2 = 6
    
Resulting:
    Action (sX, sN) = 
        minS = min(a | a is in Sx) / The set containing the minimum element of sX
        sX = sX \ minS // sX set minus minSet
        sN = sN union sMin // minSet put on sN 
        
Goal:
    (Stack1 union Stack 2) is emptySet
  
Path costs:
    step cost is 1
    Path cost = sum of steps
    
### b)

<img src='http://g.gravizo.com/g?
 digraph G {
   1 [label="([2,1],[],[])"];
   2 [label="([],[2,1],[])"];
   3 [label="([],[],[2,1])"];
   4 [label="([1],[2],[])"];
   5 [label="([1],[],[2])"];
   6 [label="([2],[1],[])"];
   7 [label="([],[1],[2])"];
   8 [label="([],[2],[1])"];
   9 [label="([2],[],[1])"];
   1 -> 6[label="Move (S1, S2)"];
   1 -> 9[label="Move (S1, S3)"];
   2 -> 4[label="Move (S2, S1)"];
   2 -> 8[label="Move (S2, S3)"];
   3 -> 5[label="Move (S3, S1)"];
   3 -> 7[label="Move (S3, S2)"];
   4 -> 2[label="Move (S1, S2)"];
   4 -> 8[label="Move (S1, S3)"];
   4 -> 5[label="Move (S2, S3)"];
   5 -> 7[label="Move (S1, S3)"];
   5 -> 3[label="Move (S1, S2)"];
   5 -> 4[label="Move (S3, S2)"];
   6 -> 9[label="Move (S2, S3)"];
   6 -> 1[label="Move (S2, S1)"];
   6 -> 7[label="Move (S1, S3)"];
   7 -> 3[label="Move (S2, S3)"];
   7 -> 6[label="Move (S2, S1)"];
   7 -> 5[label="Move (S3, S1)"];
   8 -> 2[label="Move (S3, S2)"];
   8 -> 4[label="Move (S3, S1)"];
   8 -> 9[label="Move (S2, S1)"];
   9 -> 1[label="Move (S3, S1)"];;
   9 -> 6[label="Move (S3, S2)"];;
   9 -> 8[label="Move (S1, S2)"];;
 }
'/>

### c)
The sie is the amount of pegs to the power of the amount of discs = 3^n


## Exercise 3
### a)

| Iteration | Fringe                                                     |
|-----------|------------------------------------------------------------|
| 0         | <244, 0, 244, L>                                           |
| 1         | <311, 70, 241, M> <440, 111, 329, T>                       |
| 2         | <387, 145, 242, D> <440, 111, 329, T>                      |
| 3         | <425, 265, 160, C> <440, 111, 329, T>                      |
| 4         | <440, 111, 329, T> <503, 403, 100, P> <604, 411, 193, RV>  |
| 5         | <503, 403, 100, P>  <595, 229, 366, A> <604, 411, 193, RV> |
| 6         | <504, 504, 0, B> <595, 229, 366, A> <604, 411, 193, RV>    |

### b)
The path is: L, M, D, C, P, B and has a path cost of 504.

## Exercise 4
### a)
With W being 1 the algorithm only takes the heuristic into consideration since (1-1) = 0 and therefore the g(n) part will always be 0.
Wit W being 0.5 we have a regular astar function since 1-0.5 = 0.5 and therefore g(n) and h(n) both gets multiplied by 0.5 and therefore has equal weight and could be 1 instead.

### b)
for all values below and including 0.5 since the heuristic here is not overvaluated compared to the real evaluation.

## Exercise 5
### a)
nah
### b)
nah