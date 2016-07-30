/**
 * 
 */
package ca.datamagic.mp3.dao;

import java.io.File;
import java.text.MessageFormat;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ca.datamagic.mp3.dto.MP3FileDTO;

/**
 * @author Greg
 *
 */
public class MP3FileDAOTester {
	private File _testDataPath = null;
	private MP3FileDAO _dao = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		DOMConfigurator.configure("src/test/resources/META-INF/log4j.cfg.xml");
	}

	@Before
	public void setUp() throws Exception {
		_testDataPath = new File("src/test/resources/data");
		_dao = new MP3FileDAO();
	}

	@After
	public void tearDown() throws Exception {
		_testDataPath = null;
		_dao = null;
	}
	
	@Test
	public void backupRollbackTest() throws Exception {
		String testPath = _testDataPath.getAbsolutePath();
		String testFile = "Track 2 - Lets Get Ridiculous.mp3";
		String testFileName = MessageFormat.format("{0}/{1}", testPath, testFile);
		String backupFileName = MessageFormat.format("{0}/{1}.bak", testPath, testFile);
		MP3FileDTO dto = _dao.loadFile(new File(testFileName));
		_dao.backupFile(dto);
		Assert.assertTrue((new File(testFileName)).exists());
		Assert.assertTrue((new File(backupFileName)).exists());
		Assert.assertTrue(_dao.hasBackupFile(dto));
		_dao.rollbackFile(dto);
		Assert.assertTrue((new File(testFileName)).exists());
		Assert.assertFalse((new File(backupFileName)).exists());
	}
}
