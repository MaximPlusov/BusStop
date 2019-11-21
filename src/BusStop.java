import java.io.*;
import java.util.*;

public class BusStop {
    public static void main(String[] args) throws Exception{
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        TreeSet<Record> set1 = new TreeSet(new RecordComparator1());
        String str;
        while(( str = in.readLine()) != null){
            Record  r = new Record(str);
            if(r.correct()){
                set1.add(r);
            }
        }
        TreeSet<Record> set2 = new TreeSet(new RecordComparator2());
        while(!set1.isEmpty()){
            set2.add(set1.pollFirst());
        }
        PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
        for(Record r : set2){
            if(r.company.equals("Posh")){
                out.println(r);
            }
        }
        out.println();
        for(Record r : set2){
            if(r.company.equals("Grotty")){
                out.println(r);
            }
        }
        out.close();
    }
}

class Time {
    int hour;
    int minute;
    Time(String s){
        hour = new Integer(s.substring(0,2));
        minute = new Integer(s.substring(3,5));
    }
    @Override
    public String toString(){
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }
    int inMinute(){
        return hour * 60 + minute;
    }
}

class Record {
    Time first;
    Time second;
    String company;
    int time(){
        return second.inMinute() - first.inMinute();
    }
    Record(String s){
        StringTokenizer st = new StringTokenizer(s);
        company = st.nextToken();
        first = new Time(st.nextToken());
        second = new Time(st.nextToken());
        if(second.inMinute() < first.inMinute()){
            second.hour += 24;
        }
    }
    public boolean correct(){
        return time() <= 60;
    }
    @Override
    public String toString(){
        String s;
        if(second.hour < 24){
            s  = company + " " + first + " " + second;
        }
        else{
            second.hour -= 24;
            s  = company + " " + first + " " + second;
            second.hour += 24;
        }
        return s;
    }
    public boolean better(Record record){
        if(time() == record.time() && first.inMinute() == record.first.inMinute()){
            if(company.equals(record.company)){
                return true;
            }
            return record.company.equals("Grotty");
        }
        if(time() < record.time() && second.inMinute() <= record.second.inMinute() && first.inMinute() >= record.first.inMinute()){
            return true;
        }
        return false;
    }
}

class RecordComparator1 implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Record r1 = (Record)o1;
        Record r2 = (Record)o2;
        if(r2.better(r1)){
            return 0;
        }
        int time = r1.time() - r2.time();
        if(time == 0){
            if(r1.first.inMinute() == r2.first.inMinute()){
                return -1;
            }
            return r1.first.inMinute() - r2.first.inMinute();
        }
        return time;
    }
}

class RecordComparator2 implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        Record r1 = (Record)o1;
        Record r2 = (Record)o2;
        if(r2.better(r1)){
            return 0;
        }
        int time = r1.first.inMinute() - r2.first.inMinute();
        if(time == 0){
            if(r1.second.equals(r2.second)){
                if(r1.company.equals(r2.company)){
                    return 0;
                }
                if(r1.company.equals("Posh")){
                    return 1;
                }
                return -1;
            }
            return -1;
        }
        return time;
    }
}