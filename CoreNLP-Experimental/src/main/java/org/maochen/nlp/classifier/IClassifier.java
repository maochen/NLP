package org.maochen.nlp.classifier;


import org.maochen.nlp.datastructure.Tuple;

import java.util.List;
import java.util.Map;

public interface IClassifier {
    IClassifier train(List<Tuple> trainingData);

    Map<String, Double> predict(Tuple predict);

    void setParameter(Map<String, String> paraMap);
}