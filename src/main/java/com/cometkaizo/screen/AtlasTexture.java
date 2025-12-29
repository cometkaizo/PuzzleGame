package com.cometkaizo.screen;

public interface AtlasTexture {
    double x();
    double y();
    boolean hasYExtension();
    AtlasTexture SINGLE_BLOCK = new AtlasTexture() {
        @Override public double x() {
            return 0;
        }
        @Override public double y() {
            return 0;
        }
        @Override public boolean hasYExtension() {
            return false;
        }
    };
}
