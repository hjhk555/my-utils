package indi.hjhk.arcadedb.datatrans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ArcadedbSchemaVertex extends ArcadedbSchema{
    public ArcadedbSchemaVertex(String name) {
        super(name, DocumentType.ARCADEDB_VERTEX);
    }

    public ArcadedbVertex resolveFromRS(ResultSet rs){
        ArcadedbVertex vertex=new ArcadedbVertex(name);
        for (Map.Entry<String, Property> entry: properties.entrySet()){
            String key= entry.getKey();
            Property property=entry.getValue();
            try {
                Object value=rs.getObject(key);
                if (value==null){
                    if ((property.constraints & Constraint.ARCADEDB_NOTNULL) !=0) {
                        System.err.println("[ERROR] find null value on property " + name);
                        return null;
                    }
                    vertex.addProperty(key, null);
                }else {
                    switch (property.propertyType) {
                        case ARCADEDB_BYTE, ARCADEDB_INTEGER, ARCADEDB_SHORT, ARCADEDB_LONG ->
                                vertex.addProperty(key, rs.getLong(key));
                        case ARCADEDB_STRING ->
                                vertex.addProperty(key, rs.getString(key));
                        default -> {
                            System.err.println("[ERROR] type of property " + name + "not supported");
                            return null;
                        }
                    }
                }
            } catch (SQLException e) {
                if ((property.constraints & Constraint.ARCADEDB_MANDATORY) !=0) {
                    System.err.println("[ERROR] value not found on property " + name);
                    return null;
                }
            }
        }
        return vertex;
    }
}
