package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;
import com.tomgibara.permute.Permutation;
import com.tomgibara.permute.PermutationSequence;

public class S implements Group<Permutation> {

	// all factorials that fit into a long
	private static final Size[] lowOrderSizes = {
		Size.ONE,
		Size.ONE,
		Size.fromLong(2L),
		Size.fromLong(6L),
		Size.fromLong(24L),
		Size.fromLong(120L),
		Size.fromLong(720L),
		Size.fromLong(5040L),
		Size.fromLong(40320L),
		Size.fromLong(362880L),
		Size.fromLong(3628800L),
		Size.fromLong(39916800L),
		Size.fromLong(479001600L),
		Size.fromLong(6227020800L),
		Size.fromLong(87178291200L),
		Size.fromLong(1307674368000L),
		Size.fromLong(20922789888000L),
		Size.fromLong(355687428096000L),
		Size.fromLong(6402373705728000L),
		Size.fromLong(121645100408832000L),
		Size.fromLong(2432902008176640000L)
	};

	private final Permutation identity;
	private Size size;
	private final Operation<Permutation> op = new Operation<Permutation>() {

		@Override
		public boolean isCommutative() {
			return false;
		}

		@Override
		public Permutation compose(Permutation e1, Permutation e2) {
			return e1.generator().apply(e2).permutation();
		}

		@Override
		public Permutation identity() {
			return identity;
		}

		@Override
		public Permutation invert(Permutation e) {
			return e.generator().invert().permutation();
		}
	};


	S(int order) {
		identity = Permutation.identity(order);
		if (order < lowOrderSizes.length) size = lowOrderSizes[order];
	}

	public int getOrder() {
		return identity.size();
	}

	@Override
	public Size getSize() {
		if (size == null) {
			BigInteger s = lowOrderSizes[lowOrderSizes.length-1].asBig();
			int order = identity.size();
			for (int i = lowOrderSizes.length; i <= order; i++) {
				s = s.multiply(BigInteger.valueOf(i));
			}
			size = Size.fromBig(s);
		}
		return size;
	}

	@Override
	public boolean contains(Permutation e) {
		return true;
	}

	@Override
	public EquRel<Permutation> equality() {
		return EquRel.equality();
	}

	@Override
	public Iterator<Permutation> iterator() {
		return new Iterator<Permutation>() {

			private PermutationSequence sequence = null;

			@Override
			public boolean hasNext() {
				return sequence == null || sequence.hasNext();
			}

			@Override
			public Permutation next() {
				if (sequence == null) {
					sequence = identity.generator().getOrderedSequence();
					return sequence.first().getGenerator().permutation();
				}
				return sequence.next().getGenerator().permutation();
			}

		};
	}

	@Override
	public Operation<Permutation> op() {
		return op;
	}

	@Override
	public boolean isAbelian() {
		return identity.size() < 3;
	}

	@Override
	public Size orderOf(Permutation e) {
		return Size.fromBig( e.info().getLengthOfOrbit() );
	}
}
