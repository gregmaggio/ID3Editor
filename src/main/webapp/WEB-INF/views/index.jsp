<html>
	<head>
		<title>ID3 Editor</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="css/demo_page.css" />
		<link type="text/css" rel="stylesheet" href="css/demo_table.css" />
		<script type="text/javascript" src="jquery-1.10.2.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="js/jquery.jeditable.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.10.3.js"></script>
        <script type="text/javascript" src="js/jquery.validate.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.editable.js"></script>
		<script type="text/javascript">
			var ID3Editor = {
				_files: null,
				initialize: function() {
					$.ajax({
						url: 'files',
						type: 'GET',
						beforeSend: function(xhr) {
							$('body').addClass("loading");
						},
						error: function (xhr,status,error) {
							alert('Error loading file.');
						},
						success: function (result,status,xhr) {
							ID3Editor.initializeTableBody(result);
						},
						complete: function(xhr,status) {
							$('body').removeClass("loading");
						}
					});
				},
				initializeTableBody: function(files) {
					ID3Editor._files = files;
					if (ID3Editor._files) {
						for (var ii = 0; ii < ID3Editor._files.length; ii++) {
							var rowNumber = ii + 1;
							var className = ((rowNumber % 2) == 0) ? "odd_gradeA" : "even_gradeC";
							var id = ID3Editor._files[ii].id;
							var fileName = ID3Editor._files[ii].fileName;
							var title = '';
							var artist = '';
							var genre = '';
							var album = '';
							var musicBrainzArtistId = '';
							var musicBrainzDiscId = '';
							var musicBrainzTrackId = '';
							var musicBrainzReleaseId = '';
							var id3Version = '';
							if (ID3Editor._files[ii].id3Tag) {
								title = ID3Editor._files[ii].id3Tag.title;
								artist = ID3Editor._files[ii].id3Tag.artist;
								genre = ID3Editor._files[ii].id3Tag.genre;
								album = ID3Editor._files[ii].id3Tag.album;
								musicBrainzArtistId = ID3Editor._files[ii].id3Tag.musicBrainzArtistId;
								musicBrainzDiscId = ID3Editor._files[ii].id3Tag.musicBrainzDiscId;
								musicBrainzTrackId = ID3Editor._files[ii].id3Tag.musicBrainzTrackId;
								musicBrainzReleaseId = ID3Editor._files[ii].id3Tag.musicBrainzReleaseId;
								id3Version = ID3Editor._files[ii].id3Tag.id3Version;
							}
							var rowContent = "";
							rowContent += '<tr class="' + className + '" id="' + id + '">';
							rowContent += '<td>' + id + '</td>';
							rowContent += '<td>' + fileName + '</td>';
							rowContent += '<td>' + title + '</td>';
							rowContent += '<td>' + artist + '</td>';
							rowContent += '<td>' + genre + '</td>';
							rowContent += '<td>' + album + '</td>';
							rowContent += '<td>' + musicBrainzArtistId + '</td>';
							rowContent += '<td>' + musicBrainzDiscId + '</td>';
							rowContent += '<td>' + musicBrainzTrackId + '</td>';
							rowContent += '<td>' + musicBrainzReleaseId + '</td>';
							rowContent += '<td>' + id3Version + '</td>';
							rowContent += '<td><a href="infoPage?id=' + id + '">Info</a></td>';
							rowContent += '<td><a href="" onclick="ID3Editor.deleteFile(\'' + id + '\')">Delete</a></td>';
							rowContent += '</tr>';
							$("#mp3Table tbody").append(rowContent);
						}
					}
					window.setTimeout(ID3Editor.makeDataTable, 500);
				},
				makeDataTable: function() {
					$('#mp3Table').dataTable().makeEditable({
						aoColumns: [
							{
								"sName": "id",
								"bSearchable": false,
                   				"bSortable": false,
								"bVisible": false
							},
							{
								"sName": "fileName",
								"bSearchable": true,
                   				"bSortable": true,
								"bVisible": true
							},
							{
								"sName": "title",
								"bSearchable": true,
                   				"bSortable": true,
								"bVisible": true
							},
							{
								"sName": "artist",
								"bSearchable": true,
                   				"bSortable": true,
								"bVisible": true
							},
							{
								"sName": "genre",
								"bSearchable": true,
                   				"bSortable": true,
								"bVisible": true
							},
							{
								"sName": "album",
								"bSearchable": true,
                   				"bSortable": true,
								"bVisible": true
							},
							{
								"sName": "musicBrainzArtistId",
								"bSearchable": false,
                   				"bSortable": false,
								"bVisible": true
							},
							{
								"sName": "musicBrainzDiscId",
								"bSearchable": false,
                   				"bSortable": false,
								"bVisible": true
							},
							{
								"sName": "musicBrainzTrackId",
								"bSearchable": false,
                   				"bSortable": false,
								"bVisible": true
							},
							{
								"sName": "musicBrainzReleaseId",
								"bSearchable": false,
                   				"bSortable": false,
								"bVisible": true
							},
							{
								"sName": "id3Version",
                   				"bSearchable": false,
                   				"bSortable": false,
                   				"fnRender": function (oObj) {
                   					return "<select></select>";
								}
							},
							{
								"sName": "info",
                   				"bSearchable": false,
                   				"bSortable": false,
                   				"fnRender": function (oObj) {
                   					return "<a href='infoPage/" + oObj.aData[0] + "'>Info</a>";
								}
							},
							{
								"sName": "delete",
                   				"bSearchable": false,
                   				"bSortable": false,
                   				"fnRender": function (oObj) {
                   					return "<a href='' onclick='IDEEditor.deleteFile(\"' + oObj.aData[0] + '\")'>Delete</a>";
								}
							}
 						],
						sUpdateURL:function(value, settings) {
								return value;
						}
					});
				},
				deleteFile: function(id) {
					alert('deleteFile: ' + id);
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
			<a href="uploadPage">Upload</a>
			<a href="download">Download</a>
			<div id="mp3TableContainer">
				<table cellpadding="0" cellspacing="0" border="0" class="display" id="mp3Table">
					<thead>
						<tr>
							<th>Id</th>
							<th>Filename</th>
							<th>Title</th>
							<th>Artist</th>
							<th>Genre</th>
							<th>Album</th>
							<th>Music Brainz Artist Id</th>
							<th>Music Brainz Disc Id</th>
							<th>Music Brainz Track Id</th>
							<th>Music Brainz Release Id</th>
							<th>ID3 Version</th>
							<th>Info</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</form>
		<script type="text/javascript">
		(function () {
			$(document).ready(ID3Editor.initialize);
		})();
		</script>
		<div class="modal"><!-- Place at bottom of page --></div>
	</body>
</html>
