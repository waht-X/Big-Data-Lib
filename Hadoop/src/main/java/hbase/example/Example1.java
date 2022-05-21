package hbase.example;

import hbase.utils.ConnectToHBase;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Example1 {

    private Connection connection = null;
    private Admin admin = null;

    public Example1(String ip) {
        connection = ConnectToHBase.getConnectionAsDefault(ip);
        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受多个传入的参数<br>
     * create 'student', 'row', 'column...'
     */
    public void create(String ...columns) throws IOException {
        TableName tableName = TableName.valueOf(columns[0]);
        if (admin.tableExists(tableName)) {
            dropTable(columns[0]);
        }
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
        for (int i = 1; i < columns.length; i++) {
            ColumnFamilyDescriptor columnFamily = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columns[i])).build();
            tableDescriptorBuilder.setColumnFamily(columnFamily);
        }
        admin.createTable(tableDescriptorBuilder.build());
    }

    /**
     * 进行数据的插入操作<br>
     * put 'tableName', 'row', 'column', 'values'<br>
     * for example:<br>
     * put 'student', 'zhangsan', 'score', '56'<br>
     * or<br>
     * put 'student', 'lisi', 'score.English', '78'
     *
     */
    public void put(String tableName, String row, String family, String qualify, String value) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(row.getBytes());
        put.addColumn(family.getBytes(), qualify == null ? null : qualify.getBytes(), value.getBytes());
        table.put(put);
//        admin.flush(TableName.valueOf(tableName));
        table.close();

    }

    public Result get(String tableName, String row, String family, String qualify) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(row.getBytes());
        get.addColumn(family.getBytes(), qualify.getBytes());
        Result result = table.get(get);
        table.close();
        return result;

    }

    public Result[] gets(String tableName, String row, String family, String[] qualifies) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<Get> list = new ArrayList<>();
        for (String qualify : qualifies) {
            Get get = new Get(row.getBytes());
            get.addColumn(family.getBytes(), qualify.getBytes());
            list.add(get);
        }
        Result[] results = table.get(list);
        return results;
    }

    public void delete(String tableName, String row, String family, String qualify) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(row.getBytes());
        delete.addColumn(family.getBytes(), qualify.getBytes());
        table.delete(delete);
        table.close();
    }

    public ResultScanner scan(String tableName, String family) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(family.getBytes());
        return scanner;
    }

    public void dropTable(String tableName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        admin.disableTable(table.getName());
        admin.deleteTable(table.getName());
    }

    public void close() {
        try {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
