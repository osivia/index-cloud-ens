$JQry(function() {

    $JQry("select.select2.select2-inline-edition").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            var url = $element.data("url");
            var options = {
                closeOnSelect : false,
                containerCssClass : "select2-inline-edition-container",
                dropdownCssClass : "select2-inline-edition-dropdown",
                theme : "bootstrap4",
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


    $JQry(".inline-edition textarea, .inline-edition input").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            var timer;

            $element.change(function(event) {
                // Clear timer
                clearTimeout(timer);

                var $target = $JQry(event.target);
                var $form = $target.closest("form");
                var $submit = $form.find("button[type=submit], input[type=submit]");

                $submit.click();
            });

            $element.keyup(function(event) {
                // Clear timer
                clearTimeout(timer);

                timer = setTimeout(function() {
                    var $target = $JQry(event.target);
                    var $form = $target.closest("form");
                    var $submit = $form.find("button[type=submit], input[type=submit]");

                    $submit.click();
                }, 500);
            });

            $element.data("loaded", true);
        }
    });


    $JQry("[contenteditable=true]").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            var timer;

            $element.keyup(function(event) {
                // Clear timer
                clearTimeout(timer);

                timer = setTimeout(function() {
                    var $target = $JQry(event.target);
                    var $form = $target.closest("form");
                    var $input = $form.find("input[type=hidden][name='inline-values']");
                    var $submit = $form.find("input[type=submit]");

                    $input.val($target.get(0).innerText);
                    $submit.click();
                }, 500);
            });


            $element.data("loaded", true);
        }
    });

});
