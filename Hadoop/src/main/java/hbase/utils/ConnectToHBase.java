package hbase.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class ConnectToHBase {
    public static Connection getConnectionAsDefault(String ip) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("hbase.rootdir", "hdfs://" + ip + ":9000/hbase");
        conf.set("hbase.zookeeper.quorum", ip);
        conf.set("hbase.zookeeper.property.clientPort","2181");
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }


}
