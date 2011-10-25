/**
 * Node.java
 * Creation Date: Sep 28, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public abstract class Node
{
	private Node parentNode = null;
	private List<Node> childNodeList = new ArrayList<Node>();
	private Map<String, String> attrMap = new HashMap<String, String>();
	private static final Map<String, Class<? extends Node>> nodeMap = new HashMap<String, Class<? extends Node>>()
	{
		{
			put("table", TableNode.class);
			put("thead", THeadNode.class);
			put("tfoot", TFootNode.class);
			put("tbody", TBodyNode.class);
			put("tr", TRNode.class);
			put("th", THNode.class);
			put("td", TDNode.class);
			put("#text", TextNode.class);
		}
	};
	
	public static Node newInstance(String type)
	{
		if (type == null)
		{
			return null;
		}
		
		if (nodeMap.containsKey(type.toLowerCase()))
		{
			try
			{
				return nodeMap.get(type.toLowerCase()).newInstance();
			}
			catch (InstantiationException e)
			{
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		throw new RuntimeException("Unsupported node type " + type);
	}
	
	public static boolean isValidNodeType(String type)
	{
		return type != null && nodeMap.containsKey(type.toLowerCase());
	}
	
	protected void setParent(Node parentNode)
	{
		this.parentNode = parentNode;
	}
	
	public Node parent()
	{
		return parentNode;
	}
	
	public void addAttribute(String attrStringWithEquals)
	{
		if (attrStringWithEquals != null)
		{
			String[] tokens = attrStringWithEquals.split("=");
			if (tokens.length != 2)
			{
				throw new RuntimeException("Invalid attrStringWithEquals " +
				         attrStringWithEquals);
			}
			attrMap.put(tokens[0], tokens[1]);
		}
	}
	
	public void addAttribute(String attrName, String attrValue)
	{
		if (attrName != null && attrValue != null)
		{
			attrMap.put(attrName, attrValue);
		}
	}
	
	public abstract String type();
	
	public abstract boolean isSupportedChildType(String childType);
	
	public abstract String getDefaultSupportedChildType();
	
	protected abstract void childNodeAdded(Node childNode);
	
	public void addChild(Node childNode)
	{
		if (isSupportedChildType(childNode.type()))
		{
			childNode.setParent(this);
			childNodeList.add(childNode);
			childNodeAdded(childNode);
		}
		else
		{
			throw new RuntimeException("Child of type " + childNode.type() +
			         " not supported under " + type());
		}
	}
	
	public void addChildSmart(Node childNode)
	{
		if (isSupportedChildType(childNode.type()))
		{
			childNode.setParent(this);
			childNodeList.add(childNode);
			childNodeAdded(childNode);
		}
		else
		{
			Node parent = this;
			while (parent.getDefaultSupportedChildType() != null &&
			         !parent.getDefaultSupportedChildType().equals(
			                  childNode.type()))
			{
				Node interMediateChild = Node.newInstance(parent
				         .getDefaultSupportedChildType());
				interMediateChild.setParent(parent);
				parent.childNodeList.add(interMediateChild);
				parent.childNodeAdded(interMediateChild);
				parent = interMediateChild;
			}
			if (parent.getDefaultSupportedChildType() == null)
			{
				throw new RuntimeException("Child of type " + childNode.type() +
				         " not supported under hierarchy of " + type());
			}
			if (parent.getDefaultSupportedChildType().equals(childNode.type()))
			{
				childNode.setParent(parent);
				parent.childNodeList.add(childNode);
				parent.childNodeAdded(childNode);
			}
		}
		
	}
	
	protected int childCount()
	{
		return childNodeList.size();
	}
	
	protected Node getChild(int index)
	{
		return childNodeList.get(index);
	}
	
	public void print()
	{
		print("", this);
	}
	
	private void print(String tab, Node node)
	{
		if ("#text".equalsIgnoreCase(node.type()))
		{
			System.out.println(tab + ((TextNode) node).getValue());
		}
		else
		{
			System.out.println(tab + "<" + node.type() + ">");
			if (node.childCount() > 0)
			{
				for (int i = 0; i < node.childCount(); i++)
				{
					print(tab + "  ", node.getChild(i));
				}
			}
			System.out.println(tab + "</" + node.type() + ">");
		}
	}
}
