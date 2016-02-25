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
    
    
