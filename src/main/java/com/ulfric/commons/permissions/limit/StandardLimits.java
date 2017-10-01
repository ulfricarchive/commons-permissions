package com.ulfric.commons.permissions.limit;

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

		@Override
		public boolean isWithinBounds(Limit ask) {
			return ask == this;
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

		@Override
		public boolean isWithinBounds(Limit ask) {
			return true;
		}
	};

	@Override
	public Limit without(Limit other) {
		return this;
	}

}
