package indi.hjhk.arcadedb.datatrans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Index{
    public List<String> properties;
    public IndexType indexType;

    Index(IndexType indexType, String... properties){
        this.indexType=indexType;
        this.properties=new ArrayList<>();
        this.properties.addAll(Arrays.asList(properties));
    }
}