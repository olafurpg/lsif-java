package com.sourcegraph.semanticdb_javac;

public enum PositionKind {
    FROM_START, FROM_POINT, FROM_POINT_PLUS_ONE;
    public boolean isFromPoint() {
        switch (this) {
            case FROM_POINT:
            case FROM_POINT_PLUS_ONE:
                return true;
            default:
                return false;
        }
    }
}
