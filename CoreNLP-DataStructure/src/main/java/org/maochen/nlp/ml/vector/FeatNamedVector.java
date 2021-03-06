package org.maochen.nlp.ml.vector;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Maochen on 9/19/15.
 */
public class FeatNamedVector extends DenseVector {
    public String[] featsName = null;

    public FeatNamedVector(double[] vector) {
        super(vector);
    }

    public FeatNamedVector(String[] feats) {
        super(IntStream.range(0, feats.length).mapToDouble(x -> 1.0D).toArray());
        this.featsName = feats;
    }

    public FeatNamedVector(String[] feats, String delimiter) {
        super(new double[feats.length]);

        this.featsName = new String[feats.length];

        for (int i = 0; i < feats.length; i++) {
            String[] fields = feats[i].split(delimiter);
            if (fields.length != 2) {
                throw new RuntimeException("Error feats format: " + Arrays.toString(feats));
            }

            featsName[i] = fields[0];
            super.getVector()[i] = Double.parseDouble(fields[1]);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FeatNamedVector)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        String[] oFeatName = ((FeatNamedVector) o).featsName;
        if (featsName.length != oFeatName.length) {
            return false;
        }

        for (int i = 0; i < featsName.length; i++) {
            if (!featsName[i].equalsIgnoreCase(oFeatName[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Arrays.hashCode(featsName);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.featsName);
    }
}
