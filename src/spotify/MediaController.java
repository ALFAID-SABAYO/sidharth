package spotify;

import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MediaController {
    private MediaPlayer mediaPlayer;
    private final PlaylistController playlistController;
    private final SpotifyTemplate spotifyTemplate;

    public MediaController(PlaylistController playlistController, SpotifyTemplate spotifyTemplate) {
        this.playlistController = playlistController;
        this.spotifyTemplate = spotifyTemplate;
    }

    public void playSong(Song song) {
        try {
            if (song == null || song.getAudioPath() == null) {
                showAlert("Error", "Invalid song or audio path.");
                return;
            }

            File file = new File(song.getAudioPath());
            if (!file.exists()) {
                showAlert("Error", "The selected file does not exist: " + file.getAbsolutePath());
                return;
            }

            Media media = new Media(file.toURI().toString());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
                Platform.runLater(() -> {
                    Duration duration = mediaPlayer.getMedia().getDuration();
                    spotifyTemplate.updateUIForSong(song, duration);
                });
            });

            mediaPlayer.setOnEndOfMedia(this::skipToNextSong);
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                Platform.runLater(() -> {
                    if (spotifyTemplate != null) {
                        spotifyTemplate.updateUIForSongProgress(newTime, mediaPlayer.getMedia().getDuration());
                    }
                });
            });

            mediaPlayer.play();
        } catch (Exception e) {
            showAlert("Error", "Failed to play the song: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void skipToNextSong() {
        Song nextSong = playlistController.getNextSong();
        if (nextSong != null) {
            playSong(nextSong);
        } else {
            showAlert("Error", "No songs in the playlist.");
        }
    }
//
//    public void skipToPreviousSong() {
//        Song prevSong = playlistController.getPreviousSong();
//        if (prevSong != null) {
//            playSong(prevSong);
//        } else {
//            showAlert("Error", "No previous song found.");
//        }
//    }

    private void showAlert(String title, String message) {
        spotifyTemplate.showAlert(title, message);
    }
}