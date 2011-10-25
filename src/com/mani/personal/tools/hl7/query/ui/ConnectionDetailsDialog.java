/**
 * ConnectionDetailsDialog.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.ui;

import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.CLIENT_MODE;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.CLIENT_MODE_VALUE;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.CONNECTION_NAMES;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.DOT;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.JNDI_USER_NAME;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.JNDI_USER_PASSWORD;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.RMI_FACTORY;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.RMI_FACTORY_VALUE;
import static com.mani.personal.tools.hl7.query.helper.ConnectionConstants.RMI_PROVIDER_URL;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.mani.personal.tools.hl7.query.helper.ConnectionConstants;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class ConnectionDetailsDialog extends JDialog
{
	private JPanel connPanel = new JPanel();
	private JTextField connNameTextField = new JTextField("rhhtb2", 25);
	private JTextField providerURLTextField = new JTextField(
	         "ormi://rhhtb2.ctisinc.com:23792/htb", 25);
	private JTextField userNameTextField = new JTextField("htbadmin", 25);
	private JPasswordField passwordTextField = new JPasswordField("applhtb", 25);
	private JButton saveButton = new JButton("Save");
	private JButton cancelButton = new JButton("Cancel");
	
	public ConnectionDetailsDialog(JComponent jComponent)
	{
		super(getRootParentComponent(jComponent), "RMI Connection Details",
		         ModalityType.APPLICATION_MODAL);
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
		connPanel.setLayout(new GridBagLayout());
		addComponentUsingGBC(connPanel, new JLabel("Connection Name"), 0, 0, 1, 1);
		addComponentUsingGBC(connPanel, connNameTextField, 2, 0, 3, 1);
		addComponentUsingGBC(connPanel, new JLabel("RMI Provider URL"), 0, 1, 1,
		         1);
		addComponentUsingGBC(connPanel, providerURLTextField, 2, 1, 3, 1);
		addComponentUsingGBC(connPanel, new JLabel("Username"), 0, 2, 1, 1);
		addComponentUsingGBC(connPanel, userNameTextField, 2, 2, 3, 1);
		addComponentUsingGBC(connPanel, new JLabel("Password"), 0, 3, 1, 1);
		addComponentUsingGBC(connPanel, passwordTextField, 2, 3, 3, 1);
		addComponentUsingGBC(connPanel, saveButton, 2, 4, 1, 1);
		addComponentUsingGBC(connPanel, cancelButton, 3, 4, 1, 1);
		
		connPanel.setBorder(new TitledBorder("RMI Connection Details"));
		setLayout(new BorderLayout(0, 0));
		add(connPanel, BorderLayout.CENTER);
		setSize(400, 200);
		
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
	
	private void addComponentUsingGBC(JComponent parent, JComponent component,
	         int x, int y, int width, int height)
	{
		int weightx = 1;
		int weighty = 0;
		int anchor = GridBagConstraints.WEST;
		int fill = GridBagConstraints.HORIZONTAL;
		Insets insets = new Insets(5, 5, 5, 5);
		GridBagConstraints gbc = new GridBagConstraints(x, y, width, height,
		         weightx, weighty, anchor, fill, insets, 0, 0);
		parent.add(component, gbc);
	}
	
	private void addListeners()
	{
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					File connFile = new File(ConnectionConstants.CONNECTION_FILE);
					if (!connFile.exists())
					{						
						boolean created = connFile.createNewFile();
						if (!created)
						{
							JOptionPane.showMessageDialog(saveButton,
							         "Could not create new connection file " +
							                  connFile.getAbsolutePath(),
							         "Save Falied!", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					validateSave();
					Properties properties = new Properties();
					FileInputStream inStream = new FileInputStream(connFile);
					properties.load(inStream);
					inStream.close();
					String connName = connNameTextField.getText();
					if (properties.containsKey(RMI_PROVIDER_URL + DOT + connName))
					{
						int option = JOptionPane.showConfirmDialog(saveButton,
						         "Connection " + connName +
						                  " already exists. Overwrite?",
						         "Connection Already Exists!",
						         JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.NO_OPTION)
						{
							return;
						}
					}
					if (!properties.containsKey(CONNECTION_NAMES))
					{
						properties.put(CONNECTION_NAMES, connName);
					}
					else
					{
						properties.put(CONNECTION_NAMES,
						         properties.get(CONNECTION_NAMES) + "," + connName);
					}
					properties.put(RMI_FACTORY + DOT + connName, RMI_FACTORY_VALUE);
					properties.put(RMI_PROVIDER_URL + DOT + connName,
					         providerURLTextField.getText());
					properties.put(JNDI_USER_NAME + DOT + connName,
					         userNameTextField.getText());
					properties.put(JNDI_USER_PASSWORD + DOT + connName, new String(
					         passwordTextField.getPassword()));
					properties.put(CLIENT_MODE + DOT + connName, CLIENT_MODE_VALUE);
					FileOutputStream outStream = new FileOutputStream(connFile);
					properties.store(outStream,
					         "Generated connection details for Mani's Query Tool. "
					                  + "Please do not edit manually.");
					outStream.close();
					JOptionPane.showMessageDialog(saveButton,
					         "Connection details for " + connName + " saved",
					         "Connection Saved", JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
				}
				catch (Throwable t)
				{
					JOptionPane.showMessageDialog(saveButton, t.toString(),
					         "Save Falied!", JOptionPane.ERROR_MESSAGE);
				}				
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
	}
	
	private void validateSave() throws Exception
	{
		String connName = connNameTextField.getText();
		if (connName == null || connName.isEmpty())
		{
			throw new Exception("Connection name can not be empty!");
		}
		String providerURL = providerURLTextField.getText();
		if (providerURL == null || providerURL.isEmpty())
		{
			throw new Exception("Provider URL can not be empty!");
		}
		
		Pattern pattern = Pattern
		         .compile("ormi://[a-zA-Z0-9\\.\\-_]+:[0-9]+/[a-zA-Z0-9_\\-]+");
		Matcher matcher = pattern.matcher(providerURL);
		if (!matcher.matches())
		{
			throw new Exception(
			         "Provider URL not in the format ormi://<hostname>:<RMI Port>/htb");
		}
		String userName = userNameTextField.getText();
		if (userName == null || userName.isEmpty())
		{
			throw new Exception("Username can not be empty!");
		}
		String password = new String(passwordTextField.getPassword());
		if (password == null || password.isEmpty())
		{
			throw new Exception("Password can not be empty!");
		}
	}
	
}
