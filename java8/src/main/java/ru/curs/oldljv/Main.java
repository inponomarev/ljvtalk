package ru.curs.oldljv;

import org.atpfivt.ljv.LJV;
import org.reflections.ReflectionUtils;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static void visualize(LJV ljv, Object o) throws IOException, URISyntaxException {
        String dot = URLEncoder.encode(ljv.drawGraph(o), "UTF8")
                .replaceAll("\\+", "%20");

        Desktop.getDesktop().browse(
                new URI("https://dreampuf.github.io/GraphvizOnline/#"
                        + dot));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
         //   hm1();
        //hm2(6);
        // hm2(11);
        // hm3(11);
        lhm();
    }


    private static String redBlackForHM(Object o) {
        Set<Field> redFields = ReflectionUtils.getAllFields(o.getClass(),
                f -> "red" .equals(f.getName())
                        && f.getType().equals(boolean.class));
        if (redFields.isEmpty()) {
            return "";
        } else {
            Field redField = redFields.iterator().next();
            redField.setAccessible(true);
            try {
                boolean b = redField.getBoolean(o);
                return b ? "color=red" : "color=black";
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static void hm3(int len) throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .addIgnoreField("value")
                .addIgnoreField("red")
                .addFieldAttribute("prev", "constraint=false,color=green")
                .addFieldAttribute("next", "constraint=false,color=green")
                .addObjectAttributesProvider(Main::redBlackForHM);

        List<String> collisions = new HashCodeCollision().genCollisionString(7);
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            map.put(collisions.get(i), i);
        }
        visualize(ljv, map);
    }


    static void hm2(int len) throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .addIgnoreField("value")
                .addFieldAttribute("prev", "constraint=false,color=green")
                .addFieldAttribute("next", "constraint=false,color=green");

        List<String> collisions = new HashCodeCollision().genCollisionString(7);
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            map.put(collisions.get(i), i);
        }
        visualize(ljv, map);
    }

    static void lhm() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .addFieldAttribute("after", "constraint=false,color=green")
                .addFieldAttribute("before", "constraint=false,color=green")
                .addFieldAttribute("head", "constraint=false,color=green")
                .addFieldAttribute("tail", "constraint=false,color=green")
                ;

        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        visualize(ljv, map);
    }

    static void hm1() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class);

        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        visualize(ljv, map);
    }

    static void arrayDeque() throws IOException, URISyntaxException {
        LJV ljv = new LJV().setTreatAsPrimitive(Integer.class).highlightChangingArrayElements();
        //note that this sets initial capacity to 5!
        Deque<Integer> arrayDeque = new ArrayDeque<>(4);
        arrayDeque.add(1);
        arrayDeque.add(2);
        arrayDeque.add(3);
        visualize(ljv, arrayDeque);

        arrayDeque.poll(); //returns 1
        arrayDeque.poll(); //returns 2
        visualize(ljv, arrayDeque);

        arrayDeque.add(4);
        arrayDeque.add(5);
        arrayDeque.add(6);
        visualize(ljv, arrayDeque);
    }

    static void priorityQueue() throws IOException, URISyntaxException {
        List<Integer> list = IntStream.range(0, 16).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        visualize(new LJV().setTreatAsPrimitive(Integer.class), list.toArray());

        LJV ljv = new LJV().setTreatAsPrimitive(Integer.class)
                .setIgnoreNullValuedFields(true)
                .highlightChangingArrayElements();
        PriorityQueue<Integer> q = new PriorityQueue<>(16);
        q.addAll(list);
        visualize(ljv, q);

        q.poll();
        visualize(ljv, q);

        q.poll();
        visualize(ljv, q);

        q.poll();
        visualize(ljv, q);

        q.add(1);
        visualize(ljv, q);
    }
}
