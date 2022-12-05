package indi.hjhk.arcadedb.datatrans;

public enum DocumentType{
    ARCADEDB_VERTEX("VERTEX"),
    ARCADEDB_EDGE("EDGE");

    String name;
    DocumentType(String name){
        this.name=name;
    }
    String getName(){
        return name;
    }
}
