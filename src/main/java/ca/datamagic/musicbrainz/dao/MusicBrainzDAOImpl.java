/**
 * 
 */
package ca.datamagic.musicbrainz.dao;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.musicbrainz.dto.ReleaseListDTO;

/**
 * @author Greg
 *
 */
public class MusicBrainzDAOImpl implements MusicBrainzDAO {
	private static Logger _logger = LoggerFactory.getLogger(MusicBrainzDAOImpl.class);
	
	@Override
	public ReleaseListDTO query(String queryText) throws Exception {
		String musicBrainz = "http://musicbrainz.org/ws/2/release/";
		String url = MessageFormat.format("{0}?query={1}&fmt=json", musicBrainz, URLEncoder.encode(queryText, "UTF8"));
		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() != 200)
				throw new Exception("Error - Status Code: " + statusLine.getStatusCode());
			HttpEntity responseEntity = response.getEntity();
			InputStream responseStream = responseEntity.getContent();
			StringBuffer responseBuffer = new StringBuffer();
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = responseStream.read(buffer, 0, buffer.length)) > 0) {
				responseBuffer.append(new String(buffer, 0, bytesRead));
			}
			String responseText = responseBuffer.toString();
			_logger.debug(responseText);
			if (responseText.length() < 1)
				throw new Exception("No content returned.");
			Gson gson = new Gson();
			return gson.fromJson(responseText, ReleaseListDTO.class);
		} finally {
			get.releaseConnection();
		}
	}
}
