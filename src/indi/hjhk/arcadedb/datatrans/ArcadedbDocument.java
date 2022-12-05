package indi.hjhk.arcadedb.datatrans;

import java.util.HashMap;
import java.util.Map;

public class ArcadedbDocument {
    DocumentType            documentType;
    String                  typeName;
    Map<String, Object>     properties  =   new HashMap<>();

    public ArcadedbDocument(DocumentType documentType, String typeName){
        this.documentType=documentType;
        this.typeName=typeName;
    }

    public void addProperty(String name, Object obj){
        properties.put(name, obj);
    }

    public String getSQLCreate(){
        StringBuilder sql=new StringBuilder(String.format("INSERT INTO %s", typeName));
        if (properties.isEmpty()) return sql.toString();
        sql.append(" SET ");
        boolean first=true;
        for (Map.Entry<String, Object> entry: properties.entrySet()){
            sql.append(String.format((first ? "%s=%s" : ", %s=%s"),
                    entry.getKey(), (entry.getValue()==null ? "null" : "'"+entry.getValue()+"'")));
            first=false;
        }
        return sql.toString();
    }
}
