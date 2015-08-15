package com.tomgibara.algebra;

import com.tomgibara.collect.EquRel;

public interface Algebra<E> {

	//throws an IAE if e is not a candidate member of the algebra?
	boolean contains(E e);

	EquRel<E> equality();
	
}
