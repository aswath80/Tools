/**
 * XPathContentHandler.java
 * Created By: E.Manikandan on Oct 14, 2011
 */
package com.mani.tools.xml;

import java.util.Arrays;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author E.Manikandan
 * @revision $Revision:$
 */
public class XPathContentHandler implements ContentHandler
{
   private final Stack<String> xPathStack = new Stack<String>();
   
   @Override
   public void startPrefixMapping(String prefix, String uri)
         throws SAXException
   {
      
   }
   
   @Override
   public void startElement(String uri, String localName, String qName,
         Attributes atts) throws SAXException
   {
      xPathStack.push(localName);
      if (atts != null)
      {
         for (int i = 0; i < atts.getLength(); i++)
         {
            System.out.println(getXPathString() + "." + atts.getLocalName(i));
         }
      }
   }
   
   @Override
   public void startDocument() throws SAXException
   {
      
   }
   
   @Override
   public void skippedEntity(String name) throws SAXException
   {
      
   }
   
   @Override
   public void setDocumentLocator(Locator locator)
   {
      
   }
   
   @Override
   public void processingInstruction(String target, String data)
         throws SAXException
   {
      
   }
   
   @Override
   public void ignorableWhitespace(char[] ch, int start, int length)
         throws SAXException
   {
      
   }
   
   @Override
   public void endPrefixMapping(String prefix) throws SAXException
   {
      
   }
   
   @Override
   public void endElement(String uri, String localName, String qName)
         throws SAXException
   {
      xPathStack.pop();
   }
   
   @Override
   public void endDocument() throws SAXException
   {
      
   }
   
   @Override
   public void characters(char[] ch, int start, int length) throws SAXException
   {
      if (ch != null && ch.length > 0)
      {
         String s = new String(Arrays.copyOfRange(ch, start, start + length));
         if (!s.trim().isEmpty())
         {
            System.out.println("#TEXT#" + s);
            System.out.println(getXPathString());
         }
      }
   }
   
   private String getXPathString()
   {
      StringBuilder builder = new StringBuilder();
      int size = xPathStack.size();
      int startIndex = 0;
      if (size > 4)
      {
         startIndex = size - 4;
      }
      for (int i = startIndex; i < size; i++)
      {
         builder.append(xPathStack.get(i));
         if (i != size - 1)
         {
            builder.append(".");
         }
      }
      return builder.toString();
   }
}
