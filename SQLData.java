package com.murro.sqlman;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class SQLData extends SQLObject implements Cloneable {
    @Getter
    private String table_name;
    private Map<String,Object> fields;

    protected SQLData(String table_name,HashMap<String,String> col_types){
        this.table_name = table_name;
        fields = new HashMap<>();
        for(var x : col_types.entrySet()){
            if(x.getValue().equalsIgnoreCase("SERIAL")){
                Integer v = null;
                fields.put(x.getKey(), v);
            } 
            else if(x.getValue().equalsIgnoreCase("INTEGER")){
                Integer v = null;
                fields.put(x.getKey(), v);
            }
            else if(x.getValue().equalsIgnoreCase("INT4")){
                Integer v = null;
                fields.put(x.getKey(), v);
            }
            else if(x.getValue().equalsIgnoreCase("VARCHAR")){
                String v = null;
                fields.put(x.getKey(), v);
            }
            else{
                log(new SQLException("Unsupported data type" + x.getValue()));
            }
        }
        return;
    }

    public void set(String key, Object value){
        fields.replace(key,value);
    }

    public void set(String key1, Object value1, String key2, Object value2){
        fields.replace(key1,value1);
        fields.replace(key2,value2);
    }

    public void set(String key1, Object value1, String key2, Object value2, String key3, Object value3){
        fields.replace(key1,value1);
        fields.replace(key2,value2);
        fields.replace(key3,value3);
    }

    public void set(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4){
        fields.replace(key1,value1);
        fields.replace(key2,value2);
        fields.replace(key3,value3);
        fields.replace(key4,value4);
    }

    public Object get(String key){
        return fields.get(key);
    }

    @Override
    public SQLData clone(){
        try{                return (SQLData)super.clone(); }
        catch(CloneNotSupportedException ex){ return null; }
    }

    protected SQLData clone(Map<String,Object> values){
        SQLData res = this.clone();
        res.fields = values;
        return res;
    }

    @Override
    public String toString(){
        return table_name+ ": " + fields.toString() + "\n";
    }
    
}


