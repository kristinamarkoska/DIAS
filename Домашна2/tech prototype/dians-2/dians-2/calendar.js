const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const daysInMonth = (month, year) => new Date(year, month, 0).getDate();

let currentDate = new Date();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();

function renderCalendar() {
    const monthYear = document.getElementById("month-year");
    const calendarDates = document.getElementById("calendar-dates");
    const daysInCurrentMonth = daysInMonth(currentMonth + 1, currentYear);

    monthYear.textContent = `${monthNames[currentMonth]} ${currentYear}`;
    calendarDates.innerHTML = '';

    const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
    let dayNumber = 1;

    // Fill in the empty slots before the 1st day of the month
    for (let i = 0; i < firstDayOfMonth; i++) {
        const emptyDiv = document.createElement("div");
        calendarDates.appendChild(emptyDiv);
    }

    // Fill in the actual days of the month
    for (let i = firstDayOfMonth; i < 7; i++) {
        const dayDiv = document.createElement("div");
        dayDiv.textContent = dayNumber;
        if (dayNumber === currentDate.getDate() && currentMonth === currentDate.getMonth() && currentYear === currentDate.getFullYear()) {
            dayDiv.classList.add("today");
        }
        calendarDates.appendChild(dayDiv);
        dayNumber++;
    }

    // Fill in the rest of the days of the month
    while (dayNumber <= daysInCurrentMonth) {
        for (let i = 0; i < 7; i++) {
            if (dayNumber > daysInCurrentMonth) break;

            const dayDiv = document.createElement("div");
            dayDiv.textContent = dayNumber;
            if (dayNumber === currentDate.getDate() && currentMonth === currentDate.getMonth() && currentYear === currentDate.getFullYear()) {
                dayDiv.classList.add("today");
            }
            calendarDates.appendChild(dayDiv);
            dayNumber++;
        }
    }
}

document.getElementById("prev-month").addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }
    renderCalendar();
});

document.getElementById("next-month").addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    renderCalendar();
});

renderCalendar();