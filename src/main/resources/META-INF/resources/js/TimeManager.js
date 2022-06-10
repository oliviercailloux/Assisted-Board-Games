class TimeManager {
    /**
     * Initialises a TimeManager
     * @param color - color of the player (for chess initially, "White" or "Black")
     */
    constructor(color) {
        this.gameId = -1;
        this.color = color;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    /**
     * Gets in database the remaining time for the player
     */
    getRemainingTime() {
        const url = '/v0/api/v1/game/' + this.gameId +'/clock/' + this.color.toLowerCase();
        let time;
        database.get(url, (data) => {
            time = data;
        });

        this.parseTime(time);
    }

    /**
     * Parses the time sent by database to our object
     * @param gameTime - string representing the time
     */
    parseTime(gameTime) {
        const clock =  document.getElementById(this.color + '-player-clock');
        clock.innerText = "00:00:00";

        let hours;
        const hoursIndex = gameTime.indexOf("H");
        let minutes;
        const minutesIndex = gameTime.indexOf("M");
        let seconds;
        const secondsIndex = gameTime.indexOf("S");

        if (hoursIndex !== -1)
            hours = gameTime.substring(gameTime.indexOf("T") + 1, hoursIndex);
        else
            hours = "00";

        if (hoursIndex === -1 && minutesIndex !== -1)
            minutes = gameTime.substring(gameTime.indexOf("T") + 1, minutesIndex);
        else if (minutesIndex !== -1)
            minutes = gameTime.substring(hoursIndex + 1, minutesIndex);
        else
            minutes = "00";

        if (secondsIndex !== -1)
            seconds = Math.round(gameTime.substring(minutesIndex + 1, secondsIndex));
        else
            seconds = "00";

        hours = Number(hours);
        minutes = Number(minutes);
        seconds = Number(seconds);

        if (seconds === 60) {
            minutes += 1;
            seconds = 0;
        }

        if (minutes === 60) {
            hours += 1;
            minutes = 0;
        }

        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * add or substract the time difference to the TimeManager
     * @param timeDifference - if positive adds the time, if negative substracts it
     */
    updateTime(timeDifference) {
        if (timeDifference === 0) {
            return;
        }

        let resultObj;
        if (timeDifference < 0) {
            timeDifference = Math.abs(timeDifference);
            resultObj = this.decreaseTime(timeDifference);
        }
        else {
            resultObj = this.increaseTime(timeDifference);
        }

        this.hours = resultObj.hours;
        this.seconds = resultObj.seconds;
        this.minutes = resultObj.minutes;
    }


    /**
     * Displays the time on the clock
     */
    displayTime() {
        const id = this.color + "-player-clock";
        const clock = document.getElementById(id);

        let seconds = this.seconds < 10 ? "0" + this.seconds.toString() :  this.seconds.toString();
        let minutes = this.minutes < 10 ? "0" + this.minutes.toString() :  this.minutes.toString();
        let hours = this.hours < 10 ? "0" + this.hours.toString() :  this.hours.toString();

        clock.innerText = `${hours}:${minutes}:${seconds}`;
    }


    /**
     * Updates the gameId for the board (necessary for post request)
     * @param gameId - integer for gameId
     */
    updateGameId(gameId) {
        this.gameId = gameId;
    }

    /**
     * Checks if the player still has time
     * @returns {boolean} - true if time is left, else if the clock is to 0
     */
    hasTimeLeft() {
        return (this.hours !== 0 || this.minutes !== 0 || this.seconds !== 0);
    }

    /**
     * Decrements the time with the decrement in argument
     * @param decrement - absolute value of the time to substract
     * @returns {number[]|{hours: (*|number), seconds: (*|number), minutes: (*|number)}} - object with the corresponding hours, minutes and seconds
     */
    decreaseTime(decrement) {
        let seconds = this.seconds;
        let minutes = this.minutes;
        let hours = this.hours;
        if (hours === 0 && minutes === 0 && seconds < decrement) {
            return [0, 0, 0];
        }
        if (seconds < decrement) {
            if (minutes === 0) {
                hours -= 1; // increment is always less than a minute
                minutes = 59;
            }
            else {
                minutes -= 1;
            }
            seconds = 60 - (decrement - seconds); //remaining number of seconds
        }
        else {
            seconds -= decrement;
        }

        return {hours, minutes, seconds};
    }

    /**
     * Increments the time with the increment in argument
     * @param increment - absolute value of the time to add
     * @returns {number[]|{hours: (*|number), seconds: (*|number), minutes: (*|number)}} - object with the corresponding hours, minutes and seconds
     */
    increaseTime(increment) {
        let seconds = this.seconds;
        let minutes = this.minutes;
        let hours = this.hours;
        if (seconds + increment > 59) {
            if (minutes + 1 > 59) {
                hours += 1;
                minutes = 0;
            } else {
                minutes += 1;
            }
            seconds = seconds + increment - 60;
        } else {
            seconds += increment;
        }

        return {hours, minutes, seconds};
    }
}