package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SongPlayerWindow {
    private JFrame frame;
    private MusicPlayer musicPlayer;
    private Song currentSong;
    private List<Song> playlist;  // For previous/next functionality
    private int currentSongIndex;  // Track the current song's position in the list

    public SongPlayerWindow(Song song, List<Song> playlist, int songIndex) {
        this.currentSong = song;
        this.playlist = playlist;
        this.currentSongIndex = songIndex;
        musicPlayer = new MusicPlayer();

        frame = new JFrame("Now Playing: " + song.getTitle());
        frame.setLayout(new BorderLayout());

        // Song details (Artist, Album, etc.)
        JLabel songDetails = new JLabel("<html><b>Title:</b> " + song.getTitle() +
                "<br><b>Artist:</b> " + song.getArtist() +
                "<br><b>Album:</b> " + song.getAlbum() + "</html>");
        frame.add(songDetails, BorderLayout.NORTH);

        // Control buttons panel (Previous, Play/Pause, Next, Stop)
        JPanel controlPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton playPauseButton = new JButton("Play");
        JButton nextButton = new JButton("Next");
        JButton stopButton = new JButton("Stop");

        // Add Action Listeners to each button
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPreviousSong();
            }
        });

        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Play".equals(playPauseButton.getText())) {
                    musicPlayer.play(currentSong.getFilePath());
                    playPauseButton.setText("Pause");
                } else {
                    musicPlayer.stop();
                    playPauseButton.setText("Play");
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNextSong();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.stop();
                playPauseButton.setText("Play");
            }
        });

        // Add buttons to the control panel
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);

        frame.add(controlPanel, BorderLayout.CENTER);

        // Set up frame settings
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            musicPlayer.stop();
            currentSongIndex--;
            currentSong = playlist.get(currentSongIndex);
            updateWindowForNewSong();
        }
    }

    private void playNextSong() {
        if (currentSongIndex < playlist.size() - 1) {
            musicPlayer.stop();
            currentSongIndex++;
            currentSong = playlist.get(currentSongIndex);
            updateWindowForNewSong();
        }
    }

    // Update the window with new song details and play the song
    private void updateWindowForNewSong() {
        frame.setTitle("Now Playing: " + currentSong.getTitle());
        ((JLabel) frame.getContentPane().getComponent(0)).setText("<html><b>Title:</b> " + currentSong.getTitle() +
                "<br><b>Artist:</b> " + currentSong.getArtist() +
                "<br><b>Album:</b> " + currentSong.getAlbum() + "</html>");
        musicPlayer.play(currentSong.getFilePath());
    }

    // This method allows the MusicPlayer to be stopped if the window is closed
    public void close() {
        musicPlayer.stop();
        frame.dispose();
    }
}
