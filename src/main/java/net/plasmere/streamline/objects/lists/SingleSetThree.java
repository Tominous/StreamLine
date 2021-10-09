package net.plasmere.streamline.objects.lists;

public class SingleSetThree<K, V, C> {
    public K key;
    public V middle;
    public C last;

    public SingleSetThree(K key, V middle, C last){
        this.key = key;
        this.middle = middle;
        this.last = last;
    }

    public void updateKey(K key){
        this.key = key;
    }

    public void updateValue(V middle){
        this.middle = middle;
    }

    public void updateLast(C last) {
        this.last = last;
    }
}
