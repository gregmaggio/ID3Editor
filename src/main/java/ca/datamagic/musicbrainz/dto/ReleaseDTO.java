/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class ReleaseDTO {
	@SerializedName("score")
	private Integer _score = null;
	@SerializedName("id")
	private String _id = null;
	@SerializedName("title")
	private String _title = null;
	@SerializedName("status")
	private String _status = null;
	@SerializedName("packaging")
	private String _packaging = null;
	@SerializedName("text-representation")
	private TextRepresentationDTO _textRepresentation = null;
	@SerializedName("release-group")
	private ReleaseGroupDTO _releaseGroup = null;
	@SerializedName("date")
	private String _date = null;
	@SerializedName("country")
	private String _country = null;
	@SerializedName("release-events")
	private List<ReleaseEventDTO> _releaseEvents = new ArrayList<ReleaseEventDTO>();
	@SerializedName("artist-credit")
	private List<ArtistCreditDTO> _artistCredits = new ArrayList<ArtistCreditDTO>();
	
	public Integer getScore() {
		return _score;
	}
	
	public String getId() {
		return _id;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public String getStatus() {
		return _status;
	}
	
	public String getPackaging() {
		return _packaging;
	}
	
	public TextRepresentationDTO getTextRepresentation() {
		return _textRepresentation;
	}
	
	public ReleaseGroupDTO getReleaseGroup() {
		return _releaseGroup;
	}
	
	public String getDate() {
		return _date;
	}
	
	public String getCountry() {
		return _country;
	}
	
	public List<ReleaseEventDTO> getReleaseEvents() {
		return _releaseEvents;
	}
	
	public List<ArtistCreditDTO> getArtistCredits() {
		return _artistCredits;
	}
	
	public void setScore(Integer newVal) {
		_score = newVal;
	}
	
	public void setId(String newVal) {
		_id = newVal;
	}
	
	public void setTitle(String newVal) {
		_title = newVal;
	}
	
	public void setStatus(String newVal) {
		_status = newVal;
	}
	
	public void setPackaging(String newVal) {
		_packaging = newVal;
	}
	
	public void setTextRepresentation(TextRepresentationDTO newVal) {
		_textRepresentation = newVal;
	}
	
	public void setReleaseGroup(ReleaseGroupDTO newVal) {
		_releaseGroup = newVal;
	}
	
	public void setDate(String newVal) {
		_date = newVal;
	}
	
	public void setCountry(String newVal) {
		_country = newVal;
	}
	
	public void setReleaseEvents(List<ReleaseEventDTO> newVal) {
		_releaseEvents = newVal;
	}
	
	public void setArtistCredits(List<ArtistCreditDTO> newVal) {
		_artistCredits = newVal;
	}
}
