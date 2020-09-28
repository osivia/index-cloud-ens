
$JQry(function() {

    // Fancytree
    $JQry(".taskbar .fancytree").fancytree({
        activeVisible: true,
        extensions: ["dnd", "glyph"],
        tabbable: false,
        titlesTabbable: true,
        toggleEffect: false,

        dnd: {
            autoExpandMS: 500,
            preventRecursiveMoves: true,
            preventVoidMoves: true,

            draggable: {
                scroll: false
            },

            dragEnter: function(targetNode, data) {
                // Only drop on folders
                if (!targetNode.folder) {
                    return false;
                }

                // Prevent drop on active node
                if (targetNode.data.current) {
                    return false;
                }

                // Target node must accept at least one sub-type
                if (targetNode.data.acceptedtypes == undefined) {
                    return false;
                }


                var $source = $JQry(data.draggable.helper.context),
                    sourceTypes = $source.data("types").split(","),
                    targetAcceptedTypes = targetNode.data.acceptedtypes.split(","),
                    accepted = "over";


                jQuery.each(sourceTypes, function(index, sourceType) {
                    var match = false;

                    jQuery.each(targetAcceptedTypes, function(index, targetType) {
                        if (sourceType === targetType) {
                            match = true;
                            return false;
                        }
                    });

                    if (!match) {
                        accepted = false;
                        return false;
                    }
                });

                return accepted;
            },

            dragOver: function(targetNode, data) {
                $JQry(targetNode.span).addClass("bg-info text-white");
            },

            dragDrop: function(targetNode, data) {
                var $menu = targetNode.tree.$div.closest(".taskbar"),

                    // Source
                    $source = $JQry(data.draggable.helper.context),
                    sourceIds = $source.data("identifiers"),

                    // Target
                    targetId = targetNode.data.id;

                // AJAX parameters
                container = null,
                    options = {
                        requestHeaders : [ "ajax", "true", "bilto" ],
                        method : "post",
                        postBody : "sourceIds=" + sourceIds + "&targetId=" + targetId,
                        onSuccess : function(t) {
                            onAjaxSuccess(t, null);
                        }
                    },
                    url = $menu.data("drop-url"),
                    eventToStop = null,
                    callerId = null;

                directAjaxCall(container, options, url, eventToStop, callerId);
            },

            dragLeave: function(targetNode, data) {
                $JQry(targetNode.span).removeClass("bg-info text-white");
            }
        },

        glyph : {
            map : {
                doc : "glyphicons glyphicons-basic-file",
                docOpen: "glyphicons glyphicons-basic-file",
                checkbox: "glyphicons glyphicons-basic-square-empty",
                checkboxSelected: "glyphicons glyphicons-basic-square-selected",
                checkboxUnknown: "glyphicons glyphicons-basic-square-indeterminate",
                error: "glyphicons glyphicons-basic-square-alert",
                expanderClosed: "glyphicons glyphicons-basic-set-down",
                expanderLazy: "glyphicons glyphicons-basic-set-down",
                expanderOpen: "glyphicons glyphicons-basic-set-down",
                folder: "glyphicons glyphicons-basic-folder",
                folderOpen: "glyphicons glyphicons-basic-folder-open",
                loading: "glyphicons glyphicons-basic-hourglass text-info"
            }
        },

        activate : function(event, data) {
            var node = data.node;
            if (node.data.href) {
                if (node.data.target) {
                    window.open(node.data.href, node.data.target);
                } else {
                    window.location.href = node.data.href;
                }
            }
        },

        lazyLoad : function(event, data) {
            var node = data.node;

            // Lazy loading URL
            var $root = node.tree.$div.closest(".taskbar");
            var url = $root.data("lazy-loading-url");

            data.result = {
                url : url,
                data : {
                    "path" : node.data.path
                },
                cache : false
            };
        }
    });

});
