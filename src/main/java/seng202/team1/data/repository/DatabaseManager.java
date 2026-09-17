package seng202.team1.data.repository;


import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import seng202.team1.exceptions.InstanceAlreadyExistsException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Singleton class responsible for interaction with MariaDB database
 * @author Morgan English
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private final String url;
    private final String user;
    private final String password;
    private final SQLDialect dialect;

    /**
     * Private constructor for singleton purposes
     */
    private DatabaseManager(String urlIn) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        if (urlIn == null || urlIn.isEmpty()) {
            this.url = dotenv.get("DB_URL", this.getSQLiteDatabasePath());
        } else {
            this.url = urlIn;
        }

        if (this.url.startsWith("jdbc:sqlite:")) {
            this.user = null;
            this.password = null;
            this.dialect = SQLDialect.SQLITE;
            if(!checkSQLiteDatabaseExists(url)){
                createSQLiteDatabaseFile(url);
                resetDB();
            }
        } else {
            this.user = dotenv.get("DB_USER");
            this.password = dotenv.get("DB_PASSWORD");
            this.dialect = SQLDialect.MARIADB;
            // We assume the mariadb database is already initialised
        }
    }

    /**
     * Singleton method to get current Instance if exists otherwise create it
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager(null);
        }
        return instance;
    }

    /**
     * WARNING Allows for setting specific database url (currently only needed for test databases, but may be useful
     * in future) USE WITH CAUTION. This does not override the current singleton instance so must be the first call.
     * @param url string url of database to load (this needs to be full url e.g. "jdbc:mariadb://...")
     * @throws InstanceAlreadyExistsException if there is already a singleton instance
     * @return current singleton instance
     */
    public static DatabaseManager initialiseInstanceWithUrl(String url) throws InstanceAlreadyExistsException {
        if(instance == null)
            instance = new DatabaseManager(url);
        else
            throw new InstanceAlreadyExistsException("Database Manager instance already exists, cannot create with url: " + url);

        return instance;
    }

    /**
     *  WARNING Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Connect to the database
     * @return database connection
     */
    public Connection connect(){
        Connection conn = null;
        try {
            if (this.user != null && this.password != null) {
                conn = DriverManager.getConnection(this.url, this.user, this.password);
            } else {
                conn = DriverManager.getConnection(this.url);
            }
        } catch (SQLException e) {
            log.error("Failed to connect to database at " + this.url, e);
        }
        return conn;
    }

    /**
     * Gets a JOOQ DSLContext for the current database connection and dialect
     * @param conn connection to use
     * @return DSLContext
     */
    public DSLContext getDSLContext(Connection conn) {
        return DSL.using(conn, this.dialect);
    }

    /**
     * Initialises the database using the sql script included in resources
     */
    public void resetDB() {
        try {
            InputStream in = getClass().getResourceAsStream("/sql/initialise_database.sql");
            executeSQLScript(in);
        } catch (NullPointerException e) {
            log.error("Error loading database initialisation file", e);
        }
    }


    /**
     * Gets path to an sqlite database named "database.sb" in the same directory as the jar file
     * @return jdbc encoded url location of database
     */
    private String getSQLiteDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:"+jarDir.getParentFile()+"/database.db";
    }

    /**
     * Check that an SQLite database exists at the specified location
     * @param url expected location to check for an SQLite database
     * @return True if database exists else false
     */
    private boolean checkSQLiteDatabaseExists(String url){
        File f = new File(url.substring(12));
        return f.exists();
    }

    /**
     * Creates an SQLite database file at the location specified by the url
     * @param url url to creat database at
     */
    private void createSQLiteDatabaseFile(String url){
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                String metaDriverLog = String.format("A new database has been created. The driver name is %s", meta.getDriverName());
                log.info(metaDriverLog);
            }
        } catch (SQLException e) {
            log.error(String.format("Error creating new database file url:%s", url));
            log.error(e);
        }
    }

    /**
     * Reads and executes all statements within the sql file provided
     * Note that each statement must be separated by '--SPLIT' this is not a desired limitation but allows for a much
     * wider range of statement types.
     * @param sqlFile input stream of file containing sql statements for execution (separated by --SPLIT)
     */
    private void executeSQLScript(InputStream sqlFile) {
        String s;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sqlFile))) {
            while((s=br.readLine()) != null) {
                sb.append(s).append("\n");
            }

            String[] individualStatements = sb.toString().split("--SPLIT");
            try (Connection conn = this.connect()) {
                DSLContext context = getDSLContext(conn);
                for (String singleStatement : individualStatements) {
                    String trimmed = singleStatement.trim();
                    if (trimmed.endsWith(";")) { // remove trailing semicolons (otherwise JOOQ complains)
                        trimmed = trimmed.substring(0, trimmed.length() - 1);
                    }
                    if (!trimmed.isEmpty()) {
                        try {
                            // Use JOOQ to translate the query from MariaDB to the current dialect
                            context.execute(DSL.using(SQLDialect.MARIADB).parser().parseQuery(trimmed));
                        } catch (Exception e) {
                            log.error("Error executing statement: " + trimmed, e);
                            throw e;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Error could not find specified database initialisation file", e);
        } catch (IOException e) {
            log.error("Error working with database initialisation file", e);
        } catch (SQLException e) {
            log.error("Error executing sql statements in database initialisation file", e);
        }
    }
}