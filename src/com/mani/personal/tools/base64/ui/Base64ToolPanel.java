/**
 * QueryToolPanel.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.base64.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.evermind.util.Base64Utils;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class Base64ToolPanel extends JPanel
{
	private JTextArea base64EncodedTextArea = new JTextArea();
	private JTextArea base64DecodedTextArea = new JTextArea();
	// private JButton encodeButton = new JButton("Encode");
	private JButton decodeButton = new JButton("Extract Document");
	
	public Base64ToolPanel()
	{
		setActions();
		addComponents();
	}
	
	/**
	 * Method setActions
	 */
	private void setActions()
	{
		// encodeButton.addActionListener(new ActionListener()
		// {
		// @Override
		// public void actionPerformed(ActionEvent e)
		// {
		// String decodedText = base64DecodedTextArea.getText();
		// base64EncodedTextArea.setText(new String(Base64Utils
		// .encode(decodedText.getBytes())));
		// }
		// });
		decodeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String soapResponse = base64EncodedTextArea.getText();
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					         .newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new StringBufferInputStream(
					         soapResponse));
					doc.getDocumentElement().normalize();
					Element rootEnvelope = doc.getDocumentElement();
					System.out.println("rootEnvelope=" + rootEnvelope.getNodeName());
					Element body = (Element) rootEnvelope.getElementsByTagName(
					         "Body").item(0);
					System.out.println("body=" + body.getNodeName());
					Element ccdResponse = (Element) body.getElementsByTagName(
					         "generateCCDResponseElement").item(0);
					System.out.println("ccdResponse=" + ccdResponse.getNodeName());
					Element ccdDoc = (Element) ccdResponse.getElementsByTagName(
					         "document").item(0);
					System.out.println("ccdDoc=" + ccdDoc.getNodeName());
					
					String encodedText = ccdDoc.getTextContent();
					// System.out.println(encodedText);
					String decodedText = new String(Base64Utils.decode(encodedText
					         .toCharArray()));
					decodedText = "<?xml-stylesheet type=\"text/xsl\" href=\"CCD.xsl\"?>" +
					         "\n" + decodedText;
					decodedText = decodedText.replace("&lt;", "<");
					decodedText = decodedText.replace("#160;", "&#160;");
					base64DecodedTextArea.setText(decodedText);
				}
				catch (Exception ex)
				{
					StringWriter sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					JOptionPane.showMessageDialog(decodeButton,
					         "Exception: " + sw.toString(),
					         "Exception in CCD Document Extraction",
					         JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
	}
	
	private void addComponents()
	{
		setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 2;
		gridBagConstraints.weighty = 8;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		
		JScrollPane base64EncodedScrollPane = new JScrollPane(
		         base64EncodedTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		base64EncodedScrollPane.setAutoscrolls(true);
		base64EncodedScrollPane.setBorder(new TitledBorder(
		         "HTB CCD SOAP Response"));
		
		add(base64EncodedScrollPane, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		
		JScrollPane base64DecodedScrollPane = new JScrollPane(
		         base64DecodedTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		base64DecodedScrollPane.setAutoscrolls(true);
		base64DecodedScrollPane.setBorder(new TitledBorder("HTB Generated CCD"));
		
		add(base64DecodedScrollPane, gridBagConstraints);
		
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 2;
		
		// add(encodeButton, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		
		add(decodeButton, gridBagConstraints);
	}
}
