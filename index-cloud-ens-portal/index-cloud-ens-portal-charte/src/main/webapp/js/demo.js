$JQry(function() {

	$JQry("select.select2.select2-inline-edition").each(function(index, element) {
		var $element = $JQry(element);

		if (!$element.data("loaded")) {
			var url = $element.data("url");
			var options = {
				closeOnSelect : false,
				containerCssClass : "select2-inline-edition-container",
				dropdownCssClass : "select2-inline-edition-dropdown",
				theme : "bootstrap",
				width : "100%"
			};
			
			if (url !== undefined) {
	            options["ajax"] = {
	                url : url,
	                dataType : "json",
	                delay : 250,
	                data : function(params) {
	                    return {
	                        filter : params.term,
	                    };
	                },
	                processResults : function(data, params) {
	                    return {
	                        results : data
	                    };
	                },
	                cache : true
	            };

	            options["escapeMarkup"] = function(markup) {
	                return markup;
	            };

	            options["templateResult"] = function(params) {
	                var $result = $JQry(document.createElement("span"));

	                if (params.loading) {
	                    $result.text(params.text);
	                } else {
	                    $result.text(params.text);
	                    if (params.level !== undefined) {
	                        $result.addClass("level-" + params.level);
	                    }
	                    if (params.optgroup) {
	                        $result.addClass("optgroup");
	                    }
	                }

	                return $result;
	            };

	            options["templateSelection"] = function(params) {
	                return params.text;
	            };
	        }

			$element.select2(options);
			
			
			$element.change(function(event) {
	            var $form = $element.closest("form");
	            var $submit = $form.find("button[type=submit], input[type=submit]");

	            $submit.click();
	        });
			
			
			$element.data("loaded", true);
		}
	});

});
