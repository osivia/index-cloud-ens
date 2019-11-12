$JQry(function () {
    var $upload = $JQry(".file-browser-upload");

    if (!$upload.data("loaded")) {
        // File upload
        $upload.fileupload({
            dropZone: ".file-browser-drop-zone",
            singleFileUploads: true,

            add: function (event, data) {
                var $target = $JQry(event.target);
                var $files = $target.find(".file-browser-upload-files");
                var $browser = $target.closest(".file-browser");
                var maxFileSize = $target.data("max-file-size");
                var messageWarningReplace = $target.data("warning-replace");
                var messageWarningReplacePronote = $target.data("warning-replace-pronote");
                var messageErrorSize = $target.data("error-size");

                $target.removeClass("d-none");

                data.context = $JQry(document.createElement("li"));
                data.context.appendTo($files);

                $JQry.each(data.files, function (index, file) {
                    var $file = $JQry(document.createElement("p"));
                    $file.appendTo(data.context);

                    // Text
                    var $text = $JQry(document.createElement("span"));
                    $text.appendTo($file);

                    // File name
                    var $fileName = $JQry(document.createElement("span"));
                    $fileName.text(file.name);
                    $fileName.appendTo($text);

                    if (file.size > maxFileSize) {
                        // Error
                        var $error = $JQry(document.createElement("strong"));
                        $error.addClass("text-danger");
                        $error.text(messageErrorSize);
                        $error.appendTo($text);
                    } else {
                        var $row = $browser.find("[data-text=\"" + file.name + "\"]");

                        if ($row.length) {
                            // Warning
                            var $warning = $JQry(document.createElement("span"));
                            if ($row.data("pronote")) {
                                $warning.addClass("badge badge-danger");
                                $warning.text(messageWarningReplacePronote);
                            } else {
                                $warning.addClass("badge badge-warning");
                                $warning.text(messageWarningReplace);
                            }
                            $warning.appendTo($text);
                        }

                        if (!index) {
                            // Start
                            var $start = $JQry(document.createElement("button"));
                            $start.attr("type", "button");
                            $start.addClass("start d-none");
                            $start.click(function () {
                                data.submit(event);
                            });
                            $start.appendTo($file);
                        }
                    }

                    if (!index) {
                        // Cancel
                        var $cancel = $JQry(document.createElement("button"));
                        $cancel.attr("type", "button");
                        $cancel.addClass("cancel btn btn-link btn-sm");
                        $cancel.append($JQry(document.createElement("i")).addClass("glyphicons glyphicons-remove"));
                        $cancel.click(function (event) {
                            data.abort();
                        })
                        $cancel.appendTo($file);
                    }
                });
            },

            stop: function (event, data) {
                var $target = $JQry(event.target);
                var url = $target.data("callback-url");

                $target.addClass("d-none");

                // Refresh
                updatePortletContent(this, url);
            },

            progressall: function (event, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10) + "%";
                $JQry(".file-browser .progress-bar").css("width", progress);
            }
        });

        $upload.find(".fileupload-buttonbar button[type=reset]").click(function (event) {
            var $target = $JQry(event.target);
            var $form = $target.closest(".file-browser-upload");

            $form.addClass("d-none");
        });


        // Drag over
        $JQry(document).bind("dragover", function (e) {
            e.preventDefault();

            var $target = $JQry(e.target);
            var $hoveredDropZone = $target.closest(".file-browser-drop-zone");
            var $dropZone = $JQry(".file-browser-drop-zone");
            var timeout = window.dropZoneTimeout;

            if (!timeout) {
                $dropZone.addClass("in");
            } else {
                clearTimeout(timeout);
            }

            if ($hoveredDropZone.length) {
                $hoveredDropZone.find(".file-browser-upload-shadowbox").addClass("bg-info");
            } else {
                $dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info");
            }

            window.dropZoneTimeout = setTimeout(function () {
                window.dropZoneTimeout = null;
                $dropZone.removeClass("in");
                $dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info");
            }, 1000);
        });


        // Drop
        $JQry(document).bind("drop", function (e) {
            var $dropZone = $JQry(".file-browser-drop-zone");

            $dropZone.removeClass("in");
            $dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info");
        });


        $upload.data("loaded", true);
    }
});
