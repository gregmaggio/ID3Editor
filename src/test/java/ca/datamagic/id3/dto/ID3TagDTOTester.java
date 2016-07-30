/**
 * 
 */
package ca.datamagic.id3.dto;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import org.apache.log4j.xml.DOMConfigurator;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ca.datamagic.id3.dto.ID3TagDTO;

/**
 * @author Greg
 *
 */
public class ID3TagDTOTester {
	private File _testDataPath = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		DOMConfigurator.configure("src/test/resources/META-INF/log4j.cfg.xml");
	}

	@Before
	public void setUp() throws Exception {
		_testDataPath = new File("src/test/resources/data");
	}

	@After
	public void tearDown() throws Exception {
		_testDataPath = null;
	}
	
	@Test
	public void test1() throws Exception {
		String testPath = _testDataPath.getAbsolutePath();
		String testFile = "Track 2 - Lets Get Ridiculous.mp3";
		String tempPath = "C:/Temp/ID3Editor";
		String testFileName = MessageFormat.format("{0}/{1}", testPath, testFile);
		String tempFileName = MessageFormat.format("{0}/{1}", tempPath, testFile);
		try {
			(new File(tempPath)).mkdirs();
			
			Files.copy( 
	                (new File(testFileName)).toPath(), 
	                (new File(tempFileName)).toPath(),
	                StandardCopyOption.REPLACE_EXISTING,
	                StandardCopyOption.COPY_ATTRIBUTES, 
	                LinkOption.NOFOLLOW_LINKS);
			
			AudioFile audioFile1 = AudioFileIO.read(new File(tempFileName));
			ID3TagDTO tag1 = new ID3TagDTO(audioFile1.getTag());
			tag1.setArtist("Redfoo");
			tag1.setGenre("Electronic dance music");
			tag1.setTitle("Let's Get Ridiculous");
			tag1.setMusicBrainzArtistId("92781466-f1a9-4ffc-9143-d65a2c77fd64");
			tag1.setMusicBrainzDiscId("");
			tag1.setMusicBrainzReleaseId("4ef06ee3-f39e-4938-b415-f6bdfab113e5");
			tag1.setMusicBrainzTrackId("");
			audioFile1.setTag(tag1.toID3v23Tag());
			audioFile1.commit();
			
			AudioFile audioFile2 = AudioFileIO.read(new File(tempFileName));
			ID3TagDTO tag2 = new ID3TagDTO(audioFile2.getTag());
			Assert.assertEquals(tag1.getArtist(), tag2.getArtist());
			Assert.assertEquals(tag1.getGenre(), tag2.getGenre());
			Assert.assertEquals(tag1.getTitle(), tag2.getTitle());
			Assert.assertEquals(tag1.getMusicBrainzArtistId(), tag2.getMusicBrainzArtistId());
			Assert.assertEquals(tag1.getMusicBrainzDiscId(), tag2.getMusicBrainzDiscId());
			Assert.assertEquals(tag1.getMusicBrainzReleaseId(), tag2.getMusicBrainzReleaseId());
			Assert.assertEquals(tag1.getMusicBrainzTrackId(), tag2.getMusicBrainzTrackId());
		} finally {
			File tempFile = new File(tempFileName);
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
}
