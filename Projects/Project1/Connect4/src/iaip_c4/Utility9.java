package iaip_c4;

/**
 * The utility class is used to tag a utility value with extra information.
 *
 * Currently this information is whether the utility represents a heuristic or an actual utility of a terminal state.
 */
public class Utility9
{
    final int utility;
    final boolean isTerminal;

    public Utility9(int utility, boolean isTerminal)
    {
        this.utility = utility;
        this.isTerminal = isTerminal;
    }

    public static Utility9 max(Utility9 a, Utility9 b)
    {
        if(a.utility > b.utility)
        {
            return a;
        }
        return b;
    }

    public static Utility9 min(Utility9 a, Utility9 b)
    {
        if(a.utility < b.utility)
        {
            return a;
        }
        return b;
    }
}