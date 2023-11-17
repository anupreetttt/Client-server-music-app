# Client-server-music-app

This repository contains two Android apps that communicate with each other using an Inter-Process Communication (IPC) mechanism. The Clip Server App provides audio playback functionalities, and the Client App acts as a user interface to control these functionalities.

## Clip Server App

- Developed and launched a Clip Server API supporting audio playback actions.
- Implemented an AIDL interface for Inter-Process Communication (IPC) to expose the service's functionality.
- The Clip Server App serves as the backend for audio playback.

## Client App

- Developed a client app to control audio playback functionalities provided by the Clip Server.
- Utilized AIDL to establish communication with the Clip Server.
- The Client App provides a user interface for playing, pausing, and stopping audio.

## Communication Flow

1. Launch the Clip Server App to initiate audio playback services.
2. Launch the Client App, which binds to the Clip Server using the AIDL interface.
3. Use the Client App's interface to control audio playback (play, pause, stop).
4. The Client App communicates with the Clip Server through AIDL, initiating the corresponding audio action.
5. The Clip Server processes the request and sends a response back to the Client App.
6. The Client App updates its interface based on the received response.

## How to Use

1. Clone the repository:

   ```bash
   https://github.com/anupreetttt/Client-server-music-app.git

2. Open it in Android Studio
