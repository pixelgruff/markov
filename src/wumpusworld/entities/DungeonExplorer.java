package wumpusworld.entities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import utils.Validate;
import utils.Vector2;
import core.Player;

public class DungeonExplorer implements DungeonEntity
{
    private Vector2 direction_;

    private final ArrayDeque<Item> items_;
    private Vector2 position_;
    private final Player owner_;

    public DungeonExplorer(final Player owner, final Vector2 position, final Vector2 direction)
    {
        Validate.notNull(owner, "Cannot create a DungeonExplorer for a null player");
        Validate.notNull(position, "Cannot create a DungeonExplorer with a null position");
        Validate.isTrue(Vector2.cardinalDirections().contains(direction),
                "Cannot create a DungeonExplorer " + "with an invalid Direction");
        owner_ = owner;
        position_ = position;
        direction_ = direction;
        items_ = new ArrayDeque<Item>();
    }

    public DungeonExplorer(final DungeonExplorer copy)
    {
        Validate.notNull(copy, "Cannot create a DungeonExplorer from a null copy");
        direction_ = copy.direction_;
        items_ = new ArrayDeque<Item>();
        copy.items_.stream().map(item -> item.withOwner(this)).forEach(item -> items_.add(item));
        position_ = copy.position_;
        owner_ = copy.owner_;
    }

    @Override
    public DungeonEntity copy()
    {
        return new DungeonExplorer(this);
    }

    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof DungeonExplorer)
        {
            final DungeonExplorer otherExplorer = (DungeonExplorer) other;
            /*
             * We say that two Explorers are equal if they're on the same space,
             * facing the same direction
             */
            return Objects.equals(position_, otherExplorer.position_)
                    && Objects.equals(direction_, otherExplorer.direction_);
        }
        return false;
    }

    public Player getOwner()
    {
        return owner_;
    }

    public Vector2 getDirection()
    {
        return direction_;
    }

    public Collection<Item> getItems()
    {
        return new ArrayList<>(items_);
    }

    @Override
    public Vector2 getPosition()
    {
        return position_;
    }

    public void grab(final Item item)
    {
        Validate.notNull(item, "Cannot grab a null item");
        items_.push(item.withOwner(this));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position_, items_);
    }

    @Override
    public boolean isPassable()
    {
        return false;
    }

    public void moveBackward()
    {
        position_ = position_.subtract(direction_);
    }

    public void moveForward()
    {
        position_ = position_.add(direction_);
    }

    public Item release()
    {
        Validate.isFalse(items_.isEmpty(), "Cannot release any items; you don't have any");
        return items_.pop();
    }

    @Override
    public String shortToString()
    {
        return "E";
    }

    public void turnLeft()
    {
        direction_ = Vector2.previousCardinalDirection(direction_);
    }

    public void turnRight()
    {
        direction_ = Vector2.nextCardinalDirection(direction_);
    }
}
