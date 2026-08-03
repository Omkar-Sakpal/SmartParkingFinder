# Smart Parking Finder

Smart Parking Finder is an Android application that enables users to locate, view, and book nearby parking spaces using OpenStreetMap, GPS, and Firebase Realtime Database. The application also provides an administrator dashboard for managing parking locations, bookings, and slot availability in real time.

## 🚀 Features

### For Users
* **Parking Search & Discovery:** View a list of available parking locations and check real-time slot availability.
* **Location Tracking:** Uses device GPS to identify current location for better parking suggestions.
* **Smart Parking Booking:** Book a parking slot in advance to secure your spot.
* **Booking History:** Keep track of all your past and current parking bookings.
* **Navigation Integration:** Navigate to your booked parking spot using location intents.
* **Multilingual Support:** Includes localization support (e.g., Marathi localization available in resources).

### For Administrators
* **Admin Dashboard:** A dedicated interface for admins to manage the entire parking ecosystem.
* **Secure Login:** Access to the admin dashboard is protected via credentials.
* **Manage Parking Spaces:** Add new parking locations, update existing ones, or remove them.
* **Manage Bookings:** View and oversee all bookings made by users across all parking locations.

## 🛠️ Technologies Used

* **Language:** Java (Android SDK)
* **Minimum SDK:** API 24 (Android 7.0)
* **Target SDK:** API 34 (Android 14)
* **UI Components:**
  * Material Design Components (`com.google.android.material:material`)
  * ConstraintLayout (`androidx.constraintlayout`)
  * RecyclerView & CardView (for displaying lists of parking spots and bookings)
* **Backend & Database:** Firebase Realtime Database (`com.google.firebase:firebase-database`) for storing and syncing parking slots and bookings in real-time.
* **Location Services:** Google Play Services Location (`com.google.android.gms:play-services-location`) for fetching coarse and fine user location.
* * **Maps:** OpenStreetMap with Leaflet for displaying parking locations and navigation.

## 🏗️ Architecture & App Logic

The application follows standard Android Activity-based architecture:
1. **MainActivity:** The entry point of the app where users can start their parking search or access other menus.
2. **ParkingListActivity:** Fetches the list of parking spots from Firebase and displays them using a RecyclerView. Users can tap on a location to view details.
3. **Booking Logic:** When a user books a slot, the app updates the Firebase Realtime Database to decrement the available slots for that parking location and records the booking details in the user's booking history.
4. **Admin Module:** 
   * **AdminLoginActivity:** Authenticates the admin using predefined credentials.
   * **AdminDashboardActivity:** Acts as the central hub for admins.
   * **Add/Update/ManageParkingActivity:** These activities allow the admin to push new data to Firebase or modify existing parking nodes (e.g., total slots, location name, price).
   * **AdminBookingsActivity:** Retrieves and displays all user bookings from the database.

## 📱 Permissions Required

To function properly, the app requests the following Android permissions:
* `INTERNET`: For communicating with the Firebase Realtime Database.
* `ACCESS_FINE_LOCATION` & `ACCESS_COARSE_LOCATION`: For fetching the user's current GPS location.

## ⚙️ Setup & Installation

1. **Clone the Repository** and open the project in Android Studio.
2. **Firebase Setup:** 
   * The app is linked to a Firebase project via the `google-services.json` file. Ensure you have the proper Firebase configurations if you intend to connect it to your own Firebase project.
   * Enable **Firebase Realtime Database** in your Firebase Console.
3. **Build & Run:** 
   * Sync the Gradle project.
   * Connect a physical Android device or start an emulator (Android 7.0+).
   * Click the 'Run' button in Android Studio.

## 📝 Future Enhancements

* Enhanced navigation features and real-time parking availability updates.
* Payment gateway integration for handling parking fees.
* Push notifications for booking confirmations and time expiration.
