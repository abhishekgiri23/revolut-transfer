package com.revolut.common.utils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static Logger LOG = Logger.getLogger(DatabaseUtils.class);
    
    private static String connection_url = CommonUtils.getProperty("h2_connection_url");
    private static String user = CommonUtils.getProperty("h2_user");
    private static String password = CommonUtils.getProperty("h2_password");
    private static String driver = CommonUtils.getProperty("h2_driver");
    
    
    DatabaseUtils() {
        // init: load driver
        DbUtils.loadDriver(driver);
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connection_url, user, password);
    }
    
    
    public static void populateData() {
        LOG.info("Populating Table and data ..... ");
        Connection conn = null;
        try {
            conn = getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/db.sql"));
        } catch (SQLException e) {
            LOG.error("populateData(): Error populating data ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            LOG.error("populateData(): Error finding test script file ", e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
    
}
