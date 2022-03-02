export const getWeekDate = (weekDate) => {
    switch (weekDate) {
        case 1:
            return "CAL.mon";
        case 2:
            return "CAL.tue";
        case 3:
            return "CAL.wed";
        case 4:
            return "CAL.thu";
        case 5:
            return "CAL.fri";
        case 6:
            return "CAL.sat";
        case 7:
            return "CAL.sun";
    }
}

export const getMonth = (monthInt) => {
    switch (monthInt){
        case 1:
            return "CAL.jan";
        case 2:
            return "CAL.feb";
        case 3:
            return "CAL.mar";
        case 4:
            return "CAL.apr";
        case 5:
            return "CAL.may";
        case 6:
            return "CAL.jun";
        case 7:
            return "CAL.jul";
        case 8:
            return "CAL.aug";
        case 9:
            return "CAL.sep";
        case 10:
            return "CAL.oct";
        case 11:
            return "CAL.nov";
        case 12:
            return "CAL.dec";
    }

}

export const dateToString = (app, t) => {
    return t(getWeekDate(app.dayWeek)) + " " + app.day + " " + t(getMonth(app.month)) + ", " + app.year +
        " " + app.hour + ":00"
}