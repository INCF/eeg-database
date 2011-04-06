/*==================================================
 *  Localization of labellers.js
 *==================================================
 */

Timeline.GregorianDateLabeller.monthNames["vi"] = [
    "Th�ng 1", "Th�ng 2", "Th�ng 3", "Th�ng 4", "Th�ng 5", "Th�ng 6", "Th�ng 7", "Th�ng 8", "Th�ng 9", "Th�ng 10", "Th�ng 11", "Th�ng 12"
];

Timeline.GregorianDateLabeller.labelIntervalFunctions["vi"] = function(date, intervalUnit)
{
    var text;
    var emphasized = false;

    var date2 = Timeline.DateTime.removeTimeZoneOffset(date, this._timeZone);

    switch (intervalUnit)
    {
        case Timeline.DateTime.DAY:
        case Timeline.DateTime.WEEK:
            text = date2.getUTCDate() + "/" + (date2.getUTCMonth() + 1);
            break;
        default:
            return this.defaultLabelInterval(date, intervalUnit);
    }

    return { text: text, emphasized: emphasized };
};
