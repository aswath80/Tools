/**
 * XslTransformer.java
 * Created By: E.Manikandan on Oct 9, 2011
 */
package com.mani.tools.xsl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLParseException;
import oracle.xml.parser.v2.XSLException;
import oracle.xml.parser.v2.XSLProcessor;
import oracle.xml.parser.v2.XSLStylesheet;

import org.xml.sax.SAXException;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class XslTransformer
{
   public static void transform(String xmlFileName, String xslFileName)
         throws IOException, XSLException, XMLParseException, SAXException
   {
      XSLProcessor xslProcessor = new XSLProcessor();
      xslProcessor.setErrorStream(System.out);
      XSLStylesheet xslStylesheet =
            xslProcessor
                  .newXSLStylesheet(new FileReader(new File(xslFileName)));
      
      DOMParser parser = new DOMParser();
      parser.parse(new FileReader(new File(xmlFileName)));
      XMLDocument xmlDocument = parser.getDocument();
      
      xslProcessor.processXSL(xslStylesheet, xmlDocument, System.out);
   }
   
   public static void main(String[] args) throws Exception
   {
      XslTransformer.transform("C:\\Mani\\Documents\\CTIS\\Inova\\I3E\\Radiology\\Siemens_Radiology_2.2.xml", "C:\\Mani\\Documents\\CTIS\\Inova\\I3E\\svn\\RosettaStone_Src\\Src\\Integration Hub\\HTB\\TranformationPipeline\\Transforms\\RADTransform.xsl");
   }
}
