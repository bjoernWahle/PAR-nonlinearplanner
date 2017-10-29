package fib.par.nonlinearplanner;

public class Arm {
    private final String name;

    public static final Arm leftArm = new Arm("L");
    public static final Arm rightArm = new Arm("R");

    private Arm(String name) {
        this.name = name;
    }

    public static Arm fromString(String string) {
        switch (string) {
            case "R":
                return rightArm;
            case "L":
                return leftArm;
            default:
                throw new IllegalArgumentException("Arm with name " + string + " not found.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arm arm = (Arm) o;

        return name != null ? name.equals(arm.name) : arm.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
