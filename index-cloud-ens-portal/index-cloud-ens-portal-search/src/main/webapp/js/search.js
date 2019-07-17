function searchOptionsLoadCallback(formId) {
    var $form = $JQry("#" + formId);
    var $source = $form.find("input[type=search]");

    var $modal = $JQry("#osivia-modal");
    var $target = $modal.find("input[name=keywords]");

    $target.val($source.val());
}


$JQry(function() {
	$JQry(".search.auto-submit").each(function(index, element) {
		var $element = $JQry(element);
		

		
		if (!$element.data("loaded")) {
			var enterKey = 13,
				timer;
			

			
			$element.find("input[name=value]").keyup(function(event) {
				// Clear timer
				clearTimeout(timer);
				
				if (event.which != enterKey) {
					
					console.log( "search input");
					
					timer = setTimeout(function() {
						var $target = $JQry(event.target),
							$formGroup = $target.closest(".form-group"),
							$submit = $formGroup.find("button[type=submit]");
						
						$submit.click();
					}, 200);
				}
			});
			
			$element.data("loaded", true);
		}
	});
});

