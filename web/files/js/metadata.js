
function checkedAll(formId, ch) {
  var md = document.getElementById(formId);

 var elm = md.getElementsByTagName("input");
  for (var i =0; i < elm.length; i++)
  {
    elm[i].checked = ch;
  }

}