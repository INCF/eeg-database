$(document).ready(function() {
  $(".changeGroup").click(function() {
    var x = $(this).attr("id");
    $("#homepageMyArticlesList tr:not(."+x+")").hide();
    $("#homepageMyArticlesList tr."+x+"").show();
  });
  $(".resetArticlesFilter").click(function() {
    $("#homepageMyArticlesList tr").show();
  });
});

