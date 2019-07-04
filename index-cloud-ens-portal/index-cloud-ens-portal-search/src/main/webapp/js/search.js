function searchOptionsLoadCallback(formId) {
    var $form = $JQry("#" + formId);
    var $source = $form.find("input[type=search]");

    var $modal = $JQry("#osivia-modal");
    var $target = $modal.find("input[name=keywords]");

    $target.val($source.val());
}
