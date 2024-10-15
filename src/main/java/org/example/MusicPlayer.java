package org.example;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;

public class MusicPlayer {
    private Player player;
    private Thread playerThread;
    private boolean isPlaying;

    public void play(String filePath) {
        stop(); // Stop any currently playing music first

        playerThread = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(filePath);
                player = new Player(fis);
                isPlaying = true;
                player.play();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        });
        playerThread.start(); // Start the playback in a new thread
    }

    public void stop() {
        if (player != null) {
            player.close(); // Stop the player
            isPlaying = false;
            player = null;
        }
        if (playerThread != null) {
            playerThread.interrupt(); // Interrupt the thread
            playerThread = null;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
