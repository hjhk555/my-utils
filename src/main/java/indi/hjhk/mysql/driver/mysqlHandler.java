package indi.hjhk.mysql.driver;

import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class mysqlHandler {
    private Connection conn=null;
    private Statement stmt=null;
    //config
    static final String JDBC_Driver="com.mysql.cj.jdbc.Driver";
    static final String extConfig="?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private int port=3306;
    private String ipAddr="172.27.132.132";
    private String dbName="testdb1";
    private String username="root";
    private String passwd="123456";
    boolean configChanged=false;

    public mysqlHandler(){
    }

    public mysqlHandler setIpAddr(@NotNull String ipAddr){
        this.ipAddr=ipAddr;
        configChanged=true;
        return this;
    }

    public mysqlHandler setPort(int port){
        if (port<0) port=3306;
        this.port=port;
        configChanged=true;
        return this;
    }

    public mysqlHandler setdbName(@NotNull String dbName){
        this.dbName=dbName;
        configChanged=true;
        return this;
    }

    public mysqlHandler setUsername(@NotNull String username){
        this.username=username;
        configChanged=true;
        return this;
    }

    public mysqlHandler setPasswd(@NotNull String passwd){
        this.passwd=passwd;
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
            String dburl="jdbc:mysql://"+ipAddr+":"+port+"/"+dbName+extConfig;
            conn = DriverManager.getConnection(dburl, username, passwd);
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
                se1.printStackTrace();
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
                se1.printStackTrace();
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
