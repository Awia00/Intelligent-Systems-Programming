# Exercise Lecture 05
## Exercise 1
1. If the sun shines then it is true that I get wet if it rains.
2. If the sun shines then it is summer.
3. It is not summer
4. Therefore, I get wet if the sun shines.

### a)

1)

    the sun shines:     ss
    i get wet:          w
    it rains:           r

2)

    the sun shines:     ss
    it is summer:       s

3)

    it is summer: s
    
4)

    I get wet: w
    the sun shines: ss
    
    
### b)
1)

    ss => (r => w)

2)
    
    ss => s
    
3)

    !s
    
4)

    ss => w
    
    
### c)

First i simplify 4

    ss => w
    <=> (by implication elimination)
    !ss || w
    
First i simplify the conjunction

    (ss => (r => w)) && (ss => s) && (!s) 
    <=> (by 3x implication elimination)
    (!ss || (!r || w)) && !ss || s && (!s)
    <=> (by removing parenthisis)
    !ss || !r || w && !ss || s && !s
    <=> (by (s&&!s <=> false), by (x||false <=> x))
    !ss || !r || w && !ss
    <=> (by communitative of &&)
    !ss && (!ss || (!r || w))
    <=> (by distributivity of && over ||)
    (!ss && !ss) || (!ss && (!r || w))
    <=> (by x&&x <=> x)
    !ss || (!ss && (!r || w))¨
    <=> (by distributivity of && over ||)
    !ss || ((!ss && !r) || (!ss && w))
    <=> (by communitative of ||)
    !ss || (!ss && !r) || (!ss && w)
    
    

## Exercise 2

### a)
    
    Smoke ⇒ Smoke
    <=>
    ¬smoke ∨ smoke
    
So that means that it is valid.

### b)

|            	| Smoke False 	| Smoke True 	|
|------------	|-------------	|------------	|
| Fire False 	| T           	| T          	|
| Fire True  	| F           	| T          	|

So it is satisfiable since there is both true and false depending on the model.


### c)

first simplify
    
    (Smoke ⇒ Fire) ⇒ (¬Smoke ⇒ ¬Fire)
    <=> by implication elimination
    ¬(Smoke ⇒ Fire) ∨ (¬Smoke ⇒ ¬Fire)
    <=> by implication elimination
    ¬(¬Smoke ∨ Fire) ∨ (¬Smoke ⇒ ¬Fire)
    <=> by de Morgan
    (¬(¬Smoke) ∧ ¬Fire) ∨ (¬Smoke ⇒ ¬Fire)
    <=> by double negation elimination
    (Smoke ∧ ¬Fire) ∨ (¬Smoke ⇒ ¬Fire)
    <=> by implication elimination
    (Smoke ∧ ¬Fire) ∨ (¬(¬Smoke) ∨ ¬Fire)
    <=> by double negation elimination
    (Smoke ∧ ¬Fire) ∨ (Smoke ∨ ¬Fire)

Then i do a truth table

| Smoke 	| Fire 	| Smoke ∧ ¬Fire 	| Smoke ∨ ¬Fire 	| (Smoke ∧ ¬Fire) ∨ (Smoke ∨ ¬Fire) 	|
|-------	|------	|---------------	|---------------	|-----------------------------------	|
| T     	| F    	| T             	| T             	| T                                 	|
| T     	| T    	| F             	| T             	| T                                 	|
| F     	| F    	| F             	| T             	| T                                 	|
| F     	| T    	| F             	| F             	| F                                 	|

So it is satisfiable since there is both true and false depending on the model.

### d)

    Smoke ∨ Fire ∨ ¬Fire
    <=>
    Smoke ∨ true
    <=>
    true
    
So this is valid.


### e)