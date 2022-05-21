package hbase.main;

import hbase.example.Example1;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

public class TestExample1 {
    public static void main(String[] args) {

        String ip = "192.168.1.108";
        Example1 example1 = new Example1(ip);
        try {

            String tableName = "studentInfo";
            String family = "score";

            //创建表
            example1.create("studentInfo", "name", "score");

            //插入数据
            example1.put(tableName, "zhangsan", family, "English", "78");
            example1.put(tableName, "zhangsan", family, "Math", "55");
            example1.put(tableName, "zhangsan", family, "Computer", "87");

            example1.put(tableName, "lisi", family, "English", "23");
            example1.put(tableName, "lisi", family, "Math", "88");
            example1.put(tableName, "lisi", family, "Computer", "99");

            //查询studentInfo表的插入数据结果
            String[] qualifies = {"English", "Math", "Computer"};
            Result[] ZS_results = example1.gets("studentInfo", "zhangsan", "score", qualifies);
            Result[] LS_results = example1.gets("studentInfo", "lisi", "score", qualifies);

            //显示数据
            System.out.println("---------------zhangsan---------------");
            for (int i = 0; i < ZS_results.length; i++) {
                String value = new String(ZS_results[i].getValue(family.getBytes(), qualifies[i].getBytes()));
                System.out.print(value + ",");
            }
            System.out.println();
            System.out.println("---------------lisi---------------");
            for (int i = 0; i < LS_results.length; i++) {
                String value = new String(LS_results[i].getValue(family.getBytes(), qualifies[i].getBytes()));
                System.out.print(value + ",");
            }
            System.out.println();
            //修改zhangsan的English的值
            System.out.println("--------------update-----------------");
            example1.put(tableName, "zhangsan", family, "English", "0");
            String value = new String(example1.get(tableName, "zhangsan", family, "English")
                    .getValue(family.getBytes(), "English".getBytes()));
            System.out.println("修改后的score:English数据：" + value);

            //删除数据
            System.out.println("------------------delete--------------");
            example1.delete(tableName, "zhangsan", family, "English");
            ResultScanner scan = example1.scan(tableName, family);
            Iterator<Result> iterator = scan.iterator();
            while (iterator.hasNext()) {
                Result next = iterator.next();
                NavigableMap<byte[], byte[]> familyMap = next.getFamilyMap(family.getBytes());
                for (Map.Entry<byte[], byte[]> m : familyMap.entrySet()) {
                    System.out.printf("%s, %s\n", new String(m.getKey()), new String(m.getValue()));
                }
                System.out.printf("\n----------------------------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        example1.close();
    }
}
