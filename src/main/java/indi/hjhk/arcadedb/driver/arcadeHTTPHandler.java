package indi.hjhk.arcadedb.driver;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class arcadeHTTPHandler {

    private boolean DEBUG = false;
    private int port = 2480;
    private String ipAddr="127.0.0.1";
    private String dbName=null;
    private String user="root";
    private String passwd="12345678";

    public arcadeHTTPHandler setDebugFlag(boolean debugFlag){
        DEBUG=debugFlag;
        return this;
    }

    public arcadeHTTPHandler setUser(@NotNull String user){
        this.user=user;
        return this;
    }

    public arcadeHTTPHandler setPort(int port) {
        if (port<0) port=2480;
        this.port = port;
        return this;
    }

    public arcadeHTTPHandler setIpAddr(@NotNull String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public arcadeHTTPHandler setDbName(@NotNull String dbName) {
        this.dbName = dbName;
        return this;
    }

    public arcadeHTTPHandler setPasswd(@NotNull String passwd) {
        this.passwd = passwd;
        return this;
    }

    public boolean createDatabse(@NotNull String dbName){
        log("--------create database "+dbName+"--------");
        String[] args= {"curl",
                "-X", "POST",
                "http://"+ipAddr+":"+port+"/api/v1/create/"+dbName,
                "--user", user+":"+passwd};
        String res= ResolveResultString(execCurl(args));
        log(res+"\n");
        return res != null && res.equals("ok");
    }

    public boolean dropDatabase(@NotNull String dbName){
        System.out.println("--------drop database "+dbName+"--------");
        String[] args= {"curl",
                "-X", "POST",
                "http://"+ipAddr+":"+port+"/api/v1/drop/"+dbName,
                "--user", user+":"+passwd};
        String res= ResolveResultString(execCurl(args));
        log(res+"\n");
        return res != null && res.equals("ok");
    }

    public JSONArray update(@NotNull String sql){
        log("--------"+sql+"--------");
        if (dbName==null){
            System.out.println("database not set");
            return null;
        }
        JSONObject jsonObj= new JSONObject();
        jsonObj.put("language", "sql");
        jsonObj.put("command", sql);

        String[] args= {"curl",
                "-X", "POST",
                "http://"+ipAddr+":"+port+"/api/v1/command/"+dbName,
                "-d", jsonObj.toString().replace("\"", "\\\""),
                "-H", "Content-Type: application/json",
                "--user", user+":"+passwd};
        JSONArray res= resolveResultArray(execCurl(args));
        log(res == null? "" : res+"\n");
        return res;
    }

    public JSONArray query(@NotNull String sql){
        log("--------"+sql+"--------");
        if (dbName==null){
            System.out.println("database not set");
            return null;
        }
        String[] args = {"curl",
                "-X", "GET",
                "http://" + ipAddr + ":" + port + "/api/v1/query/" + dbName + "/sql/" + URLEncoder.encode(sql, StandardCharsets.UTF_8),
                "--user", user + ":" + passwd};
        JSONArray res= resolveResultArray(execCurl(args));
        log(res == null? "" : res+"\n");
        return res;
    }

    private String execCurl(String[] args){
        ProcessBuilder processBuilder=new ProcessBuilder(args);
        Process process;
        try{
            process=processBuilder.start();
            BufferedReader reader= new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder strBuilder = new StringBuilder();
            String str;
            while ((str=reader.readLine())!=null){
                strBuilder.append(str).append(System.getProperty("line.separator"));
            }
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject parseJSONObject(String strRes){
        if (strRes==null || strRes.length()==0){
            System.out.println("curl returns no result, server maybe offline");
            return null;
        }
        try{
            JSONObject jsonObj=JSONObject.parseObject(strRes);
            if (jsonObj == null){
                System.out.println("cannot parse result to json:\n"+strRes);
                return null;
            }
            String exception= jsonObj.getString("exception");
            if (exception!=null){
                String detail= jsonObj.getString("detail");
                new SQLException(exception+" ("+detail+")").printStackTrace();
                return null;
            }
            return jsonObj;
        }catch (JSONException je){
            System.out.println("exception occurred when resolving:\n"+strRes);
            return null;
        }
    }

    private JSONArray resolveResultArray(String strRes){
        JSONObject jsonObj=parseJSONObject(strRes);
        if (jsonObj == null){
            return null;
        }
        try{
            JSONArray res = jsonObj.getJSONArray("result");
            if (res == null) {
                System.out.println("cannot resolve result from json:\n" + jsonObj);
                return null;
            }
            return res;
        }catch (JSONException je){
            System.out.println("exception occurred when resolving:\n"+strRes);
            je.printStackTrace();
            return null;
        }
    }

    private String ResolveResultString(String strRes){
        JSONObject jsonObj=parseJSONObject(strRes);
        if (jsonObj == null){
            return null;
        }
        try{
            String res=jsonObj.getString("result");
            return res;
        }catch (JSONException je){
            System.out.println("exception occurred when resolving:\n"+strRes);
            je.printStackTrace();
            return null;
        }
    }

    private void log(@NotNull String content){
        if (DEBUG){
            System.out.println(content);
        }
    }
}
