package org.jpwh.env;

import bitronix.tm.resource.jdbc.PoolingDataSource;

import java.util.Properties;

public enum DatabaseProduct {

    ORACLE(
            new DataSourceConfiguration() {
                @Override
                public void configure(PoolingDataSource ds, String connectionURL) {
                    ds.setClassName("oracle.jdbc.xa.client.OracleXADataSource");
                    ds.getDriverProperties().put(
                            "URL",
                            connectionURL != null
                                    ? connectionURL :
                                    "jdbc:oracle:thin:test/test@192.168.56.101:1521:xe"
                    );

                    // Required for reading VARBINARY/LONG RAW columns easily, see
                    // http://stackoverflow.com/questions/10174951
                    Properties connectionProperties = new Properties();
                    connectionProperties.put("useFetchSizeWithLongColumn", "true");
                    ds.getDriverProperties().put("connectionProperties", connectionProperties);
                }
            },
            org.hibernate.dialect.Oracle10gDialect.class.getName()
    ),


    MYSQL (
            (ds, connectionURL) -> {
                ds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
                ds.getDriverProperties().put( "url", connectionURL );
                ds.getDriverProperties().put("user", "arvind");
                ds.getDriverProperties().put("password", "Password12!");
                ds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");

                ds.setMaxPoolSize(5);
                ds.setAllowLocalTransactions(true);

            },
            org.hibernate.dialect.MySQL57InnoDBDialect.class.getName()
    );


    public  DataSourceConfiguration configuration;
    public String hibernateDialect;


    private DatabaseProduct(DataSourceConfiguration configuration,
                            String hibernateDialect) {

        this.configuration = configuration;
        this.hibernateDialect = hibernateDialect;
    }

    public interface DataSourceConfiguration {
        void configure(PoolingDataSource ds, String connectionUrl);
    }

}
