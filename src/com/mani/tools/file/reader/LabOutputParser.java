package com.mani.tools.file.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LabOutputParser
{
   public void parserLabSqlOutput(String outputFileName) throws IOException
   {
      BufferedReader reader = null;
      List<String> attrList = new ArrayList<String>();
      try
      {
         reader = new BufferedReader(new FileReader(outputFileName));
         String line;
         int recordCount = 0;
         while ((line = reader.readLine()) != null)
         {
            if (!line.trim().isEmpty())
            {
               attrList.add(line.trim());
            }
            else
            {
               recordCount++;
               for (int i = 0; i < attrList.size(); i++)
               {
                  System.out.print(attrList.get(i) + " , ");
               }
               System.out.println();
               attrList.clear();
            }
         }
         System.out.println("Total records = " + recordCount);
      }
      finally
      {
         if (reader != null)
         {
            reader.close();
         }
      }
   }
   
   public static void main(String[] args) throws IOException
   {
      new LabOutputParser()
            .parserLabSqlOutput("C:\\Mani\\Documents\\CTIS\\Inova\\I3E\\LabTempTable\\Lab_Output.txt");
   }
}
