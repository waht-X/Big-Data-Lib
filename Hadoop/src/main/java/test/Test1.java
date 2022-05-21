package test;

import java.math.*;

public class Test1 {
    public static void main(String[] args) {
        BigInteger a1 = new BigInteger("123");
        BigInteger a2 = new BigInteger("456");
        System.out.println(a1.multiply(a2));    //a1 * a2
        System.out.println(a1.add(a2)); //a1 + a2
        System.out.println(a1.subtract(a2));    //a1 - a2
        System.out.println(a1.divide(a2));  //a1 / a2

    }
}
