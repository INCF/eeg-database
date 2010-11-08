$(document).ready(function() {

  /**
   * Display confirmation dialog on all elements (not only links) with class="confirm"
   **/
  $(".confirm").click(function() {
    return confirm("Do you really want to continue?")
  });

  /**
   * Enables table sorting for all tables with class="tableSorter"
   **/
  $("table.tableSorter").tablesorter();

/**
 * Enables submit on change for all selects with class="submitOnChange"
 */
  $(".submitOnChange").change(function() {
    this.form.submit();
  });
});

