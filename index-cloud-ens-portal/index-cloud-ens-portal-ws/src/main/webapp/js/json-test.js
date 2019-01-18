function drive( id) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {

			var text = this.responseText;
			var html = '';
			
			var jsonData = JSON.parse(text);
			
			if (jsonData.type !== 'root')
				html = html + '<a  href="javascript:drive(\''+jsonData.parentId+'\')" >..</a><br/>';
			
			for (var i = 0; i < jsonData.childrens.length; i++) {
				var file = jsonData.childrens[i];
				html = html + '<a  href="javascript:drive(\''+file.id+'\')" >' + file.title + "</a><br/>";
			}
			
	        $JQry('#folderId').val(jsonData.id);

	        $JQry('#files').html( html);
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
	
	$JQry("#btnSubmit").each(function(index, element) {
		
		var $element = $JQry(element);
		
		$element.click(function() {
	        // Get form
	        var form = $JQry('#fileUploadForm')[0];

			// Create an FormData object 
	        var data = new FormData(form);

			// If you want to add an extra field for the FormData
	        // data.append("CustomField", "This is some extra data, testing");

	        data.append("pronoteQualifiers", "{\"level\": \"3\"}");
	        
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

});



