/**
 * 
 */
package ca.datamagic.musicbrainz.dao;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.datamagic.musicbrainz.dto.ReleaseListDTO;

/**
 * @author Greg
 *
 */
public class MusicBrainzDAOTester {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure("src/test/resources/META-INF/log4j.cfg.xml");
	}

	@Test
	public void test1() throws Exception {
		MusicBrainzDAOImpl dao = new MusicBrainzDAOImpl();
		ReleaseListDTO releaseList = dao.query("Courage");
		Assert.assertNotNull(releaseList);
		Assert.assertNotNull(releaseList.getReleases());
		Assert.assertTrue(releaseList.getReleases().size() > 0);
	}

}
