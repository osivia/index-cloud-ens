function base64url(source) {
	// Encode in classical base64
	encodedSource = CryptoJS.enc.Base64.stringify(source);

	// Remove padding equal characters
	encodedSource = encodedSource.replace(/=+$/, '');

	// Replace characters according to base64url specifications
	encodedSource = encodedSource.replace(/\+/g, '-');
	encodedSource = encodedSource.replace(/\//g, '_');

	return encodedSource;
}

function base64(source) {
	// Encode in classical base64
	encoded = CryptoJS.enc.Base64.stringify(source);

	return encoded;
}

function humanFileSize(bytes, si) {
	var thresh = si ? 1000 : 1024;
	if (Math.abs(bytes) < thresh) {
		return bytes + ' B';
	}
	var units = si ? [ 'kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ] : [
			'KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB', 'ZiB', 'YiB' ];
	var u = -1;
	do {
		bytes /= thresh;
		++u;
	} while (Math.abs(bytes) >= thresh && u < units.length - 1);
	return bytes.toFixed(1) + ' ' + units[u];
}

/**
 * responsible for calling the appropriate functon to make the OAuth call using
 * AJAX call
 * 
 * @author Sandeep G
 * @since 20160913
 */

var oauth = {

	/**
	 * All constants goes here
	 */
	params : {
		accessUrl : 'https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/oauth/token'
			
	// access url where you would request for token
	},

	/**
	 * Fetch out the token
	 */
	getToken : function() {
		if (this.params.token) {
			return this.params.token;
		} else {
			return false;
		}
	},

	/**
	 * set the token
	 */
	authenticate : function(clientId, clientSecretId, scope) {
		var deferred = jQuery
				.ajax({
					url : this.params.accessUrl,
					method : 'POST',
					dataType : 'text',
					data : {
						scope : 'trust',
						grant_type: 'password',
						username : 'demo',
						password: 'osivia'
					},
				
					headers : {
						'Accept' : 'application/json, application/x-www-form-urlencoded',
						'Content-Type' : 'application/x-www-form-urlencoded',
						'Authorization' : 'Basic '+ btoa( clientId + ":")
					},
					complete : function(xhr, data) {
						console.warn(data);
						// called when complete
					}
				});
		return deferred.promise();
	}
}

function authToken() {
	// Defining our token parts
	var header = {
		"alg" : "HS256",
		"typ" : "JWT"
	};

	var data = {};
	data.sub = $JQry('#authUserId').val();
	data.iss = "pronote";

	var secret = "??PRONOTESECRET??";

	var stringifiedHeader = CryptoJS.enc.Utf8.parse(JSON.stringify(header));
	var encodedHeader = base64url(stringifiedHeader);

	var stringifiedData = CryptoJS.enc.Utf8.parse(JSON.stringify(data));
	var encodedData = base64url(stringifiedData);

	var signature = encodedHeader + "." + encodedData;
	signature = CryptoJS.HmacSHA256(signature, secret);
	signature = base64url(signature);

	$JQry('#authToken')
			.val(encodedHeader + "." + encodedData + "." + signature);
}

function drive(id) {
	var url = "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.content";
	//var url = "https://cloud-ens.osivia.org/index-cloud-portal-ens-ws/photos/user/message";	
	if (typeof id !== 'undefined') {
		url = url + "?id=" + id;
		// url = url + "?id=XXXXXX" ;
	}

	$JQry
			.ajax({
				type : "GET",
				url : url,
				headers : {
					'Content-Type' : undefined,
//					"Authorization" : "Bearer " + $JQry('#authToken').val()
					"Authorization" : "Bearer " + oauth.getToken()
				},
				contentType : false,
				cache : false,
				timeout : 600000,
				success : function(jsonData) {
					if (jsonData.returnCode != 0)
						alert("erreur" + returnCode);
					else {
						var list = '';
						var detail = '';

						if (jsonData.type == 'file') {
							$JQry('#detail').show();
							$JQry('#contentId').val(jsonData.id);
							$JQry('#pubShare').val(jsonData.shareLink);
						} else {
							$JQry('#detail').hide();

							if (jsonData.type !== 'root') {
								// Breadcrumb
								for (var i = 0; i < jsonData.parents.length; i++) {
									list = list
											+ '<a  href="javascript:drive(\''
											+ jsonData.parents[i].id + '\')" >'
											+ jsonData.parents[i].title
											+ '</a> > ';
								}

								list += jsonData.title + "<br/>";

								// upper level
								var parentId = jsonData.parents[jsonData.parents.length - 1].id;
								list = list + '<a  href="javascript:drive(\''
										+ parentId + '\')" >..</a><br/>';
							}

							if (jsonData.childrens !== undefined) {
								for (var i = 0; i < jsonData.childrens.length; i++) {
									list = list + '<div class="row">';
									var child = jsonData.childrens[i];
									list = list
											+ '<div class="col-lg-5"> <a  href="javascript:drive(\''
											+ child.id + '\')" >' + child.title
											+ "</a></div>";
									list = list + '<div class="col-lg-2">';
									if (child.fileSize !== undefined) {
										list = list
												+ humanFileSize(child.fileSize,
														true);
									}
									list = list + '</div>';
									list = list
											+ '<div class="col-lg-4">'
											+ new Date(child.lastModified)
													.toLocaleString()
											+ "</div>";
									list = list + "</div>";
								}
							}
							$JQry('#folderId').val(jsonData.id);
							$JQry('#files').html(list);
						}
					}
				},
				error : function(e) {
					console.log("ERROR : ", e);
				}
			});

}

$JQry(function() {

	$JQry("#btnUploadSubmit")
			.each(
					function(index, element) {

						var $element = $JQry(element);

						$element
								.click(function() {
									// Get form
									var form = $JQry('#fileUploadForm')[0];

									// Create an FormData object
									var data = new FormData(form);

									var params = {};
									params.parentId = $JQry('#folderId').val();
									params.properties = {};
									params.properties.level = $JQry(
											'#uploadMDLevel').val();

									data.append("uploadInfos", JSON
											.stringify(params));

									$JQry
											.ajax({
												type : "POST",
												url : "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.upload",
												headers : {
													'Content-Type' : undefined,
													"Authorization" : "Bearer "
															+ $JQry(
																	'#authToken')
																	.val()
												},
												data : data,
												processData : false,
												contentType : false,
												cache : false,
												timeout : 600000,
												success : function(data) {
													drive($JQry('#folderId')
															.val());
												},
												error : function(e) {
													console.log("ERROR : ", e);
												}
											});

								});
					});

	$JQry("#btnPubSubmit")
			.each(
					function(index, element) {

						var $element = $JQry(element);
						$element
								.click(function() {
									var params = {};
									params.contentId = $JQry('#contentId')
											.val();
									params.properties = {};
									params.properties.level = $JQry('#pubLevel')
											.val();

									$JQry
											.ajax({
												type : "POST",
												url : "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.publish",
												headers : {
													"Authorization" : "Bearer "
															+ $JQry(
																	'#authToken')
																	.val()
												},
												dataType : 'json',
												contentType : 'application/json',
												data : JSON.stringify(params),
												success : function(data) {
													$JQry('#pubShare')
															.val(data);
												},
												error : function(e) {
													console.log("ERROR : ", e);
												}
											});

								});
					});

	$JQry("#btnAuthTokenSubmit").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			authToken();

		});
	});

	function fetchAuthToken(clientId, clientSecretId) {
		var auth = oauth.authenticate(clientId, clientSecretId);

		auth.done(function(data) {
			console.warn(data);

			/* YOUR WORK STARTS HERE */
			data = JSON.parse(data);
			var token = data.access_token;

			console.warn("Token: " + token);
			oauth.params.token = token;
		});

		auth.fail(function() {
			console.error("Error in client Id or client secret Id");
		});
	}

	$JQry("#OAuth2authentication").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			var clientId =  'my-trusted-client';
			var clientSecretId = '';


			/* get the authorization token[append to #auth_token] */
			fetchAuthToken(clientId, clientSecretId, 'READ');

		});
	});

	$JQry("#btnGotoDrive").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			drive();
		});
	});

});