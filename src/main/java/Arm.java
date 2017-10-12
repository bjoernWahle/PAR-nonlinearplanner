public class Arm {
    String name;

    public static Arm leftArm = new Arm("Left");
    public static Arm rightArm = new Arm("Right");

    public Arm(String name) {
        this.name = name;
    }
}
