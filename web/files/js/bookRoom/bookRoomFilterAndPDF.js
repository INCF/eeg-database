/**
 * Auhor: Jenda Kolena, jendakolena@gmail.com
 */

function selectAllReservations()
{
    var count = $("#reservationsCount").attr('value');

    for (var i = 0; i < count; i++)
    {
        $("#check_" + i).attr('checked', true);
    }
}

function resetFilter()
{
    $("#filterDate").attr('value', localize("bookRoom.filter.date.click"));
    document.getElementById("filterGroup").selectedIndex = 0;
    showChosenData();
}

function downloadPDF(id)
{
    $("#pdfId").attr('value', id);
    $("#pdfType").attr('value', 'single');
    document.forms["pdf"].submit();
}

function downloadPDFList()
{
    $("#pdfType").attr('value', 'range');
    $("#pdfDate").attr('value', $("#date").attr('value'));
    document.forms["pdf"].submit();
}