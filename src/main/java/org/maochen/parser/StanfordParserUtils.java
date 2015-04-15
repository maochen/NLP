package org.maochen.parser;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;
import org.maochen.parser.stanford.nn.StanfordNNDepParser;
import org.maochen.utils.LangTools;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maochen on 4/6/15.
 */
public class StanfordParserUtils {

    public static String getCoNLLXString(Collection<TypedDependency> deps, List<CoreLabel> tokens) {
        StringBuilder bf = new StringBuilder();

        Map<Integer, TypedDependency> indexedDeps = new HashMap<>(deps.size());
        for (TypedDependency dep : deps) {
            indexedDeps.put(dep.dep().index(), dep);
        }

        int idx = 1;

        if (tokens.get(0).lemma() == null) {
            StanfordNNDepParser.tagLemma(tokens);
        }

        for (CoreLabel token : tokens) {
            String word = token.word();
            String pos = token.tag();
            String cPos = (token.get(CoreAnnotations.CoarseTagAnnotation.class) != null) ?
                    token.get(CoreAnnotations.CoarseTagAnnotation.class) : LangTools.getCPOSTag(pos);
            String lemma = token.lemma();
            Integer gov = indexedDeps.containsKey(idx) ? indexedDeps.get(idx).gov().index() : 0;
            String reln = indexedDeps.containsKey(idx) ? indexedDeps.get(idx).reln().toString() : "erased";
            String out = String.format("%d\t%s\t%s\t%s\t%s\t_\t%d\t%s\t_\t_\n", idx, word, lemma, cPos, pos, gov, reln);
            bf.append(out);
            idx++;
        }
        bf.append("\n");
        return bf.toString();
    }
}