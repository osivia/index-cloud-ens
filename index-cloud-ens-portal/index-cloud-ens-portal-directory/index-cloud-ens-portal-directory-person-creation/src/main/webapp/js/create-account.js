$JQry(function() {
    var passwordRulesInformationTimer;
    var passwordRulesInformationXhr;

    $JQry(".create-account input[name=newpassword]").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {


            $element.on("input", function(event) {
                // Clear timer
                clearTimeout(passwordRulesInformationTimer);

                passwordRulesInformationTimer = setTimeout(function()	{
                    var $controlPasswrod = $JQry(".password-control");
                    var $placeholder = $controlPasswrod.find("[data-password-control-url]");
                    
                    $placeholder.popover('show');
                	
                }, 500);
            });

            $element.data("loaded", true);
        }
    });


    function updatePasswordRulesInformation() {
        var $controlPasswrod = $JQry(".password-control");
        var $placeholder = $controlPasswrod.find("[data-password-control-url]");
        var $input = $JQry(".create-account input[name=newpassword]");

        // Abort previous AJAX request
        if (passwordRulesInformationXhr && passwordRulesInformationXhr.readyState !== 4) {
            passwordRulesInformationXhr.abort();
        }

        passwordRulesInformationXhr = jQuery.ajax({
            url: $placeholder.data("url"),
            type: "POST",
            async: true,
            cache: false,
            data: {
                password: $input.val()
            },
            dataType: "html",
            success : function(data, status, xhr) {
                $placeholder.data('bs.popover').options.content = data;
            }
        });
    }

});



$JQry(function() {
	var $controlPasswrod = $JQry(".password-control");
	var $input = $JQry(".create-account input[name=newpassword]");

	if (!$controlPasswrod.data("loaded")) {
		// Location popover
		$controlPasswrod.find("[data-password-control-url]").popover({
			content: function () {
				var $this = $JQry(this);
				var result;
				
				jQuery.ajax({
					url: $this.data("password-control-url"),
					async: false,
					cache: true,
					headers: {
						"Cache-Control": "max-age=0, public"
					},
					data: {
						password: $input.val()
					},
					dataType: "html",
					success: function (data, status, xhr) {
						result = data;
					}
				});

				return result;
			},
			html: true,
			placement: function () {
				if( window.innerWidth < 768)
					return "auto";
				else
					return "left";
			},
			trigger: "focus"
		});


		// Loaded indicator
		$controlPasswrod.data("loaded", true);
	}

});








