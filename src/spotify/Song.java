package spotify;

import java.net.URI;
import javafx.scene.media.Media;

public class Song {
    private String name;        // Name of the song
    private String artist;      // Artist of the song
    private URI audioPath;      // File path for the song's audio
    private String coverPath;   // File path for the song's cover image
    private Media media;        // Media object for playback

    // Constructor
    public Song(String name, String artist, URI audioPath, String coverPath) {
        this.name = (name != null && !name.isEmpty()) ? name : "Unknown"; // Default to "Unknown" if no name provided
        this.artist = (artist != null && !artist.isEmpty()) ? artist : "Unknown"; // Default to "Unknown" if no artist provided
        this.audioPath = audioPath; // Audio path must be provided
        this.coverPath = (coverPath != null && !coverPath.isEmpty()) ? coverPath : "file:default_album_cover.jpg"; // Default cover
        this.media = new Media(audioPath.toString()); // Create Media object for playback
    }

    // Getter and Setter Methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "Unknown";
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist != null ? artist : "Unknown";
    }

    public URI getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(URI audioPath) {
        this.audioPath = audioPath;
        this.media = new Media(audioPath.toString()); // Update media object when path changes
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath != null ? coverPath : "file:default_album_cover.jpg";
    }

    public Media getMedia() {
        return media;
    }

    // Debugging/Utility Method
    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", audioPath=" + audioPath +
                ", coverPath='" + coverPath + '\'' +
                '}';
    }
}
