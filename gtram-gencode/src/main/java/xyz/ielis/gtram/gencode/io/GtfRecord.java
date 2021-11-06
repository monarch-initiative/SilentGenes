package xyz.ielis.gtram.gencode.io;

import org.monarchinitiative.svart.BaseGenomicRegion;
import org.monarchinitiative.svart.Contig;
import org.monarchinitiative.svart.Coordinates;
import org.monarchinitiative.svart.Strand;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

class GtfRecord extends BaseGenomicRegion<GtfRecord> {

    private final GtfSource source;
    private final GtfFeature feature;
    private final GtfFrame frame;
    private final String geneId;
    private final Map<String, String> attributes;

    private GtfRecord(Contig contig,
                      Strand strand,
                      Coordinates coordinates,
                      GtfSource source,
                      GtfFeature feature,
                      GtfFrame frame,
                      String geneId,
                      Map<String, String> attributes) {
        super(contig, strand, coordinates);
        this.source = source;
        this.feature = feature;
        this.frame = frame;
        this.geneId = geneId;
        this.attributes = attributes;
    }

    static GtfRecord of(Contig contig,
                        Strand strand,
                        Coordinates coordinates,
                        GtfSource source,
                        GtfFeature feature,
                        GtfFrame frame,
                        String geneId,
                        Map<String, String> attributes) {
        return new GtfRecord(contig, strand, coordinates, source, feature, frame, geneId, attributes);
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
        return geneId;
    }

    public boolean hasAttribute(String attribute) {
        return attributes.containsKey(attribute);
    }

    public Set<String> attributes() {
        return attributes.keySet();
    }

    public String attribute(String key) {
        return attributes.get(key);
    }

    @Override
    protected GtfRecord newRegionInstance(Contig contig, Strand strand, Coordinates coordinates) {
        return new GtfRecord(contig, strand, coordinates, source, feature, frame, geneId, attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GtfRecord gtfRecord = (GtfRecord) o;
        return source == gtfRecord.source && feature == gtfRecord.feature && frame == gtfRecord.frame && Objects.equals(geneId, gtfRecord.geneId) && Objects.equals(attributes, gtfRecord.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, feature, frame, geneId, attributes);
    }

    @Override
    public String toString() {
        return "GtfRecord{" +
                "source=" + source +
                ", feature=" + feature +
                ", frame=" + frame +
                ", geneId='" + geneId + '\'' +
                ", attributes=" + attributes +
                "} " + super.toString();
    }
}
