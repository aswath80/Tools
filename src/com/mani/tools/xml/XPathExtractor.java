/**
 * XPathExtractor.java
 * Created By: E.Manikandan on Oct 14, 2011
 */
package com.mani.tools.xml;

import java.io.File;
import java.io.FileInputStream;

import oracle.xml.parser.v2.SAXParser;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class XPathExtractor
{
   private static final String xmlFileName = "C:/Users/Mani/Eclipse-Workspace/Tools/src/temp/Siemens_Radiology_2.2.out.xml";
   
   public static void extractXmlXPath(String xmlFileName) throws Exception
   {
      SAXParser saxParser = new SAXParser();
      saxParser.setContentHandler(new XPathContentHandler());
      saxParser.parse(new FileInputStream(new File(xmlFileName)));
   }
   
   public static void main(String[] args) throws Exception
   {
      XPathExtractor.extractXmlXPath(xmlFileName);
   }
}
