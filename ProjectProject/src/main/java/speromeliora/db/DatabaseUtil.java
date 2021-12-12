package speromeliora.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class DatabaseUtil {
	public static String rdsMySqlDatabaseUrl;
	public static String dbUsername;
	public static String dbPassword;
		
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	   
	public final static String dbName = "sys";           // Whatever Schema you created in tutorial.
	public final static String testingName = "TESTING";       // used for testing (also default created)
	
	// pooled across all usages.
	static Connection conn;
 
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect(LambdaLogger logger) throws Exception {
		if (conn != null) { return conn; }
		logger.log("started connecting to db");
		boolean useTestDB = System.getenv("TESTING") != null;
		
		// this is resistant to any SQL-injection attack since we choose one of two possible ones.
		String schemaName = dbName;
		if (useTestDB) { 
			schemaName = testingName;
			System.out.println("USE TEST DB:" + useTestDB);
		}
		
		dbUsername = System.getenv("dbUsername");
		if (dbUsername == null) {
			System.err.println("Environment variable dbUsername is not set!");
		}
		dbPassword = System.getenv("dbPassword");
		if (dbPassword == null) {
			System.err.println("Environment variable dbPassword is not set!");
		}
		rdsMySqlDatabaseUrl = System.getenv("rdsMySqlDatabaseUrl");
		if (rdsMySqlDatabaseUrl == null) {
			System.err.println("Environment variable rdsMySqlDatabaseUrl is not set!");
		}
		logger.log("enviromental variables retrieved");
		try {
			logger.log("connecting...");
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection(jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + schemaName + multiQueries,
					dbUsername,
					dbPassword);
			logger.log("connected");
			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Failed in database connection");
		}
	}
	
	protected static Connection connect() throws Exception {
		if (conn != null) { return conn; }
		boolean useTestDB = System.getenv("TESTING") != null;
		
		// this is resistant to any SQL-injection attack since we choose one of two possible ones.
		String schemaName = dbName;
		if (useTestDB) { 
			schemaName = testingName;
			System.out.println("USE TEST DB:" + useTestDB);
		}
		
		dbUsername = System.getenv("dbUsername");
		if (dbUsername == null) {
			System.err.println("Environment variable dbUsername is not set!");
		}
		dbPassword = System.getenv("dbPassword");
		if (dbPassword == null) {
			System.err.println("Environment variable dbPassword is not set!");
		}
		rdsMySqlDatabaseUrl = System.getenv("rdsMySqlDatabaseUrl");
		if (rdsMySqlDatabaseUrl == null) {
			System.err.println("Environment variable rdsMySqlDatabaseUrl is not set!");
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection(jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + schemaName + multiQueries,
					dbUsername,
					dbPassword);
			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Failed in database connection");
		}
	}
}
