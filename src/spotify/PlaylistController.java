package spotify;
import java.util.List;

public class PlaylistController {
    private final CircularLinkedList playlist = new CircularLinkedList();
    private Node currentSongNode = null;

    // Add a song to the playlist
    public void addSong(Song song) {
        playlist.addSong(song);
        if (currentSongNode == null) { // If no song is playing, start with the first song
            currentSongNode = playlist.getHead();
        }
    }

    // Get a song by name
    public Song getSongByName(String name) {
        Node temp = playlist.getHead();
        if (temp == null) return null;

        do {
            if (temp.getSong().getName().equalsIgnoreCase(name)) {
                return temp.getSong();
            }
            temp = temp.getNext();
        } while (temp != playlist.getHead());

        return null; // Return null if the song is not found
    }

    // Get all songs in the playlist
    public void printAllSongs() {
        playlist.printSongs();
    }

    // Get current song
    public Song getCurrentSong() {
        if (currentSongNode != null) {
            return currentSongNode.getSong();
        }
        return null;
    }

    // Shuffle the playlist
    public void shuffle() {
        playlist.shuffle();
        // Reset the current song to the new head
        currentSongNode = playlist.getHead();
    }

    // Skip to the next song
    public void skipToNextSong() {
        if (currentSongNode != null && currentSongNode.getNext() != null) {
            currentSongNode = currentSongNode.getNext();
        } else {
            // If at the end, loop back to the start
            currentSongNode = playlist.getHead();
        }
    }

    // Skip to the previous song
    public void skipToPreviousSong() {
        if (currentSongNode != null && currentSongNode.getPrev() != null) {
            currentSongNode = currentSongNode.getPrev();
        } else {
            // If at the beginning, loop to the end
            currentSongNode = playlist.getTail();
        }
    }

    // Remove a song by name
    public boolean removeSongByName(String name) {
        Node songToRemove = playlist.findSongNodeByName(name);
        if (songToRemove != null) {
            // Adjust the current song node if necessary
            if (currentSongNode == songToRemove) {
                currentSongNode = currentSongNode.getNext(); // Move to the next song
            }
            playlist.removeSongNode(songToRemove);
            if (playlist.isEmpty()) {
                currentSongNode = null;
            }
            return true;
        }
        return false;
    }

    public List<Song> getAllSongs() {
        return playlist.getAllSongs(); // Call the method from the circular linked list
    }

    // Get the next song (but do not change current song)
    public Song getNextSong() {
        if (currentSongNode != null && currentSongNode.getNext() != null) {
            return currentSongNode.getNext().getSong();
        }
        return null;
    }

    // Clear the playlist
    public void clearPlaylist() {
        playlist.clear();
        currentSongNode = null;
    }
}
