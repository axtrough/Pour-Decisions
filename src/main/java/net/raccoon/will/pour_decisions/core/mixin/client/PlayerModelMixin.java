package net.raccoon.will.pour_decisions.core.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.raccoon.will.pour_decisions.core.registry.PDItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void afterSetupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayer player) {
            PlayerModel<?> model = (PlayerModel<?>) (Object) this;

            float pitchRad = headPitch * ((float)Math.PI / 180F);
            boolean hasRevolverInMainHand = player.getMainHandItem().is(PDItems.REVOLVER);
            boolean hasRevolverInOffHand = player.getOffhandItem().is(PDItems.REVOLVER);

            if (hasRevolverInMainHand) {
                model.rightArm.xRot = pitchRad - 1.5F;
                model.rightArm.yRot = netHeadYaw * ((float)Math.PI / 180F) * 1F;
                model.rightSleeve.copyFrom(model.rightArm);
            }

            if (hasRevolverInOffHand) {
                model.leftArm.xRot = -1.75f;
                model.leftArm.zRot = -0.15f;
                model.leftArm.yRot = -0.85f;
                model.leftSleeve.copyFrom(model.leftArm);
            }
        }
    }
}