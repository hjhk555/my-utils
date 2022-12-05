package indi.hjhk.arcadedb.driver;

import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PSQLState;

import java.sql.*;
import java.util.Properties;

public class arcadeJDBCHandler {
    private Connection conn=null;
    private Statement stmt=null;
    //config
    static final String JDBC_Driver="org.postgresql.Driver";
    static final String extConfig="";

    private int port=5432;
    private String ipAddr="127.0.0.1";
    private String dbName="test";
    private Properties prop=new Properties();
    boolean configChanged=false;

    public arcadeJDBCHandler(){
        prop.setProperty("ssl", "false");
        prop.setProperty("user", "root");
        prop.setProperty("password", "12345678");
    }

    public arcadeJDBCHandler setIpAddr(@NotNull String ipAddr){
        this.ipAddr=ipAddr;
        configChanged=true;
        return this;
    }

    public arcadeJDBCHandler setPort(int port){
        if (port<0) port=3306;
        this.port=port;
        configChanged=true;
        return this;
    }

    public arcadeJDBCHandler setdbName(@NotNull String dbName){
        this.dbName=dbName;
        configChanged=true;
        return this;
    }

    public arcadeJDBCHandler setUser(@NotNull String user){
        prop.setProperty("user", user);
        configChanged=true;
        return this;
    }

    public arcadeJDBCHandler setPasswd(@NotNull String passwd){
        prop.setProperty("password", passwd);
        configChanged=true;
        return this;
    }

    public boolean connnect(){
        if (configChanged){
            disConnect();
            configChanged = false;
        }
        try{
            if (conn!=null && !conn.isClosed() && stmt!=null && !stmt.isClosed()){
                return true;
            }
            disConnect();
            Class.forName(JDBC_Driver);
            String dburl="jdbc:postgresql://"+ipAddr+":"+port+"/"+dbName+extConfig;
            conn = DriverManager.getConnection(dburl, prop);
            stmt = conn.createStatement();
            return true;
        }catch (SQLException se1){
            se1.printStackTrace();
            disConnect();
            return false;
        } catch (ClassNotFoundException cnfe1) {
            cnfe1.printStackTrace();
            return false;
        }
    }

    public ResultSet query(@NotNull String sql){
        if (connnect()){
            try{
                return stmt.executeQuery(sql);
            } catch (SQLException se1) {
                if (!se1.getSQLState().equals(PSQLState.TOO_MANY_RESULTS.getState())) se1.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    public int update(@NotNull String sql){
        if (connnect()){
            try{
                return stmt.executeUpdate(sql);
            } catch (SQLException se1) {
                if (!se1.getSQLState().equals(PSQLState.TOO_MANY_RESULTS.getState())) se1.printStackTrace();
                return 0;
            }
        }else{
            return 0;
        }
    }

    public void disConnect(){
        try{
            if (stmt!=null && !stmt.isClosed()){
                stmt.close();
            }
        }catch (SQLException se1){
            //do nothing
        }finally {
            stmt = null;
        }
        try{
            if (conn!=null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException se2) {
            //do nothing
        }finally {
            conn = null;
        }
    }
}
