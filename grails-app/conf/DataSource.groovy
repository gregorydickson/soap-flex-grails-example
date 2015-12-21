dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"

    dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
    url = "jdbc:mysql://localhost:3306/labdb"

    properties {
        maxActive = 100                           //The maximum number of active connections that can be allocated from this pool at the same time..
        initialSize = 5                           //The initial number of connections that are created when the pool is started.
        minIdle = 5                               //The minimum number of active connections that can remain idle in the pool
        maxIdle = 10                              //The maximum number of connections that can remain idle in the pool
        testWhileIdle = true                      //Test connection health while it's sleeping
        testOnBorrow = true                       //Test connection health at taking it
        testOnReturn = false                      //Test connection health at returning it to the pool
        validationQuery = "SELECT 1"              //MySql SQL query used to validate connections from the pool before returning them to the caller
        validationQueryTimeout = 1                //The timeout in seconds before connection validation queries fail.
        validationInterval = 30 * 1000            //avoid excess validation, only run validation at most at this frequency - time in milliseconds. If a connection is due for validation, but has been validated previously within this interval, it will not be validated again. The default value is 30000 (30 seconds).
        maxWait = 10 * 1000                       //The maximum number of milliseconds that the pool will wait for a connection to be returned before throwing an exception.
        maxAge = 60 * 60 * 1000                   //Time in milliseconds to keep this connection. When a connection is returned to the pool, the pool will check to see if the now - time-when-connected > maxAge has been reached, and if so, it closes the connection rather than returning it to the pool. The default value is 0, which implies that connections will be left open and no age check will be done upon returning the connection to the pool.
        minEvictableIdleTimeMillis = 5 * 1000     //The minimum amount of time an object may sit idle in the pool before it is eligable for eviction by the idle object evictor (if any).
        timeBetweenEvictionRunsMillis = 15 * 1000 //The number of milliseconds to sleep between runs of the idle object evictor thread.
        // causes stacktrace recording overhead, use only for debugging
        logAbandoned = true                       // (Tomcat JDBC Pool only, boolean) Flag to log stack traces for application code which abandoned a Connection.
        // controls for leaked connections
        removeAbandoned = false                    //Flag to remove abandoned connections if they exceed the removeAbandonedTimout
        removeAbandonedTimeout = 180              //Timeout in seconds before an abandoned connection can be removed. A connection is considered abandoned and eligible for removal if it has been in use longer than the removeAbandonedTimeout. This way db connections can be recovered from applications that fail to close a connection. The value should be set to the longest running query your applications might have.
        abandonWhenPercentageFull = 10            //(Tomcat JDBC Pool only, int) Connections that have been abandoned (timed out) wont get closed and reported up unless the number of connections in use are above the percentage defined by abandonWhenPercentageFull.

        //http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html#JDBC_interceptors
        jdbcInterceptors = "ConnectionState;StatementCache(max=200);QueryTimeoutInterceptor(queryTimeout=50);SlowQueryReport(threshold=3000,maxQueries=50);"
        defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED // safe default
    }
    configClass = org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}

// environment specific settings
environments {
    development {
        dataSource {
            username="root"
            password=""
        }
    }

    scrape {
        dataSource {
            dbCreate = "validate"
            url = "jdbc:mysql://labdbprod.cozoij7to0qa.sa-east-1.rds.amazonaws.com:3306/labdb"
            username="labdbuser"
            password="labdbprod"
        }
    }
    test {
        dataSource {
            url = System.getProperty("JDBC_CONNECTION_STRING")
        }
    }
    production {
        dataSource {
            url = System.getProperty("JDBC_CONNECTION_STRING")
        }
    }
}