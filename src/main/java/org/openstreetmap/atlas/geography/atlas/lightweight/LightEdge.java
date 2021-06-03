package org.openstreetmap.atlas.geography.atlas.lightweight;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.PolyLine;
import org.openstreetmap.atlas.geography.atlas.complete.EmptyAtlas;
import org.openstreetmap.atlas.geography.atlas.items.Edge;
import org.openstreetmap.atlas.geography.atlas.items.Node;
import org.openstreetmap.atlas.geography.atlas.items.Relation;

/**
 * A lightweight edge with basic information
 *
 * @author Taylor Smock
 */
public class LightEdge extends Edge implements LightLineItem<LightEdge>
{
    private final long identifier;
    private final long[] relationIdentifiers;
    private final long startNodeIdentifier;
    private final long endNodeIdentifier;
    private final Location[] pointLocations;

    /**
     * Create a new LightEdge from another Edge
     *
     * @param from
     *            The edge to copy from
     * @return A new LightEdge
     */
    static LightEdge from(final Edge from)
    {
        return new LightEdge(from);
    }

    /**
     * Create a LightEdge with just an identifier
     *
     * @param identifier
     *            The identifier
     */
    LightEdge(final long identifier)
    {
        this(identifier, EMPTY_LOCATION_ARRAY);
    }

    /**
     * Create a new LightEdge with an identifier and locations
     *
     * @param identifier
     *            The identifier
     * @param points
     *            The location points
     */
    LightEdge(final long identifier, final Location... points)
    {
        super(new EmptyAtlas());
        this.identifier = identifier;
        this.relationIdentifiers = EMPTY_LONG_ARRAY;
        this.startNodeIdentifier = 0;
        this.endNodeIdentifier = 0;
        this.pointLocations = points.length > 0 ? points.clone() : points;
    }

    /**
     * Create a new LightEdge from another Edge
     *
     * @param from
     *            The edge to copy from
     */
    LightEdge(final Edge from)
    {
        super(new EmptyAtlas());
        this.identifier = from.getIdentifier();
        this.relationIdentifiers = from.relations().stream().mapToLong(Relation::getIdentifier)
                .toArray();
        this.startNodeIdentifier = from.start().getIdentifier();
        this.endNodeIdentifier = from.end().getIdentifier();
        this.pointLocations = from.asPolyLine().toArray(EMPTY_LOCATION_ARRAY);
    }

    @Override
    public PolyLine asPolyLine()
    {
        return new PolyLine(this.pointLocations);
    }

    @Override
    public Node end()
    {
        return new LightNode(this.endNodeIdentifier,
                this.pointLocations[this.pointLocations.length - 1]);
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

    @Override
    public Set<Relation> relations()
    {
        return LongStream.of(this.getRelationIdentifiers()).mapToObj(LightRelation::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Node start()
    {
        return new LightNode(this.startNodeIdentifier, this.pointLocations[0]);
    }
}
