package com.add;


import com.add.thread.FileDownloadHandleThread;

public class Main {
    public static void main(String[] args) {
        new Thread(new FileDownloadHandleThread()).start();
    }
}
