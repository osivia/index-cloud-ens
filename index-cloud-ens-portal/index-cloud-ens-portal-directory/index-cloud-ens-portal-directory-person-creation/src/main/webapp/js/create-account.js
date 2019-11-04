$JQry(function() {
    var passwordRulesInformationTimer;
    var passwordRulesInformationXhr;

    $JQry(".create-account input[name=newpassword]").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            updatePasswordRulesInformation();

            $element.on("input", function(event) {
                // Clear timer
                clearTimeout(passwordRulesInformationTimer);

                passwordRulesInformationTimer = setTimeout(updatePasswordRulesInformation, 200);
            });

            $element.data("loaded", true);
        }
    });


    function updatePasswordRulesInformation() {
        var $placeholder = $JQry(".create-account [data-password-information-placeholder]");
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
                $placeholder.html(data);
            }
        });
    }

});
