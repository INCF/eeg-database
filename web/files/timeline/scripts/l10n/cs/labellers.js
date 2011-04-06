/*==================================================
 *  Localization of labellers.js
 *==================================================
 */

Timeline.GregorianDateLabeller.monthNames["cs"] = [
    "Leden", "�nor", "B�ezen", "Duben", "Kv�ten", "�erven", "�ervenec", "Srpen", "Z���", "��jen", "Listopad", "Prosinec"
];

Timeline.GregorianDateLabeller.dayNames["cs"] = [
    "Ne", "Po", "�t", "St", "�t", "P�", "So"
];

Timeline.GregorianDateLabeller.labelIntervalFunctions["cs"] = function(date, intervalUnit)
{
    var text;
    var emphasized = false;

    var date2 = Timeline.DateTime.removeTimeZoneOffset(date, this._timeZone);

    switch (intervalUnit)
    {
        case Timeline.DateTime.DAY:
        case Timeline.DateTime.WEEK:
            text = date2.getUTCDate() + ". " + (date2.getUTCMonth() + 1) + ".";
            break;
        default:
            return this.defaultLabelInterval(date, intervalUnit);
    }

    return { text: text, emphasized: emphasized };
};
