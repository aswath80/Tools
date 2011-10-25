/**
 * TRNode.java
 * Creation Date: Sep 28, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class TRNode extends Node
{
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#type()
	 */
	@Override
	public String type()
	{
		return "tr";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#isSupportedChildType(java.lang.String)
	 */
	@Override
	public boolean isSupportedChildType(String childType)
	{
		return "th".equalsIgnoreCase(childType) ||
		         "td".equalsIgnoreCase(childType);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mani.personal.temp.Node#getDefaultSupportedChildType()
	 */
	@Override
	public String getDefaultSupportedChildType()
	{
		return "td";
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
