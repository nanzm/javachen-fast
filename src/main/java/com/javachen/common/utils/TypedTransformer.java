package com.javachen.common.utils;

import org.apache.commons.collections4.Transformer;

/**
 * A class that provides for a typed transformer.
 *
 * @param <K> the type of the value that will be returned by the transformer
 * @author Andre Azzolini (apazzolini)
 */
public interface TypedTransformer<K> extends Transformer {

    public K transform(Object input);

}
