package com.gitlab.unchuckable.paleo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.apache.avro.generic.IndexedRecord;

public class RecordWalker<T> implements BiConsumer<Object, T> {

    private List<BiConsumer<Object, T>> consumers = new ArrayList<>();
    private Map<Integer, RecordWalker<T>> fieldWalkers = new HashMap<>();
    private Map<Object, RecordWalker<T>> entryWalker = new HashMap<>();
    private Map<Predicate<Object>, RecordWalker<T>> conditionalWalkers = new HashMap<>();

    @Override
    public void accept(Object object, T contextObject) {
        if (object == null) {
            return;
        }
        if (object instanceof Iterable) {
            accept((Iterable<?>) object, contextObject);
        } else if (object instanceof Map) {
            accept((Map<?, ?>) object, contextObject);
        } else if (object instanceof IndexedRecord) {
            accept((IndexedRecord) object, contextObject);
        } else {
            applyConsumers(object, contextObject);
        }
    }

    public void accept(Iterable<?> iterable, T contextObject) {
        for (Object thisObject : (Iterable<?>) iterable) {
            accept(thisObject, contextObject);
        }
    }

    public void accept(Map<?, ?> map, T contextObject) {
        applyConsumers(map, contextObject);
        for (Entry<?, ?> thisEntry : map.entrySet()) {
            RecordWalker<T> recordWalker = entryWalker.get(thisEntry.getKey());
            if (recordWalker != null) {
                recordWalker.accept(thisEntry.getValue(), contextObject);
            }
        }
    }

    public void accept(IndexedRecord record, T contextObject) {
        applyConsumers(record, contextObject);
        for (Entry<Integer, RecordWalker<T>> thisFieldHandler : fieldWalkers.entrySet()) {
            thisFieldHandler.getValue().accept(record.get(thisFieldHandler.getKey()), contextObject);
        }
    }

    private void applyConsumers(Object object, T contextObject) {
        consumers.forEach(consumer -> consumer.accept(object, contextObject));
        for (Entry<Predicate<Object>, RecordWalker<T>> thisConditionalWalker : conditionalWalkers.entrySet()) {
            if (thisConditionalWalker.getKey().test(object)) {
                thisConditionalWalker.getValue().accept(object, contextObject);
            }
        }
    }

}