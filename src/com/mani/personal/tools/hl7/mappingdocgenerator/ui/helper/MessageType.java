/**
 * MessageType.java
 * Creation Date: Aug 10, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.mappingdocgenerator.ui.helper;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class MessageType
{
	private String messageTypeName;
	private String interactionId;
	
	public MessageType(String messageTypeName, String interactionId)
	{
		this.messageTypeName = messageTypeName;
		this.interactionId = interactionId;
	}
	
	/**
	 * Method getMessageTypeName
	 * 
	 * @return the messageTypeName
	 */
	public String getMessageTypeName()
	{
		return messageTypeName;
	}
	
	/**
	 * Method getInteractionId
	 * 
	 * @return the interactionId
	 */
	public String getInteractionId()
	{
		return interactionId;
	}
	
	public String toString()
	{
		return messageTypeName;
	}
}
