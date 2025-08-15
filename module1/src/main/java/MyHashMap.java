public class MyHashMap <K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int capacity;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
    }

    public V put(K key, V value) {
        if (size >= capacity * loadFactor) {
            resize();
        }

        int index = key.hashCode() & (capacity - 1);;
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.key.equals(key)) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            node = node.next;
        }

        table[index] = new Node<>(key, value, table[index]);
        size++;

        return null;
    }

    public V get(K key) {
        int index = key.hashCode() & (capacity - 1);;
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = key.hashCode() & (capacity - 1);;
        Node<K, V> node = table[index];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.key.equals(key)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node.value;
            }
            prev = node;
            node = node.next;
        }

        return null;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        capacity = capacity * 2;
        table = new Node[capacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = node.key.hashCode() & (capacity - 1);;
                node.next = table[newIndex];
                table[newIndex] = node;
                node = next;
            }
        }
    }
}
