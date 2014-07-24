package io.github.si14.stringsearch;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MagicEightBench {
    @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"})
    int arraySize;

    int toSearchIdx;
    String[] unsortedArray;
    String toSearchInterned;
    int foundIdx;

    @Setup
    public void prepare() {
        String[] allStrings = Misc.shortStrings;

        unsortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, unsortedArray, 0, arraySize);
        toSearchIdx = arraySize / 2;
        toSearchInterned = allStrings[toSearchIdx].intern();
        assert toSearchInterned == allStrings[toSearchIdx] : "string constants are interned";
    }

    @TearDown
    public void checkFound() {
        assert toSearchIdx == foundIdx : "didn't find the right element";
    }

    private int linearSearchUninterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private int linearSearchInterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i] == s) {
                return i;
            }
        }
        return -1;
    }

    @Benchmark
    public void benchInterned() {
        foundIdx = linearSearchInterned(toSearchInterned);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + MagicEightBench.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}