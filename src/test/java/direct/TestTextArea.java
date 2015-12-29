package direct;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import gui.MainWindow;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.w3c.dom.*", "javax.xml.*", "org.xml.*", "javax.*" })
public class TestTextArea {

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
	public void testAreaTest() throws InvocationTargetException, InterruptedException, Error {
		String testStr = "test test";
		SwingUtilities.invokeAndWait(() -> {
			mw.setVisible(true);
		});
		try {
			Robot robot;
			robot = new Robot();
			for (int i = 0; i < testStr.length(); i++) {
				int keycode;
				keycode = KeyEvent.getExtendedKeyCodeForChar(testStr.charAt(i));
				robot.keyPress(keycode);
				robot.keyRelease(keycode);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
		SwingUtilities.invokeAndWait(() -> {

		});
		assertThat(mw.getText(), is(testStr));
	}
}
