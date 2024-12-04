document.getElementById('searchBtn').addEventListener('click', function() {
    let query = document.getElementById('searchInput').value;
    if (query) {
        alert("Searching for: " + query);
        // Add your map API functionality here (e.g., Google Maps or Leaflet)
    }
});

// Иницијација на мапата
var map = L.map('map').setView([41.9981, 21.4254], 13); // Почетна локација: Скопје

// Додавање на мапски слој од OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Додавање на маркер
var marker = L.marker([41.9981, 21.4254]).addTo(map); // Маркер на Скопје
marker.bindPopup("<b>Скопје</b>").openPopup();
