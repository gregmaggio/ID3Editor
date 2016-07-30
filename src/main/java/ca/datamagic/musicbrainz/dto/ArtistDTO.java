/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class ArtistDTO {
	@SerializedName("id")
	private String _id = null;
	@SerializedName("name")
	private String _name = null;
	@SerializedName("sort-name")
	private String _sortName = null;
	@SerializedName("aliases")
	private List<AliasDTO> _aliases = null;
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getSortName() {
		return _sortName;
	}
	
	public List<AliasDTO> getAliases() {
		return _aliases;
	}
	
	public void setId(String newVal) {
		_id = newVal;
	}
	
	public void setName(String newVal) {
		_name = newVal;
	}
	
	public void setSortName(String newVal) {
		_sortName = newVal;
	}
	
	public void setAliases(List<AliasDTO> newVal) {
		_aliases = newVal;
	}
}
