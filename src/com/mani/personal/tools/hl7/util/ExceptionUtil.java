/**
 * ExceptionUtil.java
 * Creation Date: Aug 15, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class ExceptionUtil
{
	public static String exceptionToString(Throwable e)
	{
		if (e != null)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		}
		return null;
	}
}
