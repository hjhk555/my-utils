package indi.hjhk.arcadedb.datatrans;

public enum PropertyType {
    //        ARCADEDB_BOOLEAN("BOOLEAN"),
    ARCADEDB_SHORT("SHORT"),
    //        ARCADEDB_DATE("DATE"),
//        ARCADEDB_DATETIME("DATETIME"),
    ARCADEDB_BYTE("BYTE"),
    ARCADEDB_INTEGER("INTEGER"),
    ARCADEDB_LONG("LONG"),
    ARCADEDB_STRING("STRING");
//        ARCADEDB_LINK("LINK"),
//        ARCADEDB_DECIMAL("DECIMAL"),
//        ARCADEDB_DOUBLE("DOUBLE"),
//        ARCADEDB_FLOAT("FLOAT"),
//        ARCADEDB_BINARY("BINARY"),
//        ARCADEDB_EMBEDDED("EMBEDDED"),
//        ARCADEDB_LIST("LIST"),
//        ARCADEDB_MAP("MAP");

    private String name;
    PropertyType(String name){
        this.name=name;
    }
    String getName(){
        return name;
    }

    public static final PropertyType MYSQL_BIT         =   PropertyType.ARCADEDB_BYTE;
    public static final PropertyType MYSQL_TINYTEXT    =   PropertyType.ARCADEDB_STRING;
    public static final PropertyType MYSQL_TINYINT     =   PropertyType.ARCADEDB_BYTE;
    public static final PropertyType MYSQL_SMALLINT    =   PropertyType.ARCADEDB_SHORT;
    public static final PropertyType MYSQL_MEDIUMINT   =   PropertyType.ARCADEDB_INTEGER;
    public static final PropertyType MYSQL_INT         =   PropertyType.ARCADEDB_INTEGER;
    public static final PropertyType MYSQL_BIGINT      =   PropertyType.ARCADEDB_LONG;
}