package com.kiocg.entity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GoalRegister {
    private final List<Register> goals = new ArrayList<>();

    public void addGoal(GoalSelector goalSelector, int priority, Goal goal) {
        goals.add(new Register(goalSelector, priority, goal, goal::registerWhen));
    }

    public void addGoal(GoalSelector goalSelector, int priority, Goal goal, BooleanSupplier supplier) {
        goals.add(new Register(goalSelector, priority, goal, supplier));
    }

    public void reassess() {
        label:
        for (Register register : this.goals) {
            if (register.when.getAsBoolean()) {
                for (WrappedGoal availableGoal : register.goalSelector.getAvailableGoals()) {
                    if (availableGoal.getGoal() == register.goal) {
                        continue label;
                    }
                }
                register.goalSelector.addGoal(register.priority, register.goal);
            } else {
                register.goalSelector.removeGoal(register.goal);
            }
        }
    }

    private record Register(GoalSelector goalSelector, int priority, Goal goal, BooleanSupplier when) {
    }
}
