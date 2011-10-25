/**
 * RIMConstants.java
 * Creation Date: Aug 4, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7;

import java.util.Arrays;
import java.util.List;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public interface RIMConstants
{
	public static final String HL7_NAMESPACE = "urn:hl7-org:v3";
	public static final String PLAYED_ROLE = "playedRole";
	public static final String SCOPER_ENTITY = "scoper";
	public static final String IB_ACT_RELATIONSHIP = "inboundRelationship";
	public static final String ATTACHMENT = "attachment";
	public static final String CONTROL_ACT = "controlAct";
	public static final String SOURCE_ACT = "source";
	public static final String PARTICIPATION = "participation";
	public static final String PLAYER_ENTITY = "player";
	public static final String ENTITY = "entity";
	public static final String LANG_COMMUNICATION = "languageCommunication";
	public static final String ACT = "act";
	public static final String OB_ACT_RELATIONSHIP = "outboundRelationship";
	public static final String COMMUNICATION_FUNCTION = "communicationFunction";
	public static final String ATTENTION_LINE = "attentionLine";
	public static final String TARGET_ACT = "target";
	public static final String ROLE = "role";
	public static final String SCOPED_ROLE = "scopedRole";
	public static final String TYPE_ATTR_NAME = "type";
	public static final String CHOICE_ATTR_NAME = "choice";
	public static final String COLLECTION_ATTR_NAME = "collection";
	public static final String ENUM_ATTR_NAME = "enumeration";
	public static final String CLASS_CODE = "classCode";
	public static final String DETERMINER_CODE = "determinerCode";
	public static final String MOOD_CODE = "moodCode";
	public static final String TYPE_CODE = "typeCode";
	public static final String TRUE_VALUE = "true";
	
	public static enum RIM_OBJECT_TYPE
	{
		ACT, ROLE, ENTITY, ACT_RELATIONSHIP, PARTICIPATION,
		COMMUNICATION_FUNCTION, ATTENTION_LINE, ATTACHMENT, MESSAGE, UNKNOWN
	};
	
	public static final List<String> RIM_STRUCTURAL_ATTR_LIST = Arrays
	         .asList(new String[] { CLASS_CODE, DETERMINER_CODE, MOOD_CODE,
	                  TYPE_CODE });
}
