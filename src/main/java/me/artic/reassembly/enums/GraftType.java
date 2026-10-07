package me.artic.reassembly.enums;

public enum GraftType {
    MOB_TO_MOB,
    BLOCK_TO_BLOCK,
    BLOCK_TO_MOB,
    MOB_TO_BLOCK;

    public GraftType next() {
        GraftType[] values = GraftType.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
