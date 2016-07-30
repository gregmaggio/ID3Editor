/**
 * 
 */
package ca.datamagic.id3.dto;

import java.util.List;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * @author Greg
 *
 */
public class ID3TagDTO {
	public static final String[] ID3VERSIONS = { "v1.0", "v1.1", "v2.2", "v2.3", "v2.4" };
	private String _title = "";
	private String _artist = "";
	private String _genre = "";
	private String _album = "";
	private String _musicBrainzArtistId = "";
	private String _musicBrainzDiscId = "";
	private String _musicBrainzTrackId = "";
	private String _musicBrainzReleaseId = "";
	private String _id3Version = "";
			
	public ID3TagDTO() {
	}

	public ID3TagDTO(Tag tag) {
		_title = getStringValue(tag, FieldKey.TITLE);
		_artist = getStringValue(tag, FieldKey.ARTIST);
		_genre = getStringValue(tag, FieldKey.GENRE);
		_album = getStringValue(tag, FieldKey.ALBUM);
		_musicBrainzArtistId = getStringValue(tag, FieldKey.MUSICBRAINZ_ARTISTID);
		_musicBrainzDiscId = getStringValue(tag, FieldKey.MUSICBRAINZ_DISC_ID);
		_musicBrainzTrackId = getStringValue(tag, FieldKey.MUSICBRAINZ_TRACK_ID);
		_musicBrainzReleaseId = getStringValue(tag, FieldKey.MUSICBRAINZ_RELEASEID);
		if (tag instanceof ID3v11Tag) {
			_id3Version = "v1.1";
		} else if (tag instanceof ID3v1Tag) {
			_id3Version = "v1.0";
		} else if (tag instanceof ID3v22Tag) {
			_id3Version = "v2.2";
		} else if (tag instanceof ID3v23Tag) {
			_id3Version = "v2.3";
		} else if (tag instanceof ID3v24Tag) {
			_id3Version = "v2.4";
		}
	}
	
	private static String getStringValue(Tag tag, FieldKey key) {
		if ((tag != null) && (key != null)) {
			List<TagField> tagFields = tag.getFields(key);
			if ((tagFields != null) && (tagFields.size() > 0)) {
				String value = tag.getValue(key, 0);
				if (value == null) {
					value = "";
				}
				return value;
			}
		}
		return "";
	}
	
	public String getTitle() {
		return _title;
	}
	
	public String getArtist() {
		return _artist;
	}
	
	public String getGenre() {
		return _genre;
	}
	
	public String getAlbum() {
		return _album;
	}
	
	public String getMusicBrainzArtistId() {
		return _musicBrainzArtistId;
	}
	
	public String getMusicBrainzDiscId() {
		return _musicBrainzDiscId;
	}
	
	public String getMusicBrainzTrackId() {
		return _musicBrainzTrackId;
	}
	
	public String getMusicBrainzReleaseId() {
		return _musicBrainzReleaseId;
	}
	
	public String getID3Version() {
		return _id3Version;
	}
	
	public void setTitle(String newVal) {
		_title = newVal;
	}
	
	public void setArtist(String newVal) {
		_artist = newVal;
	}
	
	public void setGenre(String newVal) {
		_genre = newVal;
	}
	
	public void setAlbum(String newVal) {
		_album = newVal;
	}
	
	public void setMusicBrainzArtistId(String newVal) {
		_musicBrainzArtistId = newVal;
	}
	
	public void setMusicBrainzDiscId(String newVal) {
		_musicBrainzDiscId = newVal;
	}
	
	public void setMusicBrainzTrackId(String newVal) {
		_musicBrainzTrackId = newVal;
	}
	
	public void setMusicBrainzReleaseId(String newVal) {
		_musicBrainzReleaseId = newVal;
	}
	
	public void setID3Version(String newVal) {
		_id3Version = newVal;
	}
	
	public ID3v11Tag toID3v11Tag() throws KeyNotFoundException, FieldDataInvalidException {
		ID3v11Tag newTag = new ID3v11Tag();
		if (_title != null) {
			newTag.addField(FieldKey.TITLE, _title);
		}
		if (_artist != null) {
			newTag.addField(FieldKey.ARTIST, _artist);
		}
		if (_genre != null) {
			newTag.addField(FieldKey.GENRE, _genre);
		}
		if (_album != null) {
			newTag.addField(FieldKey.ALBUM, _album);
		}
		if (_musicBrainzArtistId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_ARTISTID, _musicBrainzArtistId);
		}
		if (_musicBrainzDiscId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_DISC_ID, _musicBrainzDiscId);
		}
		if (_musicBrainzTrackId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_TRACK_ID, _musicBrainzTrackId);
		}
		if (_musicBrainzReleaseId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_RELEASEID, _musicBrainzReleaseId);
		}
		return newTag;
	}
	
	public ID3v1Tag toID3v1Tag() throws KeyNotFoundException, FieldDataInvalidException {
		ID3v1Tag newTag = new ID3v1Tag();
		if (_title != null) {
			newTag.addField(FieldKey.TITLE, _title);
		}
		if (_artist != null) {
			newTag.addField(FieldKey.ARTIST, _artist);
		}
		if (_genre != null) {
			newTag.addField(FieldKey.GENRE, _genre);
		}
		if (_album != null) {
			newTag.addField(FieldKey.ALBUM, _album);
		}
		if (_musicBrainzArtistId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_ARTISTID, _musicBrainzArtistId);
		}
		if (_musicBrainzDiscId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_DISC_ID, _musicBrainzDiscId);
		}
		if (_musicBrainzTrackId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_TRACK_ID, _musicBrainzTrackId);
		}
		if (_musicBrainzReleaseId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_RELEASEID, _musicBrainzReleaseId);
		}
		return newTag;
	}
	
	public ID3v22Tag toID3v22Tag() throws KeyNotFoundException, FieldDataInvalidException {
		ID3v22Tag newTag = new ID3v22Tag();
		if (_title != null) {
			newTag.addField(FieldKey.TITLE, _title);
		}
		if (_artist != null) {
			newTag.addField(FieldKey.ARTIST, _artist);
		}
		if (_genre != null) {
			newTag.addField(FieldKey.GENRE, _genre);
		}
		if (_album != null) {
			newTag.addField(FieldKey.ALBUM, _album);
		}
		if (_musicBrainzArtistId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_ARTISTID, _musicBrainzArtistId);
		}
		if (_musicBrainzDiscId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_DISC_ID, _musicBrainzDiscId);
		}
		if (_musicBrainzTrackId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_TRACK_ID, _musicBrainzTrackId);
		}
		if (_musicBrainzReleaseId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_RELEASEID, _musicBrainzReleaseId);
		}
		return newTag;
	}
	
	public ID3v23Tag toID3v23Tag() throws KeyNotFoundException, FieldDataInvalidException {
		ID3v23Tag newTag = new ID3v23Tag();
		if (_title != null) {
			newTag.addField(FieldKey.TITLE, _title);
		}
		if (_artist != null) {
			newTag.addField(FieldKey.ARTIST, _artist);
		}
		if (_genre != null) {
			newTag.addField(FieldKey.GENRE, _genre);
		}
		if (_album != null) {
			newTag.addField(FieldKey.ALBUM, _album);
		}
		if (_musicBrainzArtistId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_ARTISTID, _musicBrainzArtistId);
		}
		if (_musicBrainzDiscId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_DISC_ID, _musicBrainzDiscId);
		}
		if (_musicBrainzTrackId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_TRACK_ID, _musicBrainzTrackId);
		}
		if (_musicBrainzReleaseId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_RELEASEID, _musicBrainzReleaseId);
		}
		return newTag;
	}
	
	public ID3v24Tag toID3v24Tag() throws KeyNotFoundException, FieldDataInvalidException {
		ID3v24Tag newTag = new ID3v24Tag();
		if (_title != null) {
			newTag.addField(FieldKey.TITLE, _title);
		}
		if (_artist != null) {
			newTag.addField(FieldKey.ARTIST, _artist);
		}
		if (_genre != null) {
			newTag.addField(FieldKey.GENRE, _genre);
		}
		if (_album != null) {
			newTag.addField(FieldKey.ALBUM, _album);
		}
		if (_musicBrainzArtistId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_ARTISTID, _musicBrainzArtistId);
		}
		if (_musicBrainzDiscId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_DISC_ID, _musicBrainzDiscId);
		}
		if (_musicBrainzTrackId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_TRACK_ID, _musicBrainzTrackId);
		}
		if (_musicBrainzReleaseId != null) {
			newTag.addField(FieldKey.MUSICBRAINZ_RELEASEID, _musicBrainzReleaseId);
		}
		return newTag;
	}
	
	public Tag toTag() throws KeyNotFoundException, FieldDataInvalidException {
		String id3Version = getID3Version();
		if (id3Version != null) {
			if (id3Version.compareToIgnoreCase("v1.1") == 0) {
				return toID3v11Tag();
			} else if (id3Version.compareToIgnoreCase("v1.0") == 0) {
				return toID3v1Tag();
			} else if (id3Version.compareToIgnoreCase("v2.2") == 0) {
				return toID3v22Tag();
			} else if (id3Version.compareToIgnoreCase("v2.3") == 0) {
				return toID3v23Tag();
			} else if (id3Version.compareToIgnoreCase("v2.4") == 0) {
				return toID3v24Tag();
			}
		}
		return null;
	}
}
