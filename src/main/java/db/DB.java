package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import gui.View;

public class DB {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/notepad";
	private static final String USER = "kovalE";
	private static final String PASS = "qwerty";
	private static Connection conn;
	private static ScheduledThreadPoolExecutor updateExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		}
	});
	private static final int INITIAL_DELAY = 2;
	private static int accessDelay = INITIAL_DELAY;
	private static Task lastTask;
	private static ScheduledFuture<?> lastTaskFuture;

	public static void init() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	public static void store(String filename, String text){

		StoreTask newTask = new StoreTask(filename, text);
		synchronized (DB.class) {
			if (lastTaskFuture != null) {
				lastTaskFuture.cancel(false);
				lastTask.isCanceled = true;
			}
			lastTask = newTask;
			lastTaskFuture = updateExecutor.schedule(newTask, 0, TimeUnit.SECONDS);
		}
	}

	public void waitTimerFinish() throws InterruptedException {
		updateExecutor.shutdown();
		updateExecutor.awaitTermination(5, TimeUnit.MINUTES);
	}

	public static void load(String filename) {
		LoadTask newTask = new LoadTask(filename);
		synchronized (DB.class) {
			if (lastTaskFuture != null) {
				lastTaskFuture.cancel(false);
				lastTask.isCanceled = true;
			}
			lastTask = newTask;
			lastTaskFuture = updateExecutor.schedule(newTask, 0, TimeUnit.SECONDS);
		}
	}

	public static void dispose() throws SQLException {
		conn.close();
	}

	private static abstract class Task implements Runnable {
		public boolean isCanceled;
	}

	private static class StoreTask extends Task {

		private String filename;
		private String text;

		public StoreTask(String filename, String text) {
			this.filename = filename;
			this.text = text;
		}

		private void store(String filename, String text) throws SQLException {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Files VALUES('" + filename + "', '" + text
					+ "') ON DUPLICATE KEY UPDATE text=" + "'" + text + "';");
		}

		@Override
		public void run() {
			System.out.println("Time: " + LocalDateTime.now());
			try {
				if (!conn.isValid(5)) {
					DB.init();
				}
				store(filename, text);
				accessDelay = INITIAL_DELAY;
				SwingUtilities.invokeLater(() -> {
					View.getInstance().getMW();
				});
			} catch (SQLException e) {
				e.printStackTrace();
				StoreTask newTask = new StoreTask(filename, text);
				synchronized (DB.class) {
					if (isCanceled) {
						return;
					}
					lastTask = newTask;
					int newDelay = 2 * accessDelay;
					final int ONE_HOUR = 60 * 60;
					accessDelay = newDelay > ONE_HOUR ? ONE_HOUR : newDelay;
					lastTaskFuture = updateExecutor.schedule(newTask, accessDelay, TimeUnit.SECONDS);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static class LoadTask extends Task {

		private String filename;

		public LoadTask(String filename) {
			this.filename = filename;
		}

		public boolean isCanceled;

		private String load(String filename) throws SQLException {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("SELECT text FROM Files WHERE filename = '" + filename + "';");
			if (!res.next()) {
				return "";
			}
			return res.getString(1);
		}

		@Override
		public void run() {
			System.out.println("Time: " + LocalDateTime.now());
			try {
				if (!conn.isValid(5)) {
					DB.init();
				}
				String text = load(filename);
				accessDelay = INITIAL_DELAY;
				SwingUtilities.invokeLater(() -> {
					View.getInstance().getMW().setText(text);
				});
			} catch (SQLException e) {
				e.printStackTrace();
				LoadTask newTask = new LoadTask(filename);
				synchronized (DB.class) {
					if (isCanceled) {
						return;
					}
					lastTask = newTask;
					int newDelay = 2 * accessDelay;
					final int ONE_HOUR = 60 * 60;
					accessDelay = newDelay > ONE_HOUR ? ONE_HOUR : newDelay;
					lastTaskFuture = updateExecutor.schedule(newTask, accessDelay, TimeUnit.SECONDS);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
