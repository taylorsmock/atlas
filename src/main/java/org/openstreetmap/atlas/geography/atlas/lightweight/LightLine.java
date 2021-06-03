package org.openstreetmap.atlas.geography.atlas.lightweight;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.PolyLine;
import org.openstreetmap.atlas.geography.atlas.complete.EmptyAtlas;
import org.openstreetmap.atlas.geography.atlas.items.Line;
import org.openstreetmap.atlas.geography.atlas.items.Relation;
import org.openstreetmap.atlas.utilities.collections.Iterables;

/**
 * A lightweight line
 *
 * @author Taylor Smock
 */
public class LightLine extends Line implements LightLineItem<LightLine>
{
    private final long identifier;
    private final long[] relationIdentifiers;
    private final Location[] locations;

    /**
     * Create a new LightLine from another line
     *
     * @param from
     *            The line to copy from
     * @return A new LightLine
     */
    static LightLine from(final Line from)
    {
        return new LightLine(from);
    }

    /**
     * Create a new LightLine with just an identifier
     *
     * @param identifier
     *            The identifier
     */
    LightLine(final long identifier)
    {
        this(identifier, EMPTY_LOCATION_ARRAY);
    }

    /**
     * Create a new LightLine with the specified points
     *
     * @param identifier
     *            The identifier for the Line
     * @param points
     *            The points for the line
     */
    LightLine(final long identifier, final Location... points)
    {
        super(new EmptyAtlas());
        this.identifier = identifier;
        this.relationIdentifiers = EMPTY_LONG_ARRAY;
        this.locations = points.length > 0 ? points.clone() : points;
    }

    /**
     * Create a new LightLine from another line
     *
     * @param from
     *            The line to copy from
     */
    LightLine(final Line from)
    {
        super(new EmptyAtlas());
        this.identifier = from.getIdentifier();
        this.relationIdentifiers = from.relations().stream().mapToLong(Relation::getIdentifier)
                .toArray();
        this.locations = Iterables.stream(from.asPolyLine()).collectToList()
                .toArray(Location[]::new);
    }

    @Override
    public PolyLine asPolyLine()
    {
        return new PolyLine(this.locations);
    }

    @Override
    public long getIdentifier()
    {
        return this.identifier;
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
     * @see Line#relations()
     * @return A set of identifier only relations
     */
    @Override
    public Set<Relation> relations()
    {
        return LongStream.of(this.getRelationIdentifiers()).mapToObj(LightRelation::new)
                .collect(Collectors.toSet());
    }
}
