
$JQry(function() {
	var $placeholder = $JQry(".delete-account");
	var $inputDisplayName = $JQry(".delete-account input[name=displayName]");
	var $agreement = $JQry(".delete-account input[name=agreement]");

	$inputDisplayName.change(function() {
		checkInputs();
	})
	
	$agreement.click(function() {
		
		checkInputs();
	})
	
	function checkInputs() {
		var userDisplayName = $placeholder.data("userdisplayname");
		
		
		if(userDisplayName == $inputDisplayName.val() && $agreement.is(":checked")) {
			$JQry("#delete-account-button").removeAttr('disabled')
		}
		else {
			$JQry("#delete-account-button").attr('disabled','disabled')

		}
	}
	
	$JQry("#delete-account-button").click(function() {
		
		console.log($placeholder.data("url"));
		
		xhr = jQuery.ajax({
			url: $placeholder.data("url"),
			type: "POST",
			async: false,
			cache: false,
			dataType: "html",
			success : function(data, status, xhr) {
				//$placeholder.html(data);
			}
		});	
		
		logout();
	
	}
	

	
)});
