package one.june.apimock.utils;

public class Utilities {
    public static <T> T getOrDefault(T data, T defaultData) {
      if (data == null) return defaultData;
      else return data;
    }
}
