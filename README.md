# FlightYoke for Android

## How to Use

Follow these steps to connect your Android device to your flight simulator.

### 1. Network Configuration
The application currently streams data to a hardcoded local IP address. To use the app, your environment must match these settings:
* **Target IP:** `192.168.31.174`
* **UDP Port:** `5005`
* **Requirement:** Both your Android device and your PC must be connected to the same Wi-Fi network.

### 2. Customizing the IP and Port (Via Android Studio)
If your computer's IP address is different from the one hardcoded, you can change it in the source code:

1.  Open the project in **Android Studio**.
2.  Open `app/src/main/java/me/habism/flightyoke/MainActivity.kt`.
3.  Locate the following line in the `onCreate` method:
    `udpSender = UdpSender("192.168.31.174", 5005)`
4.  Replace `"192.168.31.174"` with your computer's IP and `5005` with your desired port.

#### Find your IP Address:
Windows:
```ipconfig```

Linux/macOS:
```ifconfig```

### 3. Deploying to Your Phone
To run the app on your physical device, you must first enable **Developer Options** and **USB Debugging** in your phone's settings (usually by tapping "Build Number" 7 times in "About Phone").

**Option A: USB Debugging (Wired)**
1.  Connect your phone to your PC via USB.
2.  Select your device in the Android Studio toolbar dropdown.
3.  Click the **Run** (green play) icon.

**Option B: Wireless Debugging (Android 11+)**
1.  In Android Studio, click the device dropdown and select **Pair Devices Using Wi-Fi**.
2.  On your phone, go to **Developer Options > Wireless Debugging > Pair device with QR code**.
3.  Scan the code on your PC and click **Run**.

### 4. FlightGear Protocol Setup
The simulator needs a protocol file to understand the incoming data format (Roll and Pitch).

1.  Locate your **FlightGear data directory** (this varies by installation and OS).
2.  Inside `data`, open the `Protocol` subfolder.
3.  Copy the `flightyoke.xml` file from the `flightgear_resources/Protocol` folder in this repo into that FlightGear `Protocol` folder.

### 5. Launching the Simulator
Start FlightGear with the following command-line argument (via the launcher or terminal) to begin listening for the app:

`--generic=socket,in,20,0.0.0.0,<port>,udp,flightyoke`

Change `<port>` to whatever port you set in the source code.

**Example:** 

```bash
fgfs --generic=socket,in,20,0.0.0.0,5005,udp,flightyoke
```

```powershell
cd "C:\Program Files\FlightGear\bin\"
.\fgfs.exe --generic=socket,in,20,0.0.0.0,5005,udp,flightyoke
```


### 6. Operating the App
1.  **Launch:** Open the app and hold your device like a steering wheel.
2.  **Calibrate:** Tap the **Calibrate** button to set your current orientation as the neutral "center."
3.  **Fly:** Tilt the device left/right for **Aileron (Roll)** and forward/backward for **Elevator (Pitch)**.
