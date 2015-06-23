package wumpusworld.states;

public enum PossiblePlayerStates
{
    PLAYER_EATEN, PLAYER_FELL_TO_DEATH, PLAYER_RAN_INTO_WALL;

    public boolean isTerminal()
    {
        switch(this)
        {
        case PLAYER_EATEN:
        case PLAYER_FELL_TO_DEATH:
            return true;
        default:
            return false;
        }
    }
}
