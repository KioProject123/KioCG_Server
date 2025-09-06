package com.kiocg.world;

public class ChunkDays {
    int days;
    int nights;

    public ChunkDays() {
    }

    public ChunkDays(int days, int nights) {
        this.days = days;
        this.nights = nights;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }
}
