package com.dfsek.terra.addons.chunkgenerator.math;

public enum BooleanOperator {
    GreaterThan {
        @Override
        public boolean evaluate(double a, double b) {
            return a > b;
        }
    },
    GreaterThanOrEqual {
        @Override
        public boolean evaluate(double a, double b) {
            return a >= b;
        }
    },
    LessThan {
        @Override
        public boolean evaluate(double a, double b) {
            return a < b;
        }
    },
    LessThanOrEqual {
        @Override
        public boolean evaluate(double a, double b) {
            return a <= b;
        }
    },
    Equals {
        @Override
        public boolean evaluate(double a, double b) {
            return a == b;
        }
    },
    NotEquals {
        @Override
        public boolean evaluate(double a, double b) {
            return a != b;
        }
    };
    
    public abstract boolean evaluate(double a, double b);
}
