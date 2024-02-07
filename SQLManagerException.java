package com.murro.sqlman;
import java.sql.*;


public class SQLManagerException extends Exception {
    public SQLManagerException(){
        super();
    }

    public SQLManagerException(String message){
        super(message);
    }

    public SQLManagerException(String message, SQLException exSQL){
        super(message + "\n" + exSQL.getMessage());
    }
}

