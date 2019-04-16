$JQry(function() {
	
	$JQry(".dashboard ul.selectable").selectable({
		cancel: "a, button",
		filter: "> li",

		selecting: function(event, ui) {
			var $selecting = $JQry(ui.selecting);
			
			if (!$selecting.hasClass("bg-primary")) {
				$selecting.addClass("bg-info");
			}
		},
		
		selected: function(event, ui) {
			$JQry(ui.selected).addClass("bg-primary").removeClass("bg-info");
		},
		
		unselecting: function(event, ui) {
			$JQry(ui.unselecting).removeClass("bg-primary bg-info");
		},
		
		unselected: function(event, ui) {
			$JQry(ui.unselected).removeClass("bg-primary");
		},
		
		stop: function(event, ui) {
			var $target = $JQry(event.target),
				$selectable = $target.closest("ul.selectable");
			
			$selectable.children().each(function(index, element) {
				var $element = $JQry(element),
					$input = $element.find("input[type=hidden][name$=selected]");
				
				$input.val($element.hasClass("ui-selected"));
			});
			
			// Update toolbar
			updateDashboardToolbar($selectable);
		}
	});
	
	
	$JQry(".dashboard .table-header .contextual-toolbar .unselect").click(function(event) {
		var $target = $JQry(event.target),
			$table = $target.closest(".table"),
			$selectable = $table.find("ul.selectable"),
			$selected = $selectable.children(".ui-selected");
		
		$selected.each(function(index, element) {
			var $element = $JQry(element);
			
			$element.removeClass("ui-selected bg-primary");
		});
		
		updateDashboardToolbar($selectable);
	});

	

	
	
	
	$JQry(".dashboard .modal button[type=submit]").click(function(event) {
		var $target = $JQry(event.target),
			$modal = $target.closest(".modal");
		

		
		$modal.modal("hide");
	});
	
});


/**
 * Update trash toolbar.
 * 
 * @param $selectable selectable UL jQuery object
 */
function updateDashboardToolbar($selectable) {
	var $selected = $selectable.children(".ui-selected"),
		$table = $selectable.closest(".table"),
		$toolbar = $table.find(".table-header .contextual-toolbar"),
		$text = $toolbar.find(".navbar-text");
	
	if ($selected.length) {
		jQuery.ajax({
			url: $text.data("url"),
			async: false,
			cache: true,
			headers: {
				"Cache-Control": "max-age=86400, public"
			},
			data: {
				count: $selected.length
			},
			dataType: "json",
			success : function(data, status, xhr) {
				var message;
				
				if ("success" == status) {
					message = data["message"];
				} else {
					message = "";
				}
				
				$text.text(message);
			}
		});
		
		$toolbar.addClass("in");
	} else {
		$toolbar.removeClass("in");
	}
}
