package ee.moo.tiny.common.tuple;

public record Triple<T, U, V>(
    T first,
    U second,
    V third
) {
}
