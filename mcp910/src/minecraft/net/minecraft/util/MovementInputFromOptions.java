package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;
import net.winterflake.objectives.Objectives;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_)
    {
        this.gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.getIsKeyPressed() || Objectives.forward)
        {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.getIsKeyPressed())
        {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.getIsKeyPressed() || Objectives.strafe)
        {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.getIsKeyPressed())
        {
            --this.moveStrafe;
        }
        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed() || Objectives.isJumping;
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
