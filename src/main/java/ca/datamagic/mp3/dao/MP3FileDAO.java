/**
 * 
 */
package ca.datamagic.mp3.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import ca.datamagic.id3.dto.ID3TagDTO;
import ca.datamagic.mp3.dto.MP3FileDTO;

/**
 * @author Greg
 *
 */
public class MP3FileDAO {
	public MP3FileDTO loadFile(File file) throws Exception {
		AudioFile audioFile = AudioFileIO.read(file);
		ID3TagDTO id3Tag = new ID3TagDTO(audioFile.getTag());
		MP3FileDTO mp3File = new MP3FileDTO();
		mp3File.setFile(file);
		mp3File.setID3Tag(id3Tag);
		return mp3File;
	}
	
	public boolean hasBackupFile(MP3FileDTO mp3File) {
		String backupFileName = MessageFormat.format("{0}.bak", mp3File.getFile().getAbsolutePath());
		return (new File(backupFileName)).exists();
	}
	
	public void deleteBackupFile(MP3FileDTO mp3File) {
		String backupFileName = MessageFormat.format("{0}.bak", mp3File.getFile().getAbsolutePath());
		File backupFile = new File(backupFileName);
		if (backupFile.exists()) {
			backupFile.delete();
		}
	}
	
	public void deleteFile(MP3FileDTO mp3File) {
		if (mp3File.getFile().exists()) {
			mp3File.getFile().delete();
		}
	}
	
	public void backupFile(MP3FileDTO mp3File) throws IOException {
		String backupFileName = MessageFormat.format("{0}.bak", mp3File.getFile().getAbsolutePath());
		File backupFile = new File(backupFileName);
		if (backupFile.exists()) {
			backupFile.delete();
		}
		Files.copy(mp3File.getFile().toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void rollbackFile(MP3FileDTO mp3File) throws Exception {
		String backupFileName = MessageFormat.format("{0}.bak", mp3File.getFile().getAbsolutePath());
		File backupFile = new File(backupFileName);
		if (!backupFile.exists()) {
			throw new Exception(MessageFormat.format("Cannot rollback {0}. Backup file does not exist.", mp3File.getFile().getName()));
		}
		Files.move(backupFile.toPath(), mp3File.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void saveFile(MP3FileDTO mp3File, String id3Version) throws Exception {
		backupFile(mp3File);
		AudioFile audioFile = AudioFileIO.read(mp3File.getFile());
		if (audioFile.getTag() != null) {
			if (id3Version.compareToIgnoreCase("v1.0") == 0) {
				audioFile.setTag(mp3File.getID3Tag().toID3v1Tag());
			} else if (id3Version.compareToIgnoreCase("v1.1") == 0) {
				audioFile.setTag(mp3File.getID3Tag().toID3v11Tag());
			} else if (id3Version.compareToIgnoreCase("v2.2") == 0) {
				audioFile.setTag(mp3File.getID3Tag().toID3v22Tag());
			} else if (id3Version.compareToIgnoreCase("v2.3") == 0) {
				audioFile.setTag(mp3File.getID3Tag().toID3v23Tag());
			} else if (id3Version.compareToIgnoreCase("v2.4") == 0) {
				audioFile.setTag(mp3File.getID3Tag().toID3v24Tag());
			}
			AudioFileIO.write(audioFile);
		}
	}
}
