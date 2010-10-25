$(document).ready(function() {

  $("a.prompt").click(function() {
    return confirm("Do you really want to continue?")
  });

});

