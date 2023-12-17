package src;

import java.util.ArrayList;

public class A10p2<K, V> implements MyMap<K, V> { // impliment mymap interface
    public static int DEFAULT_INITIAL_CAPACITY = 4; // default values
    public static float DEFAULT_MAX_LOAD_FACTOR = 0.5f;
    public int capacity;
    public float loadFactorThreshold;
    public int size = 0;
    public ArrayList<MyMap.Entry<K, V>> table;
    

    public A10p2() { // current cap 
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public A10p2(int initialCapacity) { // constructor for intial cap
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }

    public A10p2(int initialCapacity, float loadFactorThreshold) { // 
        this.capacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.table = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) { // as long as we are not passing cap we keep going and initialize the map
            table.add(null);
            
        }
    }
    
    

    @Override
    public void clear() { // clear the contents of map and resets size to 0 
        size = 0;
        table.clear();
        for (int i = 0; i < capacity; i++) { 
            table.add(null);
        }
    }

    @Override
    public boolean containsKey(K key) { // if current map is null for fail check
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) { // iterate through table and compare values 
        for (MyMap.Entry<K, V> entry : table) {
            if (entry != null && entry.getValue().equals(value)) { 
                return true;
            }
        }
        return false;
        
    }

    @Override
    public java.util.Set<MyMap.Entry<K, V>> entrySet() { // iterate through table and if not none add to set
        java.util.Set<MyMap.Entry<K, V>> set = new java.util.HashSet<>();

        for (MyMap.Entry<K, V> entry : table) {
            if (entry != null) {
                set.add(entry);
            }
        }

        return set;
    }

    @Override
    public V get(K key) { // if request for value, look for key in map and get value
        int index = hash(key.hashCode());

        while (table.get(index) != null) {
            if (table.get(index).getKey().equals(key)) {
                return table.get(index).getValue();
                
            }
            index = (index + 1) % capacity;
        }

        return null;
    }

    @Override
    public boolean isEmpty() { // check if current size is 0 after clear
    	
        return size == 0;
    }

    @Override
    public java.util.Set<K> keySet() { // add non-null values to set after iterating through table
        java.util.Set<K> set = new java.util.HashSet<>();

        for (MyMap.Entry<K, V> entry : table) {
            if (entry != null) {
                set.add(entry.getKey());
            }
        }

        return set;
    }

    @Override
    
    public V put(K key, V value) {  // adds entreis to the map by using addressing
        int index = hash(key.hashCode());

        while (table.get(index) != null) {
            if (table.get(index).getKey().equals(key)) { // if key exists upddate it and return old vlaue
                Entry<K, V> entry = table.get(index);
                V oldValue = entry.getValue();
                entry.value = value;
                table.set(index, entry);
                return oldValue;
            }
            index = (index + 1) % capacity;
        }

        table.set(index, new MyMap.Entry<>(key, value));
        
        size++;

        if (size >= capacity * loadFactorThreshold) { // if load vfacotr exceeds start rehash
        	if (capacity == DEFAULT_MAX_LOAD_FACTOR)
				throw new RuntimeException("Exceeding maximum capacity");
            rehash();
        }

        return value;
    }

    
    @Override
    public void remove(K key) { // remove keys oin requests
        int index = hash(key.hashCode());

        while (table.get(index) != null) {
            if (table.get(index).getKey().equals(key)) {
            	
                table.set(index, null);
                size--;
                return;
            }
            index = (index + 1) % capacity;
        }
    }

    @Override
    public int size() { // return size
    	
        return size;
    }

    @Override
    public java.util.Set<V> values() {
        java.util.Set<V> set = new java.util.HashSet<>();

        for (MyMap.Entry<K, V> entry : table) {
            if (entry != null) {
                set.add(entry.getValue());
            }
        }

        return set;
    }

    
    public int hash(int hashCode) {
        return supplementalHash(hashCode) % capacity;
    }

    
    public static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    public void rehash() { // extend size of map and rehash 
        java.util.Set<MyMap.Entry<K, V>> set = entrySet();
        capacity <<= 1;
        size = 0;
        
        table.clear();
        

        for (int i = 0; i < capacity; i++) {
            table.add(null);
        }

        for (MyMap.Entry<K, V> entry : set) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
