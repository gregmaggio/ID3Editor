<html>
	<head>
		<title>Info</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="/ID3Editor/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/ID3Editor/js/json2.js"></script>
		<script type="text/javascript">
			var InfoPage = {
				_id: "${id}",
				_file: null,
				initialize: function() {
					$.ajax({
						url: '/ID3Editor/file/' + InfoPage._id,
						type: 'GET',
						beforeSend: function(xhr) {
							$('body').addClass("loading");
						},
						error: function (xhr,status,error) {
							alert('Error loading file.');
						},
						success: function (result,status,xhr) {
							document.getElementById('fileName').innerHTML = result.fileName;
							if (result.id3Tag) {
								document.getElementById('title').value = result.id3Tag.title;
								document.getElementById('genre').value = result.id3Tag.genre;
								document.getElementById('album').value = result.id3Tag.album;
								document.getElementById('artist').value = result.id3Tag.artist;
								document.getElementById('musicBrainzTrackId').value = result.id3Tag.musicBrainzTrackId;
								document.getElementById('musicBrainzDiscId').value = result.id3Tag.musicBrainzDiscId;
								document.getElementById('musicBrainzArtistId').value = result.id3Tag.musicBrainzArtistId;
								document.getElementById('musicBrainzReleaseId').value = result.id3Tag.musicBrainzReleaseId;
								document.getElementById('id3Version').value = result.id3Tag.id3Version;
							}
							InfoPage._file = result
						},
						complete: function(xhr,status) {
							$('body').removeClass("loading");
						}
					});
				},
				saveButtonClick: function() {
					InfoPage._file.id3Tag = {};
					InfoPage._file.id3Tag.title = document.getElementById('title').value;
					InfoPage._file.id3Tag.genre = document.getElementById('genre').value;
					InfoPage._file.id3Tag.album = document.getElementById('album').value;
					InfoPage._file.id3Tag.artist = document.getElementById('artist').value;
					InfoPage._file.id3Tag.musicBrainzTrackId = document.getElementById('musicBrainzTrackId').value;
					InfoPage._file.id3Tag.musicBrainzDiscId = document.getElementById('musicBrainzDiscId').value;
					InfoPage._file.id3Tag.musicBrainzArtistId = document.getElementById('musicBrainzArtistId').value;
					InfoPage._file.id3Tag.musicBrainzReleaseId = document.getElementById('musicBrainzReleaseId').value;
					InfoPage._file.id3Tag.id3Version = document.getElementById('id3Version').value;
					$.ajax({
						url: '/ID3Editor/file/' + InfoPage._id,
						type: 'PUT',
						contentType: 'application/json; charset=utf-8',
						data: JSON.stringify(InfoPage._file),
						beforeSend: function(xhr) {
							$('body').addClass("loading");
						},
						error: function (xhr,status,error) {
							alert('Error updating file.');
						},
						success: function (result,status,xhr) {
							window.location = '/ID3Editor/';
						},
						complete: function(xhr,status) {
							$('body').removeClass("loading");
						}
					});
				},
				cancelButtonClick: function() {
					window.location = '/ID3Editor/';
				},
				queryButtonClick: function() {
					var query = document.getElementById('query');
					if (!query.value) {
						alert('Enter a search query.');
						query.focus();
						return;
					}
					$.ajax({
						url: '/ID3Editor/query/' + encodeURIComponent(query.value),
						type: 'GET',
						beforeSend: function(xhr) {
							$('body').addClass("loading");
						},
						error: function (xhr,status,error) {
							alert('Error querying music brainz.');
						},
						success: function (result,status,xhr) {
							var releaseList = result.releaseList;
							var releases = releaseList.releases;
							if (releases.length > 0) {
								var release = releases[0];
								var score = release.score;
								var artistCredit = release.artistCredit;
								var nameCredit = artistCredit.nameCredit;
								var artist = nameCredit.artist;
								if (score == 100) {
									document.getElementById('title').value = release.title;
									//document.getElementById('genre').value = result.id3Tag.genre;
									//document.getElementById('album').value = result.id3Tag.album;
									document.getElementById('artist').value = artist.name;
									//document.getElementById('musicBrainzTrackId').value = result.id3Tag.musicBrainzTrackId;
									//document.getElementById('musicBrainzDiscId').value = result.id3Tag.musicBrainzDiscId;
									document.getElementById('musicBrainzArtistId').value = artist.id;
									document.getElementById('musicBrainzReleaseId').value = release.id;
								}
							}
						},
						complete: function(xhr,status) {
							$('body').removeClass("loading");
						}
					});
				}
			};
		</script>
		<style type="text/css">
		/* Start by setting display:none to make this hidden.
		   Then we position it in relation to the viewport window
		   with position:fixed. Width, height, top and left speak
		   speak for themselves. Background we set to 80% white with
		   our animation centered, and no-repeating */
		.modal {
			display:    none;
			position:   fixed;
			z-index:    1000;
			top:        0;
			left:       0;
			height:     100%;
			width:      100%;
			background: rgba( 255, 255, 255, .8 ) 
						url('img/loading.gif') 
						50% 50% 
						no-repeat;
		}

		/* When the body has the loading class, we turn
		   the scrollbar off with overflow:hidden */
		body.loading {
			overflow: hidden;   
		}

		/* Anytime the body has the loading class, our
		   modal element will be visible */
		body.loading .modal {
			display: block;
		}
		</style>
	</head>
	<body>
		<form id="mainForm">
			<table>
				<tr>
					<td style="text-align:right;">Filename:</td>
					<td style="width:5px;"></td>
					<td><span id="fileName"></span></td>
				</tr>
				<tr>
					<td style="text-align:right;">Title:</td>
					<td style="width:5px;"></td>
					<td><input id="title" type="text" size="55" maxlength="255" /></td>
				</tr>
				<tr>
					<td style="text-align:right;">Genre:</td>
					<td style="width:5px;"></td>
					<td><input id="genre" type="text" size="55" maxlength="255" /></td>
				</tr>
				<tr>
					<td style="text-align:right;">Album:</td>
					<td style="width:5px;"></td>
					<td><input id="album" type="text" size="55" maxlength="255" /></td>
				</tr>
				<tr>
					<td style="text-align:right;">Artist:</td>
					<td style="width:5px;"></td>
					<td><input id="artist" type="text" size="55" maxlength="255" /></td>
				</tr>
				<tr>
					<td style="text-align:right;">Music Brainz Track Id:</td>
					<td style="width:5px;"></td>
					<td><input id="musicBrainzTrackId" type="text" size="55" maxlength="255" readonly="readonly"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Music Brainz Disc Id:</td>
					<td style="width:5px;"></td>
					<td><input id="musicBrainzDiscId" type="text" size="55" maxlength="255" readonly="readonly"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Music Brainz Artist Id:</td>
					<td style="width:5px;"></td>
					<td><input id="musicBrainzArtistId" type="text" size="55" maxlength="255" readonly="readonly"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Music Brainz Release Id:</td>
					<td style="width:5px;"></td>
					<td><input id="musicBrainzReleaseId" type="text" size="55" maxlength="255" readonly="readonly"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">ID3 Version:</td>
					<td style="width:5px;"></td>
					<td><select id="id3Version"><option value="v1.0">v1.0</option><option value="v1.1">v1.1</option><option value="v2.2">v2.2</option><option value="v2.3">v2.3</option><option value="v2.4">v2.4</option></select></td>
				</tr>
				<tr>
					<td style="text-align:right;">Query Music Brainz:</td>
					<td style="width:5px;"></td>
					<td><input id="query" type="text" size="55" maxlength="255" />&nbsp;<input id="queryButton" type="button" value="Query" onclick="InfoPage.queryButtonClick();" /></td>
				</tr>
				<tr>
					<td style="text-align:center;" colspan="3">
						<input id="saveButton" type="button" value="Save" onclick="InfoPage.saveButtonClick();" />
						<input id="cancelButton" type="button" value="Cancel" onclick="InfoPage.cancelButtonClick();" />
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		(function () {
			$(document).ready(InfoPage.initialize);
		})();
		</script>
		<div class="modal"><!-- Place at bottom of page --></div>
	</body>
</html>
