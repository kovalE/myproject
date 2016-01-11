package mocked;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.awt.AWTException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.easymock.EasyMock;
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
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DB.class, View.class})
@PowerMockIgnore({"org.w3c.dom.*", "javax.xml.*", "org.xml.*", "javax.*"})
public class TestDB {

	private static MainWindow mw;
		private static final String filename = "1.txt";

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
    Files.deleteIfExists(Paths.get(filename));
	}

	@Test
	public void loadFromDB() throws InvocationTargetException, InterruptedException, AWTException {
		mockStatic(DB.class);
		mockStatic(View.class);
		DB.load(filename);
		expectLastCall().anyTimes();
		View view = EasyMock.partialMockBuilder(View.class).addMockedMethod("getMW").createMock();
		expect(View.getInstance()).andReturn(view).anyTimes();
		expect(view.getMW()).andReturn(mw).anyTimes();
		replayAll();
		SwingUtilities.invokeAndWait(() -> {
			mw.setVisible(true);
			DB.load(filename);
		});
		verifyAll();
	}

    @Test
    public void storeToDB() throws InvocationTargetException, InterruptedException, AWTException {
        mockStatic(DB.class);
        mockStatic(View.class);
        String testStr = "asdf";
        DB.store(filename, testStr);
        expectLastCall().anyTimes();
        View view = EasyMock.partialMockBuilder(View.class).addMockedMethod("getMW").createMock();
        expect(View.getInstance()).andReturn(view).anyTimes();
        expect(view.getMW()).andReturn(mw).anyTimes();
        replayAll();
        SwingUtilities.invokeAndWait(() -> {
                mw.setVisible(true);
            });
        Thread.sleep(1000);
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
                mw.saveFile(filename);
            });
 
        Thread.sleep(1000);
        SwingUtilities.invokeAndWait(() -> {

            });

        verifyAll();
    }

}
