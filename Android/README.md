# Android

## 1. Description

This is a part of UCL NHSX prject on the Android, including an package provding a series of APIs, an app for demonstrating them, and a NHSX app integrated with the package. As to the api, please see this [api document](docs/api_document.md). As to these two apps, please see this [apps document](docs/apps_document.md).  

## 2. Files Structure

``` java
 📂Android                              // The root directory
 ┣ 📂api                                // The directory for the APIs
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂androidTest                                 
 ┃ ┃ ┃ ┣ 📦java.uclsse.comp0102.nhsxapp.api
 ┃ ┃ ┃ ┗ ...
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📦java.uclsse.comp0102.nhsxapp.api
 ┃ ┃ ┃ ┗ 📂res
 ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┗ 📂values
 ┃ ┃ ┃ ┃ ┃ ┗ 📜strings.xml              // The directory for the string values used in APIs
 ┃ ┃ ┗ ...
 ┃ ┗ 📜build.gradle
 ┣ 📂app                                // The directory for the demo app
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂androidTest
 ┃ ┃ ┃ ┣ 📦java.uclsse.comp0102.nhsxapp.android.demo
 ┃ ┃ ┃ ┗ ...
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📦java.uclsse.comp0102.nhsxapp.android.demo
 ┃ ┃ ┃ ┣ 📂res
 ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┗ 📂values
 ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┗ 📜strings.xml              // The directory for the string values used in the demo app
 ┃ ┃ ┗ ...
 ┃ ┗ 📜build.gradle
 ┣ 📂undergrad                          // The directory for the NHSX app developed by second year students
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂androidTest
 ┃ ┃ ┃ ┣ 📦java.com.uk.ac.ucl.carefulai
 ┃ ┃ ┃ ┗ ...
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📦java.com.uk.ac.ucl.carefulai
 ┃ ┃ ┃ ┣ 📂res
 ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┗ 📂values
 ┃ ┃ ┃ ┃ ┃ ┣ ...
 ┃ ┃ ┃ ┃ ┃ ┗ 📜strings.xml              // The directory for the string values used in the NHSX app
 ┃ ┃ ┗ ...
 ┃ ┗ 📜build.gradle
 ┣ 📂gradle
 ┣ 📜build.gradle
 ┗ ...
```



