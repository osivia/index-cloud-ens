function saveSearchFiltersLocation(button) {
	var $button = $JQry(button);
	var $modalForm = $button.closest("form");
	var path = $modalForm.find("input[name$=targetPath]").val();
	var $form = $JQry(".search-filters-container form");

	// Update hidden input
	$form.find("input[type=hidden][name$=locationPath]").val(path);

	// Submit form
	$form.find("input[type=submit][name='update-location']").click();
}


function searchOptionsLoadCallback(formId) {
    var $form = $JQry("#" + formId);
    var $source = $form.find("input[type=search]");

    var $modal = $JQry("#osivia-modal");
    var $target = $modal.find("input[name=keywords]");

    $target.val($source.val());
}


function saveSearch(popoverForm) {
	var $popoverForm = $JQry(popoverForm);
	var displayName = $popoverForm.find("input[type=text]").val();
	var $container = $popoverForm.closest(".search-filters-container");
	var $form = $container.children("form");

	// Update hidden input
	$form.find("input[type=hidden][name$=savedSearchDisplayName]").val(displayName);

	// Submit form
	$form.find("input[type=submit][name='save-search-popover-callback']").click();
}


$JQry(function() {
	$JQry(".search.auto-submit").each(function(index, element) {
		var $element = $JQry(element);

		if (!$element.data("loaded")) {
			var enterKey = 13;
			var timer;

			$element.find("input[name=value]").keyup(function(event) {
				// Clear timer
				clearTimeout(timer);
				
				if (event.which != enterKey) {
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


	$JQry("button[data-save-search-popover]").each(function(index, element) {
		var $element = $JQry(element);

		if (!$element.data("loaded")) {
			$element.popover({
				container: ".search-filters-container",
				content: function() {
					var result;

					jQuery.ajax({
						url: adaptAjaxRedirection($element.data("save-search-popover")),
						async: false,
						dataType: "html",
						success: function(data, status, xhr) {
							result = data.trim();
						}
					});

					return result;
				},
				html: true,
				placement: "bottom",
				sanitize: false
			});

			$element.data("loaded", true);
		}
	});

});

