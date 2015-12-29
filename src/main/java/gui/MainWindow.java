package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import db.DB;
import utils.Constants;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		JMenuItem mntmSaveToDb = new JMenuItem("Save to DB");
		mnFile.add(mntmSaveToDb);

		JMenuItem mntmLoadFromDb = new JMenuItem("Load from DB");
		mnFile.add(mntmLoadFromDb);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		mntmSave.addActionListener((ActionEvent e) -> {
			saveFile();
		});
		mntmOpen.addActionListener((ActionEvent e) -> {
			openFile();
		});
		mntmNew.addActionListener((ActionEvent e) -> {
			textArea.setText("");
		});
		mntmAbout.addActionListener((ActionEvent e) -> {
			JOptionPane.showMessageDialog(this,
					"My notepad\nVersion: " + Constants.VERSION + "\nAuthors: Kovalenko A, Abramenko A");
		});
		mntmSaveToDb.addActionListener((ActionEvent e) -> {
			String filename = JOptionPane.showInputDialog(this, "Enter filename");
			DB.store(filename, textArea.getText());
		});
		mntmLoadFromDb.addActionListener((ActionEvent e) -> {
			String filename = JOptionPane.showInputDialog(this, "Enter filename");
			DB.load(filename);
		});
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl O"), "file open");
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl S"), "file save");
		contentPane.getActionMap().put("file open", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		contentPane.getActionMap().put("file save", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public String getText() {
		return textArea.getText();
	}
	
	private void openFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			FileReader fr;
			try {
				fr = new FileReader(f);
				char[] buf = new char[1024];
				StringBuilder sb = new StringBuilder(1024);
				int num;
				while ((num = fr.read(buf)) != -1) {
					sb.append(buf, 0, num);
				}
				textArea.setText(sb.toString());
				fr.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void saveFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			FileWriter fw;
			try {
				fw = new FileWriter(f);
				fw.write(textArea.getText());
				fw.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
