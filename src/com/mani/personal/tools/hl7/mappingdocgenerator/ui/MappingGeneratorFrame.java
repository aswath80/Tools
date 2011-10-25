/**
 * MappingGeneratorFrame.java
 * Creation Date: Aug 10, 2011
 * Created By: E.Manikandan
 */
package com.mani.personal.tools.hl7.mappingdocgenerator.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author E.Manikandan
 * @version $Revision:$
 */
public class MappingGeneratorFrame extends JFrame
{
	public MappingGeneratorFrame()
	{
		super("V3 Mapping XPath Generator - By E.Manikandan");
		setLayout(new BorderLayout());
		add(new MappingGeneratorPanel(), BorderLayout.CENTER);
	}
	
	public static void main(String[] args)
	{
		MappingGeneratorFrame mappingGeneratorFrame = new MappingGeneratorFrame();
		mappingGeneratorFrame.setSize(900, 600);
		mappingGeneratorFrame.setResizable(false);
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Determine the new location of the window
		int w = mappingGeneratorFrame.getSize().width;
		int h = mappingGeneratorFrame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		
		// Move the window
		mappingGeneratorFrame.setLocation(x, y);
		mappingGeneratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mappingGeneratorFrame.setVisible(true);
	}
}
