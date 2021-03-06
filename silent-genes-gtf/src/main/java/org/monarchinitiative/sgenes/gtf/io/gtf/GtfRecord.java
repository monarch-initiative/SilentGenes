package org.monarchinitiative.sgenes.gtf.io.gtf;

import org.monarchinitiative.svart.GenomicRegion;
import org.monarchinitiative.sgenes.model.Located;

import java.util.*;

public class GtfRecord implements Located {

    private final GenomicRegion location;
    private final GtfSource source;
    private final GtfFeature feature;
    private final GtfFrame frame;
    private final Map<String, List<String>> attributes;
    private final Set<String> tags;

    public static GtfRecord of(GenomicRegion location,
                               GtfSource source,
                               GtfFeature feature,
                               GtfFrame frame,
                               Map<String, List<String>> attributes,
                               Set<String> tags) {
        Objects.requireNonNull(location, "Location must not be null");
        Objects.requireNonNull(source, "GtfSource must not be null");
        Objects.requireNonNull(feature, "GtfFeature must not be null");
        Objects.requireNonNull(frame, "GtfFrame must not be null");
        Objects.requireNonNull(attributes, "Attributes map must not be null");
        Objects.requireNonNull(tags, "Tags must not be null");

        List<String> geneId = attributes.get("gene_id");
        if (geneId == null || geneId.isEmpty())
            throw new IllegalArgumentException("Missing the mandatory `gene_id` attribute: " + attributes);

        return new GtfRecord(location, source, feature, frame, Map.copyOf(attributes), tags);
    }

    private GtfRecord(GenomicRegion location,
                      GtfSource source,
                      GtfFeature feature,
                      GtfFrame frame,
                      Map<String, List<String>> attributes,
                      Set<String> tags) {
        this.location = location;
        this.source = source;
        this.feature = feature;
        this.frame = frame;
        this.attributes = attributes;
        this.tags = tags;
    }

    @Override
    public GenomicRegion location() {
        return location;
    }

    public GtfSource source() {
        return source;
    }

    public GtfFeature feature() {
        return feature;
    }

    public GtfFrame frame() {
        return frame;
    }

    public String geneId() {
        return attributes.get("gene_id").get(0);
    }

    public boolean hasAttribute(String attribute) {
        return attributes.containsKey(attribute);
    }

    public Set<String> attributes() {
        return attributes.keySet();
    }

    public List<String> attribute(String key) {
        return attributes.get(key);
    }

    public String firstAttribute(String key) {
        List<String> values = attributes.get(key);
        return values != null ? values.get(0) : null;
    }

    public Set<String> tags() {
        return tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, source, feature, frame, attributes, tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GtfRecord gtfRecord = (GtfRecord) o;
        return Objects.equals(location, gtfRecord.location) && source == gtfRecord.source && feature == gtfRecord.feature && frame == gtfRecord.frame && Objects.equals(attributes, gtfRecord.attributes) && Objects.equals(tags, gtfRecord.tags);
    }

    @Override
    public String toString() {
        return "GtfRecord{" +
                "location=" + location +
                ", source=" + source +
                ", feature=" + feature +
                ", frame=" + frame +
                ", attributes=" + attributes +
                ", tags=" + tags +
                '}';
    }
}
