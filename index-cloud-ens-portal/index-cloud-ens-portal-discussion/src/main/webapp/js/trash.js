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
