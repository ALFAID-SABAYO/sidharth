package spotify;
public class Node {
    private Song data; // The song data for this node
    private Node next; // Reference to the next node
    private Node prev; // Reference to the previous node

    public Node(Song data) {
        this.data = data;  // Initialize the song data
    }

    // Getter for the song data
    public Song getSong() {
        return data;  // Return the song stored in this node
    }

    public void setSong(Song song) {
        this.data = song;  // Set the song data for this node
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
