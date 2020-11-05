import com.google.common.cache.*;

import java.util.concurrent.TimeUnit;

/**
 * @author liuhaipeng
 * @date 2020/11/2
 */
public class TestCache {

    public static void main(String[] args) throws Exception {
        TestCache testCache = new TestCache();
        testCache.test1();
    }

    /**
     * expireAfterWrite
     */
    private void test1() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(5, TimeUnit.SECONDS)
            .build();
        cache.put("1", 10);
        cache.put("2", 20);
        cache.put("3", 30);
        cache.put("4", 40);

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));
    }

    /**
     * expireAfterAccess
     */
    private void test2() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5, TimeUnit.SECONDS)
            .build();
        cache.put("1", 10);
        cache.put("2", 20);
        cache.put("3", 30);
        cache.put("4", 40);

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));
    }

    /**
     * load
     */
    private void test3() throws Exception {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(5,
            TimeUnit.SECONDS).build(new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws Exception {
                if("1".equals(key)) {
                    return 100;
                } else if("2".equals(key)) {
                    return 200;
                } else if("3".equals(key)) {
                    return 300;
                } else if("4".equals(key)) {
                    return 400;
                }
                return null;
            }
        });

        cache.put("1", 10);
        cache.put("2", 20);
        cache.put("3", 30);
        cache.put("4", 40);

        TimeUnit.SECONDS.sleep(5);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        System.out.println(cache.get("1"));
        System.out.println(cache.get("2"));
        System.out.println(cache.get("3"));
        System.out.println(cache.get("4"));
    }

    /**
     * maximumSize 通过AccessQueue进行LRU淘汰
     */
    private void test4() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(3).build();
        cache.put("1", 10);
        cache.put("2", 20);
        //2->1->3->4
        System.out.println(cache.getIfPresent("1"));
        cache.put("3", 30);
        cache.put("4", 40);

        System.out.println();
        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));
    }

    /**
     * weakValues
     */
    private void test5() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(1000,
            TimeUnit.SECONDS).weakValues().build();
        cache.put("1", new Integer(10));
        cache.put("2", new Integer(20));
        cache.put("3", new Integer(30));
        cache.put("4", new Integer(40));

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        System.gc();

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));
    }

    /**
     * weakKeys
     */
    private void test6() throws Exception {
        Cache<Object, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(1000,
            TimeUnit.SECONDS).weakKeys().build();
        cache.put(new Integer(1), new Integer(10));
        cache.put(new Integer(2), new Integer(20));
        cache.put(new Integer(3), new Integer(30));
        cache.put(new Integer(4), new Integer(40));

        //weakKeys和weakValues是通过==来判断是否相等，而不是通过equals
        System.out.println(cache.getIfPresent(new Integer(1)));
        System.out.println(cache.getIfPresent(new Integer(2)));
        System.out.println(cache.getIfPresent(new Integer(3)));
        System.out.println(cache.getIfPresent(new Integer(4)));

        Integer integer1 = new Integer(1);
        Integer integer2 = new Integer(2);
        Integer integer3 = new Integer(3);
        Integer integer4 = new Integer(4);
        cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(1000, TimeUnit.SECONDS).weakKeys()
            .build();
        cache.put(integer1, new Integer(10));
        cache.put(integer2, new Integer(20));
        cache.put(integer3, new Integer(30));
        cache.put(integer4, new Integer(40));

        //weakKeys和weakValues是通过==来判断是否相等，而不是通过equals
        System.out.println(cache.getIfPresent(integer1));
        System.out.println(cache.getIfPresent(integer2));
        System.out.println(cache.getIfPresent(integer3));
        System.out.println(cache.getIfPresent(integer4));

        integer1 = null;
        integer2 = null;
        integer3 = null;
        integer4 = null;
        System.gc();

        //weakKeys和weakValues是通过==来判断是否相等，而不是通过equals
        try {
            System.out.println(cache.getIfPresent(integer1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(cache.getIfPresent(integer2));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(cache.getIfPresent(integer3));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(cache.getIfPresent(integer4));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * removalListener 只有在手动删除或者超过容量删除时才会通知，过期删除不会通知
     */
    private void test7() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(5, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<String, Object>() {
                public void onRemoval(RemovalNotification<String, Object> notification) {
                    System.out.println(notification);
                }
            }).build();
        cache.put("1", 10);
        cache.put("2", 20);
        cache.put("3", 30);
        cache.put("4", 40);

        cache.invalidate("4");

        TimeUnit.SECONDS.sleep(5);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));
    }

    /**
     * recordStats
     */
    private void test8() throws Exception {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(5, TimeUnit.SECONDS)
            .recordStats().build();
        cache.put("1", 10);
        cache.put("2", 20);
        cache.put("3", 30);
        cache.put("4", 40);

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        TimeUnit.SECONDS.sleep(4);

        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
        System.out.println(cache.getIfPresent("4"));

        System.out.println(cache.stats());
    }

}

