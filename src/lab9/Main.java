package lab9;

import java.io.*;
import java.util.*;

public class Main
{
    public static ArrayList<ArrayList<Integer>> g;
    public static HashMap<String, Integer> norm;
    public static HashMap<Integer, String> unnorm;
    public static int cnt;
    public static void ReadIn(String Path)
    {
        cnt = 0;
        g = new ArrayList<ArrayList<Integer>>();
        norm = new HashMap<String, Integer>();
        unnorm = new HashMap<Integer, String>();
        File f = new File(Path);
        if(!f.exists() || !f.isFile())
        {
            System.out.println("Error reading file");
            System.exit(1);
        }
        try
        {
            FileInputStream fileInputStream = new FileInputStream(f);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String lineContent = "";
            while ((lineContent = reader.readLine()) != null)
            {
                String[] gates = lineContent.split(" ");
                if(gates.length <= 1)continue;
                int last = -1;
                for(int i = 1; i < gates.length;i++)
                {
                    String x = gates[i];
                    if(!norm.containsKey(x))
                    {
                        norm.put(x, cnt);
                        unnorm.put(cnt, x);
                        ++cnt;
                        g.add(new ArrayList<Integer>());
                    }
                    if(last != -1)
                    {
                        g.get(last).add(norm.get(x));
                        g.get(norm.get(x)).add(last);
                    }
                    last = norm.get(x);
                }
            }
            fileInputStream.close();
            inputStreamReader.close();
            reader.close();
            return;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static int[] dist;
    public static int[] upd;
    public static boolean[] inqueue;
    public static final int INF = 0x23333333;//infinity
    public static void SPFA(int s)
    {
        for(int i=0;i<cnt;i++)
        {
            dist[i] = INF;
            upd[i] = -1;
            inqueue[i] = false;
        }
        dist[s] = 0;
        Queue<Integer> q = new LinkedList<Integer>();
        q.offer(s);
        while(!q.isEmpty())
        {
            int f = q.poll();
            for(int x: g.get(f))
            {
                if(dist[x] > dist[f] + 1)
                {
                    dist[x] = dist[f] + 1;
                    upd[x] = f;
                    if(!inqueue[x])
                    {
                        inqueue[x] = true;
                        q.offer(x);
                    }
                }
            }
        }
    }
    public static void main(String[] args)
    {
        ReadIn("data.txt");
        dist = new int[cnt];
        upd = new int[cnt];
        inqueue = new boolean[cnt];
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.print("请输入起点站: ");
            String frm = sc.next();
            if(!norm.containsKey(frm))
            {
                System.out.println("您输入的起点站不是一个地铁站");
                continue;
            }
            System.out.print("请输入终点站: ");
            String dest = sc.next();
            if(!norm.containsKey(dest))
            {
                System.out.println("您输入的终点站不是一个地铁站");
                continue;
            }
            if(frm.equals( dest))
            {
                System.out.println("您输入的起点站和终点站是一个地铁站，不用坐地铁");
                continue;
            }
            int frmi = norm.get(frm);
            int to = norm.get(dest);
            SPFA(frmi);
            if(dist[to] >= INF)
            {
                System.out.println("您输入的起点站和终点站地铁不连通");
                continue;
            }
            int c = to;
            Stack<Integer> sta = new Stack<Integer>();
            while(upd[c] != -1)
            {
                sta.push(c);
                c = upd[c];
            }
            sta.push(frmi);
            while(!sta.empty())
            {
                int t = sta.pop();
                System.out.print(unnorm.get(t));
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

}
