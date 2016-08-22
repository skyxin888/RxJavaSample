package cn.golden.rxjava;

/**
 * Created by user on 16/7/19.
 */
public class Opertor {
    public static class Create{
        public static final int just = 0;
        public static final int from = 1;
        public static final int repeat = 2;
        public static final int repeatWhen = 3;
        public static final int create = 4;
        public static final int defer = 5;
        public static final int ranger = 6;
        public static final int interval = 7;
        public static final int timer = 8;
        public static final int empty = 9;
        public static final int error = 10;
        public static final int never = 11;
    }

    public static class Transform{
        public static final int map = 0;
        public static final int flatmap = 1;
        public static final int switchMap = 2;
        public static final int scan = 3;
        public static final int groupBy = 4;
        public static final int buffer= 5;
        public static final int window = 6;
        public static final int cast = 7;
    }
}
