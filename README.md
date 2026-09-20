## Trash App

# Overview

TrashApp is a desktop application designed to help users view nearby bins. The app shows 


## Authors
- SENG202 Teaching team

## Prerequisites
- JDK >= 21 [click here to get the latest stable OpenJDK release (as of writing this README)](https://jdk.java.net/18/)
- Gradle [Download](https://gradle.org/releases/) and [Install](https://gradle.org/install/)


## What's Included
This project comes with some basic examples of the following (including dependencies in the build.gradle file):
- JavaFX
- Logging (with Log4J)
- Junit 5
- Mockito (mocking unit tests)
- Cucumber (for acceptance testing)

We have also included a basic setup of the Gradle project and Tasks required for the course including:
- Required dependencies for the functionality above
- Build plugins:
    - JavaFX Gradle plugin for working with (and packaging) JavaFX applications easily

You are expected to understand the content provided and build your application on top of it. If there is anything you
would like more information about please reach out to the tutors.

## Importing Project (Using IntelliJ)
IntelliJ has built-in support for Gradle. To import your project:

- Launch IntelliJ and choose `Open` from the start up window.
- Select the project and click open
- At this point in the bottom right notifications you may be prompted to 'load gradle scripts', If so, click load

**Note:** *If you run into dependency issues when running the app or the Gradle pop up doesn't appear then open the Gradle sidebar and click the Refresh icon.*

# TODO
- todo: nice readme


## Rename Project
- Open `build.gradle` and change all references to `team0` with your team number eg. `team13`
- Rename the directory `src/main/java/seng202/team0` to your team number eg. `src/main/java/seng202/team1`

## Build Project 
1. Open a command line interface inside the project directory and run `./gradlew run` to build a .jar file. The file is located at target/seng202_team0-1.0-SNAPSHOT.jar

## Run App
- If you haven't already, Build the project.
- Open a command line interface inside the project directory and run `cd target` to change into the target directory.
- Run the command `java -jar seng202_team0-1.0-SNAPSHOT.jar` to open the application.

# Credits
- Icons: https://www.svgrepo.com (CC0)
- Font: https://fontlibrary.org (OFL)
- Map: https://leafletjs.com (BSD)
- Map Tiles: https://snazzymaps.com (CC0)
- Map Tiles Style Conversion: https://github.com/julienben/gmaps-apistyle-encoder
- Map Location Server: 
- Map Location Sample Code: https://eng-git.canterbury.ac.nz/public-seng-resources/seng202/seng202-advanced-fx-public/-/tree/master?ref_type=heads

# Thirdparty
- unpkg