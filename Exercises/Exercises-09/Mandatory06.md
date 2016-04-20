# Mandatory Mandatory Exercise 6 
**Anders Wind - awis@itu.dk**

    X={X1, X2, X3, X4, X5}
    
    D={{1,2,3},{5,7,8},{1,2,4},{0,2,4},{1,2}}
    
    C={  Cx1x2={(1,7),(2,8)},
            Cx1x5={(1,2),(2,1)},
            Cx2x3={(8,4),(7,2)},
            Cx2x4={(8,0),(7,4)}}

## Task 1
Reduce the domains of the variables of W such that W becomes arc consistent.

First i reduce the first domain: X1{1,2} and X2{7,8} ,since constrant C<sub>x1,x2</sub> have no value where X1 can be 3 and X2 5.
Then I reduce X3{2,4} since C<sub>x2,x3</sub> states no value where X3 can be 1.
Then I reduce X4{0,4} since C<sub>x2,x4</sub> states no value where X4 can be 2.

So the situation is now:

    X={X1, X2, X3, X4, X5}
    
    D={{1,2},{7,8},{2,4},{0,4},{1,2}}
    
    C={  Cx1x2={(1,7),(2,8)},
            Cx1x5={(1,2),(2,1)},
            Cx2x3={(8,4),(7,2)},
            Cx2x4={(8,0),(7,4)}}


## Task 2
Write a solution to W.

With the reduced domains I can now conclude that variables 1 and 2 must be in X1 and X5 since they can only be those two variables.
Therefore we can remove 2 from X3. this makes X3{4}

Now we can remove 4 from any other domain:
    D={{1,2},{7,8},{4},{0},{1,2}}
    
Which leaves X4 to be 0. 
With this value we can see that the constraints from X2 to both x3 and x4 holds if X2 is 8:
    D={{1,2},{8},{4},{0},{1,2}}

Now we can see that to hold the constrain C<sub>x1,x2</sub> X1 must be 2 and then to hold C<sub>x1,x5</sub> X5 must be 1:
    D={{2},{8},{4},{0},{1}}
    
Therefore: D={{2},{8},{4},{0},{1}} is a solution to W.

## Task 3
Describe in words a polynomial time algorithm that can find a solution to an arbitrary binary CSP
where the constraint graph forms a tree.

foreach domain in D
    foreach constraint of domain
        remove variable from domain if not in constraint

       

