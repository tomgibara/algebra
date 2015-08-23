package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Zn implements Group<BigInteger> {

	final BigInteger n;
	final BigInteger g; // divides n
	final EquRel<BigInteger> cosetEqu;
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
		this.n = n;
		this.g = BigInteger.ONE;
		cosetEqu = equality();
	}
	
	Zn(BigInteger n, BigInteger g) {
		this.n = n;
		this.g = g.equals(BigInteger.ONE) ? BigInteger.ONE : g;
		cosetEqu = ZCosetEqu.forGenerator(g);
	}
	
	public BigInteger getN() {
		return n;
	}

	@Override
	public boolean contains(BigInteger e) {
		if (e == null) throw new IllegalArgumentException("null e");
		if (e.signum() < 0 || e.compareTo(n) >= 0) return false;
		return g == BigInteger.ONE || e.remainder(g).signum() == 0;
	}
	
	@Override
	public EquRel<BigInteger> equality() {
		return EquRel.equality();
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
	public boolean commutes(BigInteger e1, BigInteger e2) {
		return true;
	}
	
	@Override
	public boolean isAbelian() {
		return true;
	}
	
	@Override
	public boolean isIdentity(BigInteger e) {
		return e.signum() == 0;
	}
	
	@Override
	public Subgroup<BigInteger> inducedSubgroup(BigInteger... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		for (BigInteger e : es) {
			if (e == null) throw new IllegalArgumentException("null e");
			if (e.signum() < 0 || e.compareTo(n) >= 0) throw new IllegalArgumentException("non element");
		}
		BigInteger gcd = Z.gcd(es);
		if (gcd.signum() == 0) return Subgroup.trivialSubgroup(this);
		if (gcd.equals(g)) return Subgroup.totalSubgroup(this);
		if (gcd.remainder(g).signum() != 0) throw new IllegalArgumentException("non-element");
		return new Subgroup<BigInteger>() {

			private final Zn subgroup  = new Zn(n, gcd);

			@Override
			public Group<BigInteger> getOvergroup() {
				return Zn.this;
			}

			@Override
			public Group<BigInteger> getSubgroup() {
				return subgroup;
			}

			@Override
			public Coset<BigInteger> rightCoset(BigInteger e) {
				return new ZnCoset(Zn.this, e.remainder(subgroup.g));
			}

			@Override
			public Coset<BigInteger> leftCoset(BigInteger e) {
				return rightCoset(e);
			}

		};
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Zn)) return false;
		Zn that = (Zn) obj;
		return this.g.equals(that.g);
	}
}
