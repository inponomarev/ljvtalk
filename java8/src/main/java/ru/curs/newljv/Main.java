package ru.curs.newljv;

import org.atpfivt.ljv.Direction;
import org.atpfivt.ljv.LJV;
import org.atpfivt.ljv.provider.impl.NewObjectHighlighter;
import org.reflections.ReflectionUtils;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static void visualize(LJV ljv, Object o) throws IOException, URISyntaxException {
        String dot = ljv.drawGraph(o);

        String encoded = URLEncoder.encode(dot, "UTF8")
                .replaceAll("\\+", "%20");
        Desktop.getDesktop().browse(
                new URI("https://dreampuf.github.io/GraphvizOnline/#"
                        + encoded));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        //ll();
        cld();
        //chm();
        //cslm();
        //tm();
        //hm1();
        //hm2(6);
        //hm2(11);
        //hm3(11);
        //lhm();
        // lhm2();
        //stream();
    }

    private static void cld() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setDirection(Direction.LR)
                .addFieldAttribute("tail", "constraint=false")
                .addFieldAttribute("prev", "constraint=false")
                .setTreatAsPrimitive(Integer.class);
        Queue<Integer> d = new ConcurrentLinkedDeque<>();
        d.add(1);
        d.add(2);
        d.add(3);
        d.add(4);
        visualize(ljv, d);

    }

    private static void ll() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setDirection(Direction.LR)
                .addFieldAttribute("last", "constraint=false")
                .addFieldAttribute("prev", "constraint=false")
                .setTreatAsPrimitive(Integer.class);
        Queue<Integer> d = new LinkedList<>();
        d.add(1);
        d.add(2);
        d.add(3);
        d.add(4);
        visualize(ljv, d);

    }


    private static void chm() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class);

        Map<String, Integer> map = new ConcurrentHashMap<>(16, 0.75f, 8);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        visualize(ljv, map);
    }

    private static void cslm() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(LongAdder.class)
                .addIgnoreField("val")
                .addFieldAttribute("node", "constraint=false,color=green")
                .addFieldAttribute("down", "constraint=false")
                .addObjectAttributesProvider(new NewObjectHighlighter());

        Map<String, Integer> map = new ConcurrentSkipListMap<>();

        insertA2H(ljv, map);
    }

    private static void tm() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .setIgnoreNullValuedFields(true)
                .addIgnoreField("color")
                .addIgnoreField("value")
                .addObjectAttributesProvider(Main::redBlack)
                .addObjectAttributesProvider(new NewObjectHighlighter());

        Map<String, Integer> map = new TreeMap<>();
        insertA2H(ljv, map);
    }


    private static void insertA2H(LJV ljv, Map map) throws IOException, URISyntaxException {
        map.put("A", 1);
        visualize(ljv, map);
        map.put("B", 2);
        visualize(ljv, map);
        map.put("C", 3);
        visualize(ljv, map);
        map.put("D", 4);
        visualize(ljv, map);
        map.put("E", 3);
        visualize(ljv, map);
        map.put("F", 4);
        visualize(ljv, map);
        map.put("G", 7);
        visualize(ljv, map);
        map.put("H", 8);
        visualize(ljv, map);
    }

    private static String redBlack(Object o) {
        Set<Field> colorFields = ReflectionUtils.getAllFields(o.getClass(),
                f -> "color".equals(f.getName())
                        && f.getType().equals(boolean.class));
        if (colorFields.isEmpty()) {
            return "";
        } else {
            Field colorField = colorFields.iterator().next();
            colorField.setAccessible(true);
            try {
                boolean b = colorField.getBoolean(o);
                return b ? "color=black" : "color=red";
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }


    private static String redBlackForHM(Object o) {
        Set<Field> redFields = ReflectionUtils.getAllFields(o.getClass(),
                f -> "red".equals(f.getName())
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
                // .setIgnoreNullValuedFields(true)
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
                .addFieldAttribute("tail", "constraint=false,color=green");

        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        visualize(ljv, map);
    }

    static void lhm2() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setDirection(Direction.LR)
                .setIgnoreNullValuedFields(true)
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .addFieldAttribute("after", "color=green")
                .addFieldAttribute("before", "color=green,constraint=false")
                .addFieldAttribute("head", "color=green,constraint=false")
                .addFieldAttribute("tail", "color=green,constraint=false")
                .addFieldAttribute("next", "constraint=false");

        Map<String, Integer> map = new LinkedHashMap<>(5, 0.8f, true);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        visualize(ljv, map);
        map.get("two");
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

    private static void stream() throws IOException, URISyntaxException {
        LJV ljv = new LJV()
                .setQualifyNestedClassNames(true)
                .setIgnoreNullValuedFields(true)
                .addFieldAttribute("sourceSpliterator", "constraint=false");
        Stream<Integer> o =
                List.of(1, 2, 3)
                        .stream()
                        .map(x -> x * x)
                        .filter(x -> x % 2 == 0);
        //DOT script
        String dot = ljv.drawGraph(o);

        //use GraphViz online
        String encoded = URLEncoder.encode(dot, "UTF8")
                .replaceAll("\\+", "%20");
        Desktop.getDesktop().browse(
                new URI("https://dreampuf.github.io/GraphvizOnline/#"
                        + encoded));

    }
}
