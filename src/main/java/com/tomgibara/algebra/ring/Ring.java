package com.tomgibara.algebra.ring;

import com.tomgibara.algebra.Algebra;
import com.tomgibara.algebra.group.Group;
import com.tomgibara.algebra.monoid.Monoid;

public interface Ring<E> extends Algebra<E> {

	Group<E> addition();

	Monoid<E> multiplication();

}
