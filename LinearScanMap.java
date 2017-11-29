import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yu Fang on 30/11/2015.
 */
public class LinearScanMap<K,V> extends AbstractMap<K,V> {
    private Set<Entry<K,V>> linearSet= new HashSet<>();

    public LinearScanMap(){
        this.linearSet= new HashSet<Entry<K,V>>();
    }

    public Set<Entry<K,V>> entrySet(){
        return linearSet;
    }

    @Override
    public V get(Object key){
        V returnValue=null;
        for (Entry entry: linearSet){
            if (entry.getKey().equals(key)){
                returnValue=((V) entry.getValue());
            }
        }
        return returnValue;
    }

    @Override
    public V put(K key, V value){
        V returnValue;

        AbstractMap.SimpleEntry newEntry= new AbstractMap.SimpleEntry<>(key, value);
        linearSet.add(newEntry);

        if (this.get(key).equals(null)){
            returnValue=null;
        }else{
            returnValue=this.get(key);
        }
        return returnValue;
    }
}
