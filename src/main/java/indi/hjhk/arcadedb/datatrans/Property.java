package indi.hjhk.arcadedb.datatrans;

public class Property{
    public PropertyType propertyType;
    public byte constraints;
    Property(PropertyType propertyType, byte constraints){
        this.propertyType=propertyType;
        this.constraints=constraints;
    }
}
