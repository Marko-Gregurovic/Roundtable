package com.example.demo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SortingUtil {

  public static long sorting(long arraySize) {
    long start = System.currentTimeMillis();
    List<String> l = new ArrayList<>();
    for (int i = 0; i <= arraySize; i++) {
      l.add(UUID.randomUUID().toString());
    }
    Collections.sort(l);
    long end = System.currentTimeMillis();
    long timeSpent = end - start;
    return timeSpent;
  }

}
