/**
 * MessageResender.java
 * Created By: E.Manikandan on Oct 20, 2011
 */
package com.mani.tools;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class MessageResender
{
   private QueueSession qSession;
   private QueueSender  qSender;
   private Connection   conn;
   
   public MessageResender()
   {
      
   }
   
   private void initInovaQAStageConnection() throws SQLException
   {
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@nbaixorq001d:1521:soainfqa", "gecestage",
            "gecestage123");
      System.out.println("Connection " + conn + " created");
   }
   
   private void initInovaQAJMS() throws NamingException, JMSException
   {
      Hashtable<String, String> environment = new Hashtable<String, String>();
      environment.put(InitialContext.INITIAL_CONTEXT_FACTORY,
            "weblogic.jndi.WLInitialContextFactory");
      environment.put(InitialContext.PROVIDER_URL, "t3://nbaixorq020a:8001");
      environment.put(InitialContext.SECURITY_PRINCIPAL, "weblogic");
      environment.put(InitialContext.SECURITY_CREDENTIALS, "rtgv7777");
      InitialContext initialContext = new InitialContext(environment);
      QueueConnectionFactory conFactory = (QueueConnectionFactory) initialContext
            .lookup("jms.b2b.B2BQueueConnectionFactory");
      QueueConnection qCon = conFactory.createQueueConnection();
      qSession = qCon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue queue = (Queue) initialContext
            .lookup("jms/resend/SIEMENS_ORU_RAD_QUEUE");
      qSender = qSession.createSender(queue);
   }
   
   private void sendV2MessagesToQueue() throws SQLException, IOException,
         NamingException, JMSException
   {
      initInovaQAStageConnection();
      initInovaQAJMS();
      
      StringBuilder sb = new StringBuilder();
      Statement stmt = null;
      
      ResultSet rslt = null;
      try
      {
         stmt = conn.createStatement();
         rslt = stmt
               .executeQuery("select message_txt from pipeline_v2_messages where last_status = 'ERROR' "
                     + "and doc_type_name = 'ORU_R01' and from_party='Siemens'");
         while (rslt.next())
         {
            Clob clob = rslt.getClob(1);
            Reader reader = clob.getCharacterStream();
            
            char[] buf = new char[1024];
            int count;
            while ((count = reader.read(buf)) == 1024)
            {
               sb.append(new String(buf));
            }
            if (count > 0)
            {
               sb.append(new String(buf, 0, count));
            }
            System.out.println("Retrived message " + sb.toString());
            sendJMSMessage(sb.toString());
            sb.delete(0, sb.length());
         }
      }
      finally
      {
         if (rslt != null)
         {
            rslt.close();
         }
         if (stmt != null)
         {
            stmt.close();
         }
         if (conn != null)
         {
            conn.close();
         }
         if (qSession != null)
         {
            qSession.close();
         }
      }
   }
   
   private void sendJMSMessage(String v2Message) throws NamingException,
         JMSException, SQLException, IOException
   {
      BytesMessage byteMessage = qSession.createBytesMessage();
      
      byteMessage.writeBytes(v2Message.getBytes());
      byteMessage.setStringProperty("DOCTYPE_NAME", "ORU_R01");
      byteMessage.setStringProperty("MSG_RECEIVED_TIME", new SimpleDateFormat(
            "EEE MMM d HH:mm:ss z yyyy").format(Calendar.getInstance()
            .getTime()));
      byteMessage.setStringProperty("DOCTYPE_REVISION", "2.2");
      byteMessage.setStringProperty("FROM_PARTY", "Siemens");
      qSender.send(byteMessage);
      System.out.println("Message of size " + v2Message.length() + " sent");
   }
   
   public static void main(String[] args) throws Exception
   {
      new MessageResender().sendV2MessagesToQueue();
   }
}
