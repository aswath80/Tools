/**
 * THNode.java
 * Creation Date: Sep 28, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class THNode extends Node
{
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#type()
	 */
	@Override
	public String type()
	{
		return "th";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#isSupportedChildType(java.lang.String)
	 */
	@Override
	public boolean isSupportedChildType(String childType)
	{
		return "#text".equalsIgnoreCase(childType);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#getDefaultSupportedChildType()
	 */
	@Override
	public String getDefaultSupportedChildType()
	{
		return "#text";
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
}
