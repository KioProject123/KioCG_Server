package com.kiocg.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GoalRegister<T extends Mob> {
    private final T mob;
    private final List<Register<T>> goals = new ArrayList<>();

    public GoalRegister(T mob) {
        this.mob = mob;
    }

    public void addGoal(int priority, Goal goal, Predicate<T> when) {
        goals.add(new Register<>(priority, goal, when));
    }

    public void reassess() {
        label:
        for (Register<T> register : this.goals) {
            GoalSelector goalSelector = this.getGoalSelector(register.goal);
            if (register.when.test(this.mob)) {
                for (WrappedGoal availableGoal : goalSelector.getAvailableGoals()) {
                    if (availableGoal.getGoal() == register.goal) {
                        continue label;
                    }
                }
                goalSelector.addGoal(register.priority, register.goal);
            } else {
                goalSelector.removeGoal(register.goal);
            }
        }
    }

    private GoalSelector getGoalSelector(Goal goal) {
        return !(goal instanceof TargetGoal) ? mob.goalSelector : mob.targetSelector;
    }

    private record Register<T>(int priority, Goal goal, Predicate<T> when) {
    }
}
