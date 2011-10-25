/**
 * QueryActionListener.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.UIConstants;
import com.mani.personal.tools.hl7.query.helper.ConnectionConstants;
import com.mani.personal.tools.hl7.query.rim.RimQueryExecutor;
import com.mani.personal.tools.hl7.util.ExceptionUtil;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class QueryActionListener implements ActionListener
{
	private JTextField rootIdTextField;
	private JTextField extnTextField;
	private JTable queryResultsTable;
	private String rimObjectToQuery = UIConstants.ACT_RADIO_COMMAND;
	private String connectionName;
	
	public QueryActionListener(JTextField rootIdTextField,
	         JTextField extnTextField, JTable queryResultsTable)
	{
		this.rootIdTextField = rootIdTextField;
		this.extnTextField = extnTextField;
		this.queryResultsTable = queryResultsTable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		File connFile = new File(ConnectionConstants.CONNECTION_FILE);
		if (UIConstants.QUERY_ACTION.equals(e.getActionCommand()))
		{
			if (connectionName == null || connectionName.isEmpty())
			{
				JOptionPane.showMessageDialog((Component) e.getSource(),
				         "No connection selected! "
				                  + "Select or add a connection first.",
				         "Connections Not Available!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!connFile.exists())
			{
				JOptionPane.showMessageDialog(
				         (Component) e.getSource(),
				         "No connection exists in connection file " +
				                  connFile.getAbsolutePath() +
				                  " Add a connection first.",
				         "No Connections Found!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try
			{
				Properties properties = new Properties();
				FileInputStream inStream = new FileInputStream(connFile);
				properties.load(inStream);
				inStream.close();
				Properties connectionProperties = new Properties();
				connectionProperties.put(
				         ConnectionConstants.RMI_FACTORY,
				         properties.get(ConnectionConstants.RMI_FACTORY +
				                  ConnectionConstants.DOT + connectionName));
				connectionProperties.put(
				         ConnectionConstants.RMI_PROVIDER_URL,
				         properties.get(ConnectionConstants.RMI_PROVIDER_URL +
				                  ConnectionConstants.DOT + connectionName));
				connectionProperties.put(
				         ConnectionConstants.JNDI_USER_NAME,
				         properties.get(ConnectionConstants.JNDI_USER_NAME +
				                  ConnectionConstants.DOT + connectionName));
				connectionProperties.put(
				         ConnectionConstants.JNDI_USER_PASSWORD,
				         properties.get(ConnectionConstants.JNDI_USER_PASSWORD +
				                  ConnectionConstants.DOT + connectionName));
				connectionProperties.put(
				         ConnectionConstants.CLIENT_MODE,
				         properties.get(ConnectionConstants.CLIENT_MODE +
				                  ConnectionConstants.DOT + connectionName));
				
				String root = rootIdTextField.getText();
				String ext = extnTextField.getText();
				if (root == null || root.isEmpty())
				{
					JOptionPane.showMessageDialog((Component) e.getSource(),
					         "Valid rootId is mandatory to query data",
					         "Missing Query Parameter!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				RimQueryExecutor rimQueryExecutor = new RimQueryExecutor(
				         connectionProperties);
				Map<String, String> rimAttributeMap = rimQueryExecutor
				         .executeRimQuery(rimObjectToQuery, root, ext);
				Vector<String> columnVector = new Vector<String>();
				columnVector.add("Attribute");
				columnVector.add("Value");
				Vector<Vector<String>> dataVector = getDataVectorForJTable(rimAttributeMap);
				((DefaultTableModel) queryResultsTable.getModel()).setDataVector(
				         dataVector, columnVector);
			}
			catch (IOException ioe)
			{
				JOptionPane.showMessageDialog(
				         (Component) e.getSource(),
				         "Error getting connection details. " +
				                  ExceptionUtil.exceptionToString(ioe),
				         "Error Retrieving Connection!", JOptionPane.ERROR_MESSAGE);
			}
			catch (Throwable t)
			{
				JOptionPane.showMessageDialog(
				         (Component) e.getSource(),
				         "Error executing RIM query. " +
				                  ExceptionUtil.exceptionToString(t),
				         "Error Executing Query!", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (UIConstants.CONN_SELECT.equals(e.getActionCommand()))
		{
			connectionName = ((JRadioButtonMenuItem) e.getSource()).getText();
		}
		else
		{
			rimObjectToQuery = e.getActionCommand();
		}
	}
	
	/**
	 * Method getDataVectorForJTable
	 * 
	 * @param rimAttributeMap
	 * @return
	 */
	private Vector<Vector<String>> getDataVectorForJTable(
	         Map<String, String> rimAttributeMap)
	{
		Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
		for (String attributeName : rimAttributeMap.keySet())
		{
			Vector<String> valueVector = new Vector<String>();
			valueVector.add(attributeName);
			valueVector.add(rimAttributeMap.get(attributeName));
			dataVector.add(valueVector);
		}
		Collections.sort(dataVector, new Comparator<Vector<String>>()
		{
			@Override
			public int compare(Vector<String> v1, Vector<String> v2)
			{
				return v1.get(0).compareTo(v2.get(0));
			}
		});
		return dataVector;
	}
}
