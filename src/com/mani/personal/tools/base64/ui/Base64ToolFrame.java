/**
 * QueryToolFrame.java
 * Creation Date: Aug 12, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.base64.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class Base64ToolFrame extends JFrame
{
	private Base64ToolPanel queryToolPanel = new Base64ToolPanel();
	
	public Base64ToolFrame()
	{
		super("HTB CCD Document Extractor");
		setLayout(new BorderLayout());
		add(queryToolPanel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args)
	{
		Base64ToolFrame queryToolFrame = new Base64ToolFrame();
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
