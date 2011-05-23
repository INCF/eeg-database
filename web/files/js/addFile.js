var fieldCounter = 0;

$(document).ready(function(){
  $("a.addFile").click(function(){
      fieldCounter++;
      $("table.formTable").append(
              '<tr><td>' +
              
              '</td><td>' +
              '<input type="file" name="dataFile' + fieldCounter + '" class="fileField"/>' +
              '</td>' +
              '</tr>'
      );
  });
});

