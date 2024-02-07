package com.murro.sqlman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.*;

public class SQLTable extends SQLObject {

    private Connection connection;
    private String name;
    private HashMap<String, String> col_types = new HashMap<>();
    private SQLData template;

    protected SQLTable(String name, Connection connection) throws SQLException{
        this.name = name;
        this.connection = connection;
        ResultSet rs = connection.getMetaData().getColumns(null,null,name,"%");
        MapHandler handler = new MapHandler();
        Map<String,Object> temp;
        while((temp = handler.handle(rs)) != null)
            if(temp != null)                 
                if(!temp.get("TYPE_NAME").toString().equalsIgnoreCase("SERIAL"))
                    col_types.put(temp.get("COLUMN_NAME").toString(),temp.get("TYPE_NAME").toString());
        DbUtils.closeQuietly(rs);
        template = new SQLData(name,col_types);
        return;
    }

    public SQLData newDataInstance(){
        return template.clone();
    }

    public void insert(SQLData instance) throws SQLException, SQLManagerException{
        if(instance.getTable_name() != name) 
            throw new SQLManagerException("Wrong table!");
        if(connection == null) 
            throw new SQLException("Connection is closed!");

        String field_names = col_types.keySet().toString(); 

        String query = "INSERT INTO " + name + "(" +
            field_names.substring(1,field_names.length() - 1) + ") VALUES(";

        for(int i = 0; i < col_types.size() - 1; ++i) 
            query += "?,";
        query += "?);";

        PreparedStatement pst = connection.prepareStatement(query);
        int i = 1;
        for(var field : col_types.keySet()){
            pst.setObject(i,instance.get(field));
            ++i;
        }

        if(pst.executeUpdate() == PreparedStatement.EXECUTE_FAILED) 
            throw new SQLException("Update failed!");
        DbUtils.closeQuietly(pst);
        return;
    }

    public List<SQLData> getBy(String field, Object value) throws SQLException{
        String field_names = col_types.keySet().toString(); 
        field_names = field_names.substring(1,field_names.length() - 1);
        String query = "SELECT " + field_names + " FROM " + name + " WHERE " + field + "=?";

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setObject(1,value);
        System.err.println(pst);

        ResultSet rs = pst.executeQuery();

        List<SQLData> res = new ArrayList<>();

        while(rs.next()){
            SQLData temp = template.clone();
            int i = 1;
            for(String x : col_types.keySet()){
                temp.set(x,rs.getObject(i));
                ++i;
            }
            res.add(temp);
        }

        DbUtils.closeQuietly(rs);
        return res;
    }
}








