/**
 * XSDUtil.java
 * Creation Date: Aug 3, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.schema.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import oracle.xml.parser.schema.XSDAny;
import oracle.xml.parser.schema.XSDAttribute;
import oracle.xml.parser.schema.XSDComplexType;
import oracle.xml.parser.schema.XSDConstrainingFacet;
import oracle.xml.parser.schema.XSDElement;
import oracle.xml.parser.schema.XSDGroup;
import oracle.xml.parser.schema.XSDNode;
import oracle.xml.parser.schema.XSDSimpleType;

import com.mani.personal.tools.hl7.RIMConstants;
import com.mani.personal.tools.hl7.RIMConstants.RIM_OBJECT_TYPE;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class XSDUtil
{
	private static final HashMap<String, String> TRANSLATIONS_MAP = new HashMap<String, String>();
	
	private static final HashMap<XSDSimpleType, Set<String>> VOC_CACHE_MAP = new HashMap<XSDSimpleType, Set<String>>();
	
	/**
	 * Method getTypeCode - used to get the default value of attributes in a
	 * XSDComplexType. For example, default value of classCode, moodCode,
	 * typeCode etc.
	 * 
	 * @param childComplexTypeA
	 * @return String - the value of XSDComplexType attribute.
	 */
	public static Set<String> getDefaultAttributeValue(
	         XSDComplexType xsdComplexType, String attributeName)
	{
		if (xsdComplexType != null && attributeName != null)
		{
			XSDAttribute[] attributes = xsdComplexType.getAttributeDeclarations();
			for (int i = 0; i < attributes.length; i++)
			{
				if (attributeName.equals(attributes[i].getName()))
				{
					String defaultValue = attributes[i].getDefaultVal();
					if (defaultValue != null)
					{
						Set<String> s = new HashSet<String>();
						s.add(defaultValue);
						return s;
					}
					else if (attributes[i].getFixedVal() != null)
					{
						Set<String> s = new HashSet<String>();
						s.add(attributes[i].getFixedVal());
						return s;
					}
					
					else
					{
						return readFromVoc((XSDSimpleType) attributes[i].getType());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Method readFromVoc
	 * 
	 * @param type
	 * @return Set of attribute values for type from voc.xsd.
	 */
	private static Set<String> readFromVoc(XSDSimpleType type, Set<String> s)
	{
		if (VOC_CACHE_MAP.get(type) != null)
		{
			return (Set<String>) VOC_CACHE_MAP.get(type);
		}
		Vector<XSDNode> v = null;
		v = type.getMemberTypes();
		if (v != null)
		{
			for (int k = 0; k < v.size(); k++)
			{
				XSDSimpleType base = (XSDSimpleType) v.elementAt(k);
				XSDConstrainingFacet[] facets = base.getFacets();
				if (facets != null && facets.length > 0)
					for (int i = 0; i < facets.length; i++)
					{
						if (facets[i] != null &&
						         facets[i].getName().equals(
						                  RIMConstants.ENUM_ATTR_NAME))
						{
							Vector<String> enum1 = facets[i].getLexicalEnumeration();
							for (int j = 0; j < enum1.size(); j++)
							{
								s.add((String) (enum1.elementAt(j)));
							}
						}
					}
				if (isLeaffMemberType(base))
				{
					readFromVoc(base, s);
				}
				else
				{
					Set<String> membertypeChildrenSet = readFromVoc(base);
					s.addAll(membertypeChildrenSet);
				}
			}
			
			VOC_CACHE_MAP.put(type, s);
		}
		else
		{
			HashSet<String> leafNodeSet = new HashSet<String>();
			
			XSDConstrainingFacet[] facets = type.getFacets();
			if (facets != null && facets.length > 0)
			{
				for (int i = 0; i < facets.length; i++)
				{
					if (facets[i] != null &&
					         facets[i].getName().equals(RIMConstants.ENUM_ATTR_NAME))
					{
						Vector<String> enum1 = facets[i].getLexicalEnumeration();
						for (int j = 0; j < enum1.size(); j++)
						{
							leafNodeSet.add((String) (enum1.elementAt(j)));
						}
					}
					else
					{
						Vector<String> vocVector = type.getEnumeration();
						if (vocVector != null)
						{
							for (int j = 0; j < vocVector.size(); j++)
							{
								s.add((String) (vocVector.elementAt(j)));
							}
						}
					}
				}
				
				if (type.getName() != null)
				{
					VOC_CACHE_MAP.put(type, leafNodeSet);
				}
			}
		}
		return s;
	}
	
	private static Set<String> readFromVoc(XSDSimpleType type)
	{
		Set<String> s = new HashSet<String>();
		return readFromVoc(type, s);
	}
	
	private static boolean isLeaffMemberType(XSDSimpleType type)
	{
		Vector<XSDNode> v = null;
		v = type.getMemberTypes();
		
		if (v != null && v.size() > 0)
		{
			return false;
		}
		else
			return true;
	}
	
	/**
	 * Translation map to populate xsi:type
	 * 
	 * @param datatype
	 * @return datatype
	 */
	public static String getDataType(String datatype)
	{
		if (TRANSLATIONS_MAP != null && !TRANSLATIONS_MAP.isEmpty())
		{
			return (String) TRANSLATIONS_MAP.get(datatype);
		}
		else
		{
			TRANSLATIONS_MAP.put("RTO<PQ,PQ>", "RTO_PQ_PQ");
			TRANSLATIONS_MAP.put("RTO<QTY,QTY>", "RTO_QTY_QTY");
			TRANSLATIONS_MAP.put("RTO<MO,PQ>", "RTO_MO_PQ");
			TRANSLATIONS_MAP.put("IVL<CD>", "IVL_CD");
			TRANSLATIONS_MAP.put("IVL<CE>", "IVL_CE");
			TRANSLATIONS_MAP.put("IVL<CS>", "IVL_CS");
			TRANSLATIONS_MAP.put("IVL<II>", "IVL_II");
			TRANSLATIONS_MAP.put("IVL<INT>", "IVL_INT");
			TRANSLATIONS_MAP.put("IVL<MO>", "IVL_MO");
			TRANSLATIONS_MAP.put("IVL<PQ>", "IVL_PQ");
			TRANSLATIONS_MAP.put("IVL<REAL>", "IVL_REAL");
			TRANSLATIONS_MAP.put("IVL<RTO<PQ,PQ>>", "IVL_RTO_PQ_PQ");
			TRANSLATIONS_MAP.put("IVL<TS>", "IVL_TS");
			TRANSLATIONS_MAP.put("IVL<EN>", "IVL_EN");
			TRANSLATIONS_MAP.put("IVL<PN>", "IVL_PN");
			TRANSLATIONS_MAP.put("IVL<ON>", "IVL_ON");
			TRANSLATIONS_MAP.put("IVL<AD>", "IVL_AD");
			return (String) TRANSLATIONS_MAP.get(datatype);
		}
	}
	
	/**
	 * Returns the RIM type (as an int constant defined in
	 * OMPMessageGeneratorConstants) of an XSD ComplexType. If the ComplexType is
	 * not of a RIM type, returns NON_RIM_TYPE.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType whose RIM type is to be found.
	 * @return The RIM type (in terms of a constant defined in
	 *         OMPMessageGeneratorConstants) of xsdComplexType, or
	 *         NON_RIM_TYPE if xsdComplexType is not
	 *         of a RIM type.
	 */
	public static RIM_OBJECT_TYPE getRimObjectType(
	         XSDComplexType parentXsdComplexType, XSDComplexType xsdComplexType)
	{
		RIM_OBJECT_TYPE rimType = RIM_OBJECT_TYPE.UNKNOWN;
		
		boolean isClassCodePresent = false;
		boolean isDeterminerCodePresent = false;
		boolean isMoodCodePresent = false;
		boolean isTypeCodePresent = false;
		
		XSDAttribute[] attributes = xsdComplexType.getAttributeDeclarations();
		if (attributes != null && attributes.length > 0)
		{
			for (int i = 0; i < attributes.length; i++)
			{
				isClassCodePresent = isClassCodePresent ? isClassCodePresent
				         : RIMConstants.CLASS_CODE.equals(attributes[i].getName());
				isDeterminerCodePresent = isDeterminerCodePresent ? isDeterminerCodePresent
				         : RIMConstants.DETERMINER_CODE.equals(attributes[i]
				                  .getName());
				isMoodCodePresent = isMoodCodePresent ? isMoodCodePresent
				         : RIMConstants.MOOD_CODE.equals(attributes[i].getName());
				isTypeCodePresent = isTypeCodePresent ? isTypeCodePresent
				         : RIMConstants.TYPE_CODE.equals(attributes[i].getName());
			}
			if (isClassCodePresent && isMoodCodePresent)
			{
				rimType = RIM_OBJECT_TYPE.ACT;
			}
			else if (isClassCodePresent && isDeterminerCodePresent)
			{
				rimType = RIM_OBJECT_TYPE.ENTITY;
			}
			else if (isClassCodePresent)
			{
				rimType = RIM_OBJECT_TYPE.ROLE;
			}
			else if (isTypeCodePresent)
			{
				RIM_OBJECT_TYPE parentRimObjectType = getRimObjectType(null,
				         parentXsdComplexType);
				
				ArrayList<XSDNode> childNodeList = getAllChildren(xsdComplexType);
				for (XSDNode xsdNode : childNodeList)
				{
					XSDElement childXsdElement = (XSDElement) xsdNode;
					XSDComplexType childXsdComplexType = getXsdComplexType(childXsdElement);
					if (isHL7Object(xsdComplexType, childXsdComplexType))
					{
						RIM_OBJECT_TYPE childRimObjectType = getRimObjectType(
						         xsdComplexType, childXsdComplexType);
						if (parentRimObjectType == RIM_OBJECT_TYPE.ACT &&
						         childRimObjectType == RIM_OBJECT_TYPE.ACT)
						{
							rimType = RIM_OBJECT_TYPE.ACT_RELATIONSHIP;
						}
						else if (parentRimObjectType == RIM_OBJECT_TYPE.ROLE ||
						         childRimObjectType == RIM_OBJECT_TYPE.ROLE)
						{
							rimType = RIM_OBJECT_TYPE.PARTICIPATION;
						}
						break;
					}
				}
				
			}
		}
		if (rimType == RIM_OBJECT_TYPE.UNKNOWN)
		{
			if (parentXsdComplexType == null)
			{
				rimType = RIM_OBJECT_TYPE.MESSAGE;
			}
		}
		return rimType;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to one of the HL7
	 * RIM classes -- Act, Entity, Role, ActRelationship, Participation,
	 * LanguageCommunication : RoleLink is not yet supported -- as opposed to one
	 * of the constructs used by the XML ITS for structuring the XSDs.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType is of an HL7 RIM type, false otherwise.
	 */
	public static boolean isHL7Object(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) != RIM_OBJECT_TYPE.UNKNOWN;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to an Act.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType corresponds to an Act, false otherwise.
	 */
	public static boolean isAct(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) == RIM_OBJECT_TYPE.ACT;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to an Entity.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType corresponds to an Entity, false otherwise.
	 */
	public static boolean isEntity(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) == RIM_OBJECT_TYPE.ENTITY;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to a Role.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType corresponds to a Role, false otherwise.
	 */
	public static boolean isRole(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) == RIM_OBJECT_TYPE.ROLE;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to a
	 * Participation.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType corresponds to a Participation, false
	 *         otherwise.
	 */
	public static boolean isParticipation(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) == RIM_OBJECT_TYPE.PARTICIPATION;
	}
	
	/**
	 * Checks whether the specified XSD ComplexType corresponds to an
	 * ActRelationship.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType to be checked.
	 * @return true if xsdComplexType corresponds to an ActRelationship, false
	 *         otherwise.
	 */
	public static boolean isActRelationship(XSDComplexType parentXsdComplexType,
	         XSDComplexType xsdComplexType)
	{
		return getRimObjectType(parentXsdComplexType, xsdComplexType) == RIM_OBJECT_TYPE.ACT_RELATIONSHIP;
	}
	
	/**
	 * Gets the data type name of the specified XSDElement, in those cases where
	 * the data type is of a collection type -- SET or BAG.
	 * 
	 * @param xsdElement
	 *           The XSDElement whose collection data type name is to be found
	 * @return The data type name of xsdElement if the data type is of a
	 *         collection type, and null otherwise.
	 */
	public static String getCollectionDataTypeName(XSDElement xsdElement)
	{
		XSDAttribute[] attributes = ((XSDComplexType) xsdElement.getType())
		         .getAttributeDeclarations();
		for (int i = 0; i < attributes.length; i++)
		{
			if (RIMConstants.COLLECTION_ATTR_NAME.equals(attributes[i].getName()))
			{
				return attributes[i].getFixedVal();
			}
		}
		return null;
	}
	
	/**
	 * Gets the ComplexType name of the specified XSD ComplexType.
	 * 
	 * @param xsdComplexType
	 *           The XSD ComplexType whose ComplexType name is to be found
	 * @return The ComplexType name of xsdComplexType.
	 */
	public static String getComplexTypeName(XSDComplexType xsdComplexType)
	{
		String typeName = null;
		if (xsdComplexType != null)
		{
			typeName = xsdComplexType.getName();
			if (typeName == null)
			{
				XSDComplexType baseType = (XSDComplexType) xsdComplexType
				         .getBaseType();
				if (baseType != null)
				{
					typeName = baseType.getName();
				}
			}
		}
		return typeName;
	}
	
	/**
	 * Checks whether the specified XSDElement corresponds to a
	 * LanguageCommunication.
	 * 
	 * @param xsdElement
	 *           The XSDElement to be checked.
	 * @return true if xsdElement corresponds to a LanguageCommunication, false
	 *         otherwise.
	 */
	public static boolean isLanguageCommunication(XSDElement xsdElement)
	{
		return RIMConstants.LANG_COMMUNICATION.equals(xsdElement.getName());
	}
	
	/**
	 * Gets the value of the htb:choice attribute of the specified XSDElement.
	 * Returns null if there the element doesn't have such an attribute.
	 */
	public static String getChoiceAttributeValue(XSDElement xsdElement)
	{
		XSDAttribute[] attributes = ((XSDComplexType) xsdElement.getType())
		         .getAttributeDeclarations();
		if (attributes != null && attributes.length > 0)
		{
			for (int i = 0; i < attributes.length; i++)
			{
				if (RIMConstants.CHOICE_ATTR_NAME.equals(attributes[i].getName()))
				{
					return attributes[i].getFixedVal();
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks whether the specified XSDElement is a 'choice' element. Such an
	 * element is identified by the attribute htb:choice="true", and is an
	 * example of an XSDElement which doesn't map directly to an HL7 concept.
	 * 
	 * @param xsdElement
	 *           The XSDElement to be checked.
	 * @return true if xsdElement is a choice element, false otherwise.
	 */
	public static boolean isChoice(XSDElement xsdElement)
	{
		return RIMConstants.TRUE_VALUE
		         .equals(getChoiceAttributeValue(xsdElement));
	}
	
	/**
	 * Returns a list of all the immediate xsdComplexType elements of the
	 * specified
	 * XSDComplexType. The list will not contain any xsdComplexType which is a
	 * group or
	 * choice : all groups and choices are replaced by their xsdComplexType
	 * elements,
	 * recursively till no group/choice is left.
	 * 
	 * @param xsdComplexType
	 *           The XSDComplexType whose children should be found.
	 * @return An ArrayList of the children of xsdComplexType.
	 */
	public static ArrayList<XSDNode> getAllChildren(XSDComplexType xsdComplexType)
	{
		ArrayList<XSDNode> allChildren = new ArrayList<XSDNode>();
		// Get the list of xsdComplexType elements of this complex type.
		XSDNode[] childElements = xsdComplexType.getElementSet();
		if (childElements != null)
		{
			for (int i = 0; i < childElements.length; i++)
			{
				// If this element's type is a group, add the contents of the
				// group to allChildren. Else add the element to allChildren.
				if (childElements[i] instanceof XSDElement)
				{
					ArrayList<XSDNode> children = getAllChildren((XSDElement) childElements[i]);
					if (children != null && children.size() > 0 &&
					         !isChoice((XSDElement) childElements[i]))
					// This condition is true iff childElements[i] is of type group.
					// In that case, add the children of that group to allChildren.
					{
						allChildren.addAll(children);
					}
					else
					// childElements[i] is a simple element, so add it to
					// allChildren.
					{
						allChildren.add(childElements[i]);
					}
				}
				// childElements[i] is of type group, so add its children to
				// allChildren.
				else if (childElements[i] instanceof XSDGroup)
				{
					allChildren.addAll(getAllChildren((XSDGroup) childElements[i]));
				}
			}
		}
		return allChildren;
	}
	
	/**
	 * Returns a list of all the immediate xsdComplexType elements of the
	 * specified
	 * XSDGroup. The list will not contain any xsdComplexType which is a group or
	 * choice :
	 * all groups and choices are replaced by their xsdComplexType elements,
	 * recursively
	 * till no group/choice is left.
	 * 
	 * @param xsdGroup
	 *           The XSDGroup whose children should be found.
	 * @return An ArrayList of the children of xsdGroup.
	 */
	public static ArrayList<XSDNode> getAllChildren(XSDGroup xsdGroup)
	{
		// Get the children of xsdGroup.
		ArrayList<XSDNode> groupElements = new ArrayList<XSDNode>(
		         xsdGroup.getNodeVector());
		// Replace all the children which are themselves groups with their
		// children, recursively till no groups are left in groupElements.
		expandGroups(groupElements);
		// Delegate the processing of each element in groupElements to the
		// appropriate method.
		for (int i = 0; i < groupElements.size(); i++)
		{
			XSDNode groupElement = (XSDNode) groupElements.get(i);
			if (groupElement instanceof XSDElement)
			{
				groupElements.addAll(getAllChildren((XSDElement) groupElement));
			}
			else if (groupElement instanceof XSDComplexType)
			{
				groupElements.addAll(getAllChildren((XSDComplexType) groupElement));
			}
		}
		return groupElements;
	}
	
	/**
	 * Returns a list of all the immediate xsdComplexType elements of the
	 * specified
	 * XSDElement, when the XSDElement is of type group. Otherwise returns an
	 * empty list. The returned list will not contain any xsdComplexType which is
	 * a group
	 * or choice : all groups and choices are replaced by their xsdComplexType
	 * elements,
	 * recursively till no group/choice is left.
	 * 
	 * @param xsdElement
	 *           The XSDElement whose children should be found.
	 * @return An ArrayList of the children of xsdElement when xsdElement is of
	 *         type group, and an empty ArrayList otherwise.
	 */
	public static ArrayList<XSDNode> getAllChildren(XSDElement xsdElement)
	{
		// Get the type and the type name of this element.
		XSDNode elementType = xsdElement.getType();
		String elementTypeName = elementType.getName();
		// If this element's type has no name, try getting its base type's name.
		// If the base type's name is also null, then this element's type must be
		// an XSDGroup : delegate the processing of this group.
		if (elementTypeName == null)
		{
			if (elementType instanceof XSDComplexType)
			{
				if (((XSDComplexType) elementType).getBaseType() != null)
				{
					elementTypeName = ((XSDComplexType) elementType).getBaseType()
					         .getName();
				}
				
				if (elementTypeName == null)
				{
					XSDGroup elementTypeGroup = ((XSDComplexType) elementType)
					         .getTypeGroup();
					if (elementTypeGroup != null)
					{
						return getAllChildren((XSDGroup) elementTypeGroup);
					}
				}
			}
		}
		return new ArrayList<XSDNode>();
	}
	
	/**
	 * In a list of XSDNodes, replace each element which is an XSDGroup with the
	 * xsdComplexType nodes of the XSDGroup. Do this repeatedly for all XSDGroup
	 * elements
	 * in the list, till no XSDGroup element is left.
	 */
	public static void expandGroups(ArrayList<XSDNode> groupElements)
	{
		for (int i = 0; i < groupElements.size(); i++)
		{
			XSDNode groupElement = (XSDNode) groupElements.get(i);
			if (groupElement instanceof XSDGroup)
			{
				groupElements.remove(i--);
				groupElements.addAll(((XSDGroup) groupElement).getNodeVector());
			}
			// Remove elements of type XSDAny from the list. NOTE: The reason
			// behind doing this is not clear : not removing these elements
			// resulted in runtime errors, hence this step. Unfortunately,
			// those errors were not documented here when they occurred,
			// and so I forgot what kind of errors were those.
			else if (groupElement instanceof XSDAny)
			{
				groupElements.remove(i--);
			}
		}
	}
	
	/**
	 * Returns an XSDComplexType instance corresponding to the specified
	 * XSDElement instance. Uses the instance to look up
	 * the XSDComplexType instance from the current type table.
	 * 
	 * @param xsdElement
	 *           The XSDElement instance whose matching XSDComplexType should be
	 *           found.
	 * @return An XSDComplexType instance matching xsdElement, provided such an
	 *         instance can be found from the current type table. Otherwise,
	 *         null.
	 */
	public static XSDComplexType getXsdComplexType(XSDElement xsdElement)
	{
		String typeName = xsdElement.getType().getName();
		if (typeName == null)
		{
			if (((XSDComplexType) xsdElement.getType()).getBaseType() != null)
			{
				typeName = ((XSDComplexType) xsdElement.getType()).getBaseType()
				         .getName();
			}
		}
		if (typeName == null)
		{
			return null;
		}
		else
		{
			return (XSDComplexType) xsdElement.getOwnerSchema()
			         .getSchemaByTargetNS(RIMConstants.HL7_NAMESPACE)
			         .getTypeDefinitionTable().get(typeName);
		}
	}
	
	/**
	 * Returns the XSDElement instance corresponding to the xsdComplexType
	 * element of the
	 * specified XSDComplexType with the specified element name.
	 * 
	 * @param parentType
	 *           The XSDComplexType instance whose xsdComplexType should be
	 *           returned.
	 * @param elementName
	 *           The name of the xsdComplexType element.
	 * @return The XSDElement instance which corresponds to the specified
	 *         xsdComplexType
	 *         of parentType.
	 */
	public static XSDElement getChildElement(XSDComplexType parentType,
	         String elementName)
	{
		XSDElement childElement = null;
		ArrayList<XSDNode> allChildren = getAllChildren(parentType);
		Iterator<XSDNode> iterator = allChildren.iterator();
		if (iterator != null)
		{
			while (iterator.hasNext())
			{
				Object xsdComplexType = iterator.next();
				if (xsdComplexType instanceof XSDElement)
				{
					if (elementName.equals(((XSDElement) xsdComplexType).getName()))
					{
						childElement = (XSDElement) xsdComplexType;
						break;
					}
				}
			}
		}
		return childElement;
	}
}
