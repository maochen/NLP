package org.maochen.nlp.ml;

import org.maochen.nlp.ml.vector.LabeledVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This tuple is for the Sequence Model using. Don't mess up with regular Tuple.
 *
 * Created by Maochen on 8/5/15.
 */
public class SequenceTuple {
    public int id;

    public List<Tuple> entries;
    public List<String> tag;

    /**
     * featMap
     *
     * 0,         [ I,     have,    a,      car]
     *
     * 1,         null
     *
     * 2,         [PRP,     VBP,    DT,     NN]
     *
     * tags       [B-NP,   B-VP,   B-NP,   I-NP]
     *
     * -------------------------------------------
     *
     * entries   Tuple1, Tuple2, Tuple3, Tuple4
     */
    public SequenceTuple(Map<Integer, List<String>> featMap, List<String> tags) {
        if (featMap == null || tags == null || featMap.values().stream().findFirst().get().size() != tags.size()) {
            throw new RuntimeException("words and tags are invalid (Size mismatch).");
        }

        int[] dimensions = featMap.values().stream().mapToInt(List::size).distinct().toArray();
        if (dimensions.length != 1) {
            throw new RuntimeException("feats dimension size mismatch).");
        }

        int featIndexMax = featMap.keySet().stream().max(Integer::compare).get();

        String[][] matrix = new String[featIndexMax + 1][];

        featMap.entrySet().forEach(entry -> matrix[entry.getKey()] = entry.getValue().stream().toArray(String[]::new));

        Tuple[] tuples = new Tuple[dimensions[0]];

        for (int col = 0; col < dimensions[0]; col++) {
            List<String> featString = new ArrayList<>();
            for (int row = 0; row < matrix.length; row++) {
                featString.add(matrix[row][col]);
            }

            LabeledVector v = new LabeledVector(featString.stream().toArray(String[]::new));
            tuples[col] = new Tuple(v);
            tuples[col].label = tags.get(col);
        }

        this.entries = Arrays.asList(tuples);
        this.tag = tags;
    }

    public SequenceTuple() {

    }
}
