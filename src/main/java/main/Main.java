package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import db.DB;
import gui.MainWindow;
import gui.View;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
		//UIManager.setLookAndFeel(Settings.getLAF());
		SwingUtilities.invokeLater(() -> {
			MainWindow mw = new MainWindow();
			View.getInstance().setMW(mw);
			mw.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
			mw.addWindowListener(new WindowAdapter() {
				@Override
				 public void windowClosing(WindowEvent e) {
					try {
						DB.dispose();
						System.out.println("Connection disposed");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			mw.setSize(400, 250);
			mw.setLocationByPlatform(true);
			mw.setVisible(true);
		});
		DB.init();
	}

}
