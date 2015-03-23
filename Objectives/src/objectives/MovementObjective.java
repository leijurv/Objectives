package objectives;
/**
 *
 * @author leijurv
 */
public class MovementObjective extends BaseObjective {
    final double targetX;
    final double targetY;
    final double targetZ;
    public static final double MC_WALKING_SPEED = 4.3;//blocks/sec
    public static final double MC_STAIR_ASCENDING_SPEED = 2;
    public MovementObjective(long updatePeriod, double x, double y, double z) {
        super(updatePeriod);
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
    }
    @Override
    protected double calculateDifficulty() {
        double curX = 5;
        double curY = 5;
        double curZ = 5;
        double XDiff = Math.abs(curX - targetX);
        double YDiff = Math.abs(curY - targetY);
        double ZDiff = Math.abs(curZ - targetZ);
        return Math.sqrt(XDiff * XDiff + ZDiff + ZDiff) * MC_WALKING_SPEED + YDiff * MC_STAIR_ASCENDING_SPEED;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof MovementObjective) {
            MovementObjective m = (MovementObjective) o;
            return m.targetX == targetX && m.targetY == targetY && m.targetZ == targetZ;
        }
        return false;
    }
}
