package committee.nova.hallelugw.api;

public interface ExtendedBreeze {
    int hallelugw$getSize();

    void hallelugw$setSize(int size);

    default double hallelugw$getScaleD() {
        return hallelugw$getSize() / 10.0;
    }

    default float hallelugw$getScaleF() {
        return hallelugw$getSize() / 10.0F;
    }

    default int hallelugw$getScaleI() {
        return Math.max(1, hallelugw$getSize() / 10);
    }
}
