import java.util.*;
import java.io.File;
import java.io.*;
import java.util.Scanner;


/**
 * Created by FW.
 */

public class driver {
    public static ArrayList<Fd> getBCNFViolations(String r,FDList F){
        ArrayList<Fd> BCNFViolations = new ArrayList<>();
        List<Fd> fdList = F.list;
        Relation ri = new Relation (r);
        for (Fd cd: fdList){
            if (cd.BCNFviolation(ri)) BCNFViolations.add(cd);
        }
        return BCNFViolations;
    }
    public static String setToString(Set<String> set){
        String R = "";
        for (String c: set){
            R += c;
        }
        return R;
    }

    public static String missingRHS(Set<String> rhs, String R){
        for (String c: rhs){
            R = R.replace("" + c, "");
        }
        return R;
    }

    public static void main(String[] args) throws Exception{
        Stack<Relation> s = new Stack<Relation>();
        FDList fdlist=new FDList();
        RelList db=new RelList();
        boolean first=true;
        Relation r=null;

        //Read the input file
        File myFile=new File("test3.txt");

        /*--------------------------------------- code used to store System.out to file-----------------------
        PrintStream o = new PrintStream(new File("testResult\\test1Result.txt"));
        // Store current System.out before assigning a new value
        PrintStream console = System.out;

        // Assign o to output stream
        System.setOut(o);
        */

        Scanner sc=new Scanner(myFile);
        while (sc.hasNextLine()){
            String s1=sc.nextLine();
            if (first)
                r=new Relation(s1.replace(" ",""));
            else{
                int pos=s1.indexOf("->");
                if (pos>0){
                    String lhs=s1.substring(0, pos).replace(" ", "");
                    String rhs=s1.substring(pos+2, s1.length()).replace(" ", "");
                    fdlist.insert(new Fd(new Relation(lhs),new Relation(rhs)));
                }
            }
            first=false;
        }
        sc.close();


        System.out.println("-------------File Read----------------");
        System.out.println(r);
        System.out.println(fdlist);
        System.out.println("--------------------------------------");

        // compute complete list of functional dependencies
        FDList completeF=new FDList();

        Relation new_rel=r.powerSetFirst();
        while (new_rel!=null){
            Relation clos=fdlist.closure(new_rel);
            if (!new_rel.equals(clos) && !clos.toString().isEmpty()){
                completeF.insert(new Fd(new_rel,clos.minus(new_rel)));
            }
            new_rel = r.powerSetNext();
        }



//        fdlist.nonTrivialFd();
//        final FDList completeF=fdlist;
        System.out.println("-------------- Non-trivial Functional Dependencies------------------------");
        System.out.println(completeF);
        System.out.println("--------------------------------------------------------------------------");

        // compute superkeys
        FDList super_keys=new FDList();
        Fd my_fd=completeF.getFirst();
        while(my_fd != null){
            Relation lr=my_fd.getLHS().union(my_fd.getRHS());
            if (r.equals(lr)){
                super_keys.insert(my_fd);
                System.out.println("superkey: "+my_fd);
            }
            my_fd=completeF.getNext();
        }

        // compute keys
        FDList keys=new FDList();
        my_fd=super_keys.getFirst();
        while(my_fd != null){
            boolean minimal=true;
            Relation lh=my_fd.getLHS();
            if (super_keys.inLhs(lh)){
                minimal=false;
            }
            if (minimal)
                System.out.println("Key: "+my_fd);
            my_fd=super_keys.getNext();
        }



        // This block of codes provide another driver to do BCNF decomposition
/*
        ArrayList<String> decomp = new ArrayList<>();
        Stack<String> S = new Stack<>();
        String R = r.toString();
        S.push(R);
        while(!S.isEmpty()){
            String A = S.pop();
            ArrayList<Fd> violations = getBCNFViolations(A,completeF);
            if (violations.isEmpty()){
                if (!decomp.contains(A)) decomp.add(A);
            } else {
                System.out.print("BCNF violation " + violations.get(0).lhs+"->"+ violations.get(0).rhs+ " of " + A + ": ");
                String union = setToString(violations.get(0).setUnion());
                if (!S.contains(union) && !decomp.contains(union)) S.push(union);

                String missing = missingRHS(violations.get(0).rhs, A);
                if (!S.contains(missing) && !decomp.contains(missing)) S.push(missing);
                System.out.println("[" + union + ", " + missing + "]");
            }
        }
        System.out.println(decomp);



*/




        boolean violation=false;
        Fd f=completeF.getFirst();
        do{
            if (f.BCNFviolation(r)){
                System.out.println("BCNF violation: "+f);
            }
           // f=completeF.getNext();
        }while((f=completeF.getNext())!=null);

        // BCNF DECOMPOSITION:
        s.push(r);
        while (!s.isEmpty()){
            Relation a=s.pop();
            violation=false;
            f=completeF.getFirst();
            while(!violation && f!=null){
                if (f.BCNFviolation(a))
                    violation=true;
                else
                    f=completeF.getNext();
            }
            if (!violation){
                System.out.println(a);
                System.out.println("--- inserted --- ("+a+")");
                db.insert(a);
            }
            else if (f!=null){
                String x=(f.getLHS()).toString();
                Relation rhs=new Relation(((f.getRHS()).intersect(a)).toString());
                String y=rhs.toString();
                Relation xy = new Relation(x+y);
                s.push(xy);
                s.push(a.minus(f.getRHS()));
                System.out.println("--------------------------");
                System.out.println("R="+a);
                System.out.println(f.getLHS()+"->"+rhs);
                System.out.println("--------------------------");
                System.out.println(s);
            }
        }

        System.out.println("------------- Tables in BCNF ----------------");
        System.out.println(db);
        System.out.println("---------------------------------------------");

/*----------------code used to store System.out to file ------------------------

        // Use stored value for output stream
        System.setOut(console);
*/
    }

}

