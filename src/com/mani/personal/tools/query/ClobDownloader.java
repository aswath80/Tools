/**
 * ClobDownloader.java
 * Created By: E.Manikandan on Oct 14, 2011
 */
package com.mani.personal.tools.query;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class ClobDownloader
{
   private static final String clobQuery        = "select  message.clob_value from "
                                                      + "b2b_business_message bus, b2b_data_storage message, b2b_ext_business_message ext "
                                                      + "where bus.sender_name = 'Siemens' and ext.business_message = bus.id "
                                                      + "and bus.state='MSG_COMPLETE' and trunc(bus.created) > sysdate-4 "
                                                      + "and bus.payload_storage=message.id order by bus.created desc";
   private static final String clobQuery2       = "select  message.clob_value   "
                                                      + "from b2b_business_message bus, "
                                                      + "b2b_wire_message wire, "
                                                      + "b2b_data_storage message, "
                                                      + "b2b_ext_business_message ext "
                                                      + "where bus.wire_message = wire.id "
                                                      + "and wire.payload_storage = message.id "
                                                      + "and bus.sender_name='Siemens'  "
                                                      + "and ext.business_message = bus.id "
                                                      + "order by bus.created desc ";
   private String              dir              = "C:\\Mani\\Documents\\CTIS\\Inova\\I3E\\Radiology\\V2Messages\\";
   private String              fileNameTemplate = "RadiologySampleV2_Pipe_$1.txt";
   
   private void downloadClob(String sql) throws Exception
   {
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      Connection conn = null;
      Statement stmt = null;
      try
      {
         conn = DriverManager.getConnection(
               "jdbc:oracle:thin:@oraqa.net.inova.org:1521:SOAINFQA",
               "qa_soainfra", "rtgv7777");
         stmt = conn.createStatement();
         ResultSet rslt = stmt.executeQuery(sql);
         int fileIndex = 1;
         String fileName;
         char[] cbuf = new char[1024];
         while (rslt.next())
         {
            fileName = fileNameTemplate.replace("$1",
                  String.valueOf(fileIndex++));
            FileWriter writer = new FileWriter(new File(dir + fileName));
            Clob clob = rslt.getClob(1);
            Reader reader = clob.getCharacterStream();
            int len;
            while ((len = reader.read(cbuf)) == cbuf.length)
            {
               writer.write(cbuf);
            }
            writer.write(cbuf, 0, len);
            writer.close();
         }
      }
      finally
      {
         if (stmt != null)
         {
            stmt.close();
         }
         if (conn != null)
         {
            conn.close();
         }
      }
   }
   
   public static void main(String[] args) throws Exception
   {
      new ClobDownloader().downloadClob(clobQuery2);
   }
}
