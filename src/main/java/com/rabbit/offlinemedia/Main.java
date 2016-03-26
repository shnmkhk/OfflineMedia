package com.rabbit.offlinemedia;

public class Main{
    public static void main(String[] argv) throws Exception{
            String anim= "|/-\\";
            for (int x =0 ; x < 100 ; x++){
                    String data = "\r" + anim.charAt(x % anim.length())  + " " + x ;
                    System.out.write(data.getBytes());
                    Thread.sleep(100);
            }
    }
}