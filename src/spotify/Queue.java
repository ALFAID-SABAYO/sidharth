package spotify;
public class Queue {
    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = rear = null;
        size = 0;
    }

    public void enqueue(Song song) {
        Node newNode = new Node(song);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public Song dequeue() {
        if (front == null) return null;
        Song song = front.song;
        front = front.next;
        size--;
        return song;
    }

    private static class Node {
        Song song;
        Node next;

        Node(Song song) {
            this.song = song;
            this.next = null;
        }
    }
}
