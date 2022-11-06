package org.rhx.window;

import java.util.HashMap;
import java.util.Map;

public enum Option {

    BENCHMARK("--benchmark", "-b");
    private final String longName;
    private final String shortName;

    Option(String longName, String shortName) {
        this.longName = longName;
        this.shortName = shortName;
    }


    public static Map<Option, Object> parseArgs(String[] args) {
        Map<Option, Object> result = new HashMap<>();
        for (int idx = 0; idx < args.length; idx++) {
            switch (args[idx]) {
                case "--benchmark":
                case "-b":
                    Integer count = null;
                    if (args.length >= idx + 2) {
                        String loopCount = args[++idx];
                        count = Integer.parseInt(loopCount);
                    }
                    result.put(BENCHMARK, count);
                    break;
            }
        }
        return result;
    }
}