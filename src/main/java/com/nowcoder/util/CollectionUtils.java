package com.nowcoder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class  CollectionUtils<T> {
    public  static <T> T[] toArray(Collection<T> collection){
        int len=collection.size();
        Object[] array=new Object[len];
        int i=0;
        for(Iterator iterator=collection.iterator();iterator.hasNext();){
            array[i++]=(T)iterator.next();
        }
        return (T[]) array;
    }
}
