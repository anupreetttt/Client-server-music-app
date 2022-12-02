// AudioInterface.aidl
package com.example.clipserver;

// Declare any non-default types here with import statements

interface AudioInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    boolean playAudio(int selectedTrack);
//    boolean pauseAudio(int selectedTrack);
//    boolean stopAudio(int selectedTrack);
//    boolean resumeAudio(int selectedTrack);
//    int getResult(int a, int b);
//    String getMessage(String name);
        void playAudio(int audioChoice);
        void pauseAudio();
        void stopAudio();
        void onStop();
        void onResume();
        void toStopService();
        int checkStatus();
        void onStartService();
}