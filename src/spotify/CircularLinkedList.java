package spotify;

import java.util.ArrayList;
import java.util.List;

public class CircularLinkedList {
    private Node head;
    private Node tail;

    public CircularLinkedList() {
        this.head = null;
        this.tail = null;
    }

    // Add a song to the playlist
    public void addSong(Song song) {
        Node newNode = new Node(song);
        if (head == null) { // List is empty
            head = newNode;
            tail = newNode;
            newNode.setNext(head);  // Circular reference
            newNode.setPrev(tail);  // Circular reference
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
            tail.setNext(head);  // Circular reference
            head.setPrev(tail);  // Circular reference
        }
    }

    // Get the head of the list
    public Node getHead() {
        return head;
    }

    // Shuffle the playlist
    public void shuffle() {
        if (head == null || head.getNext() == head) {
            return; // No shuffling needed for an empty or single-item list
        }

        List<Node> nodes = new ArrayList<>();
        Node current = head;
        do {
            nodes.add(current);
            current = current.getNext();
        } while (current != head);

        // Shuffle the nodes
        java.util.Collections.shuffle(nodes);

        // Rebuild the circular linked list
        head = nodes.get(0);
        Node previous = head;

        for (int i = 1; i < nodes.size(); i++) {
            Node currentNode = nodes.get(i);
            previous.setNext(currentNode);
            currentNode.setPrev(previous);
            previous = currentNode;
        }

        previous.setNext(head);
        head.setPrev(previous);
        tail = previous;
    }

    // Find a song by name
    public Node findSongNodeByName(String name) {
        Node temp = head;
        if (temp != null) {
            do {
                if (temp.getSong().getName().equalsIgnoreCase(name)) {
                    return temp;
                }
                temp = temp.getNext();
            } while (temp != head);
        }
        return null; // Song not found
    }

    // Remove a song node
    public void removeSongNode(Node nodeToRemove) {
        if (nodeToRemove == null || head == null) {
            return; // Nothing to remove
        }

        if (nodeToRemove == head && nodeToRemove == tail) {
            head = null;
            tail = null; // List becomes empty
        } else if (nodeToRemove == head) {
            head = head.getNext();
            tail.setNext(head);
            head.setPrev(tail);
        } else if (nodeToRemove == tail) {
            tail = tail.getPrev();
            tail.setNext(head);
            head.setPrev(tail);
        } else {
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
        }
    }

    // Get all songs as a list
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        Node temp = head;
        if (temp != null) {
            do {
                songs.add(temp.getSong());
                temp = temp.getNext();
            } while (temp != head);
        }
        return songs;
    }

    // Clear the playlist
    public void clear() {
        if (head != null) {
            Node temp = head;
            do {
                Node nextNode = temp.getNext(); // Get the next node before unlinking
                temp.setNext(null);  // Unlink the current node from the next
                temp.setPrev(null);  // Unlink the current node from the previous
                temp = nextNode;     // Move to the next node
            } while (temp != head);

            head = null;
            tail = null; // Playlist is now cleared
        }
    }

    // Print all songs (for debugging purposes)
    public void printSongs() {
        Node temp = head;
        if (temp != null) {
            do {
                System.out.println(temp.getSong().getName());
                temp = temp.getNext();
            } while (temp != head);
        }
    }

    // Check if the playlist is empty
    public boolean isEmpty() {
        return head == null;
    }
}
