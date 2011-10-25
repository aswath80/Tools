/**
 * V3SchemaParser.java
 * Creation Date: Aug 3, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.schema.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import oracle.xml.parser.schema.XMLSchema;
import oracle.xml.parser.schema.XMLSchemaNode;
import oracle.xml.parser.schema.XSDAttribute;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XSDComplexType;
import oracle.xml.parser.schema.XSDElement;
import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.schema.XSDNode;

import com.mani.personal.tools.hl7.RIMConstants.RIM_OBJECT_TYPE;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class V3SchemaParser
{
	private static final String HL7_NAMESPACE = "urn:hl7-org:v3";
	// private static final int DEPTH = 6;
	
	private final Stack<String> xPathStack = new Stack<String>();
	private final Stack<String> complexTypeStack = new Stack<String>();
	private final Stack<String> outputStack = new Stack<String>();
	
	private final Map<XSDElement, Set<String>> rimAttributeMap = new HashMap<XSDElement, Set<String>>();
	private final Map<String, Set<String>> complexTypeVisitMap = new HashMap<String, Set<String>>();
	private String interactionId;
	
	public void parseV3Schema(String schemaFileName) throws XSDException,
	         FileNotFoundException, MalformedURLException
	{
		clearCachedData();
		
		XSDBuilder xsdBuilder = new XSDBuilder();
		File schemaFile = new File(schemaFileName);
		interactionId = schemaFile.getName().substring(0,
		         schemaFile.getName().lastIndexOf("."));
		System.out.println("InteractionId: " + interactionId);
		XMLSchema xmlSchema = xsdBuilder.build(schemaFile.toURI().toURL());
		XMLSchemaNode xmlSchmeNode = xmlSchema.getSchemaByTargetNS(HL7_NAMESPACE);
		
		parseSchemaForXPaths(null,
		         getElementFromSchemaNode(xmlSchmeNode, interactionId));
		
		xmlSchmeNode = null;
		xmlSchema = null;
		xsdBuilder = null;
		
		writeOutputToFile();
	}
	
	/**
	 * Method clearCachedData
	 */
	private void clearCachedData()
	{
		xPathStack.clear();
		complexTypeStack.clear();
		outputStack.clear();
		rimAttributeMap.clear();
		complexTypeVisitMap.clear();
	}
	
	public void parseV3Schema(URL schemaResource) throws XSDException,
	         FileNotFoundException, MalformedURLException
	{
		clearCachedData();
		
		XSDBuilder xsdBuilder = new XSDBuilder();
		interactionId = schemaResource.getPath().substring(
		         schemaResource.getPath().lastIndexOf("/") + 1,
		         schemaResource.getPath().lastIndexOf("."));
		System.out.println("InteractionId: " + interactionId);
		XMLSchema xmlSchema = xsdBuilder.build(schemaResource);
		XMLSchemaNode xmlSchmeNode = xmlSchema.getSchemaByTargetNS(HL7_NAMESPACE);
		
		parseSchemaForXPaths(null,
		         getElementFromSchemaNode(xmlSchmeNode, interactionId));
		
		xmlSchmeNode = null;
		xmlSchema = null;
		xsdBuilder = null;
	}
	
	public XSDElement getElementFromSchemaNode(XMLSchemaNode xmlSchemaNode,
	         String elementName)
	{
		if (xmlSchemaNode != null)
		{
			XSDNode[] xsdNodes = xmlSchemaNode.getElementSet();
			for (XSDNode xsdNode : xsdNodes)
			{
				if (xsdNode.getName().equals(elementName))
				{
					return (XSDElement) xsdNode;
				}
			}
		}
		return null;
	}
	
	public void parseV3SchemaFromjar(String schemaPath) throws XSDException,
	         FileNotFoundException, MalformedURLException
	{
		clearCachedData();
		
		XSDBuilder xsdBuilder = new XSDBuilder();
		interactionId = schemaPath.substring(schemaPath.lastIndexOf("/") + 1,
		         schemaPath.lastIndexOf("."));
		System.out.println("InteractionId: " + interactionId);
		// URL schemaURL = getClass().getResource(schemaPath);
		// InputStream schemaIn = getClass().getResourceAsStream(schemaPath);
		URL schemaURL = getClass().getResource(schemaPath);
		XMLSchema xmlSchema = xsdBuilder.build(schemaURL);
		XMLSchemaNode xmlSchmeNode = xmlSchema.getSchemaByTargetNS(HL7_NAMESPACE);
		
		parseSchemaForXPaths(null,
		         getElementFromSchemaNode(xmlSchmeNode, interactionId));
		
		xmlSchmeNode = null;
		xmlSchema = null;
		xsdBuilder = null;
	}
	
	private void parseSchemaForXPaths(XSDComplexType parentXsdComplexType,
	         XSDElement xsdElement)
	{
		String xsdElementName = xsdElement.getName();
		if (xsdElementName.equals(interactionId))
		{
			xPathStack.push("message");
		}
		else
		{
			xPathStack.push(xsdElementName);
		}
		
		XSDComplexType xsdComplexType = XSDUtil.getXsdComplexType(xsdElement);
		String xsdComplexTypeName = XSDUtil.getComplexTypeName(xsdComplexType);
		complexTypeStack.push(xsdComplexTypeName);
		ArrayList<XSDNode> childrenList = XSDUtil.getAllChildren(xsdComplexType);
		for (XSDNode xsdNode : childrenList)
		{
			XSDElement childXsdElement = (XSDElement) xsdNode;
			XSDComplexType childXsdComplexType = XSDUtil
			         .getXsdComplexType(childXsdElement);
			String childXsdComplexTypeName = XSDUtil
			         .getComplexTypeName(childXsdComplexType);
			String childXsdElementName = childXsdElement.getName();
			if (!complexTypeVisitMap.containsKey(childXsdElementName))
			{
				complexTypeVisitMap.put(childXsdElementName, new HashSet<String>());
			}
			
			if (XSDUtil.isHL7Object(xsdComplexType, childXsdComplexType))
			{
				if (complexTypeVisitMap.get(childXsdElementName)
				         .add(xsdElementName))
				{
					parseSchemaForXPaths(xsdComplexType, childXsdElement);
				}
				// else
				// {
				// System.out.println(childXsdElementName + "(" +
				// childXsdComplexTypeName +
				// ") is already visisted as parent of " + xsdElementName +
				// "(" + xsdComplexTypeName + ")");
				// }
			}
			else
			{
				if (!rimAttributeMap.containsKey(xsdElement))
				{
					rimAttributeMap.put(xsdElement, new HashSet<String>());
				}
				rimAttributeMap.get(xsdElement).add(childXsdElementName);
			}
		}
		writeXPathContentToOutputStack(parentXsdComplexType, xsdElement,
		         xsdComplexType, xsdComplexTypeName);
		xPathStack.pop();
	}
	
	/**
	 * Method writeXPathContentToFile
	 * 
	 * @param parentXsdComplexType
	 * @param xsdElement
	 * @param xsdComplexType
	 * @param xsdComplexTypeName
	 */
	private void writeXPathContentToOutputStack(
	         XSDComplexType parentXsdComplexType, XSDElement xsdElement,
	         XSDComplexType xsdComplexType, String xsdComplexTypeName)
	{
		List<String> outputList = new ArrayList<String>();
		
		String xPathStringFromXPathStack = getMinimalXPathStringFromXPathStack();
		
		RIM_OBJECT_TYPE rimObjectType = XSDUtil.getRimObjectType(
		         parentXsdComplexType, xsdComplexType);
		outputList.add("// Mapping for " + xsdElement.getName() + "(" +
		         xsdComplexTypeName + " : " + rimObjectType + ")");
		outputList.add(xPathStringFromXPathStack);
		XSDAttribute[] xsdAttributes = xsdElement.getAttributeDeclarations();
		for (XSDAttribute xsdAttribute : xsdAttributes)
		{
			if (!"nullFlavor".equals(xsdAttribute.getName()))
			{
				Set<String> defaultValue = XSDUtil.getDefaultAttributeValue(
				         xsdComplexType, xsdAttribute.getName());
				if (defaultValue != null && !defaultValue.isEmpty())
				{
					outputList.add(xPathStringFromXPathStack + "." +
					         xsdAttribute.getName() + " // RMIM Value(s): " +
					         defaultValue);
				}
				else
				{
					outputList.add(xPathStringFromXPathStack + "." +
					         xsdAttribute.getName());
				}
			}
		}
		Set<String> attributeList = rimAttributeMap.get(xsdElement);
		for (String attrName : attributeList)
		{
			outputList.add(xPathStringFromXPathStack + "." + attrName);
		}
		if (!outputStack.isEmpty())
		{
			outputStack.push("\n");
		}
		for (int i = outputList.size() - 1; i >= 0; i--)
		{
			outputStack.push(outputList.get(i));
		}
	}
	
	private void writeOutputToFile() throws FileNotFoundException
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(new File("C:\\" + interactionId +
			         "_XPath.txt"));
			while (!outputStack.isEmpty())
			{
				writer.println(outputStack.pop());
				writer.flush();
			}
		}
		finally
		{
			if (writer != null)
			{
				writer.close();
			}
		}
		
	}
	
	private String getMinimalXPathStringFromXPathStack()
	{
		StringBuilder builder = new StringBuilder();
		if (!xPathStack.isEmpty())
		{
			if (xPathStack.size() == 1)
			{
				builder.append(xPathStack.get(0));
			}
			else if (xPathStack.size() == 2)
			{
				builder.append(xPathStack.get(xPathStack.size() - 2) + "." +
				         xPathStack.get(xPathStack.size() - 1));
			}
			else
			{
				builder.append(".." + xPathStack.get(xPathStack.size() - 2) + "." +
				         xPathStack.get(xPathStack.size() - 1));
			}
		}
		return builder.toString();
	}
	
	/**
	 * Method getOutputStack
	 * 
	 * @return the outputStack
	 */
	public Stack<String> getOutputStack()
	{
		return outputStack;
	}
	
	public static void main(String[] args) throws FileNotFoundException,
	         XSDException, MalformedURLException
	{
		new V3SchemaParser()
		         .parseV3Schema("C:\\Mani\\CTIS\\Inova\\I3E\\defs\\rim214101\\schemas\\POLB_IN004102.xsd");
	}
}
