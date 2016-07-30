/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class ReleaseGroupDTO {
	@SerializedName("id")
	private String _id = null;
	@SerializedName("type")
	private String _type = null;
	@SerializedName("primary-type")
	private String _primaryType = null;
	
	public String getId() {
		return _id;
	}
	
	public String getType() {
		return _type;
	}
	
	public String getPrimaryType() {
		return _primaryType;
	}
	
	public void setId(String newVal) {
		_id = newVal;
	}
	
	public void setType(String newVal) {
		_type = newVal;
	}
	
	public void setPrimaryType(String newVal) {
		_primaryType = newVal;
	}
}
