package com.murro.sqlman;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class SQLObject {
    protected void log(SQLException ex){
        Logger lggr = Logger.getLogger(this.getClass().toString());
        lggr.log(Level.SEVERE, ex.getMessage(), ex);
        return;
    }
}

public class SQLManager extends SQLObject {

    private String url;
    private String user;
    private String password;
    private Connection connection;


    public SQLManager(String url, String user, String password) throws SQLException{
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
        return;
    }

    private void connect() throws SQLException{
        if(connection != null) throw new SQLException("Connection is still open!");
        this.connection = DriverManager.getConnection(url, user, password); 
        return;
    }

    public void close() throws SQLException{
        if(connection == null) throw new SQLException("Connection is already closed!");
        this.connection.close(); 
        this.connection = null;
        return;
    }

    public SQLTable newTableInstance(String table_name) throws SQLException{
        if(connection == null) throw new SQLException("Connection is closed!");
        return new SQLTable(table_name,connection);
    }


}


