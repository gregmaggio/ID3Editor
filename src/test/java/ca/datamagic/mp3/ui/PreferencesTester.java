/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.io.File;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Greg
 *
 */
public class PreferencesTester {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		DOMConfigurator.configure("src/test/resources/META-INF/log4j.cfg.xml");
	}

	@Test
	public final void testLoad() throws Exception {
		Preferences preferences = new Preferences();
		preferences.load();
	}

	@Test
	public final void testSave() throws Exception {
		Preferences preferences1 = new Preferences();
		preferences1.getMusicFolders().clear();
		
		File folder1 = new File("C:/Users/Greg/Documents/Les Mills - BODYPUMP 86");
		File folder2 = new File("C:/Users/Greg/Documents/Les Mills - BODYPUMP 87");
		
		preferences1.getMusicFolders().add(folder1.getAbsolutePath());
		preferences1.getMusicFolders().add(folder2.getAbsolutePath());
		preferences1.save();
		
		Preferences preferences2 = new Preferences();
		preferences2.load();
		Assert.assertEquals(preferences1.getMusicFolders().size(), preferences2.getMusicFolders().size());
	}

}
