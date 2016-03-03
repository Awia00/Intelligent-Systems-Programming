# Mandatory Exercise - Simple Tic-Tac-Toe
**Anders Wind - awis@itu.dk**
## Task 1
Backwards chaining is goal driven and can have a better time complexity than forward-chaining.
This is due to the fact that Backwards chaining only "searches" the relevant part of the knowledge base, but forward-chaining has to search all different implications of the given facts.
Therefore situations where we have a lot of facts that do not have any impact on the query backwards chaining will be advantageous.

## Task 2
I assume that the KB is in CNF

    function PL-BC-Entails? (KB, q, Value-Map) returns true or false
        inputs: KB, the knowledge base, a set of propositional definite clauses
                q, the query, a propositional symbol
                Value-Map, a map of propositional symbol keys mapping to boolean values, empty to begin with
        
        Value-Map ← Check-Clause(KB, q, Value-Map)
        if q is in Value-Map
            return Value-Map[q]
            
        return false
        
    function Check-Clause(KB, q, Value-Map) returns map
        inputs: KB, the knowledge base, a set of propositional definite clauses
                q, the query, a propositional symbol
                Value-Map, a map of propositional symbol keys mapping to boolean values, empty to begin with
        
        if q is in Value-Map
            return Value-Map
        
        for each clause c in KB which implies q
            if c is fact 
                Value-Map.add(q, true)
                return Value-Map
                
            else
                result ← true
                
                Value-Map.add(q, false)
                Value-Map ← Update-Map(KB, c.PREMISE, Value-Map)
                for each (key, value) in Value-Map where key is in c.PREMISE
                    if !value
                        result ← false
                
                if result
                    Value-Map.add[q] ← result
                    return Value-Map

        return Value-Map
        
    
    function Update-Map(KB, symbols, Value-Map) returns map
        inputs: KB, the knowledge base, a set of propositional definite clauses
                symbols, a set of propositional symbols
                Value-Map, a map of propositional symbol keys mapping to boolean value
    
        for each Propositional-Symbol ps in symbols
            Value-Map ← Check-Clause(KB, ps, Value-Map)
            
        return Value-Map

## Task 3
Yes it returns false due to a cyclic case. My algorithm handles cyclic cases by checking if a Symbol has already been already exists in the Value-Map
 
 ## Task 4
 I assume that it is meant what time complexity the algorithm runs in based on the amount of clauses.
 
 My algorithm