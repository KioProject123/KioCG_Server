package com.kiocg.world;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.Arrays;

public class ChunkHot {
    private static final Logger LOGGER = LogUtils.getLogger();

    // 热度统计总区间数量
    private static final int TIMES_LENGTH = 10;
    // 当前统计区间下标
    private static int index = -1;

    // 统计是否进行中
    private boolean started = false;
    // 热度统计总区间
    private final long[] times = new long[TIMES_LENGTH];
    // 存放正在统计的区间数值
    private long time;
    // 所有区间的热度总值
    private long total;

    // 用于每个具体统计的计算
    private long nanos;
    // 统计计算深度, 防止嵌套的统计重复计算
    private int deep;

    /**
     * 更新区间下标
     */
    public static void nextTick() {
        index = ++index % TIMES_LENGTH;
    }

    /**
     * 开始统计一个新区间
     */
    public void newTick() {
        if (!started) {
            started = true;
            return;
        }

        // 结束前一个区间的统计
        total -= times[index];
        times[index] = time;
        total += times[index];
        time = 0L;
    }

    /**
     * 开始一个具体统计
     */
    public void startTicking() {
        if (!started || deep++ != 0) return;
        nanos = System.nanoTime();
    }

    /**
     * 结束一个具体统计
     * 将统计值计入当前热度区间
     */
    public void endTickingAndCount() {
        if (!started || --deep != 0) return;
        // 限制一个具体统计的最大值为 1,000,000
        // 有时候某个具体统计的计算值会在某1刻飙升，可能是由于保存数据到磁盘？
        time += Math.min(System.nanoTime() - nanos, 1_000_000L);
    }

    /**
     * 清空统计 (当区块卸载时)
     */
    public void clear() {
        started = false;
        Arrays.fill(times, 0L);
        time = 0L;
        total = 0L;
        nanos = 0L;
        deep = 0;
    }

    /**
     * @return 获取区块热度平均值
     */
    public long getAverage() {
        return total / ((long) TIMES_LENGTH * 20L);
    }
}
