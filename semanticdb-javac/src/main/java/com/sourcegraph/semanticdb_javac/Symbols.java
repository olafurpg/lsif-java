package com.sourcegraph.semanticdb_javac;


import java.util.Objects;

final public class Symbols {

    public static String encodeName(String name) {
        if (name == null || name.isEmpty()) return "``";
        boolean isStartOk = Character.isJavaIdentifierStart(name.charAt(0));
        boolean isPartsOk = true;
        for (int i = 1; isPartsOk && i < name.length(); i++) {
            isPartsOk = Character.isJavaIdentifierPart(name.charAt(i));
        }
        if (isStartOk && isPartsOk) return name;
        else return "`" + name + "`";
    }

    public static String NONE = "";
    public static String ROOT_PACKAGE = "_root_/";

    public static String global(String owner, Descriptor desc) {
        if (desc == Descriptor.NONE) return Symbols.NONE;
        else if (!ROOT_PACKAGE.equals(owner)) return owner + desc.encode();
        else return desc.encode();
    }

    public static String local(String suffix) {
        if (suffix.indexOf('/') == -1 && suffix.indexOf(';') == -1) return "local" + suffix;
        else throw new IllegalArgumentException(suffix);
    }

    public static boolean isGlobal(String symbol) {
        return !symbol.startsWith("local");
    }

    public final static class Descriptor {

        public static Descriptor NONE = new Descriptor(Kind.None, "", "");

        public enum Kind {
            None,
            Term,
            Method,
            Type,
            Package,
            Parameter,
            TypeParameter;
        }

        public final Kind kind;
        public final String name;
        public final String disambiguator;

        public Descriptor(Kind kind, String name) {
            this(kind, name, "");
        }

        public Descriptor(Kind kind, String name, String disambiguator) {
            this.kind = kind;
            this.name = name;
            this.disambiguator = disambiguator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Descriptor that = (Descriptor) o;
            return kind == that.kind && Objects.equals(name, that.name) && Objects.equals(disambiguator, that.disambiguator);
        }

        @Override
        public int hashCode() {
            return Objects.hash(kind, name, disambiguator);
        }

        @Override
        public String toString() {
            return "Descriptor{" +
                    "kind=" + kind +
                    ", name='" + name + '\'' +
                    ", disambiguator='" + disambiguator + '\'' +
                    '}';
        }

        public String encode() {
            switch (kind) {
                case None:
                    return "";
                case Term:
                    return encodeName(name) + ".";
                case Method:
                    return encodeName(name) + disambiguator + ".";
                case Type:
                    return encodeName(name) + "#";
                case Package:
                    return encodeName(name) + "/";
                case Parameter:
                    return "(" + encodeName(name) + ")";
                case TypeParameter:
                    return "[" + encodeName(name) + "]";
                default:
                    throw new IllegalArgumentException(String.format("%s", kind));
            }
        }
    }
}
