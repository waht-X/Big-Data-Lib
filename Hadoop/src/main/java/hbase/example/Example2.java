package hbase.example;

import hbase.utils.ConnectToHBase;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Example2 {

    Connection connection = null;
    Admin admin = null;

    public Example2(String ip) throws IOException {
        connection = ConnectToHBase.getConnectionAsDefault(ip);
        admin = connection.getAdmin();
    }

    /**
     * 创建表，如果HBase已存在名为tableName的表的时候，先删除原有表，再创建表
     * @param tableName 需要创建的表的名称
     * @param fields  存储记录各个域名称的数组
     */
    public void createTable(String tableName, String[] fields) throws IOException {
        TableName tableNameEntity = TableName.valueOf(tableName);
        if (admin.tableExists(tableNameEntity)) {
            //如果表存在，则现删除原表
            dropTable(tableName);
        }
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableNameEntity);
        for (String field : fields) {
            ColumnFamilyDescriptor column = ColumnFamilyDescriptorBuilder.newBuilder(field.getBytes()).build();
            tableDescriptorBuilder.setColumnFamily(column);
        }
        admin.createTable(tableDescriptorBuilder.build());
    }

    /**
     * 删除表
     * @param tableName 需要删除的表的名称
     */
    public void dropTable(String tableName) throws IOException {
        admin.disableTable(TableName.valueOf(tableName));
        admin.deleteTable(TableName.valueOf(tableName));
    }

    /**
     * 向指定单元格添加对应数据values
     * @param tableName 需要进行操作的表名
     * @param row  行键
     * @param fields 列族，如果每个元素对应的列族下还有相应的列限定符，用“columnFamily:column”表示
     * @param values 存储对应列族的值
     */
    public void addRecord(String tableName, String row, String[] fields, String[] values) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<Put> puts = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Put put = new Put(row.getBytes());
            //判断是否有列限定符
            boolean hasQualify = fields[i].indexOf(":") > 0;
            String family = fields[i].split(":")[0];
            String quality = hasQualify ? fields[i].split(":")[1] : "";
            put.addColumn(family.getBytes(), quality.getBytes(), values[i].getBytes());
            puts.add(put);
        }
        table.put(puts);
        table.close();
    }

    /**
     * 浏览tableName某一列的数据，如果某一行记录中该数据不存在，则返回null。
     * 当参数column为某一列族名称时，如果底下有若干个限定符，则列出每个列限定符代表的列的数据；
     * 当参数column为某一列具体名称，如：score：Math时，只需要列出该列的数据
     * @param tableName
     * @param column
     */
    public ResultScanner scanColumn(String tableName, String column) throws IOException{
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        //判断是否有列限定符
        boolean hasQualify = column.indexOf(":") > 0;
        String family = column.split(":")[0];
        String quality = hasQualify ? column.split(":")[1] : "";
        if (hasQualify) {
            scan.addColumn(family.getBytes(), quality.getBytes());
        } else {
            scan.addFamily(family.getBytes());
        }
        ResultScanner scanner = table.getScanner(scan);
        table.close();
        return scanner;
    }

    /**
     * 打印出ResultScanner的值
     */
    public void printResultScanner(ResultScanner results) {
        Iterator<Result> iterator = results.iterator();
        while (iterator.hasNext()) {
            Result next = iterator.next();
            List<Cell> cells = next.listCells();
            for (Cell cell : cells) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String row = Bytes.toString(CellUtil.cloneRow(cell));
                String family = Bytes.toString(CellUtil.cloneFamily(cell));
                String qualify = Bytes.toString(CellUtil.cloneQualifier(cell));
                if (qualify.isEmpty()) {
                    qualify = "null";
                }
                System.out.printf("row:%s family:%s qualify:%s value:%s\n", row, family, qualify, value);
            }
        }
    }

    /**
     * 修改表tableName，行row，列column指定的单元格的数据
     */
    public void modifyData(String tableName, String row, String column, String value) throws IOException{
        addRecord(tableName, row, new String[]{column}, new String[]{value});
    }

    /**
     * 删除tableName中row指定的行的记录
     */
    public void deleteRow(String tableName, String row) throws IOException{
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(row.getBytes());
        table.delete(delete);
        table.close();
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
