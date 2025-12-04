package net.raccoon.will.pour_decisions.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.raccoon.will.pour_decisions.client.event.RevolverEventHandler;
import net.raccoon.will.pour_decisions.core.registry.*;
import net.raccoon.will.structura.api.feature.Hitscan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static net.raccoon.will.structura.api.feature.Hitscan.HitscanResult.HitType.ENTITY;

public class RevolverItem extends Item {

    private static final int NUM_CHAMBERS_DEFAULT = 6;
    private static final int SHOOT_COOLDOWN_TICKS = 20; //1 sec
    private static final int SPIN_COOLDOWN_TICKS = 30; //1.5 sec
    private static final int RELOAD_COOLDOWN_TICKS = 40; //2 sec

    public RevolverItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    public static String visualizeChambers(ItemStack stack) {
        int chambers = stack.getOrDefault(PDComponents.NUM_CHAMBERS.get(), 6);
        int current = stack.getOrDefault(PDComponents.CURRENT_CHAMBER.get(), 0);
        List<Integer> bullets = stack.getOrDefault(PDComponents.BULLET_CHAMBERS.get(), new ArrayList<>());

        StringBuilder sb = new StringBuilder();
        sb.append("Cylinder: [");
        for (int i = 0; i < chambers; i++) {
            if (i == current) sb.append(">"); // indicate current chamber
            else sb.append(" ");

            if (bullets.contains(i)) sb.append("X"); // bullet
            else sb.append("#"); // empty

            if (i == current) sb.append("<");
            else sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public List<Integer> getBulletChambers(ItemStack stack) {
        return stack.getOrDefault(PDComponents.BULLET_CHAMBERS.get(), new ArrayList<>());
    }

    public int getBulletCount(ItemStack stack) {
        return getBulletChambers(stack).size();
    }

    public int getNumChambers(ItemStack stack) {
        return stack.getOrDefault(PDComponents.NUM_CHAMBERS.get(), NUM_CHAMBERS_DEFAULT);
    }

    public int getCurrentChamber(ItemStack stack) {
        return stack.getOrDefault(PDComponents.CURRENT_CHAMBER.get(), 0);
    }

    public void setCurrentChamber(ItemStack stack, int value) {
        stack.set(PDComponents.CURRENT_CHAMBER.get(), value % getNumChambers(stack));
    }

    private void advanceCylinder(ItemStack stack) {
        setCurrentChamber(stack, getCurrentChamber(stack) + 1);
    }

    private void spinCylinder(ItemStack stack, RandomSource random, Player player) {
        int chambers = getNumChambers(stack);
        setCurrentChamber(stack, random.nextInt(chambers));
        if (player.getCooldowns().isOnCooldown(this)) return;

        player.getCooldowns().addCooldown(this, SPIN_COOLDOWN_TICKS);
        System.out.println(visualizeChambers(stack));
    }

    private boolean removeBulletFromInventory(Player player) {
        if (player.isCreative()) return true;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(PDItems.BULLET.get())) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public void reload(ItemStack stack, Player player) {
        System.out.println(visualizeChambers(stack));
        if (player.getCooldowns().isOnCooldown(this)) return;

        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        int maxChambers = getNumChambers(stack);

        // calculate empty chambers
        List<Integer> emptyChambers = new ArrayList<>();
        for (int i = 0; i < maxChambers; i++) {
            if (!bullets.contains(i)) emptyChambers.add(i);
        }

        if (emptyChambers.isEmpty()) return; // cylinder full

        // shuffle empty chambers for randomness
        Collections.shuffle(emptyChambers, new Random(player.getRandom().nextLong()));

        int bulletsLoaded = 0;
        for (int chamber : emptyChambers) {
            if (removeBulletFromInventory(player)) {
                bullets.add(chamber);
                bulletsLoaded++;
            } else {
                break; // no more bullets in inventory
            }
        }

        if (bulletsLoaded == 0) {
            player.displayClientMessage(Component.literal("No bullets to reload!"), true);
            return;
        }

        // set bullets in stack
        stack.set(PDComponents.BULLET_CHAMBERS.get(), bullets);

        // pick a random bullet in cylinder to be current
        int randomCurrent = bullets.get(new Random().nextInt(bullets.size()));
        setCurrentChamber(stack, randomCurrent);

        // play sound and set cooldown
        player.level().playLocalSound(player, PDSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1f, 1f);
        player.getCooldowns().addCooldown(this, RELOAD_COOLDOWN_TICKS);
    }

    // ----- Shooting ----- \\
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        int current = getCurrentChamber(stack);

        //spin cylinder if crouching
        if (player.isCrouching()) {
            spinCylinder(stack, player.getRandom(), player);
            level.playLocalSound(player, PDSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1f, 1f);
            advanceCylinder(stack);
            return InteractionResultHolder.consume(stack);
        }

        //cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.consume(stack);
        }

        //shooting
        if (bullets.contains(current)) {
            bullets.remove((Integer) current);
            stack.set(PDComponents.BULLET_CHAMBERS.get(), bullets);
            player.getCooldowns().addCooldown(this, SHOOT_COOLDOWN_TICKS);

            if (!level.isClientSide) {
                Hitscan.HitscanResult result = Hitscan.performHitscan(player, 40);
                if (result.type() == ENTITY) {
                    EntityHitResult hit = result.entityHit();
                    Entity target = hit.getEntity();
                    if (target instanceof LivingEntity living) {
                        living.hurt(PDDamageSources.revolver(living.level(), player), 67f);
                    }
                } else if (hand == InteractionHand.OFF_HAND) {
                    player.hurt(PDDamageSources.revolver(player.level(), player), 67f);
                }
            }

            if (level.isClientSide) {
                final double MUZZLE_FORWARD_OFFSET = 0.95D; //barrel
                final double MUZZLE_UP_OFFSET = -0.25D; //
                final double HAND_RIGHT_OFFSET = 0.3125D; // right of center the hand is
                final double HAND_DOWN_OFFSET = 0.3125D; //eye level the hand is

                final float partialTicks = 1.0F;
                Vec3 eyePos = player.getEyePosition(partialTicks);
                Vec3 lookVec = player.getViewVector(partialTicks); // head/eye Look (forward)

                float bodyYawRad = player.yBodyRot * ((float)Math.PI / 180F);

                float rightYawRad = bodyYawRad + (float)Math.PI / 2.0F;
                Vec3 bodyRightVec = Vec3.directionFromRotation(0.0F, rightYawRad * (180.0F / (float)Math.PI));

                //local up vector
                Vec3 localUpVec = lookVec.cross(bodyRightVec).normalize();

                //calculate the hand pivot position (where the arm meets the body)
                Vec3 handPivotPos = eyePos
                        //subtract vertical offset
                        .subtract(0, HAND_DOWN_OFFSET, 0)
                        //add lateral offset using the rotated bodyRightVec
                        .add(bodyRightVec.x * HAND_RIGHT_OFFSET,
                                bodyRightVec.y * HAND_RIGHT_OFFSET,
                                bodyRightVec.z * HAND_RIGHT_OFFSET);


                // Move FORWARD along the LookVec (barrel length)
                double spawnX = handPivotPos.x + (lookVec.x * MUZZLE_FORWARD_OFFSET);
                double spawnY = handPivotPos.y + (lookVec.y * MUZZLE_FORWARD_OFFSET);
                double spawnZ = handPivotPos.z + (lookVec.z * MUZZLE_FORWARD_OFFSET);

                //apply LOCAL UPWARD OFFSET (Final adjustment using the local up vector)
                spawnX += localUpVec.x * MUZZLE_UP_OFFSET;
                spawnY += localUpVec.y * MUZZLE_UP_OFFSET;
                spawnZ += localUpVec.z * MUZZLE_UP_OFFSET;

                level.addParticle(PDParticles.MUZZLEFLASH_PARTICLE.get(),
                        spawnX, spawnY, spawnZ,
                        0.0D, 0.0D, 0.0D);
            }

            level.playLocalSound(player, PDSounds.REVOLVER_SHOOT.get(), SoundSource.PLAYERS, 1f, 1f);
            RevolverEventHandler.onRevolverShoot(player, stack);

            if (RevolverEventHandler.didShoot(player)) {
                player.setXRot(player.getXRot() - 4);
            }
        } else {
            level.playLocalSound(player, PDSounds.REVOLVER_EMPTY.get(), SoundSource.PLAYERS, 1f, 1f);
        }
        advanceCylinder(stack);
        return InteractionResultHolder.consume(stack);
    }
}