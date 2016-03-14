# Exercise Lecture 06
## Exercise 1
### DNF Satisfiable algorithm

Check all the conjunctions. If there exist at least one conjunction of symbols where A and ¬A does not exist the function is satisfiable.

i.e:

    the following DNF is satisfiable because the second conjunction does not contain A and ¬A
    (¬B ∧ A ∧ B) ∨ (¬A ∧ C)
    
    the following DNF is not satisfiable because the none both conjunctions have either A ∧ ¬A or C ∧ ¬C
    (A ∧ ¬A ∧ B) ∨ (C ∧ ¬C)

### CNF Tautology algorithm

Check all the disjunctions and see if there exist in all of them a disjunction of A and ¬A. If there does then it is a tautology

    the following DNF is valid because the all disjunctions does contain a: A and ¬A
    (A ∨ ¬A ∨ B) ∧ (C ∨ ¬C)
    
    the following DNF is not valid because it is not all the disjunctions which contain a: A ∧ ¬A
    (A ∨ ¬A) ∧ (¬A ∨ C)

## Exercise 2
