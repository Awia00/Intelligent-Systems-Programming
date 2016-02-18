# Exercise Lecture 04
## Exercise 1

### a)
Draw the game tree of depth 2
<img src='http://g.gravizo.com/g?
 digraph G {
   1 [label="[A5], [A4]"];
   12 [label="[A3, A4, A5], Ø"];
   13 [label="[A5, B5], [A4]"];
   14 [label="[A5, B4], [A4]"];
   121 [label="[A3, A4, A5], [A2]"];
   122 [label="[A3, A4, A5], [B5]"];
   123 [label="[A3, A4, A5], [B4]"];
   124 [label="[A3, A4, A5], [B3]"];
   131 [label="[A5, B5], [A3, A4]"];
   132 [label="[A5, B5], [A4, B4]"];
   133 [label="[A5, B5], [A4, C5]"];
   141 [label="[A5, B4], [A3, A4]"];
   142 [label="[A5, B4], [A4, B3]"];
   143 [label="[A5, B4], [A4, B5]"];
   144 [label="[A5], [A4, B4, C4]"];
   1 -> 12[label="B takes on A3 and steals A4"];
   1 -> 13[label="B takes on B5"];
   1 -> 14[label="B takes on B4"];
   12 -> 121[label="W takes on A2"];
   12 -> 122[label="W takes on B5"];
   12 -> 123[label="W takes on B4"];
   12 -> 124[label="W takes on B3"];
   13 -> 131[label="W takes on A3"];
   13 -> 132[label="W takes on B4"];
   13 -> 133[label="W takes on C5"];
   14 -> 141[label="W takes on A3"];
   14 -> 142[label="W takes on B3"];
   14 -> 143[label="W takes on B5"];
   14 -> 144[label="W takes on C4 and steals B4"];
 }
'/>

### b)
Find the minimax value of the initial state.

Utility of a state is how many pieces each player has.

<img src='http://g.gravizo.com/g?
 digraph G {
   1 [label="[A5], [A4] MM=3"];
   12 [label="[A3, A4, A5], Ø MM=3"];
   13 [label="[A5, B5], [A4] MM=2"];
   14 [label="[A5, B4], [A4] MM=1"];
   121 [label="[A3, A4, A5], [A2] U=3"];
   122 [label="[A3, A4, A5], [B5] U=3"];
   123 [label="[A3, A4, A5], [B4] U=3"];
   124 [label="[A3, A4, A5], [B3] U=3"];
   131 [label="[A5, B5], [A3, A4] U=2"];
   132 [label="[A5, B5], [A4, B4] U=2"];
   133 [label="[A5, B5], [A4, C5] U=2"];
   141 [label="[A5, B4], [A3, A4] U=2"];
   142 [label="[A5, B4], [A4, B3] U=2"];
   143 [label="[A5, B4], [A4, B5] U=2"];
   144 [label="[A5], [A4, B4, C4] U=1"];
   1 -> 12;
   1 -> 13;
   1 -> 14;
   12 -> 121;
   12 -> 122;
   12 -> 123;
   12 -> 124;
   13 -> 131;
   13 -> 132;
   13 -> 133;
   14 -> 141;
   14 -> 142;
   14 -> 143;
   14 -> 144;
 }
'/>


### c)
The minimax descision of Black is to take A3 (thereby stealing A4) in the first move, since this provides MiniMax value of 3.


## Exercise 2
### a)
By MiniMax evaluation Move C will be the best choice for Max.

### b)

To go over the algorithm 

<img src='http://g.gravizo.com/g?
 digraph G {
   1 [label="A", shape="triangle"];
   11 [label="B", shape="invtriangle"];
   12 [label="C", shape="invtriangle"];
   13 [label="D", shape="invtriangle"];
   111 [label="E", shape="triangle"];
   112 [label="F", shape="triangle"];
   113 [label="G", shape="triangle"];
   1111 [label="L(2)", shape="box"];
   1112 [label="M(3)", shape="box"];
   1121 [label="N(8)", shape="box"];
   1122 [label="O(5)", shape="box"];
   1131 [label="P(7)", shape="box"];
   1132 [label="Q(6)", shape="box"];
   121 [label="H", shape="triangle"];
   122 [label="I", shape="triangle"];
   1211 [label="R(0)", shape="box"];
   1212 [label="S(1)", shape="box"];
   1221 [label="T(5)", shape="box"];
   1222 [label="U(2)", shape="box"];
   131 [label="J", shape="triangle"];
   132 [label="K", shape="triangle"];
   1311 [label="V(8)", shape="box"];
   1312 [label="W(4)", shape="box"];
   1321 [label="X(10)", shape="box"];
   1322 [label="Y(2)", shape="box"];
   1 -> 11[label="a=3 ,b=inf"];
   1 -> 12[label="a=1 ,b=inf"];
   1 -> 13[label="a=inf ,b=inf"];
   11 -> 111[label="a=3 ,b=inf"];
   11 -> 112[label="a=8 ,b=inf"];
   11 -> 113[label="a= ,b="];
   111 -> 1111[label="a=2 ,b=inf"];
   111 -> 1112[label="a=3 ,b=inf"];
   112 -> 1121[label="a=8 ,b=inf"];
   112 -> 1122[label="CUT"];
   113 -> 1131[label="a= ,b="];
   113 -> 1132[label="CUT"];
   12 -> 121[label="a=3 ,b=1"];
   12 -> 122[label="CUT"];
   121 -> 1211[label="a=3 ,b="];
   121 -> 1212[label="a=3 ,b="];
   122 -> 1221;
   122 -> 1222;
   13 -> 131[label="a= ,b="];
   13 -> 132[label="a= ,b="];
   131 -> 1311[label="a= ,b="];
   131 -> 1312[label="a= ,b="];
   132 -> 1321[label="a= ,b="];
   132 -> 1322[label="CUT"];
 }
'/>
