/**
 * QueryToolPanel.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.UIConstants;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class QueryToolPanel extends JPanel
{
	private JRadioButton actRadioButton = new JRadioButton("Act", true);
	private JRadioButton entityRadioButton = new JRadioButton("Entity", false);
	private JRadioButton roleRadioButton = new JRadioButton("Role", false);
	private JTextField rootIdTextField = new JTextField(25);
	private JTextField extnTextField = new JTextField(25);
	private JButton queryButton = new JButton(UIConstants.QUERY_ACTION);
	private JTable queryResultsTable = new JTable();
	
	private QueryActionListener queryActionListener = new QueryActionListener(
	         rootIdTextField, extnTextField, queryResultsTable);
	
	public QueryToolPanel()
	{
		setActions();
		addComponents();
	}
	
	/**
	 * Method setActions
	 */
	private void setActions()
	{
		actRadioButton.setActionCommand(UIConstants.ACT_RADIO_COMMAND);
		entityRadioButton.setActionCommand(UIConstants.ENTITY_RADIO_COMMAND);
		roleRadioButton.setActionCommand(UIConstants.ROLE_RADIO_COMMAND);
		
		actRadioButton.addActionListener(queryActionListener);
		entityRadioButton.addActionListener(queryActionListener);
		roleRadioButton.addActionListener(queryActionListener);
		
		queryButton.addActionListener(queryActionListener);
	}
	
	private void addComponents()
	{
		setLayout(new BorderLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(actRadioButton);
		buttonGroup.add(entityRadioButton);
		buttonGroup.add(roleRadioButton);
		
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		radioButtonPanel.add(actRadioButton);
		radioButtonPanel.add(entityRadioButton);
		radioButtonPanel.add(roleRadioButton);
		radioButtonPanel.setBorder(new TitledBorder("RIM Object Type"));
		
		JPanel searchPanel = new JPanel(new GridBagLayout());
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		searchPanel.add(new JLabel("Root Id"), gridBagConstraints);
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		searchPanel.add(rootIdTextField, gridBagConstraints);
		searchPanel.add(new JLabel("Extension Text"), gridBagConstraints);
		searchPanel.add(extnTextField, gridBagConstraints);
		searchPanel.add(queryButton, gridBagConstraints);
		searchPanel.setBorder(new TitledBorder("RIM Object II"));
		
		JScrollPane xPathTextScrollPane = new JScrollPane(queryResultsTable,
		         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		xPathTextScrollPane.setAutoscrolls(true);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(radioButtonPanel);
		topPanel.add(searchPanel);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(xPathTextScrollPane, BorderLayout.CENTER);
		bottomPanel.setBorder(new TitledBorder("Query Results"));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(bottomPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Method getQueryActionListener
	 * 
	 * @return the queryActionListener
	 */
	QueryActionListener getQueryActionListener()
	{
		return queryActionListener;
	}
}
