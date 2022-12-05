package indi.hjhk.arcadedb.datatrans;

public class ArcadedbVertex extends ArcadedbDocument{

    public ArcadedbVertex(String typeName) {
        super(DocumentType.ARCADEDB_VERTEX, typeName);
    }
}
