package indi.hjhk.arcadedb.datatrans;

import java.util.*;

public class ArcadedbSchema {
    String                  name;
    DocumentType            documentType;
    Map<String, Property>   properties      =   new HashMap<>();
    List<Index>             indexes         =   new ArrayList<>();

    public ArcadedbSchema(String name, DocumentType documentType){
        this.name=name;
        this.documentType=documentType;
    }

    public void addProperty(String name, PropertyType propertyType){
        addProperty(name, propertyType, Constraint.ARCADEDB_DEFAULT);
    }

    public void addProperty(String name, PropertyType propertyType, byte constraints){
        properties.put(name, new Property(propertyType, constraints));
    }

    public void dropProperty(String name){
        properties.remove(name);
    }

    public void clearProperties(){
        properties.clear();
    }

    private boolean checkValidIndex(Index index){
        for (String property: index.properties){
            if (this.properties.get(property)==null) return false;
        }
        return true;
    }

    public void addIndex(IndexType indexType, String... properties){
        if (properties.length==0) return;
        indexes.add(new Index(indexType, properties));
    }

    private String getStrConstraints(byte constraints){
        String res="";
        boolean first=true;
        if ((constraints & Constraint.ARCADEDB_MANDATORY) !=0) {
            res += " (mandatory true";
            first = false;
        }
        if ((constraints & Constraint.ARCADEDB_NOTNULL) !=0) {
            res += first ? " (notnull true" : ", notnull true";
            first = false;
        }
        if ((constraints & Constraint.ARCADEDB_READONLY) !=0) {
            res += first ? " (readonly true" : ", readonly true";
            first = false;
        }
        if (!first) {
            res += ")";
        }
        return res;
    }

    public String getSQLCreateSchema(){
        StringBuilder sql= new StringBuilder(String.format("CREATE %s TYPE %s", documentType.getName(), name));
        for (Map.Entry<String, Property> entry: properties.entrySet()){
            sql.append(String.format("; CREATE PROPERTY %s.%s %s%s", name, entry.getKey(),
                    entry.getValue().propertyType.getName(), getStrConstraints(entry.getValue().constraints)));
        }
        for (Index index: indexes){
            if (checkValidIndex(index)) {
                sql.append(String.format("; CREATE INDEX ON %s(", name));
                boolean first = true;
                for (String property : index.properties) {
                    sql.append(first ? property : ", " + property);
                    first = false;
                }
                sql.append(String.format(") %s", index.indexType.getName()));
            }
        }
        return sql.toString();
    }
}
