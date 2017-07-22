package com.ulfric.embargo.limit;

public enum StandardLimits implements Limit {

	NONE {
		@Override
		public boolean isWithinBounds(int ask) {
			return false;
		}

		@Override
		public Limit and(Limit other) {
			return other;
		}
	},

	UNLIMITED {
		@Override
		public boolean isWithinBounds(int ask) {
			return true;
		}

		@Override
		public Limit and(Limit other) {
			return this;
		}
	};

	@Override
	public Limit without(Limit other) {
		return this;
	}

}
