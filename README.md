# Mesway - Find Mess Meal

[![Google Play Store](https://img.shields.io/badge/Download%20on-Google%20Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=in.mesway)

Mesway is an Android application designed to simplify the process of finding nearby mess services and managing meal subscriptions, with features like password-less sign-in via OTP, user profile management, location-based search, and meal cancellation. Mesway offers flexible subscription plan tailored to user preferences with options spanning 1, 7, 15 and 30 days.

## Other related Projects

- **[Mesway Seller](https://github.com/imsidharthpriyadarshi/Mesway-Seller)**: This repository contains the codebase for the Mesway Seller application, where mess owners and meal providers can manage their offerings, orders, and interact with customers.
- **[Mesway OTP](https://github.com/imsidharthpriyadarshi/Mesway-OTP)**: Mesway OTP is responsible for sending OTP for user sign in & sign up

## Screenshots

<img src="screenshots/sc5.png" alt="Login Screen" width="137" height="296">  <img src="screenshots/sc 1.webp" alt="Home Screen" width="137" height="296"> <img src="screenshots/sc 2.webp" alt="Meals Management" width="137" height="296"> <img src="screenshots/sc 3.webp" alt="Subscription history" width="137" height="296">  <img src="screenshots/sc6.png" alt="Setting" width="137" height="296">  <img src="screenshots/sc7.png" alt="Order History" width="137" height="296">


## Features

- **OAuth Authentication**: By implementing OAuth2 with JWT tokens, Mesway ensures a reliable and scalable authentication solution, safeguarding user accounts and enhancing the overall user experience.

- **Password-less Sign-in/Sign-up**: Authenticate users via OTP, eliminating the need for passwords and ensuring a secure login process.

- **Location-Based Search**: Enable users to find nearby mess services based on their current location, enhancing convenience and accessibility.

- **Delivery Code**: Users can generate a unique delivery code for their orders, simplifying the pickup process and ensuring secure delivery.

- **Contact Delivery Boy**: Users have the option to directly call the assigned delivery personnel for real-time updates or to communicate specific instructions regarding their order.

- **Order History**: Users can conveniently track their order history, providing insights into past purchases and deliveries.

- **User Profile Management**: Allow users to manage their profiles, including personal details and preferences, for a customized experience.

- **Review and Rating System**: Empower users to leave reviews and ratings for mess services, facilitating informed decision-making for others.

- **Meal Cancellation Restrictions**: Implement meal cancellation restrictions based on specific times of the day(eg. After 7 am, breakfast cancellations are not permitted), ensuring order accuracy and reducing operational complexities.

- **Detailed Mess Profiles**: Provide comprehensive information about nearby mess services, including menu offerings, subscription plans, and operating hours, to help users make informed choices.

- **Subscription Plans**: Offer various subscription plans with customizable meal combinations, catering to diverse user preferences and schedules.

- **Notifications**: Send timely notifications to users for important updates such as order confirmations or reminders, enhancing user engagement and experience.

## Technologies Used

- **Language**: Java
- **Authentication**: OAuth2 using JWT token
- **Backend Integration**: Utilizes FastAPI for backend services, PostgreSQL for the database, and AWS services (S3, Lambda, RDS) for deployment.
- **Login & Signup**: OTP based login & Signup
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Android Navigation Component
- **LiveData**: Android Architecture Components (LiveData)
- **Notifications**: Firebase Cloud Messaging (FCM)
- **Retrofit**: For network requests
- **Glide**: For image loading
- **Obfuscation & Shrinking**: I obfuscate and shrink the code using progaurd

## Installation

Mesway is available for download on the [Google Play Store](https://play.google.com/store/apps/details?id=in.mesway). Simply visit the store page and install it on your Android device.
