import indi.hjhk.arcadedb.datatrans.*;
import indi.hjhk.mysql.driver.mysqlHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class dataTrans {
    public static void main(String[] args){
        ArcadedbSchemaVertex snapShot=new ArcadedbSchemaVertex("SnapShot");
        snapShot.addProperty("id", PropertyType.MYSQL_TINYTEXT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("carId", PropertyType.MYSQL_TINYTEXT, Constraint.MYSQL_DEFAULT);
        snapShot.addProperty("predId", PropertyType.MYSQL_TINYTEXT, Constraint.MYSQL_DEFAULT);
        snapShot.addProperty("succId", PropertyType.MYSQL_TINYTEXT, Constraint.MYSQL_DEFAULT);
        snapShot.addProperty("averageSpeed", PropertyType.MYSQL_INT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("currentSpeed", PropertyType.MYSQL_INT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("lastPeriodTime", PropertyType.MYSQL_BIGINT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("lastPeriodMinMile", PropertyType.MYSQL_INT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("TimeExpired", PropertyType.MYSQL_BIT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("timestamp", PropertyType.MYSQL_BIGINT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("expired", PropertyType.MYSQL_BIGINT, Constraint.MYSQL_NOTNULL);
        snapShot.addProperty("passid", PropertyType.MYSQL_TINYTEXT, Constraint.MYSQL_NOTNULL);
        snapShot.addIndex(IndexType.MYSQL_INDEX, "passid");

        mysqlHandler mysql=new mysqlHandler().setdbName("test");
        mysql.connnect();

        String sql="SELECT * FROM SnapShot LIMIT 0, 1";
        ResultSet rs= mysql.query(sql);
        try {
            if (rs!=null && rs.next()){
                System.out.println(snapShot.resolveFromRS(rs).getSQLCreate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        mysql.disConnect();
    }
}
