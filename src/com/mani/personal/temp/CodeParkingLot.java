/**
 * CodeParkingLot.java
 * Creation Date: Aug 10, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

import java.util.Stack;

import oracle.apps.ctb.fwk.base.common.CTBException;
import oracle.apps.ctb.fwk.base.common.CTBRuntimeException;
import oracle.apps.ctb.hl7.common.RimObjectException;
import oracle.xml.parser.schema.XSDComplexType;
import oracle.xml.parser.schema.XSDElement;

import com.mani.personal.tools.hl7.RIMConstants;
import com.mani.personal.tools.hl7.RIMConstants.RIM_OBJECT_TYPE;
import com.mani.personal.tools.hl7.schema.util.XSDUtil;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class CodeParkingLot
{
   private Stack<String> xPathStack = new Stack<String>();
   
   private void pushRimXPathData(XSDComplexType parentXsdComplexType,
         XSDElement xsdElement)
   {
      XSDComplexType xsdComplexType = XSDUtil.getXsdComplexType(xsdElement);
      RIM_OBJECT_TYPE rimObjectType =
            XSDUtil.getRimObjectType(parentXsdComplexType, xsdComplexType);
      switch (rimObjectType)
      {
      case ACT:
         String classCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.CLASS_CODE).toString();
         String moodCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.MOOD_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(Act" + classCode + "/"
               + moodCode + ")");
         break;
      case ENTITY:
         classCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.CLASS_CODE).toString();
         String determinerCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.DETERMINER_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(Entity" + classCode + "/"
               + determinerCode + ")");
         break;
      case ROLE:
         classCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.CLASS_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(Role" + classCode + ")");
         break;
      case ACT_RELATIONSHIP:
         String typeCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.TYPE_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(ActRelationship" + typeCode
               + ")");
         break;
      case PARTICIPATION:
         typeCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.TYPE_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(Participation" + typeCode
               + ")");
         break;
      case COMMUNICATION_FUNCTION:
         typeCode =
               XSDUtil.getDefaultAttributeValue(xsdComplexType,
                     RIMConstants.TYPE_CODE).toString();
         xPathStack.push(xsdElement.getName() + "(CommunicationFunction"
               + typeCode + ")");
         break;
      case MESSAGE:
         xPathStack.push(xsdElement.getName() + "(Message)");
         break;
      default:
         System.out.println(rimObjectType);
         xPathStack.push(xsdElement.getName() + "(UnknownRimType)");
      }
   }
   
   private String getXPathStringFromXPathStack()
   {
      StringBuilder builder = new StringBuilder();
      for (String xpathElement : xPathStack)
      {
         builder.append(xpathElement + ".");
      }
      builder.deleteCharAt(builder.length() - 1);
      return builder.toString();
   }
   
   private void debug(XSDElement xsdElement)
   {
      System.out.println(xsdElement.getName());
      System.out.println(((XSDComplexType) xsdElement.getType()).getBaseType()
            .getName());
      System.out.println(((XSDComplexType) xsdElement.getType()).getBaseType()
            .getNodeType() + " " + XSDComplexType.TYPE);
   }
   
   private void debugCTBException(String tab, CTBException ctbe)
   {
      if (ctbe != null)
      {
         System.err.println(tab + ctbe.getCode() + ":" + ctbe.getMessage());
         if (ctbe instanceof RimObjectException)
         {
            System.err.println("Failed RIM Object:"
                  + ((RimObjectException) ctbe).getIds());
         }
         Throwable cause = ctbe.getCause();
         debugThrowable(tab + "\t", cause);
         CTBException[] bundledExceptions = ctbe.getBundledExceptions();
         if (bundledExceptions != null)
         {
            for (CTBException ctbException : bundledExceptions)
            {
               debugCTBException(tab + "\t", ctbException);
            }
         }
      }
   }
   
   private void debugCTBRuntimeException(String tab, CTBRuntimeException ctbe)
   {
      if (ctbe != null)
      {
         System.err.println(tab + ctbe.getCode() + ":" + ctbe.getMessage());
         Throwable cause = ctbe.getCause();
         debugThrowable(tab + "\t", cause);
         Exception[] childExceptions = ctbe.getChildren();
         if (childExceptions != null)
         {
            for (Exception exception : childExceptions)
            {
               debugThrowable(tab + "\t", exception);
            }
         }
      }
   }
   
   private void debugThrowable(String tab, Throwable t)
   {
      if (t instanceof CTBRuntimeException)
      {
         debugCTBRuntimeException(tab + "\t", (CTBRuntimeException) t);
      }
      else if (t instanceof CTBException)
      {
         debugCTBException(tab + "\t", (CTBException) t);
      }
      else
      {
         System.err.println("Non-CTBException:" + t.toString());
      }
   }
   
}
