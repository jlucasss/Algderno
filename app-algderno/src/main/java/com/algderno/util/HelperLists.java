package com.algderno.util;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 *
 * This class contains methods on manipulating lists.
 *
 * Methods taken from: https://qastack.com.br/programming/5283047/intersection-and-union-of-arraylists-in-java
 *
 * Note: Although the code is small, it is likely to be slow (according to the source site)
 *
 * */

public class HelperLists {

	public HelperLists() {
	}

	public static <T> Collection<T> getIntersection(Collection<T> coll1, Collection<T> coll2){
	    return Stream.concat(coll1.stream(), coll2.stream())
	            .filter(coll1::contains)
	            .filter(coll2::contains)
	            .collect(Collectors.toSet());
	}

	public static <T> Collection<T> getUnion(Collection<T> coll1, Collection<T> coll2){
	    return Stream.concat(coll1.stream(), coll2.stream())
	    		.distinct()
	    		.collect(Collectors.toSet());
	}

	public static <T> Collection<T> getMinus(Collection<T> coll1, Collection<T> coll2){
	    return coll1.stream().filter(not(coll2::contains)).collect(Collectors.toSet());
	}

	public static <T> Predicate<T> not(Predicate<T> t) {
	    return t.negate();
	}

}
