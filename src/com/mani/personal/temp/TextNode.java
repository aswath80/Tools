/**
 * TextNode.java
 * Creation Date: Sep 28, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class TextNode extends Node
{
	private String value;
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#type()
	 */
	@Override
	public String type()
	{
		return "#text";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#isSupportedChildType(java.lang.String)
	 */
	@Override
	public boolean isSupportedChildType(String childType)
	{
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#getDefaultSupportedChildType()
	 */
	@Override
	public String getDefaultSupportedChildType()
	{
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.mani.personal.temp.Node#childNodeAdded(com.mani.personal.temp.Node)
	 */
	@Override
	protected void childNodeAdded(Node childNode)
	{
		
	}
	
	/**
	 * Method getValue
	 * 
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Method setValue
	 * 
	 * @param value
	 *           the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
