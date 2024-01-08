package at;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Klass(
        String name,
        KlassType type,
        Set<Klass> children,
        Set<Klass> parents,
        Set<Klass> impls,
        Set<Klass> implementedBy
) { // Then filter classes without parent and add "root" as parent.

    public Klass {
        if (name == null || type == null) {
            throw new IllegalArgumentException();
        }
        if (children == null) {
            children = new HashSet<>();
        }
        if (parents == null) {
            parents = new HashSet<>();
        }
        if (impls == null) {
            impls = new HashSet<>();
        }
        if (implementedBy == null) {
            implementedBy = new HashSet<>();
        }
    }

    public String shortName() {
        var kn = name.split("\\.");
        return kn[kn.length - 1];
    }

    public static Klass of(String name, KlassType type) {
        return new Klass(name, type, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public static Klass of(String name, KlassType type, HashSet<Klass> children, HashSet<Klass> parents, HashSet<Klass> impls, HashSet<Klass> d) {
        return new Klass(name, type, children, parents, impls, d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Klass klass = (Klass) o;
        return Objects.equals(name, klass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
