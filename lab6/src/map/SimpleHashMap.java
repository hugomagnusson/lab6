package map;

public class SimpleHashMap<K, V> implements Map<K, V> {
	private Entry[] table;
	private double loadFac;
	private int capacity;
	private int size;

	/**
	 * Constructs an empty hashmap with the default initial capacity (16) and the
	 * default load factor (0.75).
	 */
	public SimpleHashMap() {
		capacity = 16;
		table = (Entry<K, V>[]) new Entry[capacity];
		loadFac = 0.75;
		size = 0;
	}

	/**
	 * Constructs an empty hashmap with the specified initial capacity and the
	 * default load factor (0.75).
	 */
	public SimpleHashMap(int capacity) {
		this.capacity = capacity;
		table = (Entry<K, V>[]) new Entry[capacity];
		loadFac = 0.75;
		size = 0;
	}

	@Override
	public V get(Object arg0) {
		K key = (K) arg0;
		int index = index(key);

		Entry<K, V> found = find(index, key);

		if (found == null) {
			return null;
		} else {
			return found.getValue();
		}
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null)
				return false;
		}
		return true;
	}

	@Override
	public V put(K arg0, V arg1) {
		if ((double) (size + 1) / capacity > loadFac) {
			rehash();
		}
		int index = index(arg0);

		Entry<K, V> found = find(index, arg0);

		if (found == null) {
			Entry<K, V> temp = table[index];
			if (temp != null) {

				while (temp.next != null) {
					temp = temp.next;

				}
				temp.next = new Entry<K, V>(arg0, arg1);
				size++;
				return null;
			} else {

				table[index] = new Entry<K, V>(arg0, arg1);
				size++;
				return null;
			}

		} else {
			return found.setValue(arg1);
		}
	}

	@Override
	public V remove(Object arg0) {
		//K key = ((Entry<K, V>) arg0).getKey();
		K key = (K) arg0;
		int index = index(key);
		Entry<K, V> rem = find(index, key);
		if (rem == null) {
			return null;
		}
		Entry<K, V> temp = table[index];
		if (!temp.getKey().equals(key)) {
			while (!temp.next.getKey().equals(key)) {
				temp = temp.next;
			}
			V val = temp.next.getValue();
			if (temp.next.next != null) {
				temp.next = temp.next.next;
			} else {
				temp.next = null;
			}
			return val;
		} else {
			V val = temp.getValue();
			table[index] = null;
			return val;
		}

	}

	// gör en lista med alla element i tabellen och håll reda på storleken
	// gör tabellen dubbelt så stor och öka capacity
	// sätt in alla elementen från listan i den nya tabellen med put()
	private void rehash() {
		Entry<K, V>[] tempList = (Entry<K, V>[]) new Entry[capacity];
		int index = 0;
		for (int i = 0; i < table.length; i++) {
			Entry<K, V> temp = table[i];

			while (temp != null) {
				tempList[index] = temp;
				index++;
				temp = temp.next;
			}
		}

		table = (Entry<K, V>[]) new Entry[capacity * 2];
		capacity = capacity * 2;
		for (int i = 0; i < index; i++) {
			put((K) tempList[i].getKey(), (V) tempList[i].getValue());
		}
	}

	@Override
	public int size() {
		int nbr = 0;
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				nbr += size(table[i]);
			}
		}
		return nbr;

	}

	private int index(K key) {
		int index = Math.abs(key.hashCode() % capacity);
		return index;
	}

	// searches list for key. if found, returns entry, else returns null
	private Entry<K, V> find(int index, K key) {
		if (index < 0 || index >= table.length) {
			return null;
		}
		Entry<K, V> temp = table[index];
		while (temp != null) {
			if (temp.getKey().equals(key)) {
				return temp;
			}
			temp = temp.next;
		}
		return null;
	}

	private int size(Entry<K, V> n) {
		if (n.next != null) {
			return size(n.next) + 1;
		}
		return 1;
	}

	public String show() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < table.length; i++) {
			sb.append(i + "\t");
			show(table[i], sb);
		}
		return null;

	}

	private void show(Entry<K, V> n, StringBuilder s) {
		if (n.next != null) {
			show(n.next, s);
			s.append(n.toString() + " ");
		}
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;
		public Entry<K, V> next;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}

		public String toString() {
			return key + " = " + value;
		}

	}
}
