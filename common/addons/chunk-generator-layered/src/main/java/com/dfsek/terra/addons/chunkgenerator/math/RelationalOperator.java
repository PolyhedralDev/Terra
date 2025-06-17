package com.dfsek.terra.addons.chunkgenerator.math;

import com.dfsek.terra.addons.chunkgenerator.util.DoubleBiPredicate;


public enum RelationalOperator implements DoubleBiPredicate {
    GreaterThan {
        @Override
        public boolean test(double a, double b) {
            return a > b;
        }
    },
    GreaterThanOrEqual {
        @Override
        public boolean test(double a, double b) {
            return a >= b;
        }
    },
    LessThan {
        @Override
        public boolean test(double a, double b) {
            return a < b;
        }
    },
    LessThanOrEqual {
        @Override
        public boolean test(double a, double b) {
            return a <= b;
        }
    },
    Equals {
        @Override
        public boolean test(double a, double b) {
            return a == b;
        }
    },
    NotEquals {
        @Override
        public boolean test(double a, double b) {
            return a != b;
        }
    };
    
    public abstract boolean test(double a, double b);
}
