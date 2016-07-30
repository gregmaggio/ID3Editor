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
public class ReleaseListDTO {
	@SerializedName("created")
	private String _created = null;
	@SerializedName("offset")
	private Integer _offset = null;
	@SerializedName("count")
	private Integer _count = null;
	@SerializedName("releases")
	private List<ReleaseDTO> _releases = new ArrayList<ReleaseDTO>();
	
	public Integer getOffset() {
		return _offset;
	}
	
	public Integer getCount() {
		return _count;
	}
	
	public List<ReleaseDTO> getReleases() {
		return _releases;
	}
	
	public void setOffset(Integer newVal) {
		_offset = newVal;
	}
	
	public void setCount(Integer newVal) {
		_count = newVal;
	}
	
	public void setReleases(List<ReleaseDTO> newVal) {
		_releases = newVal;
	}
}
