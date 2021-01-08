package ru.curs.oldljv;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void visualize(Object o) throws IOException, URISyntaxException {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        LJV.defaultContext.showPackageNamesInClasses = false;
        LJV.generateDOT(LJV.defaultContext, o, pw);
        String dot = URLEncoder.encode(writer.toString(), "UTF8")
                .replaceAll("\\+", "%20");
        Desktop.getDesktop().browse(
                new URI("https://dreampuf.github.io/GraphvizOnline/#"
                        + dot));
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        chm();
        //newString();

    }

    private static void chm() throws IOException, URISyntaxException {
        LJV.defaultContext.treatAsPrimitive(Integer.class);
        LJV.defaultContext.treatAsPrimitive(String.class);
        Map<String, Integer> map =
                new ConcurrentHashMap<String, Integer>(
                        /*initialCapacity:*/16 ,
                        /*loadFactor:*/0.75f,
                        /*concurrencyLevel:*/8);

        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        visualize(map);
    }

    private static void newString() throws IOException, URISyntaxException {
        /*String[] s = new String[]{"f5a5a608", "abc"};
        System.out.println(s[0].hashCode()); //0
        System.out.println(s[1].hashCode()); //0
        */
        String x = "Hello";
        String[] s = new String[]{
                x, new String(x),
                new String(x.toCharArray()),
                x + "",
                x.concat("")};
        visualize(s);
    }
}
