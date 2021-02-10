package minimized;

public abstract class AbstractClasses {
//              ^^^^^^ definition minimized/AbstractClasses#`<init>`().
//              ^^^^^^^^^^^^^^^ definition minimized/AbstractClasses#
    public String defaultImplementation() { return ""; }
//         ^^^^^^ reference java/lang/String#
//                ^^^^^^^^^^^^^^^^^^^^^ definition minimized/AbstractClasses#defaultImplementation().
    public abstract String abstractImplementation();
//                  ^^^^^^ reference java/lang/String#
//                         ^^^^^^^^^^^^^^^^^^^^^^ definition minimized/AbstractClasses#abstractImplementation().
}
