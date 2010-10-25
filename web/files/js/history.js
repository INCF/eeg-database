$(document).ready(function() {
  $("#monthlyHistory tr:not(:lt(20)) td").hide();
  $("a#history.showAll").click(function(){
    $("#monthlyHistory tr td").show();
  });
  $("#monthlyHistory").tablesorter();
});


