import java.util.*;

/**
 * Created by FW.
 */
public class RelList {
    public List<Relation> list = new ArrayList<Relation>();
    public Iterator<Relation> it = this.list.iterator();
    public RelList(){
        this.list = new ArrayList<Relation>();
    }
    public String toString(){
        String s="";
        for(Relation r: this.list){
            s+=r.toString();
            s+="\n";
        }
        return s;
    }
    public void insert(Relation r){
        if(!this.list.contains(r))
            this.list.add(r);
    }
    public Relation getFirst(){
        this.it = this.list.iterator();
        if(this.it.hasNext()) {
            Relation first=this.it.next();
            return first;
        }
        return null;
    }
    public Relation getNext(){
        if (this.it.hasNext()){
            Relation next = this.it.next();
            return next;
        }else
            return null;
    }
}
