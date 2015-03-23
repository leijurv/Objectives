package objectives;
/**
 *
 * @author leijurv
 */
public class LookAtBlockObjective extends BaseObjective {
    final int x;
    final int y;
    final int z;
    public LookAtBlockObjective(int x, int y, int z) {
        super(1000);
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    protected double calculateDifficulty() {
        return 1;//return getCurrentLookingDirection()-directionToLookAt(x,y,z)
    }
}
