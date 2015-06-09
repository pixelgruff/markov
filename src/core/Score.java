package core;

import utils.Validate;

/**
 * Immutable double wrapper for Game-Scoring purposes.
 */
public class Score implements Comparable<Score>
{
    private double value_;

    public Score()
    {
        this(0);
    }

    public Score(final double score)
    {
        value_ = score;
    }

    public Score(final Score copy)
    {
        Validate.notNull(copy, "Cannot create a copy of a null Score");
        value_ = copy.getValue();
    }

    public double getValue()
    {
        return value_;
    }

    public Score add(final Score other)
    {
        if(other == null)
        {
            return this;
        }

        return new Score(getValue() + other.getValue());
    }

    @Override
    public int compareTo(Score other)
    {
        if(other == null)
        {
            return 1;
        }

        return Double.compare(getValue(), other.getValue());
    }
}
