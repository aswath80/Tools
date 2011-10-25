/**
 * TextFindDialog.java
 * Creation Date: Aug 11, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.mappingdocgenerator.ui.find;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class TextFindDialog extends JDialog
{
	private JPanel findPanel = new JPanel();
	private JTextField findTextField = new JTextField();
	private JButton findNextButton = new JButton("Find Next");
	private JButton cancelButton = new JButton("Cancel");
	private int lastFindIndex = 0;
	private JTextArea jTextArea;
	
	public TextFindDialog(JTextArea jTextArea)
	{
		super(getRootParentComponent(jTextArea), "Find Text",
		         ModalityType.APPLICATION_MODAL);
		this.jTextArea = jTextArea;
		layoutCompoents();
		addListeners();
	}
	
	private static Window getRootParentComponent(JComponent jComponent)
	{
		Container parent = jComponent;
		while (parent != null && !(parent instanceof Window))
		{
			parent = parent.getParent();
		}
		return (parent instanceof Window) ? (Window) parent : null;
	}
	
	private void layoutCompoents()
	{
		findPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		findPanel.add(findTextField, gbc);
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		findPanel.add(findNextButton, gbc);
		gbc.gridx = 1;
		findPanel.add(cancelButton, gbc);
		findPanel.setBorder(new TitledBorder("Find Text"));
		setLayout(new BorderLayout());
		add(findPanel, BorderLayout.CENTER);
		setSize(250, 150);
		
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Determine the new location of the window
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		
		// Move the window
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setResizable(false);
	}
	
	private void addListeners()
	{
		findNextButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String text = jTextArea.getText();
				String findText = findTextField.getText();
				if (text != null && findText != null && !"".equals(text.trim()) &&
				         !"".equals(findText.trim()))
				{
					lastFindIndex = (lastFindIndex < text.length()) ? lastFindIndex
					         : 0;
					int startIndex = text.indexOf(findText, lastFindIndex);
					int endIndex = startIndex + findText.length();
					if (startIndex != -1 && endIndex != -1)
					{
						jTextArea.setCaretPosition(startIndex);
						jTextArea.moveCaretPosition(endIndex);
					}
					else
					{
						JOptionPane.showMessageDialog(findNextButton,
						         "Text not found!", "Find Text Failed!",
						         JOptionPane.WARNING_MESSAGE);
					}
					lastFindIndex = endIndex + 1;
				}
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				lastFindIndex = 0;
				setVisible(false);
			}
		});
	}
}
