package com.tomgibara.algebra;

public interface Operation<E> {

	E compose(E e1, E e2);
	
	boolean isCommutative();

}
