package hbase.main;

import hbase.example.Example2;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.io.IOException;

public class TestExample2 {
    public static void main(String[] args) {

        String ip = "192.168.1.108";
        try {
            Example2 example2 = new Example2(ip);
            //测试创建表
            String[] fields = {"sNo", "student", "course"};
            example2.createTable("sc", fields);

            //测试添加数据
            String[] fields2 = {"student:sName", "student:sSex", "student:sAge",
                                "course:Math", "course:computer science", "course:English"};
            String[] zhangsan = {"Zhangsan", "male", "23", "86", "", "69"};
            String[] mary =  {"Mary", "female", "22", "", "77", "99"};
            String[] lisi = {"Lisi", "male", "24", "98", "95", ""};
            example2.addRecord("sc", "2015001", fields2, zhangsan);
            example2.addRecord("sc", "2015002", fields2, mary);
            example2.addRecord("sc", "2015003", fields2, lisi);

            //测试修改数据
            example2.modifyData("sc", "2015001", "course:Math", "100");

            //测试删除数据
            example2.deleteRow("sc", "2015002");

            //测试输出数据
            ResultScanner results = example2.scanColumn("sc", "course");
            example2.printResultScanner(results);

            example2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
