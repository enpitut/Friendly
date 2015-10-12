package com.yiibai.file;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class AppendToFile 
{
    public static void main( String[] args )
    { 
     try{
      String data = " 这里是新的要读入的内容";

      File file =new File("test.txt");

      //if file doesnt exists, then create it
      if(!file.exists()){
       file.createNewFile();
      }

      //true = append file
      FileWriter fileWritter = new FileWriter(file.getName(),true);
             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
             bufferWritter.write(data);
             bufferWritter.close();

         System.out.println("Done");

     }catch(IOException e){
      e.printStackTrace();
     }
    }
}