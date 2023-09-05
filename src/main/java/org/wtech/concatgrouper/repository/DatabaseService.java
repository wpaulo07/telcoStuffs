package org.wtech.concatgrouper.repository;

import java.sql.*;

public class DatabaseService {
    private static final String JDBC_URL = "jdbc:oracle:thin:@10.12.35.28:1521/pdb_ebscsb2b.snexacli.tdeclprodgru.oraclevcn.com";
    private static final String USERNAME = "NKADM";
    private static final String PASSWORD = "NKADM";
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}