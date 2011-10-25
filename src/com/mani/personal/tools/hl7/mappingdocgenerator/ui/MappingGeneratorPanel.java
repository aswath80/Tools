/**
 * MappingGeneratorPanel.java
 * Creation Date: Aug 10, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.mappingdocgenerator.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.action.MappingActionListener;
import com.mani.personal.tools.hl7.mappingdocgenerator.ui.find.TextFindDialog;
import com.mani.personal.tools.hl7.mappingdocgenerator.ui.helper.MessageType;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class MappingGeneratorPanel extends JPanel
{
	private JTextArea xPathTextArea = new JTextArea();
	private JComboBox interactionIdComboBox = new JComboBox();
	private JButton generateButton = new JButton(
	         UIConstants.GENERATE_MAPPING_ACTION);
	private TextFindDialog textFindDialog = new TextFindDialog(xPathTextArea);
	
	private MappingActionListener mappingActionListener = new MappingActionListener(
	         xPathTextArea);
	
	public MappingGeneratorPanel()
	{
		populateMessageTypeNames();
		setActions();
		addComponents();
	}
	
	private void populateMessageTypeNames()
	{
		interactionIdComboBox
		         .setActionCommand(UIConstants.SELECT_MESSAGE_TYPE_ACTION);
		interactionIdComboBox.setAutoscrolls(true);
		interactionIdComboBox.addItem(new MessageType("Select Message Type",
		         UIConstants.INVALID_VALUE));
		interactionIdComboBox.addItem(new MessageType("Admit Request",
		         "PRPA_IN420001"));
		interactionIdComboBox.addItem(new MessageType(
		         "Bed Status Observation Event", "PRPA_IN000220"));
		interactionIdComboBox.addItem(new MessageType(
		         "Clinical Document Message", "POCD_IN000001"));
		interactionIdComboBox.addItem(new MessageType(
		         "Clinical Trial Laboratory Observation Periodic Report",
		         "PORT_IN010001"));
		interactionIdComboBox.addItem(new MessageType("Condition Problem",
		         "POPR_IN000001"));
		interactionIdComboBox.addItem(new MessageType(
		         "Detailed Financial Transaction", "FIAB_IN000001"));
		interactionIdComboBox.addItem(new MessageType(
		         "Diagnostic Report Observation Event", "POXX_IN112003"));
		interactionIdComboBox.addItem(new MessageType("Diet Request",
		         "PODO_IN000001"));
		interactionIdComboBox.addItem(new MessageType("Discharge Request",
		         "PRPA_IN430001"));
		interactionIdComboBox.addItem(new MessageType("Encounter Appointment",
		         "PRPA_IN410001"));
		interactionIdComboBox.addItem(new MessageType("Encounter Event",
		         "PRPA_IN400000"));
		interactionIdComboBox.addItem(new MessageType(
		         "Individual Case Safety Report (Adverse Event Report)",
		         "PORR_IN040001UV01"));
		interactionIdComboBox.addItem(new MessageType(
		         "Intolerance Observation Event", "PRPA_IN000601"));
		interactionIdComboBox.addItem(new MessageType("Medication Supply Event",
		         "POSP_IN000101"));
		interactionIdComboBox.addItem(new MessageType(
		         "Notifiable Condition (Case Report)", "PORR_IN100001"));
		interactionIdComboBox.addItem(new MessageType("Observation Event",
		         "POXX_IN000003"));
		interactionIdComboBox.addItem(new MessageType("Observation Order",
		         "POXX_IN120001"));
		interactionIdComboBox.addItem(new MessageType("Patient Referral Request",
		         "REPC_IN002001"));
		interactionIdComboBox.addItem(new MessageType("Person Merge",
		         "PRPA_IN000004"));
		interactionIdComboBox.addItem(new MessageType("Person Registry",
		         "PRPA_IN000001"));
		interactionIdComboBox.addItem(new MessageType("Person Unmerge",
		         "PRPA_IN000005"));
		interactionIdComboBox.addItem(new MessageType("Procedure Order",
		         "POXX_IN130001"));
		interactionIdComboBox.addItem(new MessageType("Result Event",
		         "POLB_IN004102"));
		interactionIdComboBox.addItem(new MessageType(
		         "Specimen Observation Event", "POXX_IN111001"));
		interactionIdComboBox.addItem(new MessageType(
		         "Specimen Observation Order", "POXX_IN121001"));
		interactionIdComboBox.addItem(new MessageType("Staff Registry",
		         "MFFI_IN000101"));
		interactionIdComboBox.addItem(new MessageType(
		         "Substance Administration Event", "POSA_IN000101"));
		interactionIdComboBox.addItem(new MessageType(
		         "Substance Administration Order", "POSA_IN000001"));
		interactionIdComboBox.addItem(new MessageType("Supply Request (Order)",
		         "POSP_IN000001"));
		interactionIdComboBox.addItem(new MessageType("Transfer Request",
		         "PRPA_IN302001"));
	}
	
	/**
	 * Method setActions
	 */
	private void setActions()
	{
		generateButton.addActionListener(mappingActionListener);
		interactionIdComboBox.addActionListener(mappingActionListener);
		xPathTextArea.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() &&
				         e.getKeyCode() == KeyEvent.VK_F)
				{
					textFindDialog.setVisible(true);
				}
			}
		});
	}
	
	private void addComponents()
	{
		setLayout(new BorderLayout());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		JPanel topPanel = new JPanel(gridBagLayout);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		topPanel.add(interactionIdComboBox, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		topPanel.add(generateButton, gridBagConstraints);
		topPanel.setBorder(new TitledBorder("Select Message Type"));
		
		JScrollPane xPathTextScrollPane = new JScrollPane(xPathTextArea,
		         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		xPathTextScrollPane.setAutoscrolls(true);
		xPathTextArea.setEditable(false);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(xPathTextScrollPane, BorderLayout.CENTER);
		bottomPanel.setBorder(new TitledBorder("XPath"));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(bottomPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
	}
}
