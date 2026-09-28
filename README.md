# TrashApp

## Product Overview

TrashApp is a desktop application designed to aid users in finding public bins around Christchurch and provide them
the ability to view their fullness, log rubbish disposal, report issues, and compete against other users in a 
sustainability challenge.

The application features an interactive map and external APIs for location and data.


## Authors
- Connor Rocket
- Archie Georgeson
- Max Tyson
- Abdalla Weza
- Marcus Demafeliz
- Stanley Easthope

## Version

Version: 1.0

This version includes:
- Interactive map of bin locations around the city
- Viewing bin status, fullness and other information
- Logging rubbish disposal and issues with bins
- Dashboard statistics
- Leaderboard
- CSV export

## Prerequisites
- JDK >= 21
- Internet connection for API features

The Gradle Wrapper is included in the project so a seperate Gradle installation is not necessary.

## Importing Project (Using IntelliJ)
IntelliJ has built-in support for Gradle. To import your project:

- Launch IntelliJ and choose `Open` from the start-up window.
- Select the project and click open
- Select the TrashApp project directory
- At this point in the bottom right notifications you may be prompted to 'load Gradle scripts', If so, click load
- Ensure project SDK is set to JDK 21 or later

**Note:** *If you run into dependency issues when running the app or the Gradle pop up doesn't appear then open the Gradle sidebar and click the Refresh icon.*

## Build Project 
1. Open the terminal in the root directory of the project and run `./gradlew build` to build a .jar file. The file is located at build/libs/seng202_team1_trashpp-1.0-SNAPSHOT.jar

## Run App
- If you haven't already, build the project.
- Open the terminal in the root directory of the project.
- Run the command `./gradlew run` to run the application.

## Credits
- Icons: https://www.svgrepo.com (CC0)
- Font: https://fontlibrary.org (OFL)
- Map: https://leafletjs.com (BSD)
- Map Tiles: https://snazzymaps.com (CC0)
- Map Tiles Style Conversion: https://github.com/julienben/gmaps-apistyle-encoder
- Location Search: https://photon.komoot.io/
- Public Bin Data: https://opendata-christchurchcity.hub.arcgis.com/datasets/ChristchurchCity::bin-opendata/explore?location=-43.591737%2C172.824069%2C11.00
- Map Location Sample Code: https://eng-git.canterbury.ac.nz/public-seng-resources/seng202/seng202-advanced-fx-public/-/tree/master?ref_type=heads

## Thirdparty
- unpkg