package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MusicPlayerGUI {
    private JFrame frame;
    private JList<Song> songList;
    private DefaultListModel<Song> listModel;
    private SongDAO songDAO;
    private MusicPlayer musicPlayer;
    private SongPlayerWindow songPlayerWindow;

    public MusicPlayerGUI() {
        songDAO = new SongDAO();
        musicPlayer = new MusicPlayer();
        frame = new JFrame("Local Music Player");
        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);

        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setCellRenderer(new DefaultListCellRenderer());

        JButton loadSongsButton = new JButton("Load Songs");
        loadSongsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSongs();
            }
        });

        JButton uploadButton = new JButton("Upload Song");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadSong();
            }
        });

        // Play song when an item is selected
        songList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedSongIndex = songList.getSelectedIndex();
                if (selectedSongIndex != -1) {
                    Song selectedSong = songList.getSelectedValue();
                    List<Song> playlist = songDAO.getAllSongs();  // Fetch all songs as the playlist

                    // Close any previously opened song player window
                    if (songPlayerWindow != null) {
                        songPlayerWindow.close();
                    }

                    // Open a new window to play the selected song, passing the playlist and current index
                    songPlayerWindow = new SongPlayerWindow(selectedSong, playlist, selectedSongIndex);
                }
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(songList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadSongsButton);
        buttonPanel.add(uploadButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadSongs() {
        listModel.clear();
        List<Song> songs = songDAO.getAllSongs();
        if (songs != null) {
            for (Song song : songs) {
                listModel.addElement(song);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No songs found in the database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uploadSong() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Song song = new Song();
            song.setTitle(selectedFile.getName());
            song.setFilePath(selectedFile.getAbsolutePath());

            String artist = JOptionPane.showInputDialog(frame, "Enter artist:");
            String album = JOptionPane.showInputDialog(frame, "Enter album:");

            if (artist == null || album == null || artist.isEmpty() || album.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Artist and album are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            song.setArtist(artist);
            song.setAlbum(album);

            songDAO.addSong(song);
            loadSongs();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicPlayerGUI::new);
    }
}
