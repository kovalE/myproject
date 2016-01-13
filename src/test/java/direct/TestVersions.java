package direct;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.Versions;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.w3c.dom.*", "javax.xml.*", "org.xml.*", "javax.*" })
public class TestVersions {

	@Test
	public void version() {
      assertThat(Versions.VERSION, is(not("")));
      // from test-merge
	}
}
