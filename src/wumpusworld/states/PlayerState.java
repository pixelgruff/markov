package wumpusworld.states;

public enum PlayerState
{
    PLAYER_EATEN, PLAYER_ESCAPED_WITH_GOLD, PLAYER_FELL_TO_DEATH, PLAYER_RAN_INTO_WALL;

    public boolean isTerminal()
    {
        switch(this)
        {
        case PLAYER_EATEN:
        case PLAYER_FELL_TO_DEATH:
        case PLAYER_ESCAPED_WITH_GOLD:
            return true;
        default:
            return false;
        }
    }
}
