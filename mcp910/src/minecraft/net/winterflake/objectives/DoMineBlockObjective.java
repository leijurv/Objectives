package net.winterflake.objectives;
/**
 *
 * @author leijurv
 */
public class DoMineBlockObjective extends BaseObjective {
    final int x;
    final int y;
    final int z;
    final AquireItemObjective item;
    public DoMineBlockObjective(int x, int y, int z, AquireItemObjective item) {
        super(10000000);
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = item;
    }
    @Override
    protected double calculateDifficulty() {
        //Return getBlock(x,y,z).getAmountofTimetoMineWithTool(itemID)
        return 10;
    }
}
