## This Project
This is the Community-Nonprofit Team 10 Github repository for the TechpointX S.O.S. challenge.
It currently contains an Android Studio project, in NonprofitApp. This project is an android app called Red Riding Hood.

The creators of this project are Joe Lyon, Andrea Wynn, Sean O'Donovan, and Hannah Cougil.

## Red Riding Hood
Red Riding Hood is an app designed to help food banks and pantries respond efficiently and effectively to the COVID-19 crisis by 
streamlining the process of food selection and delivery and helping food banks reduce the logistical work required to remain in operation.

Customers may select one of many pre-defined food bag types, which accommodate a wide variety of dietary restrictions and preferences, 
based on availability at their preferred food pantry. They may also select a preferred pick-up time and date at their local food bank. 

Additionally, volunteers may view all existing orders in one place, and track existing orders and inventory all through the app, 
which connects to a database that stores all necessary information in an easily accessible and scalable manner. 

## Tools
The project is coded entirely in Java and XML through Android Studio.

The app uses Firebase Firestore to store all data and provide user authentication for the service. 

This project follows the Model-View-ViewModel (MVVM) architecture to separate the display, database connectivity, and business logic of the app. 

## Prequisites and Running
Red Riding Hood requires at least Android 9 and runs best on the Pixel 4. It may run all the way down to Android 5, but that requires updating play
services (meaning it won't work on appetize.io) and we can't guarantee that it'll work. Right now Google is reviewing our application for an open beta
on the play store. In the meantime, with or without an Android phone, this appetize link will run the app in the browser, but can accomodate only 1 person at a time.

https://appetize.io/app/7vmvnqrh8h936ba0bm5n4ehj8g?device=pixel4&scale=75&orientation=portrait&osVersion=10.0

If you have an Android phone, and want to load our app, copy the file called ```app-release.apk``` onto your phone and double click it to load. There are quite a few
tutorials on how to do this if you google something like "how to install apk on android from pc".
