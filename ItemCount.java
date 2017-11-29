import java.util.Comparator;

/**
 * Created by Yu Fang on 26/11/2015.
 */
public class ItemCount implements Comparable<ItemCount> {
    private Object obj;
    private int num;

    public ItemCount(Object object, int number){
        this.obj=object;
        this.num=number;
    }

    public ItemCount() {
        super();
    }

    public Object getObject(){
        return obj;
    }

    public int getCount(){
        return num;
    }

    @Override
    public int compareTo(ItemCount o) {
        if(this.getCount() < o.getCount()){
            return 1;
        }else if(this.getCount() == o.getCount()){
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString(){
        return this.getObject().toString()+"\t"+this.getCount();
    }
}
