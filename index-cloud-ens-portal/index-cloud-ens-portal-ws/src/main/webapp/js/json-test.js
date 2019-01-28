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

function authToken( )	{
	// Defining our token parts
	var header = {
		"alg" : "HS256",
		"typ" : "JWT"
	};

	var data = {	};
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

	$JQry('#authToken').val( encodedHeader+"."+ encodedData+"."+signature);
}



function drive( id) {
	var url = "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.content";
	if (typeof id !== 'undefined')	{
		url = url + "?id=" + id;
	}
	
	$JQry.ajax({
	    type: "GET",
	    url: url,
	    headers: {'Content-Type': undefined, "Authorization" : "Bearer "+ $JQry('#authToken').val()},
	    contentType: false,
	    cache: false,
	    timeout: 600000,
	    success: function (jsonData) {

			var list = '';
			var detail = '';			
			
			if (jsonData.type == 'file')	{
				$JQry('#detail').show();
				$JQry('#contentId').val(jsonData.id);
				$JQry('#pubShare').val( jsonData.shareLink);
			}	else	{
				$JQry('#detail').hide();	
				
				if (jsonData.type !== 'root')
					list = list + '<a  href="javascript:drive(\''+jsonData.parentId+'\')" >..</a><br/>';

				if( jsonData.childrens !== undefined){
					for (var i = 0; i < jsonData.childrens.length; i++) {
						var file = jsonData.childrens[i];
						list = list + '<a  href="javascript:drive(\''+file.id+'\')" >' + file.title + "</a><br/>";
					}
				}			
		        $JQry('#folderId').val(jsonData.id);
		        $JQry('#files').html( list);				
			}
	    },
	    error: function (e) {
	        console.log("ERROR : ", e);
	    }
	});

}



$JQry(function() {
	
	$JQry("#btnUploadSubmit").each(function(index, element) {
		
		var $element = $JQry(element);
		
		$element.click(function() {
	        // Get form
	        var form = $JQry('#fileUploadForm')[0];

			// Create an FormData object 
	        var data = new FormData(form);

	        var params = { };
	        params.parentId = $JQry('#folderId').val();
	        params.properties = { };
	        params.properties.level = $JQry('#uploadMDLevel').val();
	        
	        data.append("uploadInfos", JSON.stringify(params));
	        
	        $JQry.ajax({
	            type: "POST",
	            url: "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.upload",
	    	    headers: {'Content-Type': undefined, "Authorization" : "Bearer "+ $JQry('#authToken').val()},
	            data: data,
	            processData: false,
	            contentType: false,
	            cache: false,
	            timeout: 600000,
	            success: function (data) {
	                drive( $JQry('#folderId').val());
	            },
	            error: function (e) {
	                console.log("ERROR : ", e);
	            }
	        });

		});
	});

	
	
	$JQry("#btnPubSubmit").each(function(index, element) {
		
		var $element = $JQry(element);
		$element.click(function() {
	        var params = { };
	        params.contentId = $JQry('#contentId').val();
	        params.properties = { };
	        params.properties.level = $JQry('#pubLevel').val();
 	        
	        $JQry.ajax({
	            type: "POST",
	            url: "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.publish",
	    	    headers: {"Authorization" : "Bearer "+ $JQry('#authToken').val()},	            
	            dataType: 'json',
	            contentType: 'application/json',
	            data: JSON.stringify(params),
	            success: function (data) {
	            	$JQry('#pubShare').val( data);
	            },
	            error: function (e) {
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

$JQry("#btnGotoDrive").each(function(index, element) {
	
	var $element = $JQry(element);
	$element.click(function() {
		
		drive();
	});
});
	
});



