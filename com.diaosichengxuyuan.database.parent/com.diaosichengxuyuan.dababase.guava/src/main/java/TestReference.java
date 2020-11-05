import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * jdk中的Reference  ReferenceQueue  ReferenceHandler可以用来监控垃圾回收中的某些过程
 *
 * @author liuhaipeng
 * @date 2020/11/5
 */
public class TestReference {

    public static void main(String[] args) throws Exception {
        //创建一个强引用对象和引用队列，此时对象存活，队列为空
        ReferenceQueue<Object> rq = new ReferenceQueue<Object>();
        Object obj = new Object();
        System.out.println(obj);
        System.out.println(rq.poll());
        System.out.println();
        System.out.println();

        //将对象封装成弱引用，此时引用队列为空
        WeakReference<Object> wr = new WeakReference(obj, rq);
        System.out.println(wr.get());
        System.out.println(rq.poll());
        System.out.println();
        System.out.println();

        //断开引用关系，垃圾回收
        obj = null;
        System.gc();
        Thread.sleep(2000);

        //弱引用对象已被回收 队列中存放着被回收的对象
        System.out.println(wr.get());
        System.out.println(rq.poll());
    }

}
