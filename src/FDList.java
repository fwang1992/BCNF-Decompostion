import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by FW.
 */

public class FDList {
    public List<Fd> list = new ArrayList<Fd>();
    public Iterator<Fd> it = this.list.iterator();
    public FDList(){
        this.list = new ArrayList<Fd>();
    }
    public String toString(){
        String s="";
        for(Fd f: this.list){
            s+=f.toString();
            s+="\n";
        }
        return s;
    }
    public void insert(Fd f){
        if(!this.list.contains(f))
            this.list.add(f);
      //  this.nonTrivialFd();
    }
    public Fd getFirst(){
        this.it = this.list.iterator();
        if(this.it.hasNext()) {
            Fd first=this.it.next();
            return first;
        }
        return null;

    }

    public Fd getNext(){
        if (this.it.hasNext()){
            Fd next = this.it.next();
            return next;
        }else
            return null;
    }
    public boolean inLhs(Relation r){
        for (Fd f:this.list){
            if(f.getLHS().toString().length()<r.toString().length())
                return true;
        }
        return false;
    }

    public Relation closure(Relation r){
        Relation closure = new Relation(r.toString());
        while(true){
            Relation r2 = closure;
            for (Fd f:this.list){
                if(closure.subset(f.getLHS())) closure=closure.union(f.getRHS());
            }
            if (r2.equals(closure)){
                Relation res=new Relation(closure.toString());
                return res;
            }
        }
    }

    public void nonTrivialFd(){
        while(true){
            boolean fdFound = false;
            ArrayList<Fd> toAdd = new ArrayList<>();
            for (Fd f1: this.list){
                for (Fd f2: this.list){
                    if(f1.equals(f2)) continue;
                    Fd sum = f1.combine(f2);
                    if (!this.list.contains(sum) && !toAdd.contains(sum)){
                        toAdd.add(sum);
                        fdFound = true;
                    }
                }
            }
            this.list.addAll(toAdd);
            if (!fdFound) break;
        }
        // remove trivial dependencies
        ArrayList<Fd> toRemove = new ArrayList<>();
        for (Fd f1: this.list) {
            if (f1.rhs.isEmpty() || f1.lhs.isEmpty()) toRemove.add(f1);
            for (Fd f2 : this.list) {
                if (f1.equals(f2)) continue;
                if (f1.lhs.equals(f2.lhs) && f1.rhs.size() > f2.rhs.size() && !toRemove.contains(f2)) toRemove.add(f2);
                /*
                else if (f2.lhs.containsAll(f1.lhs) && f1.rhs.containsAll(f2.rhs) && !toRemove.contains(f2)){
                    toRemove.add(f2);
                }
                */
            }
        }
        this.list.removeAll(toRemove);
    }

/*
    public static void main(String[] args){
        Relation r0 = new Relation("ABCDE");
        Relation r1 = new Relation("AB");
        Relation r2 = new Relation("C");
        Relation r3 = new Relation("D");
        Relation r4 = new Relation("BE");
        Fd f1 = new Fd(r1,r2);
        Fd f2 = new Fd(r2,r3);
        Fd f3 = new Fd(r3,r4);
        FDList fdlist = new FDList();
        fdlist.insert(f1);
        fdlist.insert(f2);
        fdlist.insert(f3);
        System.out.println(fdlist.getFirst());
        System.out.println(fdlist.getNext());
        System.out.println(fdlist.getFirst());
        //System.out.println(fdlist);
        //Relation xx=fdlist.closure(r1);
        //System.out.println(xx);
        //System.out.println(f3.BCNFviolation((r0)));
        //System.out.println(fdlist);
    }

*/
}
