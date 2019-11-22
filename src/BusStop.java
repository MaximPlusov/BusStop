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
        ArrayList<Record> list = new ArrayList(set1.size());
        while(!set1.isEmpty()){
            Record r = set1.pollFirst();
            boolean flag = false;
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).better(r)){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                list.add(r);
            }
        }
        Collections.sort(list,new RecordComparator2());
        PrintWriter out = new PrintWriter(new FileWriter("output.txt"));
        for(Record r : list){
            if(r.company.equals("Posh")){
                out.println(r);
            }
        }
        out.println();
        for(Record r : list){
            if(r.company.equals("Grotty")){
                out.println(r);
            }
        }
        out.close();
    }
}

class Time {
    private int hour;
    private int minute;
    int inMinute;
    Time(String s){
        hour = new Integer(s.substring(0,2));
        minute = new Integer(s.substring(3,5));
        inMinute = hour * 60 + minute;
    }
    @Override
    public String toString(){
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }
}

class Record {
    Time first;
    Time second;
    String company;
    int time;
    Record(String s){
        StringTokenizer st = new StringTokenizer(s);
        company = st.nextToken();
        first = new Time(st.nextToken());
        second = new Time(st.nextToken());
        time = second.inMinute - first.inMinute;
        if(time < 0){
            time += 24 * 60;
        }
    }
    public boolean correct(){
        return time <= 60;
    }
    @Override
    public String toString(){
        return company + " " + first + " " + second;
    }
    public boolean better(Record record){
        if(time == record.time && first.inMinute == record.first.inMinute){
            if(company.equals(record.company)){
                return true;
            }
            return record.company.equals("Grotty");
        }
        boolean n1 = second.inMinute >= first.inMinute;
        boolean n2 = record.second.inMinute >= record.first.inMinute;
        if((n1 && n2) || (!n1 && !n2)){
            if(time < record.time && first.inMinute >= record.first.inMinute && second.inMinute <= record.second.inMinute){
                return true;
            }
        }
        else if(n1 && !n2){
            if(time < record.time && first.inMinute >= record.first.inMinute){
                return true;
            }
            if(time < record.time && second.inMinute <= record.second.inMinute){
                return true;
            }
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
        int time = r1.time - r2.time;
        if(time == 0){
            if(r1.first.inMinute == r2.first.inMinute){
                return -1;
            }
            return r1.first.inMinute - r2.first.inMinute;
        }
        return time;
    }
}

class RecordComparator2 implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        Record r1 = (Record)o1;
        Record r2 = (Record)o2;
        return r1.first.inMinute - r2.first.inMinute;
    }
}