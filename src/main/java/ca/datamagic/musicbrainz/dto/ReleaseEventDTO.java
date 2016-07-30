/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class ReleaseEventDTO {
	@SerializedName("date")
	private String _date = null;
	
	public String getDate() {
		return _date;
	}
	
	public void setDate(String newVal) {
		_date = newVal;
	}
}
