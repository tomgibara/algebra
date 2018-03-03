package com.tomgibara.algebra.field;

import java.math.BigDecimal;
import java.math.MathContext;

import com.tomgibara.algebra.group.Groups;

public final class Fields {

	public static Field<Double> Rdbl() {
		return Rdbl.instance;
	}

	public static Field<BigDecimal> Rdbl(MathContext context, int compareTo) {
		return new Rbig(Groups.RmultBig(context, compareTo));
	}

}
