<!DOCTYPE html>
<html lang="en">
    <head>
    <meta charset="UTF-8">
    <title>Invest MK</title>
<link rel="stylesheet" href="pocetna.css">
    <!-- za da se vcita mapata -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
</head>
<body>
<nav class="navbar">
    <ul>
        <li><a href="#home">Home</a></li>
        <li><a href="#calendar">Calendar</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#map-section">Map</a></li>
        <li><a href="#profile">Profile</a></li>
        <li><a href="#login" class="login-btn">Log in</a></li>
        <li><a href="#signup" class="signup-btn">Sign Up</a></li>
    </ul>
</nav>

<!-- Копче Почетна -->
<button id="back-to-top">Home</button>

<!-- Почетна секција -->
<section id="home" class="fullscreen-background"></section>


<!-- Секција со календар -->
<section id="calendar" class="calendar-section">
    <div class="calendar-container">
        <div class="calendar-header">
            <button id="prev-month">&lt;</button>
            <h2 id="month-year"></h2>
            <button id="next-month">&gt;</button>
        </div>
        <div class="calendar-days">
            <div>Mon</div>
            <div>Tue</div>
            <div>Wed</div>
            <div>Thu</div>
            <div>Fri</div>
            <div>Sat</div>
            <div>Sun</div>
        </div>
        <div class="calendar-dates" id="calendar-dates"></div>
    </div>
</section>


<!-- Други секции -->
<section id="about">
    <h1>About Us</h1>
    <p>Learn more about us.</p>
</section>


<!-- Секција за МАПАТА -->
<div id="map-section" class="container">
    <h1>Search a company location</h1>
    <div class="search-bar">
        <input type="text" id="searchInput" placeholder="Search..." />
        <button id="searchBtn">🔍</button>
    </div>
</div>

<div id="map" style="height: 400px;"></div>



<!-- Профил секција -->
<section id="profile" class="profile-section">
    <h1>Your Profile</h1>
    <img src="Images-dom.2/profilna.png" alt="Profile Picture" class="profile-image">

        <form>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" placeholder="b.davcheva" disabled>

                <label for="email">E-mail:</label>
                <input type="email" id="email" name="email" placeholder="blagica.davcheva@students.finki.ukim.mk" disabled>

                    <label for="faq">FAQ:</label>
                    <textarea id="faq" name="faq" placeholder="List your frequently asked questions here..." disabled>
            1. What are the main functions of the stock exchange?
            2. How does stock exchange trading work?
            3. Who can invest on the Macedonian stock exchange?
            4. How are investors' rights protected?
            5. Are there simulators for beginners to practice trading?
            6. Do I need a broker to trade?
        </textarea>

                    <button type="submit">Update Profile</button>
        </form>
</section>

<!-- Log In секција -->
<section id="login">
    <h1>Log In</h1>
    <p>Sign in to your account</p>
    <form>
        <label for="username">E-mail</label><br>
        <input type="text" id="username" name="username" placeholder="Enter your email..." required><br>
            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" placeholder="Enter your password..." required><br>
                <button type="submit">Log In</button>
    </form>
</section>

<!-- Sign Up секција -->
<section id="signup">
    <h1>Sign Up</h1>
    <p>Create New Account</p>
    <form>
        <label for="new-username">Name and Surname</label><br>
        <input type="text" id="new-username" name="new-username" placeholder="Enter your Name and Surname..." required><br>
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" placeholder="Enter your email..." required><br>
                <label for="new-password">Password:</label><br>
                <input type="password" id="new-password" name="new-password" placeholder="Enter your password..." required><br>
                    <button type="submit">Sign Up</button>
    </form>
</section>




<section>
    <button class="logo-button">Invest MK</button>
</section>

<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_GOOGLE_MAPS_API_KEY&callback=initMap" async defer></script>
<script src="map.js"></script>
<script src="calendar.js"></script>
</body>
</html>