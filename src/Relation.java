import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by FW.
 */
public class Relation{
    public int[] A = new int[26];
    public Set<Set<String>> powerSetList = new HashSet<Set<String>>();
    public Iterator<Set<String>> it = this.powerSetList.iterator();
    public Relation(String in_r){
        char[] CharInr = in_r.toCharArray();
        for(char c: CharInr) {
            this.A[c - 'A'] = 1;
        }
        this.updatePowerSet();
    }

    public static <String> Set<Set<String>> powerSet(Set<String> originalSet) {
        Set<Set<String>> sets = new HashSet<Set<String>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<String>());
            return sets;
        }
        List<String> list = new ArrayList<String>(originalSet);
        String head = list.get(0);
        Set<String> rest = new HashSet<String>(list.subList(1, list.size()));
        for (Set<String> set : powerSet(rest)) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    // Update the current powerSet corresponding to the Relation
    public void updatePowerSet(){
        String[] attr = this.toString().split("");
        Set<String> mySet = new HashSet<String>(Arrays.asList(attr));
        this.powerSetList = this.powerSet(mySet);
        this.it = this.powerSetList.iterator();
    }
    public String toString(){
        String r = "";
        for (int i=0;i<26;i++){
            if (this.A[i]==1){
                r += (char)('A'+i);
            }
        }
        return r;
    }

    public boolean equals(Relation r2){
        for (int i=0;i<26;i++){
            if(this.A[i]!=r2.A[i])
                return false;
        }
        return true;
    }

    public boolean contains(char c){
        if(this.A[c - 'A']==1){
            return true;
        }
        return false;
    }

    public boolean subset(Relation r2){
        for(int i=0;i<26;i++){
            if (r2.A[i]==1){
                if (this.A[i]!=1){
                    return false;
                }
            }
        }
        return true;
    }

    public Relation powerSetFirst(){
        Set<String> first = new HashSet<String>();
        if(!this.powerSetList.isEmpty()){
            first=this.it.next();
        }
        if(this.it.hasNext()) {
            first=this.it.next();
        }
        String attr = "";
        for (String s :first){
            attr += s;
        }
        Relation r = new Relation(attr);
        return r;
    }

    public Relation powerSetNext(){
        if (this.it.hasNext()){
            Set<String> first = this.it.next();
            String attr = "";
            for (String s :first){
                attr += s;
            }
            Relation r = new Relation(attr);
            return r;
        }else
            return null;
    }

    public Relation union(Relation r2){
        Relation r = new Relation("");
        for(int i=0;i<26;i++){
            if(this.A[i]==1 || r2.A[i] ==1){
                r.A[i]=1;
            }
        }
        r.updatePowerSet();
        return r;
    }

    public Relation minus(Relation r2){
        Relation r = new Relation(this.toString());
        for(int i=0;i<26;i++){
            if(this.A[i]==1 && r2.A[i] ==1){
                r.A[i]=0;
            }
        }
        r.updatePowerSet();
        return r;
    }

    public Relation intersect(Relation r2){
        Relation r = new Relation("");
        for(int i=0;i<26;i++){
            if(this.A[i]==1 && r2.A[i] ==1){
                r.A[i]=1;
            }
        }
        r.updatePowerSet();
        return r;
    }

    public static void main(String[] args){
        Relation r = new Relation("ABCDE");
        Relation r2 = new Relation("CD");
        System.out.println(r.powerSetList);

//       System.out.println(r.powerSetList);
//
//        Relation rF= r.powerSetFirst();
//        Relation rN= r.powerSetNext();
//        System.out.println(rN.toString());
//
//        String[] s=r.toString().split("");
//        for (int i=0;i<s.length;i++)
//           System.out.println(s[i]);
//
//        System.out.println(r.minus(r2));
    }


}