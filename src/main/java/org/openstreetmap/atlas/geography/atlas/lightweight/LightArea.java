package org.openstreetmap.atlas.geography.atlas.lightweight;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.Polygon;
import org.openstreetmap.atlas.geography.atlas.complete.EmptyAtlas;
import org.openstreetmap.atlas.geography.atlas.items.Area;
import org.openstreetmap.atlas.geography.atlas.items.Relation;

/**
 * A lightweight area.
 *
 * @author Taylor Smock
 */
public class LightArea extends Area implements LightLineItem<LightArea>
{
    private final long identifier;
    private final long[] relationIdentifiers;
    private final Location[] locations;

    /**
     * Create a new area from another area
     *
     * @param from
     *            The area to clone
     * @return A new LightArea
     */
    static LightArea from(final Area from)
    {
        return new LightArea(from);
    }

    /**
     * Create a new LightArea with just an identifier
     *
     * @param identifier
     *            The identifier
     */
    LightArea(final long identifier)
    {
        this(identifier, EMPTY_LOCATION_ARRAY);
    }

    /**
     * Create a new LightArea with just an identifier and points
     *
     * @param identifier
     *            The identifier
     * @param points
     *            The points of the area
     */
    LightArea(final long identifier, final Location... points)
    {
        super(new EmptyAtlas());
        this.identifier = identifier;
        this.relationIdentifiers = EMPTY_LONG_ARRAY;
        this.locations = points.length > 0 ? points.clone() : points;
    }

    /**
     * Create a new LightArea from another Area
     *
     * @param from
     *            The area to copy from
     */
    LightArea(final Area from)
    {
        super(new EmptyAtlas());
        this.identifier = from.getIdentifier();
        this.relationIdentifiers = from.relations().stream().mapToLong(Relation::getIdentifier)
                .toArray();
        this.locations = from.asPolygon().toArray(EMPTY_LOCATION_ARRAY);
    }

    @Override
    public Polygon asPolygon()
    {
        return new Polygon(this.locations);
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
     * @see Area#relations()
     * @return A set of identifier only relations
     */
    @Override
    public Set<Relation> relations()
    {
        return LongStream.of(this.relationIdentifiers).mapToObj(LightRelation::new)
                .collect(Collectors.toSet());
    }
}
