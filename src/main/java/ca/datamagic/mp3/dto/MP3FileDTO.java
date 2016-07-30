/**
 * 
 */
package ca.datamagic.mp3.dto;

import java.io.File;

import ca.datamagic.id3.dto.ID3TagDTO;

/**
 * @author Greg
 *
 */
public class MP3FileDTO {
	private File _file = null;
	private ID3TagDTO _id3Tag = null;
	
	public MP3FileDTO() {
	}
	
	public File getFile() {
		return _file;
	}
	
	public ID3TagDTO getID3Tag() {
		return _id3Tag;
	}
	
	public void setFile(File newVal) {
		_file = newVal;
	}
	
	public void setID3Tag(ID3TagDTO newVal) {
		_id3Tag = newVal;
	}
}
