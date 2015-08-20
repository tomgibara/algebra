package com.tomgibara.algebra.monoid;

import static com.tomgibara.algebra.Constants.MAX_INT_VALUE;

import java.math.BigInteger;

import com.tomgibara.algebra.Algebra;
import com.tomgibara.algebra.Size;

//may throw an IllStaExc if not countable
public interface Monoid<E> extends Algebra<E>, Iterable<E> {

	interface Operation<E> extends com.tomgibara.algebra.Operation<E> {

		E identity();
		
		default E power(E e, long p) {
			if (e == null) throw new IllegalArgumentException("null e");
			if (p < 0) throw new IllegalArgumentException("negative p");
			if (p < 3) {
				switch ((int) p) {
				case 0 : return identity();
				case 1 : return e;
				case 2 : return compose(e, e);
				}
			}
			E t = e;
			E a = identity();
			while (true) {
				if ((p & 1L) != 0) a = compose(a, t);
				if (p == 0) return a;
				p >>= 1;
				t = compose(t, t);
			}
		}
		
		default E power(E e, BigInteger p) {
			if (p == null) throw new IllegalArgumentException("null p");
			if (p.signum() < 0) throw new IllegalArgumentException("negative p");
			if (p.compareTo(MAX_INT_VALUE) <= 0) return power(e, p.intValue());
			int limit = p.bitCount();
			E t = e;
			E a = identity();
			for (int i = 0; i < limit; i++, t = compose(t, t)) {
				if (p.testBit(i)) a = compose(a, t);
			}
			return a;
		}

	}

	Operation<E> op();
	
	Size getSize();
	
	default boolean commutes(E e1, E e2) {
		Operation<E> op = op();
		if (op.isCommutative()) return true;
		E lr = op.compose(e1, e2);
		E rl = op.compose(e2, e1);
		return equality().isEquivalent(lr, rl);
	}

}
