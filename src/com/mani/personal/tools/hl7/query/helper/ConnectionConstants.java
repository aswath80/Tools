/**
 * ConnectionConstants.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.helper;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public interface ConnectionConstants
{
	public static final String CONNECTION_NAMES = "connection.names";
	public static final String RMI_FACTORY = "java.naming.factory.initial";
	public static final String RMI_FACTORY_VALUE = "com.evermind.server.rmi.RMIInitialContextFactory";
	public static final String RMI_PROVIDER_URL = "java.naming.provider.url";
	public static final String JNDI_USER_NAME = "java.naming.security.principal";
	public static final String JNDI_USER_PASSWORD = "java.naming.security.credentials";
	public static final String CLIENT_MODE = "ClientMode";
	public static final String CLIENT_MODE_VALUE = "remote";
	public static final String DOT = ".";
	public static final String CONNECTION_FILE = "C:\\QueryToolConnections.properties";
}
