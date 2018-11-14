package actors;

import java.util.Collection;

public class Protocols {

    private Protocols() {
        throw new IllegalStateException("Utility class");
    }

    public static class Heartbeat {} {

    }

    public static class Bulk {
        Collection<Operation> operations;

        public Bulk(Collection<Operation>  operations) {
            this.operations = operations;
        }
    }

    public static class Operation {

        public enum Type {
            INSERT,
            UPDATE,
            DELETE
        }

        Object obj;
        Type type;

        public Operation(Object obj, Operation.Type type) {
            this.obj = obj;
            this.type = type;
        }
    }

    public static class Subscribe { }
    public static class Unsubscribe { }
}
