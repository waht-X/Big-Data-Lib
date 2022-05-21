package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HadoopExample1 {
    public static void main(String[] args) {
        //安装Hbase的虚拟机地址
        String ip = "192.168.1.108";
        Configuration conf = HBaseConfiguration.create();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("hbase.rootdir", "hdfs://" + ip + ":9000/hbase");
        //hbase 服务地址 zookeeper
        conf.set("hbase.zookeeper.quorum", ip);
        //端口号默认2128不用改
        conf.set("hbase.zookeeper.property.clientPort","2181");
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(conf);
            Admin admin = connection.getAdmin();
            TableName[] tableNames = admin.listTableNames();
            for (TableName tableName : tableNames) {
                System.out.println((tableName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
