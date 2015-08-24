package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Zn extends Z_ {

	final BigInteger n;
	private Size size = null;
	private final Operation<BigInteger> op = new Operation<BigInteger>() {

		@Override
		public BigInteger identity() {
			return BigInteger.ZERO;
		}

		@Override
		public BigInteger compose(BigInteger e1, BigInteger e2) {
			BigInteger e = e1.add(e2);
			return e.compareTo(n) >= 0 ? e.subtract(n) : e;
		}

		@Override
		public boolean isCommutative() {
			return true;
		}

		@Override
		public BigInteger invert(BigInteger e) {
			return e.signum() == 0 ? e : n.subtract(e);
		}
		
	};
	
	Zn(BigInteger n) {
		super(BigInteger.ONE, EquRel.equality());
		this.n = n;
	}
	
	Zn(BigInteger n, BigInteger g) {
		super(g, ZCosetEqu.forGenerator(g));
		this.n = n;
	}
	
	public BigInteger getN() {
		return n;
	}

	@Override
	public boolean contains(BigInteger e) {
		return
				e != null &&
				e.signum() >=0
				&& e.compareTo(n) < 0 &&
				containsImpl(e);
	}
	
	@Override
	public Size getSize() {
		if (size == null) {
			size = g == BigInteger.ONE ? Size.fromBig(n) : Size.fromBig(n.divide(g));
		}
		return size;
	}
	
	@Override
	public Operation<BigInteger> op() {
		return op;
	}
	
	@Override
	public Set<BigInteger> elements() {
		return new AbstractSet<BigInteger>() {

			final int size = getSize().asInt();

			@Override
			public Iterator<BigInteger> iterator() {
				return Zn.this.iterator();
			}

			@Override
			public boolean contains(Object o) {
				if (!(o instanceof BigInteger)) return false;
				return Zn.this.contains((BigInteger) o);
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public int size() {
				return size;
			}
		};
	}
	
	@Override
	public Iterator<BigInteger> iterator() {
		return new Iterator<BigInteger>() {
			private BigInteger next = BigInteger.ZERO;

			@Override
			public boolean hasNext() {
				return next.compareTo(n) < 0;
			}

			@Override
			public BigInteger next() {
				BigInteger tmp = next;
				next = next.add(g);
				return tmp;
			}
		};
	}
	
	@Override
	public Subgroup<BigInteger> inducedSubgroup(BigInteger... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		for (BigInteger e : es) {
			if (e == null) throw new IllegalArgumentException("null e");
			if (e.signum() < 0 || e.compareTo(n) >= 0) throw new IllegalArgumentException("non element");
		}
		return generated(es);
	}
	
	@Override
	Z_ subgroup(BigInteger g) {
		return new Zn(n, g);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Zn)) return false;
		Zn that = (Zn) obj;
		return
				this.n.equals(that.n) &&
				this.g.equals(that.g);
	}
}
