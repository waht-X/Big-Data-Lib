package hbase.main;

import hbase.example.Example1;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        String ip = "192.168.1.108";
        Example1 example1 = new Example1(ip);

        put(example1);

        example1.close();
    }

    static void delete(Example1 example1) {
        try {
            example1.delete("studentInfo", "zhangsan", "score", "English");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void put(Example1 example1) {
        try {
            example1.put("studentInfo", "zhangsan", "score", null, "222");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
