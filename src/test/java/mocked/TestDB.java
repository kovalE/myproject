package mocked;

import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.awt.AWTException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import db.DB;
import gui.MainWindow;
import gui.View;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DB.class, View.class})
@PowerMockIgnore({"org.w3c.dom.*", "javax.xml.*", "org.xml.*", "javax.*"})
public class TestDB {

	private static MainWindow mw;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SwingUtilities.invokeLater(() -> {
			mw = new MainWindow();
			mw.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			mw.setSize(400, 250);
			mw.setLocationByPlatform(true);
		});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mw.dispose();
	}

	@Test
	public void loadFromDB() throws InvocationTargetException, InterruptedException, AWTException {
		mockStatic(DB.class);
		mockStatic(View.class);
		String filename = "1.txt";
		DB.load(filename);
		expectLastCall().anyTimes();
		View.getInstance();
		expectLastCall().anyTimes();
		replayAll();
		SwingUtilities.invokeAndWait(() -> {
			mw.setVisible(true);
			DB.load(filename);
		});
		verifyAll();
	}

}
