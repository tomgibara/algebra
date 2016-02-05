package com.tomgibara.algebra;

import com.tomgibara.collect.Equivalence;

public interface Algebra<E> {

	//throws an IAE if e is not a candidate member of the algebra?
	boolean contains(E e);

	Equivalence<E> equality();

	Size getSize();

}
