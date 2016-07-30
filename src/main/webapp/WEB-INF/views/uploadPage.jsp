<html>
	<head>
		<title>Upload</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="css/bootstrap.css">
		<link type="text/css" rel="stylesheet" href="css/style.css">
		<link type="text/css" rel="stylesheet" href="css/jquery.fileupload-ui.css" />
		<script type="text/javascript" src="jquery-1.10.2.js"></script>
		<script type="text/javascript" src="js/vendor/jquery.ui.widget.js"></script>
		<script type="text/javascript" src="js/jquery.iframe-transport.js"></script>
		<script type="text/javascript" src="js/jquery.fileupload.js"></script>
		<script type="text/javascript" src="js/json2.js"></script>
		<script type="text/javascript">
			var UploadPage = {
				initialize: function() {
					$('#mp3Upload').fileupload({
						//url: 'upload?sessionId=' + UploadPage._sessionId,
						url: 'upload',
						dataType: 'json',
						start: function(e) {
							$('body').addClass("loading");
						},
						done: function (e, data) {
							$('body').removeClass("loading");
							window.location = "/ID3Editor";
						},
						progressall: function (e, data) {
							var progress = parseInt(data.loaded / data.total * 100, 10);
							$('#progress .bar').css('width',progress + '%');
						}
					});
					$('#zipUpload').fileupload({
						//url: 'uploadZip?sessionId=' + UploadPage._sessionId,
						url: 'uploadZip',
						dataType: 'json',
						start: function(e) {
							$('body').addClass("loading");
						},
						done: function (e, data) {
							$('body').removeClass("loading");
							window.location = "/ID3Editor";
						},
						progressall: function (e, data) {
							var progress = parseInt(data.loaded / data.total * 100, 10);
							$('#progress .bar').css('width',progress + '%');
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
					<td>Upload an MP3:</td>
					<td style="width:5px;></td>
					<td>
						<div class="container">
							<span class="btn btn-success fileinput-button">
								<i class="icon-plus icon-white"></i>
								<span>Select file...</span>
								<!-- The file input field used as target for the file upload widget -->
								<input id="mp3Upload" type="file" name="mp3Upload" />
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td>Upload a Zip File:</td>
					<td style="width:5px;></td>
					<td>
						<div class="container">
							<span class="btn btn-success fileinput-button">
								<i class="icon-plus icon-white"></i>
								<span>Select file...</span>
								<!-- The file input field used as target for the file upload widget -->
								<input id="zipUpload" type="file" name="zipUpload" />
							</span>
						</div>
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		(function () {
			$(document).ready(UploadPage.initialize);
		})();
		</script>
		<div class="modal"><!-- Place at bottom of page --></div>
	</body>
</html>
