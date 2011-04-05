/**
 * Script generates dynamic form for adding weather
 * User: pbruha
 * Date: 24.3.11
 * Time: 9:29
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function() {
  $("div.newFormWeather").hide();
  $("a.addWeather").click(function(){
    $("div.newFormWeather").show();
  });
    $("input.addNewWeather").click(function(){
    $("div.newFormWeather").hide();
        location.reload();
  });

});


