/**
 * XPathTest.java
 * Created By: E.Manikandan on Oct 20, 2011
 */
package com.mani.tools.xml;

import java.io.FileReader;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class XPathTest
{
   public static void testXPath() throws Exception
   {
      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPath xpath = xpathFactory.newXPath();
      xpath.setNamespaceContext(new NamespaceContext()
      {
         @Override
         public Iterator getPrefixes(String namespaceURI)
         {
            System.out.println("Called");
            return null;
         }
         
         @Override
         public String getPrefix(String namespaceURI)
         {
            System.out.println(namespaceURI + " maps to ns0");
            return "ns0";
         }
         
         @Override
         public String getNamespaceURI(String prefix)
         {
            System.out
                  .println("namespaceURI=NS_EF42F4DF79A141DB890D0269F38F622B20070423183755");
            return "NS_EF42F4DF79A141DB890D0269F38F622B20070423183755";
         }
      });
      String pat = "/ns0:ORU_R01/ns0:ORU_R01.PATIENT_RESULT/ns0:ORU_R01.PATIENT/ns0:PID/ns0:PID.3/ns0:CM_PAT_ID.1";
      String msg = "/ns0:ORU_R01/ns0:MSH/ns0:MSH.10";
      XPathExpression xPathExpression1 = xpath.compile(pat);
      XPathExpression xPathExpression2 = xpath.compile(msg);
      XPathExpression xPathExpression3 = xpath
            .compile("/ns0:ORU_R01/ns0:EVN/ns0:EVN.4");
      String fileName = "C:\\Mani\\Documents\\CTIS\\Inova\\I3E\\Radiology\\V2_ORU.xml";
      DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
      dFactory.setNamespaceAware(true);
      DocumentBuilder docBuilder = dFactory.newDocumentBuilder();
      docBuilder = dFactory.newDocumentBuilder();
      dFactory.setNamespaceAware(true);
      dFactory.setIgnoringElementContentWhitespace(true);
      dFactory.setValidating(false);
      
      InputSource ipSource = new InputSource(new FileReader(fileName));
      Document doc = docBuilder.parse(ipSource);
      String value1 = (String) xPathExpression1.evaluate(doc,
            XPathConstants.STRING);
      System.out.println("value1=" + value1);
      String value2 = (String) xPathExpression2.evaluate(doc,
            XPathConstants.STRING);
      System.out.println("value2=" + value2);
      String value3 = (String) xPathExpression3.evaluate(doc,
            XPathConstants.STRING);
      System.out.println("value3=" + value3);
   }
   
   public static void main(String[] args) throws Exception
   {
      XPathTest.testXPath();
   }
}
