# Automated Mobile Testing with Appium

## Prerequisites

Before running the tests, ensure you have the following installed:

- [Java JDK 11 or later](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi) (Verify installation with `mvn -version`)
- [Android SDK](https://developer.android.com/studio) (Ensure `ANDROID_HOME` is correctly set)
- [Appium Server](https://appium.io/) (Run `npm install -g appium` to install)
- [Node.js](https://nodejs.org/) (Required for Appium installation)
- [Appium Inspector](https://github.com/appium/appium-inspector) (Optional but recommended for debugging UI elements)
- [Android Emulator or Physical Device](https://developer.android.com/studio/run/managing-avds)

## Setup

### 1. Configure Environment Variables

Ensure the following environment variables are set:

#### Windows (PowerShell)
```powershell
$env:ANDROID_HOME="C:\Users\YourUser\AppData\Local\Android\Sdk"
$env:JAVA_HOME="C:\Program Files\Java\jdk-11"
$env:PATH+=";$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\tools;$env:ANDROID_HOME\tools\bin"
```

#### macOS / Linux
```sh
export ANDROID_HOME=$HOME/Library/Android/sdk
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin
```

### 2. Install Dependencies
Navigate to the project root and install dependencies:
```sh
mvn clean install
```

### 3. Start Appium Server
Start the Appium server before running tests:
```sh
appium --allow-cors
```

### 4. Configure Emulator or Device
- **For an emulator**, ensure an AVD is created and running:
  ```sh
  emulator -avd YourEmulatorName
  ```
- **For a physical device**, enable **Developer Mode** and **USB Debugging** and check if the device is connected:
  ```sh
  adb devices
  ```

## Running Tests
To execute the test suite, use:
```sh
mvn test
```

Alternatively, run a specific test class:
```sh
mvn -Dtest=simpleTest test
```

## Troubleshooting

### 1. Appium Not Starting
If Appium fails to start, try reinstalling:
```sh
npm uninstall -g appium
npm install -g appium
```

### 2. Android Emulator Hypervisor Driver Issue
If you encounter an error related to the emulator hypervisor, try:
```sh
cd C:\Users\YourUser\AppData\Local\Android\Sdk\extras\google\Android_Emulator_Hypervisor_Driver
silent_uninstall.bat
silent_install.bat
```

### 3. Emulator or Device Not Found
- Ensure the emulator is running or the device is connected.
- Run `adb devices` to confirm the connection.
- Restart the `adb` server:
  ```sh
  adb kill-server
  adb start-server
  ```

## License
This project is licensed under the MIT License.

