/**
 * Created by IntelliJ IDEA.
 * User: Jan Stebetak
 * Date: 4.4.2011
 * Time: 20:38:55
 * To change this template use File | Settings | File Templates.
 */

function setDate(date) {
    $("#endDate").attr('value', date);
    $("#startDate:text:visible:first").focus();
}


function changeEndDate() {
    $("#endDate:text:visible:first").focus();
}


function startTimeChange(time) {
    var hourAndMin = time.split(":");
    if (hourAndMin.length == 2) {
        var hour = ((parseInt(hourAndMin[0], 10)) + 1) % 24;
        if (hour < 10) {
            $("#endTime").attr('value', "0" + hour + ":" + hourAndMin[1]);
        } else {
            $("#endTime").attr('value', hour + ":" + hourAndMin[1]);
        }
    }
}

