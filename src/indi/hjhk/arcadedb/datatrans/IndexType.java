package indi.hjhk.arcadedb.datatrans;

public enum IndexType{
    ARCADEDB_UNIQUE("UNIQUE"),
    ARCADEDB_NOTUNIQUE("NOTUNIQUE"),
    ARCADEDB_FULLTEXT("FULL_TEXT");

    String name;
    IndexType(String name){
        this.name=name;
    }
    String getName(){
        return name;
    }

    public static final IndexType MYSQL_INDEX   =   IndexType.ARCADEDB_NOTUNIQUE;
    public static final IndexType MYSQL_UNIQUE  =   IndexType.ARCADEDB_UNIQUE;
    public static final IndexType MYSQL_FULLTEXT=   IndexType.ARCADEDB_FULLTEXT;
}