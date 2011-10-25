/**
 * QueryParamSubstitutor.java
 * Creation Date: Sep 15, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class QueryParamSubstitutor
{
	public void substituteQueryParams(String qryFile, String paramPropsFile)
	         throws IOException
	{
		Properties paramProps = new Properties();
		paramProps.load(new FileInputStream(new File(paramPropsFile)));
		System.out.println("paramProps=" + paramProps);
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(new File(qryFile)));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				int startIndex = 0;
				int paramEndIndex = 0;
				int colonIndex = line.indexOf(":");
				while (colonIndex != -1)
				{
					paramEndIndex = line.indexOf(" ", colonIndex);
					paramEndIndex = (paramEndIndex == -1) ? line.indexOf(")",
					         colonIndex) : paramEndIndex;
					paramEndIndex = (paramEndIndex == -1) ? line.indexOf(";",
					         colonIndex) : paramEndIndex;
					paramEndIndex = (paramEndIndex == -1) ? line.length()
					         : paramEndIndex;
					
					String qryParam = line.substring(colonIndex + 1, paramEndIndex);
					System.out.println("qryParam = " + qryParam);
					sb.append(line.substring(startIndex, colonIndex));
					sb.append(" " + paramProps.get(qryParam) + " ");
					startIndex = paramEndIndex;
					colonIndex = line.indexOf(":", paramEndIndex);
				}
				sb.append(line.substring(paramEndIndex) + " ");
			}
			System.out.println(sb.toString());
		}
		finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		new QueryParamSubstitutor().substituteQueryParams("C:\\Query.txt",
		         "C:\\QueryParams.txt");
	}
}
