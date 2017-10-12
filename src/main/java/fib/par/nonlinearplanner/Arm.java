package fib.par.nonlinearplanner;

public class Arm {
    final String name;

    public static Arm leftArm = new Arm("Left");
    public static Arm rightArm = new Arm("Right");

    public Arm(String name) {
        this.name = name;
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
