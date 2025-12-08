package net.raccoon.will.pour_decisions.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ChipEntity extends Entity {

    public static final EntityDataAccessor<Integer> VALUE =
            SynchedEntityData.defineId(ChipEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> AMOUNT =
            SynchedEntityData.defineId(ChipEntity.class, EntityDataSerializers.INT);

    public ChipEntity(EntityType<? extends ChipEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;
        this.canBeCollidedWith();
    }

    public int getValue() {
        return this.entityData.get(VALUE);
    }

    public void setValue(int value) {
        this.entityData.set(VALUE, value);
    }

    public int getAmount() {
        return this.entityData.get(AMOUNT);
    }

    public void setAmount(int amount) {
        this.entityData.set(AMOUNT, amount);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VALUE, 1);
        builder.define(AMOUNT, 1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setValue(tag.getInt("Value"));
        setAmount(tag.getInt("Amount"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Value", getValue());
        tag.putInt("Amount", getAmount());
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}