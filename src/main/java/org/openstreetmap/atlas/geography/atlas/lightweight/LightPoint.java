package org.openstreetmap.atlas.geography.atlas.lightweight;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.atlas.complete.EmptyAtlas;
import org.openstreetmap.atlas.geography.atlas.items.Point;
import org.openstreetmap.atlas.geography.atlas.items.Relation;

/**
 * A lightweight Point. If it doesn't need to be stored in a field, it isn't.
 *
 * @author Taylor Smock
 */
public class LightPoint extends Point implements LightLocationItem<LightPoint>
{
    private final long identifier;
    private final Location location;
    private final long[] relationIdentifiers;

    static LightPoint from(final Point point)
    {
        return new LightPoint(point);
    }

    /**
     * Create a new light point.
     *
     * @param identifier
     *            The identifier for the new point
     * @param location
     *            The location of the point
     * @param relationIdentifiers
     *            Any relations for the point
     */
    public LightPoint(final Long identifier, final Location location,
            final Set<Long> relationIdentifiers)
    {
        super(new EmptyAtlas());
        this.identifier = identifier;
        this.location = location;
        this.relationIdentifiers = relationIdentifiers.stream().mapToLong(Long::longValue)
                .toArray();
    }

    /**
     * A basic point with just an identifier
     *
     * @param identifier
     *            The identifier for the point
     */
    LightPoint(final long identifier)
    {
        this(identifier, null);
    }

    /**
     * Create a new LightPoint with an id and location
     *
     * @param identifier
     *            The identifier
     * @param location
     *            The location
     */
    LightPoint(final long identifier, final Location location)
    {
        this(identifier, location, Collections.emptySet());
    }

    /**
     * Create a lightweight point from another point
     *
     * @param from
     *            The point to copy information from
     */
    LightPoint(final Point from)
    {
        super(new EmptyAtlas());
        this.identifier = from.getIdentifier();
        this.location = from.getLocation();
        this.relationIdentifiers = from.relations().stream().mapToLong(Relation::getIdentifier)
                .toArray();
    }

    @Override
    public long getIdentifier()
    {
        return this.identifier;
    }

    @Override
    public Location getLocation()
    {
        return this.location;
    }

    @Override
    public long[] getRelationIdentifiers()
    {
        return this.relationIdentifiers.clone();
    }

    /**
     * Please note that the relations returned from this method should *only* be used for
     * identifiers.
     *
     * @see Point#relations()
     * @return A set of identifier only relations
     */
    @Override
    public Set<Relation> relations()
    {
        return LongStream.of(this.getRelationIdentifiers()).mapToObj(LightRelation::new)
                .collect(Collectors.toSet());
    }
}
