let map;
let userMarker, assistantMarker;
let userCircle, assistantCircle;
let userLat, userLon, assistantLat, assistantLon;

// Initialize the map
function initMap() {
    map = L.map('map').setView([1.3521, 103.8198], 13); // Default center

    // Load map tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    // Add markers for User and Assistant
    userMarker = L.marker([1.3521, 103.8198]).addTo(map).bindPopup("User Location");
    assistantMarker = L.marker([1.2966, 103.8530]).addTo(map).bindPopup("Assistant Location");

    // Add circles for tracking proximity
    userCircle = L.circle([1.3521, 103.8198], { radius: 50 }).addTo(map);
    assistantCircle = L.circle([1.2966, 103.8530], { radius: 50 }).addTo(map);
}

// Update user and assistant positions
function updateMap(newUserLat, newUserLon, newAssistantLat, newAssistantLon) {
    userLat = newUserLat;
    userLon = newUserLon;
    assistantLat = newAssistantLat;
    assistantLon = newAssistantLon;

    userMarker.setLatLng([userLat, userLon]);
    assistantMarker.setLatLng([assistantLat, assistantLon]);

    userCircle.setLatLng([userLat, userLon]);
    assistantCircle.setLatLng([assistantLat, assistantLon]);

    map.setView([userLat, userLon], 15); // Adjust zoom dynamically
}

// Auto-run map setup
document.addEventListener("DOMContentLoaded", initMap);
