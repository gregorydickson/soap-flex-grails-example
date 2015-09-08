dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}
hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}
// environment specific settings
environments {
  development {
    dataSource {
        dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
        pooled = true
        //url="jdbc:mysql://labdb.cpzaxj0nihxk.us-east-1.rds.amazonaws.com:3306/labdb"
        //username="labdbuser"
        //password="labdbuser"
        // PRODUCTION
        //url = "jdbc:mysql://labdbprod.cozoij7to0qa.sa-east-1.rds.amazonaws.com:3306/labdb"
        //username="labdbuser"
        //password="labdbprod"
        // END PRODUCTION
        
        url="jdbc:mysql://localhost:3306/labdb"
        username="root"
        password=""

        properties {
          maxActive = 2
          maxIdle = 1
          minIdle = 1
          initialSize = 2
          minEvictableIdleTimeMillis = 60000
          timeBetweenEvictionRunsMillis = 60000
          maxWait = 10000
          validationQuery = "/* ping */"
          testOnBorrow=true
          testWhileIdle=true
          testOnReturn=true

        }
    }
  }
  
  production {
    dataSource {
          dbCreate = "update" 
          url = "jdbc:mysql://labdbprod.cozoij7to0qa.sa-east-1.rds.amazonaws.com:3306/labdb"
          username="labdbuser"
          password="labdbprod"

          pooled = true
          properties {
             maxActive = 30
             minEvictableIdleTimeMillis=1800000
             timeBetweenEvictionRunsMillis=1800000
             numTestsPerEvictionRun=3
             testOnBorrow=true
             testWhileIdle=true
             testOnReturn=true
             validationQuery="SELECT 1"
    
          }
      }
  }
  
}