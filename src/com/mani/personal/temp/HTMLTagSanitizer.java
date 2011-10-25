/**
 * HTMLTagSanitizer.java
 * Creation Date: Sep 28, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.temp;

import java.util.StringTokenizer;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class HTMLTagSanitizer
{
	private static final String TEST_TABLE_TAG = "&lt;table border=\"1\" width=\"100%\">&lt;thead>&lt;tr>&lt;th>Medication&lt;/th>&lt;th>Instructions&lt;/th>&lt;th>Start Date&lt;/th>&lt;th>Status&lt;/th>&lt;/tr>&lt;/thead>&lt;tbody>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403091200.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102100.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403090800.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102100.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090403131900.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403091200.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090402131700.000;20090504235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113500.000;20090403113500.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403180000.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403090800.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403131600.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403090800.000;20090403090800.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403113400.000;20090403113400.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403102000.000;20090403102000.000]&lt;/td>&lt;td>&lt;td>&lt;td>aborted&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090401091800.000;20090331235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;td>#160;&lt;/td>&lt;td>#160;&lt;/td>&lt;td>[20090403131800.000;20090505235959.000]&lt;/td>&lt;td>&lt;td>&lt;td>completed&lt;td>&lt;td>&lt;/tbody>&lt;/table>";
	
	public static String sanitizeTableTag(String tableTagSnippet)
	{
		String tableXml = tableTagSnippet.replaceAll("&lt;", "<");
		tableXml = tableXml.replaceAll("&gt;", ">");
		System.out.println("tableXml=" + tableXml);
		Node rootNode = null;
		Node currNode = null;
		StringTokenizer st = new StringTokenizer(tableXml, "<>", false);
		while (st.hasMoreTokens())
		{
			String nextToken = st.nextToken();
			String[] subTokens = nextToken.split(" ");
			System.out.println("nextToken = " + nextToken);
			String data = subTokens[0];
			if (data.startsWith("/"))
			{
				data = data.substring(data.indexOf("/") + 1);
				if (currNode.type().equalsIgnoreCase(data))
				{
					currNode = currNode.parent();
				}
				else
				{
					// Ignore unnecessary closing tags
					System.out.println("Ignored end tag " + data);
				}
			}
			else
			{
				if (Node.isValidNodeType(data))
				{
					if (rootNode == null)
					{
						rootNode = Node.newInstance(subTokens[0]);
						currNode = rootNode;
					}
					else if (currNode.isSupportedChildType(data))
					{
						Node childNode = Node.newInstance(data);
						currNode.addChild(childNode);
						currNode = childNode;
					}
					else
					{
						System.out.println("Incorrect start tag " + data);
					}
					for (int i = 1; i < subTokens.length; i++)
					{
						currNode.addAttribute(subTokens[i]);
					}
				}
				else
				{
					Node childNode = Node.newInstance("#text");
					((TextNode) childNode).setValue(nextToken);
					currNode.addChildSmart(childNode);
					currNode = childNode.parent();
				}
			}
		}
		rootNode.print();
		return null;
	}
	
	public static void main(String[] args)
	{
		HTMLTagSanitizer.sanitizeTableTag(HTMLTagSanitizer.TEST_TABLE_TAG);
	}
}
