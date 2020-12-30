package ru.curs.oldljv;

import org.atpfivt.ljv.Direction;
import org.atpfivt.ljv.LJV;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    private static void visualize(LJV ljv, Object o) throws IOException, URISyntaxException {
        String dot = URLEncoder.encode(ljv.drawGraph(o), "UTF8")
                .replaceAll("\\+", "%20");

        Desktop.getDesktop().browse(
                new URI("https://dreampuf.github.io/GraphvizOnline/#"
                        + dot));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        LJV ljv = new LJV().setQualifyNestedClassNames(true).setIgnoreNullValuedFields(true);
        Stream<Integer> stream = List.of(1, 2, 3).stream().map(x -> x * x).filter(x -> x % 2 == 0);

        visualize(ljv, stream);

    }
}
