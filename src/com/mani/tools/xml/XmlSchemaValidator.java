/**
 * XmlSchemaValidator.java
 * Created By: E.Manikandan on Oct 13, 2011
 */
package com.mani.tools.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import oracle.xml.parser.schema.XMLSchema;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLParseException;
import oracle.xml.parser.v2.XMLParser;

import org.xml.sax.SAXException;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class XmlSchemaValidator
{
   public static void validateXml(String xmlFileName, String xsdFileName)
         throws IOException, XSDException, XMLParseException, SAXException
   {
      // Build Schema Object
      XSDBuilder builder = new XSDBuilder();
      FileInputStream xsdInStream = new FileInputStream(xsdFileName);
      XMLSchema schemadoc = (XMLSchema) builder.build(xsdInStream, null);
      // Parse the input XML document with Schema Validation
      
      FileInputStream xmlInStream = new FileInputStream(xmlFileName);
      DOMParser dp = new DOMParser();
      // Set Schema Object for Validation
      dp.setXMLSchema(schemadoc);
      dp.setValidationMode(XMLParser.SCHEMA_VALIDATION);
      dp.setPreserveWhitespace(true);
      dp.setErrorStream(new PrintWriter(System.out));
      
      dp.parse(xmlInStream);
      
      System.out.println("The input XML parsed without errors.");
   }
   
   public static void main(String[] args) throws Exception
   {
      XmlSchemaValidator
            .validateXml(
                  "C:/Mani/Documents/CTIS/nova/I3E/Radiology/Siemens_Radiology_2.2.out.xml",
                  "C:/Users/Mani/Eclipse-Workspace/Tools/src/defs/rim214101/schemas/POLB_IN004202.xsd");
   }
}
