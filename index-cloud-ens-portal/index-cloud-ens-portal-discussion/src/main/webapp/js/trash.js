$JQry(function() {
	var $trash = $JQry(".trash");

	if (!$trash.data("loaded")) {
		// Location popover
		$trash.find("a[data-location-path]").popover({
			content: function () {
				var $this = $JQry(this);
				var result;

				jQuery.ajax({
					url: $trash.data("location-url"),
					async: false,
					cache: true,
					headers: {
						"Cache-Control": "max-age=86400, public"
					},
					data: {
						path: $this.data("location-path")
					},
					dataType: "html",
					success: function (data, status, xhr) {
						result = data;
					}
				});

				return result;
			},
			html: true,
			placement: "bottom",
			trigger: "focus"
		});


		// Loaded indicator
		$trash.data("loaded", true);
	}

});



$JQry(function() {
    $JQry(".discussion-messages").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            var $modals = $element.find(".modal");
 
            // Update modal content
            $modals.on("show.bs.modal", function(event) {
                var $button = $JQry(event.relatedTarget);
                var $modal = $JQry(event.currentTarget);

                $modal.find("input[name=messageId]").val($button.data("message-id"));
                $modal.find("input[name=displayName]").val($button.data("display-name"));
            });

            // Submit modal form
            $modals.find("button[data-submit]").click(function(event) {
                var $button = $JQry(event.currentTarget);
                var $modal = $button.closest(".modal");

                // Close modal
                $modal.modal("hide");

                // Submit form
                $modal.find("input[type=submit]").click();
            });

          

            $element.data("loaded", true);
        }
    });
});
