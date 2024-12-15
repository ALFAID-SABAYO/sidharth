package spotify;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpotifyTemplate extends Application {
    private final PlaylistController playlistController = new PlaylistController();
    private final MediaController mediaController;
    private Label songDetails;
    private ListView<String> playlistView;
    private static final Label timeLabel = new Label("00:00 / 00:00");
    private static final Slider progressSlider = new Slider();
    private static ImageView albumCoverImageView;
    private String selectedSongName;
    private TextField searchField;

    public SpotifyTemplate() {
        mediaController = new MediaController(playlistController, this); // Pass this instance
    }

    @Override
    public void start(Stage primaryStage) {
        // Main Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header Section
        Label title = new Label("SPOTIFY");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER);
        root.setTop(header);

        // Search Bar Section
        searchField = new TextField();
        searchField.setPromptText("Search for a song...");
        searchField.textProperty().addListener((obs, oldText, newText) -> filterPlaylist(newText));

        HBox searchBox = new HBox(10, new Label("Search:"), searchField);
        searchBox.setAlignment(Pos.CENTER);

        // Playlist Section
        playlistView = new ListView<>();
        playlistView.setOnMouseClicked(event -> playSelectedSong());
        VBox playlistBox = new VBox(10, searchBox, new Label("Playlist"), playlistView);
        playlistBox.setAlignment(Pos.CENTER);

        // Song Details
        songDetails = new Label("Song Details will appear here.");
        songDetails.setStyle("-fx-font-size: 14px;");

        // Control Buttons
        HBox controls = createControlButtons();

        // Volume Control
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> mediaController.setVolume(newVal.doubleValue()));
        HBox volumeBox = new HBox(10, new Label("Volume"), volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        // Album Cover
        albumCoverImageView = new ImageView();
        albumCoverImageView.setFitWidth(200);
        albumCoverImageView.setFitHeight(200);
        albumCoverImageView.setPreserveRatio(true);

        // Center Section
        VBox center = new VBox(20, playlistBox, songDetails, timeLabel, controls, volumeBox, albumCoverImageView);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        // Add, Delete, Clear Playlist Buttons
        HBox bottom = createBottomButtons();
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);

        // Scene and CSS Application
        Scene scene = new Scene(root, 500, 700);
        String cssPath = Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle("Spotify - JavaFX Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void filterPlaylist(String query) {
        List<String> filteredSongs = playlistController.getAllSongs().stream()
                .filter(song -> song.getName().toLowerCase().contains(query.toLowerCase()))
                .map(Song::getName)
                .collect(Collectors.toList());

        playlistView.getItems().setAll(filteredSongs);
    }

    private HBox createControlButtons() {
        Button prevButton = new Button("⏮");
        Button playPauseButton = new Button("⏯ Play");
        Button nextButton = new Button("⏭");
        Button shuffleButton = new Button("🔀 Shuffle");

        prevButton.setOnAction(e -> skipToPreviousSong());
        playPauseButton.setOnAction(e -> togglePlayPause(playPauseButton));
        nextButton.setOnAction(e -> skipToNextSong());
        shuffleButton.setOnAction(e -> shuffleAndPlay());

        HBox controls = new HBox(10, prevButton, playPauseButton, nextButton, shuffleButton);
        controls.setAlignment(Pos.CENTER);
        return controls;
    }

    private HBox createBottomButtons() {
        Button addSongButton = new Button("Add Song");
        addSongButton.setOnAction(e -> addSongToPlaylist());
        Button deleteSongButton = new Button("Delete Song");
        deleteSongButton.setOnAction(e -> deleteSongFromPlaylist());
        Button clearPlaylistButton = new Button("Clear Playlist");
        clearPlaylistButton.setOnAction(e -> clearPlaylist());

        HBox bottomButtons = new HBox(10, addSongButton, deleteSongButton, clearPlaylistButton);
        bottomButtons.setAlignment(Pos.CENTER);
        return bottomButtons;
    }

    private void addSongToPlaylist() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            URI audioUri = selectedFile.toURI();
            String songName = selectedFile.getName();
            String defaultCoverPath = "assets/default_cover.jpg";

            Song newSong = new Song(songName, "Unknown", audioUri, defaultCoverPath);

            playlistController.addSong(newSong);
            playlistView.getItems().add(newSong.getName());
        } else {
            showAlert("Error", "No file selected.");
        }
    }

    private void playSelectedSong() {
        selectedSongName = playlistView.getSelectionModel().getSelectedItem();
        if (selectedSongName != null) {
            Song song = playlistController.getSongByName(selectedSongName);
            if (song != null) {
                mediaController.playSong(song);
                songDetails.setText(song.getName() + " - " + song.getArtist());
                Image coverImage = new Image(song.getCoverPath());
                albumCoverImageView.setImage(coverImage);
            } else {
                showAlert("Error", "Failed to find the selected song.");
            }
        } else {
            showAlert("Error", "No songs in the playlist!");
        }
    }

    private void togglePlayPause(Button playPauseButton) {
        if (mediaController.isPlaying()) {
            mediaController.pauseSong();
            playPauseButton.setText("⏯ Play");
        } else {
            Song song = playlistController.getCurrentSong();
            if (song != null) {
                mediaController.playSong(song);
                playPauseButton.setText("⏸ Pause");
            }
        }
    }

    private void skipToPreviousSong() {
        playlistController.skipToPreviousSong();
        playSelectedSong();
    }

    private void skipToNextSong() {
        playlistController.skipToNextSong();
        playSelectedSong();
    }

    private void shuffleAndPlay() {
        playlistController.shuffle();
        playSelectedSong();
    }

    private void deleteSongFromPlaylist() {
        String selectedSongName = playlistView.getSelectionModel().getSelectedItem();
        if (selectedSongName != null) {
            playlistController.removeSongByName(selectedSongName);
            playlistView.getItems().remove(selectedSongName);
        } else {
            showAlert("Error", "No song selected to delete.");
        }
    }

    private void clearPlaylist() {
        playlistController.clearPlaylist();
        playlistView.getItems().clear();
        showAlert("Success", "Playlist cleared!");
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
