
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by FW.
 */


public class Fd{
    public Set<String> lhs,rhs;

    public Fd(Relation in_lhs, Relation in_rhs){
        String[] attr = in_lhs.toString().split("");
        this.lhs= new HashSet<String>(Arrays.asList(attr));
        attr = in_rhs.toString().split("");
        this.rhs= new HashSet<String>(Arrays.asList(attr));
    }

    public String toString(){
        String r="";
        Set<String> tmp=this.lhs;
        for (String s:tmp){
            r+=s;
        }
        r+=" -> ";
        tmp=this.rhs;
        for (String s:tmp){
            r+=s;
        }
        return r;
    }
    public Set<String> setUnion(){
        Set<String> union = new HashSet<>(this.lhs);
        union.addAll(this.rhs);
        return union;
    }
    public boolean BCNFviolation(Relation s){
        String s_lhs="";
        String s_rhs="";
        Set<String> tmp=this.lhs;
        for (String t:tmp){
            s_lhs+=t;
        }
        tmp=this.rhs;
        for (String t:tmp){
            s_rhs+=t;
        }
        Set<String> common = new HashSet<String>(this.lhs);
        common.addAll(this.rhs);
        String xy="";
        for (String t:common){
            xy += t;
        }
        String source = s.toString();
//        System.out.println("X: "+s_lhs);
//        System.out.println("Y: "+s_rhs);
//        System.out.println("XY: "+xy);
//        System.out.println("S: "+source);
        if(!checkContains(xy,source)){
            if(checkContains(source,s_lhs)){
                if(twoStrings(s_rhs,source)){
                    return true;
                }
            }
        }
        return false;
        /*
        boolean valid=false;
        for (char r:s_rhs.toCharArray()){
            if (source.contains("" + r)) valid = true;
        }
        if(!valid) return false;
        // lhs is subset of attributes
        for (char l: s_lhs.toCharArray()){
            if (!source.contains("" + l)) return false;
        }
        // all attributes must appear in Fd
        for (char a: source.toCharArray()){
            if (!s_lhs.contains(""+a) && !s_rhs.contains(""+a)) return true;
        }
        return false;
        */
    }
    public boolean checkContains(String s1,String s2){
        Set<Character> first = new HashSet<Character>();
        char[] CharInr = s1.toCharArray();
        for(char c: CharInr) {
            first.add(c);
        }
        boolean valid = true;
        CharInr = s2.toCharArray();
        for(char c: CharInr) {
            if(!first.contains(c)){
                valid = false;
            }
        }
        return valid;
    }
    public boolean twoStrings(String s1,String s2){
        // vector for storing character occurrences
        boolean v[]=new boolean[26];
        Arrays.fill(v,false);
        // increment vector index for every
        // character of str1
        for (int i = 0; i < s1.length(); i++)
            v[s1.charAt(i) - 'A'] = true;
        // checking common substring of str2 in str1
        for (int i = 0; i < s2.length(); i++)
            if (v[s2.charAt(i) - 'A'])
                return true;
        return false;
    }

    public Relation getLHS(){
        String r="";
        Set<String> tmp=this.lhs;
        for (String s:tmp){
            r+=s;
        }
        Relation res = new Relation(r);
        return res;
    }

    public Relation getRHS(){
        String r="";
        Set<String> tmp=this.rhs;
        for (String s:tmp){
            r+=s;
        }
        Relation res = new Relation(r);
        return res;
    }
    public Fd combine(Fd other){
        String r="";
        for (String s:other.lhs){
            r+=s;
        }
        Relation r1= new Relation(r);
        r="";
        for (String s:rhs){
            r+=s;
        }
        Relation r2= new Relation(r);
        Fd sum = new Fd(r1,r2);
        sum.rhs.removeAll(other.lhs);
        sum.lhs.removeAll(rhs);
        sum.lhs.addAll(lhs);
        sum.rhs.addAll(other.rhs);
        sum.rhs.removeAll(sum.lhs);
        return sum;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null || !Fd.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        Fd other = (Fd) obj;
        return other.lhs.equals(lhs) && other.rhs.equals(rhs);
    }
/* test
    public static void main(String[] args) {

        Relation r0 = new Relation("ABCDEFGH");
        Relation r1 = new Relation("BE");
        Relation r2 = new Relation("GH");
        Relation r3 = new Relation("G");
        Relation r4 = new Relation("FA");
        Relation r5 = new Relation("D");
        Relation r6 = new Relation("C");
        Relation r7 = new Relation("F");
        Relation r8 = new Relation("B");
        Relation r9 = new Relation("EGH");
        Relation r10 = new Relation("EG");
        Relation r11 =new Relation("ABFH");
        Fd f1 = new Fd(r1,r2);
        Fd f2 = new Fd(r3,r4);
        Fd f3 = new Fd(r5,r6);
        Fd f4 = new Fd(r7,r8);
        Fd f5 = new Fd(r10,r11);
        System.out.println(f5.BCNFviolation(r9));
//        Relation r0= new Relation("BD");
//        Relation r1= new Relation("C");
//        Relation r2= new Relation("BDE");
//        Fd f1 = new Fd(r1,r2);
//        System.out.println(f1.BCNFviolation(r0));
    }
*/
}