function drive( id) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {

			var text = this.responseText;
			var list = '';
			var detail = '';			
			
			var jsonData = JSON.parse(text);
			
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
		}
	};
	
	var url = "https://cloud-ens-ws.osivia.org/index-cloud-portal-ens-ws/rest/Drive.content";
	if (typeof id !== 'undefined')	{
		url = url + "?id=" + id;
	}
	
	xmlhttp.open("GET",url,true);
	
	xmlhttp.send();

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
	            headers: {'Content-Type': undefined},
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
	
});



