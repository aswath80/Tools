/**
 * MappingActionListener.java
 * Creation Date: Aug 10, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.mappingdocgenerator.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.UIConstants;
import com.mani.personal.tools.hl7.mappingdocgenerator.ui.helper.MessageType;
import com.mani.personal.tools.hl7.schema.util.V3SchemaParser;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class MappingActionListener implements ActionListener
{
	private JTextArea xPathTextArea;
	private String selectedMessageType;
	private String selectedInteractionId;
	private V3SchemaParser v3SchemaParser = new V3SchemaParser();
	
	/**
	 * Constructor MappingActionListener
	 * 
	 * @param xPathTextArea
	 */
	public MappingActionListener(JTextArea xPathTextArea)
	{
		this.xPathTextArea = xPathTextArea;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		Object source = actionEvent.getSource();
		if (source instanceof JButton)
		{
			if (UIConstants.GENERATE_MAPPING_ACTION.equals(actionEvent
			         .getActionCommand()))
			{
				if (selectedInteractionId == null ||
				         UIConstants.INVALID_VALUE.equals(selectedInteractionId))
				{
					JOptionPane.showMessageDialog((Component) source,
					         "Select a message type from drop down!",
					         "No Message Type Selected!",
					         JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					try
					{
						xPathTextArea.setText("Generating XPath for message type " +
						         selectedMessageType + " ...");
						// URL schemaURL = getClass().getResource(
						// "/defs/rim214101/schemas/" + selectedInteractionId +
						// ".xsd");
						String schemaLoc = "/defs/rim214101/schemas/" +
						         selectedInteractionId + ".xsd";
						System.out.println("schemaLoc = " + schemaLoc);
						v3SchemaParser.parseV3SchemaFromjar(schemaLoc);
						Stack<String> outputStack = v3SchemaParser.getOutputStack();
						StringBuilder sb = new StringBuilder();
						while (!outputStack.isEmpty())
						{
							sb.append(outputStack.pop() + "\n");
						}
						xPathTextArea.setText(sb.toString());
						xPathTextArea.select(0, 0);
					}
					catch (Throwable e)
					{
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						JOptionPane.showMessageDialog((Component) source,
						         sw.toString(), "XPath Generation Failed!",
						         JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		}
		else if (source instanceof JComboBox)
		{
			if (UIConstants.SELECT_MESSAGE_TYPE_ACTION.equals(actionEvent
			         .getActionCommand()))
			{
				MessageType messageType = (MessageType) ((JComboBox) source)
				         .getSelectedItem();
				selectedMessageType = messageType.getMessageTypeName();
				selectedInteractionId = messageType.getInteractionId();
			}
		}
	}
}
