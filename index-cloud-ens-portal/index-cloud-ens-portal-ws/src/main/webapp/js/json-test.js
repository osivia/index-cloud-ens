
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
 */

var oauth = {

	/**
	 * All constants goes here
	 */
	params : {

			
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
	authenticate : function( username, password) {
		var deferred = jQuery
				.ajax({
					url : this.params.accessUrl,
					method : 'POST',
					dataType : 'text',
					data : {
						scope : 'drive',
						grant_type: 'password',
						username : username,
						password: password
					},
				
					headers : {
						'Accept' : 'application/json, application/x-www-form-urlencoded',
						'Content-Type' : 'application/x-www-form-urlencoded',
						'Authorization' : 'Basic '+ btoa( this.params.clientId + ":" + this.params.clientSecretId)
					},
					complete : function(xhr, data) {
						console.warn(data);
						// called when complete
					}
				});
		return deferred.promise();
	},
	
	
	refresh : function()	{
		var deferred = jQuery
		.ajax({
			url : this.params.accessUrl,
			method : 'POST',
			dataType : 'text',
			data : {
				grant_type: 'refresh_token',
				refresh_token: this.params.refreshToken,
	
			},
		
			headers : {
				'Accept' : 'application/json, application/x-www-form-urlencoded',
				'Content-Type' : 'application/x-www-form-urlencoded',
				'Authorization' : 'Basic '+ btoa( this.params.clientId + ":" + this.params.clientSecretId)
			},
			complete : function(xhr, data) {
				console.warn(data);
				// called when complete
			}
		});
		return deferred.promise();		
	},
	
	
	grant : function( code)	{
		
		console.debug('grant' + code);
		
		var deferred = jQuery
		.ajax({
			url : this.params.accessUrl,
			method : 'POST',
			dataType : 'text',
			data : {
				grant_type: 'authorization_code',
				code: code,
// Comment to enable header authorisation 				
				client_id: this.params.clientId,
				client_secret: this.params.clientSecretId,
// End comment				
				redirect_uri: oauth.params.clientUrl					
			},
		
			headers : {
				'Accept' : 'application/json, application/x-www-form-urlencoded',
				'Content-Type' : 'application/x-www-form-urlencoded',
// Uncomment to enable header authorisation 
//				'Authorization' : 'Basic '+ btoa( this.params.clientId + ":" + this.params.clientSecretId)
			},
			complete : function(xhr, data) {
				console.warn(data);
				// called when complete
			}
		});
		return deferred.promise();		
	}
	
}


function refreshToken()	{
	/* get the authorization token[append to #auth_token] */
	var auth = oauth.refresh();

	auth.done(function(data) {
		console.warn(data);

		data = JSON.parse(data);
		var token = data.access_token;
		var refreshToken = data.refresh_token;

		console.log("Token: " + token);
		oauth.params.token = token;
		oauth.params.refreshToken = refreshToken;
	});

	auth.fail(function() {
		console.error("Error in client Id or client secret Id");
	});
}


function grant( code)	{
	
	var auth = oauth.grant( code);

	auth.done(function(data) {
		console.log(data);

		data = JSON.parse(data);
		var token = data.access_token;
		var refreshToken = data.refresh_token;

		console.log("Token: " + token);
		oauth.params.token = token;
		oauth.params.refreshToken = refreshToken;
	});

	auth.fail(function() {
		console.error("Bad dredentials");
	});
}



function drive(id) {
	var url = oauth.params.resourceUrl+"/Drive.content";

	if (typeof id !== 'undefined') {
		url = url + "?id=" + id;
	}

	$JQry
			.ajax({
				type : "GET",
				url : url,
				headers : {
					'Content-Type' : undefined,
					"Authorization" : "Bearer " + oauth.getToken()
				},
				contentType : false,
				cache : false,
				timeout : 600000,
				success : function(jsonData) {
					if (jsonData.returnCode != 0)
						$JQry.notify("Error #"+jsonData.returnCode, "error");
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
				error : function(xhr, status, e) {
					alert(e);
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
									params.properties.subject = $JQry(
									'#uploadMDSubject').val();
									data.append("uploadInfos", JSON
											.stringify(params));

									$JQry
											.ajax({
												type : "POST",
												url : oauth.params.resourceUrl+"/Drive.upload",
												headers : {
													'Content-Type' : undefined,
													"Authorization" : "Bearer " + oauth.getToken()
												},
												data : data,
												processData : false,
												contentType : false,
												cache : false,
												timeout : 600000,
												
												success : function(jsonData) {
													if (jsonData.returnCode != 0)
														$JQry.notify("Error #"+jsonData.returnCode, "error");
													else {
														$JQry.notify("Contenu créé", "success");
														drive($JQry('#folderId').val());														
													}

												},
												error : function(e) {
													$JQry.notify("HTTP Error #"+e.status, "error");
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
									params.contentId = $JQry('#contentId').val();
									params.format = $JQry('#pubFormat').val();									
									params.pubId = $JQry('#pubId').val();
									params.pubTitle = $JQry('#pubTitle').val();
									params.pubOrganization = $JQry('#pubOrganization').val();									
									params.properties = {};
									params.properties.level = $JQry('#pubLevel').val();
									params.properties.subject = $JQry('#pubSubject').val();
									$JQry
											.ajax({
												type : "POST",
												url : oauth.params.resourceUrl+"/Drive.publish",
												headers : {
													"Authorization" : "Bearer " + oauth.getToken()
												},
												dataType : 'json',
												contentType : 'application/json',
												data : JSON.stringify(params),
												
												success : function(jsonData) {
													if (jsonData.returnCode != 0)
														$JQry.notify("Error #"+jsonData.returnCode, "error");
													else	{
														$JQry.notify("Contenu publié", "success");
														$JQry('#pubShare').val(jsonData.shareId);
													}

												},
												error : function(e) {
													$JQry.notify("HTTP Error #"+e.status, "error");
												}
												
											});

								});
					});

	$JQry("#btnUnpubSubmit")
	.each(
			function(index, element) {

				var $element = $JQry(element);
				$element
						.click(function() {
							var params = {};
							params.contentId = $JQry('#contentId').val();
							params.pubId = $JQry('#unpubId').val();


							$JQry
									.ajax({
										type : "POST",
										url : oauth.params.resourceUrl+"/Drive.unpublish",
										headers : {
											"Authorization" : "Bearer " + oauth.getToken()
										},
										dataType : 'json',
										contentType : 'application/json',
										data : JSON.stringify(params),
										
										success : function(jsonData) {
											if (jsonData.returnCode != 0)
												$JQry.notify("Error #"+jsonData.returnCode, "error");
											else
												$JQry.notify("Contenu dépublié", "success");

										},
										error : function(e) {
											$JQry.notify("HTTP Error #"+e.status, "error");
										}
										
									});

						});
			});



	$JQry("#OAuth2authenticationCredentials").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {


			var username = $JQry('#authUserId').val();
			var password = $JQry('#authUserPassword').val();
			
			/* get the authorization token[append to #auth_token] */
			var auth = oauth.authenticate(username, password);

			auth.done(function(data) {
				console.warn(data);

				data = JSON.parse(data);
				var token = data.access_token;
				var refreshToken = data.refresh_token;

				console.log("Token: " + token);
				oauth.params.token = token;
				oauth.params.refreshToken = refreshToken;
			});

			auth.fail(function() {
				console.error("Error in client Id or client secret Id");
			});

		});
	});
	

	$JQry("#OAuth2Authorize").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			var newLocation = oauth.params.authorizeUrl + "?client_id="+oauth.params.clientId+"&redirect_uri="+oauth.params.clientUrl+"&response_type=code&scope="+oauth.params.scope+"&state=Q2FyMc";
			window.location.href = newLocation;
		});
	});
	
	
	$JQry("#OAuth2refreshToken").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			refreshToken();


		});
	});

	$JQry("#btnRefreshDrive").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {

			drive();
		});
	});
	
	

	$JQry("#btnPreviewDrive").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {
				var contentId = $JQry('#contentId').val();
				var url = oauth.params.resourceUrl+"/Drive.webUrl";

				url = url + "?id=" + contentId + "&type=viewer";


				$JQry
						.ajax({
							type : "GET",
							url : url,
							headers : {
								'Content-Type' : undefined,
								"Authorization" : "Bearer " + oauth.getToken()
							},
							contentType : false,
							cache : false,
							timeout : 600000,
							success : function(jsonData) {
								if (jsonData.returnCode != 0)
									$JQry.notify("Error #"+jsonData.returnCode, "error");
								else {
									window.open(jsonData.url,"preview","menubar=no, status=no, scrollbars=no, menubar=no, width=1000, height=600");
								
									}
								
							},
							error : function(xhr, status, e) {
								alert(e);
							}
						});
	
		});
		
	});

	
	
	$JQry("#btnCreateUser").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {
			console.log("demande création utilisateur ");			


			var params = {};
			params.firstName = $JQry('#userFirstName').val();
			params.lastName = $JQry('#userLastName').val();
			params.mail = $JQry('#userMail').val();


			$JQry
					.ajax({
						type : "POST",
						url : oauth.params.resourceUrl+"/Admin.createUser",
						headers : {
							"Authorization" : "Bearer " + oauth.getToken()
						},
						dataType : 'json',
						contentType : 'application/json',
						data : JSON.stringify(params),
						success : function(jsonData) {
							if (jsonData.returnCode != 0)
								$JQry.notify("Error #"+jsonData.returnCode, "error");
							else
								$JQry.notify("Utilisateur créé", "success");

						},
						error : function(e) {
							$JQry.notify("HTTP Error #"+e.status, "error");
						}
					});

			});
	});
	
	
	$JQry("#btnError").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {
			console.log("génération d'une erreur ");			


			var params = {};

			$JQry
					.ajax({
						type : "POST",
						url : oauth.params.resourceUrl+"/Drive.error",
						headers : {
							"Authorization" : "Bearer " + oauth.getToken()
						},
						dataType : 'json',
						contentType : 'application/json',
						data : JSON.stringify(params),
						success : function(jsonData) {
							if (jsonData.returnCode != 0)
								$JQry.notify("Error #"+jsonData.returnCode, "error");
						},
						error : function(e) {
							$JQry.notify("HTTP Error #"+e.status, "error");
						}
					});

			});
	});
	
	
	$JQry("#btnSignUp").each(function(index, element) {

		var $element = $JQry(element);
		$element.click(function() {
			console.log("signup utilisateur ");			

			$JQry
					.ajax({
						type : "GET",
						url : oauth.params.resourceUrl+"/User.signup",
						headers : {
							"Authorization" : "Bearer " + createSignUpToken()
						},
						dataType : 'json',
						contentType : 'application/json',
						success : function(jsonData) {
							if (jsonData.returnCode != 0)
								$JQry.notify("Error #"+jsonData.returnCode, "error");
							else
								$JQry.notify("Utilisateur créé", "success");

						},
						error : function(e) {
							$JQry.notify("HTTP Error #"+e.status, "error");
						}
					});

			});
	});	
});

function createSignUpToken( )	{
	// Defining our token parts
	var header = {
		"alg" : "HS256",
		"typ" : "JWT"
	};


	var data = {	};
	data.firstName = $JQry('#userFirstName').val();
	data.lastName = $JQry('#userLastName').val();
	data.mail = $JQry('#userMail').val();
	data.iss = "pronote";

	var secret = "??PRONOTESECRET??";

	var stringifiedHeader = CryptoJS.enc.Utf8.parse(JSON.stringify(header));
	var encodedHeader = base64url(stringifiedHeader);

	var stringifiedData = CryptoJS.enc.Utf8.parse(JSON.stringify(data));
	var encodedData = base64url(stringifiedData);

	var signature = encodedHeader + "." + encodedData;
	signature = CryptoJS.HmacSHA256(signature, secret);
	signature = base64url(signature);

	return encodedHeader+"."+ encodedData+"."+signature;
}

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

function init() {
    var code = null,
        tmp = [];
    var items = location.search.substr(1).split("&");
    for (var index = 0; index < items.length; index++) {
        tmp = items[index].split("=");
        if (tmp[0] === 'code') code = decodeURIComponent(tmp[1]);
    }
    
    var host = "https://"+window.location.hostname;
    //var host = https://cloud-ens-ws.osivia.org
    
    oauth.params.accessUrl = host+'/index-cloud-portal-ens-ws/oauth/token';
    oauth.params.authorizeUrl= host+'/index-cloud-portal-ens-ws/oauth/authorize';  
    oauth.params.resourceUrl = host+'/index-cloud-portal-ens-ws/rest';
	oauth.params.clientId =  'pronote1234';
	oauth.params.clientSecretId = 'secret1234';
	oauth.params.scope = 'drive';
	oauth.params.clientUrl = host+'/index-cloud-portal-ens-ws/html/test.html';	
    
   if( code != null)	{
	   grant(code);
   }
}

