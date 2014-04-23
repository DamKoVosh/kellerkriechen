package dd.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dd.Board;
import dd.Intelligence.UserInterfaceIntelligence;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final JTextArea logText = new JTextArea();
	
	public boolean endTurnClicked = false;
	
	private MapPanel mapPanel;
	
	private DefaultListModel<String> actionListMinor;
	private DefaultListModel<String> actionListMove;
	private DefaultListModel<String> actionListStandard;
	
	public Window(Board b) {
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout(2, 2));
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2,
				(d.height - getSize().height) / 2);
		
		mapPanel = new MapPanel(b);
		
		add(mapPanel, BorderLayout.CENTER);
		add(buildMenue(), BorderLayout.LINE_END);
		add(buildLog(), BorderLayout.PAGE_END);
		
		setVisible(true);
	}
	
	public void log(String text) {
		boolean update = (logText.getCaretPosition() == logText.getDocument()
				.getLength());
		
		logText.append(text + "\n");
		
		if (update) {
			logText.setCaretPosition(logText.getDocument().getLength());
		}
		// logText.setText(logText.getText() + text + "\n");
	}
	
	public void updateMap() {
		mapPanel.repaint();
	}
	
	public MapPanel getMap() {
		return mapPanel;
	}
	
	private Component buildActionLists() {
		JPanel actionLists = new JPanel();
		actionLists.setLayout(new BoxLayout(actionLists, BoxLayout.PAGE_AXIS));
		
		for (final String label : new String[] { "Standard", "Move", "Minor", "Free" }) {
			
			JPanel listBox = new JPanel();
			listBox.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(label),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			listBox.setLayout(new BoxLayout(listBox, BoxLayout.PAGE_AXIS));
			
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			
			switch (label) {
			case "Standard":
				actionListStandard = listModel;
				break;
			case "Move":
				actionListMove = listModel;
				break;
			case "Minor":
				actionListMinor = listModel;
				break;
			default:
				
			}
			
			//listModel.addElement("Walk");
			final JList<String> list = new JList<String>(listModel);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// list.setVisibleRowCount(5);
			JScrollPane listScrollPane = new JScrollPane(list);
			
			listBox.add(listScrollPane);
			
			final JButton butt = new JButton("Use");
			butt.setEnabled(false);
			listBox.add(butt);
			
			list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					if (list.getSelectedIndex() >= 0) {
						butt.setEnabled(true);
					} else {
						butt.setEnabled(false);
					}
				}
			});
			
			
			butt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String action;
					
					action = list.getSelectedValue();
					
					UserInterfaceIntelligence.useActionButton(label, action);
				}
			});
			
			actionLists.add(listBox);
			
		}
		
		return actionLists;
	}
	
	private Component buildMenue() {
		JPanel menue = new JPanel();
		JButton endTurn = new JButton("End Turn");
		endTurn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				endTurnClicked = true;
			}
		});
		
		menue.setLayout(new BoxLayout(menue, BoxLayout.PAGE_AXIS));
		
		menue.add(buildActionLists());
		
		menue.add(endTurn);
		
		return menue;
	}
	
	private Component buildLog() {
		JTabbedPane pane = new JTabbedPane();
		JPanel logPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		logText.setRows(8);
		
		JScrollPane logField = new JScrollPane(logText,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				logText.setText("");
			}
		});
		
		buttonPanel.add(clear);
		
		logPanel.setLayout(new BorderLayout());
		logPanel.add(logField, BorderLayout.CENTER);
		logPanel.add(buttonPanel, BorderLayout.PAGE_END);
		
		pane.add("Log", logPanel);
		
		return pane;
	}
	
	public void addAction(String type, String action) {
		DefaultListModel<String> list = null;
		
		switch (type) {
		case "Standard":
			list = actionListStandard;
			break;
		case "Move":
			list = actionListMove;
			break;
		case "Minor":
			list = actionListMinor;
			break;
		default:
			list = actionListMinor;
		}
		
		list.addElement(action);
		
	}
	
	public void clearActionLists() {
		actionListStandard.clear();
		actionListMove.clear();
		actionListMinor.clear();
	}
	
}
