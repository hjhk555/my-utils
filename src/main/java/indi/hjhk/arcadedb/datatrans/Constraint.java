package indi.hjhk.arcadedb.datatrans;

public class Constraint {
    public static final byte ARCADEDB_DEFAULT       =   0x0;
    public static final byte ARCADEDB_MANDATORY     =   0x1;
    public static final byte ARCADEDB_NOTNULL       =   0x2;
    public static final byte ARCADEDB_READONLY      =   0x4;
    public static final byte MYSQL_DEFAULT          =   ARCADEDB_MANDATORY;
    public static final byte MYSQL_NOTNULL          =   ARCADEDB_MANDATORY | ARCADEDB_NOTNULL;
}
