/**
 * QueryToolFrame.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.query.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.mani.personal.tools.hl7.mappingdocgenerator.ui.UIConstants;
import com.mani.personal.tools.hl7.query.helper.ConnectionConstants;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class QueryToolFrame extends JFrame
{
	private QueryToolPanel queryToolPanel = new QueryToolPanel();
	
	public QueryToolFrame()
	{
		super("HTB Data Query Tool - By E.Manikandan");
		setLayout(new BorderLayout());
		addConnectionMenu();
		add(queryToolPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Method addConnectionMenu
	 */
	private void addConnectionMenu()
	{
		final JMenu connectionMenu = new JMenu("Connections");
		final JMenuItem addConnectionMenuItem = new JMenuItem("Add Connection");
		addConnectionMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ConnectionDetailsDialog connectionDetailsDialog = new ConnectionDetailsDialog(
				         addConnectionMenuItem);
				connectionDetailsDialog.setVisible(true);
				connectionMenu.removeAll();
				connectionMenu.add(addConnectionMenuItem);
				loadMenuItemForEachConnection(connectionMenu);
			}
		});
		connectionMenu.add(addConnectionMenuItem);
		loadMenuItemForEachConnection(connectionMenu);
		JMenuBar connMenubar = new JMenuBar();
		connMenubar.add(connectionMenu);
		setJMenuBar(connMenubar);
	}
	
	private void loadMenuItemForEachConnection(JMenu connMenu)
	{
		File connFile = new File(ConnectionConstants.CONNECTION_FILE);
		if (connFile.exists())
		{
			try
			{
				Properties properties = new Properties();
				FileInputStream inStream = new FileInputStream(connFile);
				properties.load(inStream);
				inStream.close();
				String connectionNames = (String) properties
				         .get(ConnectionConstants.CONNECTION_NAMES);
				if (connectionNames != null && !connectionNames.isEmpty())
				{
					String connNames[] = connectionNames.split(",");
					ButtonGroup buttonGroup = new ButtonGroup();
					connMenu.addSeparator();
					for (String connName : connNames)
					{
						JRadioButtonMenuItem connMenuItem = new JRadioButtonMenuItem(
						         connName);
						connMenuItem.setActionCommand(UIConstants.CONN_SELECT);
						connMenuItem.addActionListener(queryToolPanel
						         .getQueryActionListener());
						buttonGroup.add(connMenuItem);
						connMenu.add(connMenuItem);
					}
				}
			}
			catch (IOException e)
			{
				// Ignore
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		QueryToolFrame queryToolFrame = new QueryToolFrame();
		queryToolFrame.setSize(1000, 700);
		queryToolFrame.setResizable(false);
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Determine the new location of the window
		int w = queryToolFrame.getSize().width;
		int h = queryToolFrame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		
		// Move the window
		queryToolFrame.setLocation(x, y);
		queryToolFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		queryToolFrame.setVisible(true);
	}
}
