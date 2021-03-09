package com.add.demo;

import org.junit.Test;

import java.io.File;

public class FileQueryTest {

    @Test
    public  void test1(){
        Integer id = 2;
        String path = "E:\\MyFTP";
        File file = new File(path + "\\" + id.toString());
        System.out.println(file.isDirectory());
        String[] list = file.list();
        /*for (String name : list){
            System.out.println(name);
        }*/

    }
}
