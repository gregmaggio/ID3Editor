/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class ArtistCreditDTO {
	@SerializedName("artist")
	private ArtistDTO _artist = null;
	
	public ArtistDTO getArtist() {
		return _artist;
	}

	public void setArtist(ArtistDTO newVal) {
		_artist = newVal;
	}
}
