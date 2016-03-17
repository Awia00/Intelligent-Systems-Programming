package iaip_c4;

/**
 * Created by Morten on 17/03/2016.
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