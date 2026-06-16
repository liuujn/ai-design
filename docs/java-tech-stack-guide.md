# Java 后端工程师技术栈完全指南

> 涵盖核心基础、主流框架、数据存储、微服务与分布式、开发与运维工具五大板块

---

## 目录

1. [Java 核心基础 (Java SE)](#1-java-核心基础-java-se)
   - [1.0 字符串与数组](#10-字符串与数组)
   - [1.1 集合框架](#11-集合框架-collections-framework)
   - [1.2 多线程与高并发](#12-多线程与高并发-juc)
   - [1.3 JVM](#13-jvmjava-虚拟机)
   - [1.4 Java 8+ 新特性](#14-java-8-新特性)
   - [1.5 Java 版本演进](#15-java-版本演进)
2. [主流开发框架 (Frameworks)](#2-主流开发框架-frameworks)
   - [2.1 Spring Framework 核心](#21-spring-framework-核心)
   - [2.2 Spring Boot](#22-spring-boot)
   - [2.3 Spring MVC](#23-spring-mvc)
   - [2.4 MyBatis / MyBatis-Plus](#24-mybatis--mybatis-plus)
   - [2.5 JPA / Hibernate](#25-jpa--hibernate)
   - [2.6 Spring AI](#26-spring-ai)
3. [数据存储 (Data Storage)](#3-数据存储-data-storage)
   - [3.1 MySQL](#31-mysql)
   - [3.2 Redis](#32-redis)
   - [3.3 Elasticsearch](#33-elasticsearch)
   - [3.4 Oracle & PL/SQL](#34-oracle--plsql)
4. [微服务与分布式 (Microservices & Distributed)](#4-微服务与分布式-microservices--distributed)
   - [4.1 Spring Cloud Alibaba](#41-spring-cloud-alibaba)
   - [4.2 消息队列](#42-消息队列-mq)
   - [4.3 Docker](#43-docker)
5. [开发与运维工具 (Tools & DevOps)](#5-开发与运维工具-tools--devops)
   - [5.1 Git](#51-git)
   - [5.2 Maven / Gradle](#52-maven--gradle)
   - [5.3 单元测试](#53-单元测试-junit--mockito)
   - [5.4 Linux](#54-linux)

---

## 1. Java 核心基础 (Java SE)

### 1.0 字符串与数组

#### 字符串 (String)

```java
// ========== 创建字符串 ==========
String s1 = "hello";                          // 字面量（字符串常量池）
String s2 = new String("hello");             // 堆上新对象
String s3 = "hello";                          // 复用常量池中的同一个对象
System.out.println(s1 == s2);                 // false（不同对象）
System.out.println(s1 == s3);                 // true（同一常量池引用）
System.out.println(s1.equals(s2));            // true（比较内容）

// ========== 字符串不可变性 ==========
String s = "hello";
s = s + " world";  // 实际创建了新对象，原 "hello" 仍在常量池中
// StringBuilder 优于字符串拼接的场景：
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100; i++) {
    sb.append("item").append(i).append(",");
}
String result = sb.toString();

// ========== 常用方法 ==========
String str = "  Hello, Java World!  ";

str.length();                        // 19
str.isEmpty();                       // false
str.isBlank();                       // false（Java 11+，忽略空白）
str.charAt(0);                       // ' '
str.substring(2, 7);                 // "Hello"
str.indexOf("Java");                 // 9
str.lastIndexOf("o");                // 13
str.contains("Java");                // true
str.startsWith("  He");              // true
str.endsWith("!  ");                 // true

str.replace("Java", "Spring");       // "  Hello, Spring World!  "
str.replaceAll("\\s+", " ");         // " Hello, Java World! "（正则）
str.replaceFirst("\\s+", "");        // "Hello,  Java World!  "

str.toUpperCase();                   // "  HELLO, JAVA WORLD!  "
str.toLowerCase();                   // "  hello, java world!  "
str.trim();                          // "Hello, Java World!"
str.strip();                         // "Hello, Java World!"（Java 11+，支持 Unicode）
str.stripLeading();                  // "Hello, Java World!  "
str.stripTrailing();                 // "  Hello, Java World!"

"abc".concat("def");                 // "abcdef"
String.join(", ", "a", "b", "c");    // "a, b, c"
String.join("-", List.of("x","y"));  // "x-y"

str.repeat(3);                       // Java 11+

// ========== 比较 ==========
"abc".equals("ABC");                 // false
"abc".equalsIgnoreCase("ABC");       // true
"abc".compareTo("abd");              // -1（字典序，c < d）
"abc".compareToIgnoreCase("ABC");    // 0

// ========== 分割与拼接 ==========
String csv = "a,b,c,d";
String[] parts = csv.split(",");            // ["a", "b", "c", "d"]
String joined = String.join("-", parts);    // "a-b-c-d"

// ========== 转换 ==========
String.valueOf(123);                        // "123"
String.format("hello %s, age %d", "Tom", 25); // "hello Tom, age 25"
Integer.parseInt("100");                    // 100
Long.parseLong("100");                      // 100L
Double.parseDouble("3.14");                 // 3.14
Integer.parseInt("100");                    // 100
Integer.valueOf("100");                     // 100（Integer 对象）

// ========== 字符与字节 ==========
str.toCharArray();                          // char[]
str.getBytes(StandardCharsets.UTF_8);       // byte[]
new String(bytes, StandardCharsets.UTF_8);  // byte[] → String

// ========== 文本块 (Text Block, Java 13+/15+) ==========
String json = """
    {
        "name": "张三",
        "age": 25,
        "hobbies": ["读书", "跑步"]
    }
    """;
String html = """
    <html>
        <body>
            <h1>Hello</h1>
        </body>
    </html>
    """.stripIndent().stripTrailing();

// ========== StringBuilder / StringBuffer ==========
// StringBuilder：线程不安全，性能最好（95% 场景用这个）
// StringBuffer：线程安全（方法用 synchronized），性能较差
StringBuilder builder = new StringBuilder();
builder.append("SELECT * FROM user WHERE 1=1");
if (name != null) builder.append(" AND name = ?");
if (age != null)  builder.append(" AND age = ?");
String sql = builder.toString();  // 动态 SQL 拼接

// ========== 字符串池与 intern() ==========
String a = new String("hello");
String b = a.intern();            // 从常量池中获取
String c = "hello";
System.out.println(b == c);       // true（intern 返回常量池引用）
```

#### 数组 (Array)

```java
// ========== 声明与初始化 ==========
int[] arr1 = new int[5];                     // 默认值 [0, 0, 0, 0, 0]
int[] arr2 = {1, 2, 3, 4, 5};               // 静态初始化
int[] arr3 = new int[]{1, 2, 3, 4, 5};       // 静态初始化（匿名数组）
int[] arr4;  arr4 = new int[]{1, 2, 3};      // 声明和初始化分开

String[] names = {"Alice", "Bob", "Charlie"};
Integer[] nums = {10, 20, 30};

// ========== 访问与遍历 ==========
arr2[0] = 10;                                // 赋值
System.out.println(arr2[2]);                 // 访问（输出 3）
System.out.println(arr2.length);             // 长度（属性，不是方法）

// for 循环
for (int i = 0; i < arr2.length; i++) {
    System.out.println(arr2[i]);
}

// for-each（增强 for 循环，不能修改元素）
for (int num : arr2) {
    System.out.println(num);
}

// Arrays.stream（Java 8+）
Arrays.stream(arr2).forEach(System.out::println);
Arrays.stream(arr2).filter(n -> n > 2).toArray();
Arrays.stream(arr2).map(n -> n * 2).toArray();

// ========== Arrays 工具类 ==========

int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};

Arrays.sort(arr);                                // 升序排序 [1, 1, 2, 3, 4, 5, 6, 9]
Arrays.parallelSort(arr);                        // 并行排序（数据量大时更快）

int idx = Arrays.binarySearch(arr, 5);           // 二分查找（需先排序）

int[] copy = Arrays.copyOf(arr, arr.length);     // 完整复制
int[] copyRange = Arrays.copyOfRange(arr, 1, 4); // 复制范围 [1,4) → [1, 1, 2]

Arrays.fill(arr, 0);                             // 全部填充为 0
Arrays.fill(arr, 2, 5, -1);                      // 范围填充 [2,5) → -1

// 比较
Arrays.equals(new int[]{1,2,3}, new int[]{1,2,3});       // true
Arrays.deepEquals(new int[][]{{1},{2}}, new int[][]{{1},{2}}); // 多维比较

// 转字符串
System.out.println(Arrays.toString(arr));                // "[1, 1, 2, 3, 4, 5, 6, 9]"
System.out.println(Arrays.deepToString(new int[][]{{1,2},{3,4}})); // "[[1, 2], [3, 4]]"

// 转 List（注意：返回的是固定大小的 List，不支持 add/remove）
List<int[]> list = Arrays.asList(arr);          // 错误！基本类型数组作为整体
List<Integer> list2 = Arrays.asList(1, 2, 3);   // 正确
// 包装类型数组才正确：
Integer[] boxed = {1, 2, 3};
List<Integer> list3 = Arrays.asList(boxed);     // ✅ 正确

// 基本类型 ↔ 包装类型数组转换（Java 8+）
int[] primitive = {1, 2, 3};
Integer[] boxedArr = Arrays.stream(primitive).boxed().toArray(Integer[]::new);
int[] back = Arrays.stream(boxedArr).mapToInt(Integer::intValue).toArray();

// ========== 多维数组 ==========
int[][] matrix = new int[3][4];                          // 3 行 4 列
int[][] matrix2 = {{1,2,3}, {4,5,6}, {7,8,9}};           // 3x3
int[][] ragged = new int[3][];                            // 不规则数组
ragged[0] = new int[]{1, 2};
ragged[1] = new int[]{3, 4, 5};
ragged[2] = new int[]{6};

// 遍历二维数组
for (int i = 0; i < matrix2.length; i++) {
    for (int j = 0; j < matrix2[i].length; j++) {
        System.out.print(matrix2[i][j] + " ");
    }
    System.out.println();
}
for (int[] row : matrix2) {
    for (int val : row) {
        System.out.print(val + " ");
    }
    System.out.println();
}

// ========== 可变参数 (Varargs) ==========
public void printAll(String... args) {
    for (String arg : args) {
        System.out.println(arg);
    }
}
printAll("a", "b", "c");                     // 直接传
printAll(new String[]{"x", "y"});            // 传数组

// ========== 数组与 List 互转 ==========
// 数组 → List
List<String> listFromArray = Arrays.asList("a", "b", "c");          // 固定大小
List<String> modifiableList = new ArrayList<>(Arrays.asList("a", "b", "c")); // 可修改

// List → 数组
String[] arrFromList = listFromArray.toArray(new String[0]);
String[] arrFromList2 = listFromArray.toArray(new String[listFromArray.size()]); // 推荐

// Java 11+：List.of 返回不可变 List
List<String> immutable = List.of("a", "b", "c");

// ========== 数组排序（对象） ==========
// 对象数组排序要求对象实现 Comparable 或传入 Comparator
String[] words = {"banana", "apple", "cherry", "date"};
Arrays.sort(words);                                           // 自然排序
Arrays.sort(words, Comparator.reverseOrder());                // 降序
Arrays.sort(words, Comparator.comparingInt(String::length));  // 按长度排序

// 自定义对象排序
record Person(String name, int age) {}
Person[] people = {new Person("Alice", 30), new Person("Bob", 25), new Person("Charlie", 35)};
Arrays.sort(people, Comparator.comparingInt(Person::age));    // 按年龄升序

// ========== 性能提示 ==========
// 1. 数组是连续内存，访问 O(1)，插入/删除 O(n)
// 2. 频繁插入删除用 ArrayList（内部也是数组，但封装了扩容）
// 3. 大数组考虑并行排序：Arrays.parallelSort()
// 4. 除非确定大小，否则优先用 ArrayList
```

### 1.1 集合框架 (Collections Framework)

Java 集合框架位于 `java.util` 包下，主要分为 `Collection`（单列集合）和 `Map`（双列集合）两大体系。

#### Collection 体系

```
Collection
  ├── List（有序、可重复）
  │    ├── ArrayList  → 基于数组，查询快，增删慢
  │    ├── LinkedList → 基于双向链表，增删快，查询慢
  │    └── Vector     → 线程安全（已过时，用 CopyOnWriteArrayList 替代）
  ├── Set（无序、不可重复）
  │    ├── HashSet        → 基于 HashMap，无序
  │    ├── LinkedHashSet  → 维持插入顺序
  │    └── TreeSet        → 基于红黑树，可排序
  └── Queue（队列）
       ├── LinkedList         → 双向队列
       ├── PriorityQueue      → 优先级队列（堆实现）
       └── ArrayDeque         → 双端队列，比 LinkedList 更高效
```

#### Map 体系

```
Map
  ├── HashMap          → 基于数组+链表+红黑树，最常用
  ├── LinkedHashMap    → 维持插入顺序或访问顺序（LRU 缓存）
  ├── TreeMap          → 基于红黑树，Key 可排序
  ├── Hashtable        → 线程安全（已过时）
  └── ConcurrentHashMap → 线程安全且高效（分段锁/CAS）
```

#### 核心集合使用示例

```java
// ========== ArrayList ==========
List<String> list = new ArrayList<>();
list.add("Java");
list.add("Python");
list.add("Go");
list.get(0);                    // 随机访问 O(1)
list.remove(1);                 // 删除元素，后续元素前移 O(n)

// 使用迭代器安全删除
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("Go")) it.remove();
}

// ========== HashMap ==========
Map<String, Integer> map = new HashMap<>(16, 0.75f); // 初始容量、负载因子
map.put("Alice", 90);
map.put("Bob", 85);
map.put("Charlie", 95);
map.get("Alice");                                    // O(1)
map.getOrDefault("David", 0);                        // 不存在时返回默认值

// 遍历
map.forEach((k, v) -> System.out.println(k + "=" + v));

// ========== ConcurrentHashMap ==========
// 高并发场景下的线程安全 HashMap
ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
concurrentMap.put("key", 1);
concurrentMap.computeIfAbsent("key2", k -> 2);       // 不存在则计算
concurrentMap.merge("key", 1, Integer::sum);         // 原子合并

// ========== HashSet ==========
Set<String> set = new HashSet<>();
set.add("a");
set.add("b");
set.contains("a");                     // true

// ========== TreeMap（有序） ==========
TreeMap<String, Integer> treeMap = new TreeMap<>();
treeMap.put("2025-01-01", 100);
treeMap.put("2025-01-03", 200);
treeMap.put("2025-01-02", 150);
treeMap.firstKey();                    // 2025-01-01
treeMap.lastKey();                     // 2025-01-03
treeMap.subMap("2025-01-01", "2025-01-03"); // 范围查询

// ========== PriorityQueue（优先级队列） ==========
Queue<Integer> pq = new PriorityQueue<>();  // 小顶堆
pq.offer(30);
pq.offer(10);
pq.offer(20);
pq.poll();  // 10（最小元素优先出队）

// 大顶堆
Queue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
```

#### ArrayList vs LinkedList 对比

| 维度 | ArrayList | LinkedList |
|------|-----------|------------|
| 底层结构 | 动态数组 | 双向链表 |
| 随机访问 get/set | O(1) | O(n) |
| 头部插入/删除 | O(n)（移动元素） | O(1) |
| 尾部插入 | O(1)（可能触发扩容） | O(1) |
| 内存占用 | 少（只存数据） | 多（额外存前后指针） |
| 适用场景 | 查询多、尾部增删 | 频繁头尾增删 |

#### HashMap 核心原理

- **数据结构**：数组 + 链表（哈希冲突） + 红黑树（链表长度 ≥8 且数组长度 ≥64 时树化）
- **put 流程**：计算 key 的 hash → 定位数组下标 → 若为空直接插入 → 否则遍历链表/红黑树 → 找到则覆盖，未找到则插入
- **扩容机制**：默认容量 16，负载因子 0.75，扩容阈值 = 容量 × 负载因子。扩容为原来的 2 倍，元素重新散列
- **线程不安全**：JDK 1.7 头插法会导致死循环，JDK 1.8 尾插法避开了死循环但仍存在数据覆盖问题

```java
// 自定义初始容量避免频繁扩容（已知数据量时）
int expectedSize = 1000;
// 容量 = expectedSize / 0.75 + 1，防止触发扩容
Map<String, Object> map = new HashMap<>((int) (expectedSize / 0.75f) + 1);
```

---

### 1.2 多线程与高并发 (JUC)

JUC 即 `java.util.concurrent` 包，是 Java 并发编程的核心工具包。

#### 创建线程的方式

```java
// ========== 1. 继承 Thread（不推荐，Java 单继承限制） ==========
class MyThread extends Thread {
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " 运行中");
    }

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.setName("worker-1");
        t.start();  // 启动线程，自动调用 run()
    }
}

// ========== 2. 实现 Runnable（推荐，更灵活） ==========
class PrintTask implements Runnable {
    private final String message;
    private final int count;

    public PrintTask(String message, int count) {
        this.message = message;
        this.count = count;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (int i = 0; i < count; i++) {
            System.out.println(name + " → " + message + " #" + (i + 1));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " 被中断");
                return;
            }
        }
    }

    public static void main(String[] args) {
        // 方式 A：传入 Runnable 实现类
        Thread t1 = new Thread(new PrintTask("Hello", 3), "worker-1");
        t1.start();

        // 方式 B：Lambda 表达式
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + " Lambda #" + (i + 1));
            }
        }, "worker-2");
        t2.start();
    }
}

// ========== 3. 实现 Callable（有返回值、可抛异常） ==========
class CalculationTask implements Callable<Integer> {
    private final int start;
    private final int end;

    public CalculationTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println(name + " 计算 " + start + " 到 " + end);
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
            Thread.sleep(10);  // 模拟耗时
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        // 通过 FutureTask 包装 Callable
        FutureTask<Integer> task = new FutureTask<>(new CalculationTask(1, 100));
        Thread t = new Thread(task, "calc-thread");
        t.start();

        // 阻塞获取结果（可设置超时）
        Integer result = task.get(5, TimeUnit.SECONDS);
        System.out.println("计算结果: " + result);

        // 异常处理方式
        if (task.isDone()) {
            try {
                Integer value = task.get();
                System.out.println("结果: " + value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("计算被中断");
            } catch (ExecutionException e) {
                System.out.println("计算异常: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("计算被取消");
            }
        }

        // 取消任务
        boolean cancelled = task.cancel(true);
        System.out.println("取消结果: " + cancelled);
    }
}

// ========== 4. 线程池（最推荐的方式） ==========
class ThreadPoolExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // execute(Runnable)：无返回值
        executor.execute(() -> System.out.println(Thread.currentThread().getName() + " 执行任务"));

        // submit(Callable)：有返回值
        Future<String> future = executor.submit(() -> {
            Thread.sleep(500);
            return "Callable 结果";
        });
        String resultFromPool = future.get(3, TimeUnit.SECONDS);
        System.out.println(resultFromPool);

        // invokeAll：批量提交，等待全部完成
        List<Callable<Integer>> tasks = Arrays.asList(
            () -> { Thread.sleep(100); return 1; },
            () -> { Thread.sleep(200); return 2; },
            () -> { Thread.sleep(150); return 3; }
        );
        List<Future<Integer>> futures = executor.invokeAll(tasks);
        for (Future<Integer> f : futures) {
            System.out.println("批量结果: " + f.get());
        }

        // invokeAny：批量提交，返回最快完成的结果
        Integer fastest = executor.invokeAny(Arrays.asList(
            () -> { Thread.sleep(300); return 100; },
            () -> { Thread.sleep(100); return 200; },
            () -> { Thread.sleep(200); return 300; }
        ));
        System.out.println("最快返回: " + fastest);

        executor.shutdown();  // 不再接受新任务，等待已提交任务完成
        // executor.shutdownNow();  // 立即停止
    }
}
```

#### 线程池 (ThreadPoolExecutor)

线程池的核心参数：

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,              // corePoolSize：核心线程数
    5,              // maximumPoolSize：最大线程数
    60L,            // keepAliveTime：非核心线程空闲存活时间
    TimeUnit.SECONDS, // 时间单位
    new LinkedBlockingQueue<>(10), // workQueue：任务队列
    Executors.defaultThreadFactory(), // threadFactory：线程工厂
    new ThreadPoolExecutor.AbortPolicy()  // 拒绝策略
);
```

**拒绝策略：**
- `AbortPolicy`：直接抛 `RejectedExecutionException`（默认）
- `CallerRunsPolicy`：调用者线程自己执行任务
- `DiscardPolicy`：直接丢弃任务
- `DiscardOldestPolicy`：丢弃队列中最旧的任务，重新提交

**使用 Executors 创建线程池的陷阱：**

```java
// ❌ 危险！队列无界，可能 OOM
ExecutorService fixed = Executors.newFixedThreadPool(10);
ExecutorService single = Executors.newSingleThreadExecutor();

// ❌ 危险！最大线程数 Integer.MAX_VALUE，可能创建过多线程
ExecutorService cached = Executors.newCachedThreadPool();
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);

// ✅ 推荐：手动创建 ThreadPoolExecutor
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    Runtime.getRuntime().availableProcessors(), // corePoolSize = CPU 核数
    Runtime.getRuntime().availableProcessors() * 2,
    60L, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100),  // 有界队列
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

#### 锁机制

```java
// ========== synchronized ==========
class SyncExample {
    // 1. 同步实例方法：锁的是 this
    public synchronized void method() { /* ... */ }

    // 2. 同步静态方法：锁的是 Class 对象
    public static synchronized void staticMethod() { /* ... */ }

    // 3. 同步代码块：锁粒度更细
    public void block() {
        synchronized (this) { /* ... */ }
    }
}

// ========== ReentrantLock（可重入锁） ==========
class LockExample {
    private final ReentrantLock lock = new ReentrantLock(true); // true = 公平锁

    public void doSomething() {
        lock.lock();
        try {
            // 临界区
        } finally {
            lock.unlock(); // 必须在 finally 中释放
        }
    }
}

// ========== ReentrantReadWriteLock（读写锁） ==========
// 读-读不互斥，读-写互斥，写-写互斥
class ReadWriteLockExample {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void read() {
        readLock.lock();
        try { /* 读数据 */ } finally { readLock.unlock(); }
    }

    public void write() {
        writeLock.lock();
        try { /* 写数据 */ } finally { writeLock.unlock(); }
    }
}

// ========== StampedLock（更高效的读写锁，JDK 8） ==========
class StampedLockExample {
    private final StampedLock stampedLock = new StampedLock();
    private int data;

    public void read() {
        long stamp = stampedLock.readLock();
        try { /* 读 */ } finally { stampedLock.unlockRead(stamp); }
    }

    public int optimisticRead() {
        // 乐观读（不会阻塞写操作，性能更高）
        long stamp = stampedLock.tryOptimisticRead();
        int current = data;
        if (!stampedLock.validate(stamp)) { // 检查是否被写过
            stamp = stampedLock.readLock();
            try { current = data; } finally { stampedLock.unlockRead(stamp); }
        }
        return current;
    }
}
```

#### volatile 关键字

- **保证可见性**：一个线程修改变量后，其他线程立即可见
- **禁止指令重排序**：防止 JVM 对代码进行优化重排
- **不保证原子性**：`count++` 这种操作仍然线程不安全

```java
// 典型用法：状态标志
private volatile boolean running = true;

public void stop() { running = false; }

public void run() {
    while (running) {
        // 执行任务，不用加锁就能感知到 running 的变化
    }
}

// 另一种经典用法：双重检查锁单例
class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton(); // volatile 防止指令重排
                }
            }
        }
        return instance;
    }
}
```

#### CAS (Compare And Swap)

CAS 是一种无锁算法，通过 CPU 原子指令实现，JUC 中很多工具类都基于 CAS。

```java
// ========== 原子类 ==========
AtomicInteger count = new AtomicInteger(0);

count.incrementAndGet();                // ++i
count.getAndIncrement();                // i++
count.addAndGet(5);                     // += 5
count.compareAndSet(6, 10);            // 如果当前值是 6 就更新为 10

// 对象中的字段原子更新
AtomicReference<String> ref = new AtomicReference<>("initial");
ref.compareAndSet("initial", "updated");

// 高性能计数器（LongAdder 比 AtomicLong 在高并发下性能更好）
LongAdder adder = new LongAdder();
adder.increment();
adder.add(10);
long sum = adder.sum();
```

**CAS 的三大问题：**
1. **ABA 问题**：变量从 A→B→A，CAS 误认为没变过。可用 `AtomicStampedReference` 加版本号解决
2. **自旋开销大**：长时间自旋会消耗 CPU
3. **只能保证一个共享变量的原子操作**

```java
// ABA 问题解决示例
AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);
int[] stampHolder = new int[1];
String value = ref.get(stampHolder); // stampHolder[0] = 0
ref.compareAndSet("A", "B", stampHolder[0], stampHolder[0] + 1);
```

#### 并发工具类

```java
// ========== CountDownLatch（倒计时器，等待所有线程完成） ==========
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        System.out.println("任务完成");
        latch.countDown(); // 计数 -1
    }).start();
}
latch.await(); // 等待计数归零
System.out.println("所有任务完成");

// ========== CyclicBarrier（循环屏障，等待线程到达屏障后一起执行） ==========
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("所有线程已到达屏障，执行汇总任务");
});
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            System.out.println("线程到达");
            barrier.await(); // 等待其他线程
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            System.out.println("屏障被破坏: " + e.getMessage());
        }
    }).start();
}

// ========== Semaphore（信号量，限流） ==========
Semaphore semaphore = new Semaphore(3); // 同时最多 3 个线程访问
for (int i = 0; i < 10; i++) {
    new Thread(() -> {
        try {
            semaphore.acquire();
            System.out.println("获取许可，执行任务");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }).start();
}
```

#### ThreadLocal（线程局部变量）

每个线程拥有自己的独立副本，互不干扰。

```java
// 典型用法：保存当前登录用户信息
public class UserContext {
    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void set(User user) { USER_HOLDER.set(user); }
    public static User get() { return USER_HOLDER.get(); }
    public static void remove() { USER_HOLDER.remove(); }
}

// 拦截器中设置
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        User user = authService.parseToken(request.getHeader("Token"));
        UserContext.set(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.remove(); // 必须清理，防止内存泄漏
    }
}

// 服务层使用
@Service
public class OrderService {
    public void createOrder(Order order) {
        User currentUser = UserContext.get(); // 获取当前用户
        order.setUserId(currentUser.getId());
        orderDao.insert(order);
    }
}
```

**⚠️ 注意**：使用完必须调用 `remove()`，否则在线程池场景下会导致内存泄漏！

#### CompletableFuture（异步编排，JDK 8+）

```java
// ========== 创建异步任务 ==========
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    System.out.println("无返回值异步任务");
});

CompletableFuture<String> supply = CompletableFuture.supplyAsync(() -> {
    return "异步结果";
});

// ========== 链式编排 ==========
CompletableFuture.supplyAsync(() -> {
    return "Hello";
}).thenApplyAsync(result -> {
    return result + " World";
}).thenAcceptAsync(result -> {
    System.out.println(result); // Hello World
}).exceptionally(ex -> {
    System.err.println("异常：" + ex.getMessage());
    return null;
});

// ========== 多任务组合 ==========
CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> "B");

// 合并两个任务的结果
task1.thenCombineAsync(task2, (a, b) -> a + b).thenAccept(System.out::println); // AB

// 等待所有任务完成
CompletableFuture.allOf(task1, task2).join();

// 任意一个完成就继续
CompletableFuture.anyOf(task1, task2).thenAccept(System.out::println);
```

---

### 1.3 JVM（Java 虚拟机）

#### JVM 内存模型（运行时数据区）

```
┌─────────────────────────────────────────────────────┐
│                    堆 (Heap)                          │
│  ┌───────────┐ ┌───────────┐ ┌────────────────────┐ │
│  │  新生代     │ │  老年代    │ │   元空间 (Metaspace) │ │
│  │ Eden + S0+S1│ │           │ │   (JDK 8+，本地内存) │ │
│  │           │ │           │ │                    │ │
│  └───────────┘ └───────────┘ └────────────────────┘ │
├─────────────────────────────────────────────────────┤
│  虚拟机栈 (VM Stack)   本地方法栈 (Native Stack)      │
│  (每个线程一个栈，包含栈帧)  (native 方法调用栈)         │
├─────────────────────────────────────────────────────┤
│  程序计数器 (PC Register)                             │
│  (记录当前线程执行的字节码行号)                        │
└─────────────────────────────────────────────────────┘
```

**JDK 8 之后的区别：**
- ❌ 永久代 (PermGen) → ✅ 元空间 (Metaspace)
- 元空间使用本地内存（Native Memory），不再受 JVM 最大内存限制
- 字符串常量池从永久代移到了堆中

#### 对象创建过程

```
1. 类加载检查 → 2. 分配内存（指针碰撞 / 空闲列表）
   → 3. 初始化零值 → 4. 设置对象头（Mark Word + 类型指针）
   → 5. 执行 \<init> 方法
```

#### 垃圾回收 (GC)

**判断对象是否存活：**
- **引用计数法**：无法解决循环引用（JVM 未采用）
- **可达性分析**：从 GC Roots 出发，不可达的对象即为垃圾

**GC Roots 包括：**
- 虚拟机栈中引用的对象
- 本地方法栈中引用的对象
- 方法区中静态属性引用的对象
- 方法区中常量引用的对象

#### 垃圾收集算法

| 算法 | 描述 | 适用区域 |
|------|------|----------|
| **标记-清除** | 标记垃圾后直接清除，有内存碎片 | 老年代 |
| **标记-复制** | 将内存分为两块，存活对象复制到空闲块，无碎片 | 新生代（Eden → Survivor） |
| **标记-整理** | 标记后让存活对象向一端移动，无碎片 | 老年代 |
| **分代收集** | 新生代复制、老年代标记整理，目前主流方式 | 整堆 |

#### 垃圾收集器

```
新生代收集器：
  Serial（单线程，暂停所有用户线程）        
  ParNew（Serial 的多线程版本）
  Parallel Scavenge（关注吞吐量）

老年代收集器：
  Serial Old（单线程老年代版）
  Parallel Old（Parallel Scavenge 老年代版）
  CMS（Concurrent Mark Sweep，低延迟，有内存碎片）

跨代收集器：
  G1（Garbage First，JDK 9+ 默认，替代 CMS）
  ZGC（JDK 15+，极低延迟 <10ms）
  Shenandoah（类似 ZGC）
```

**G1 收集器特点（JDK 9+ 默认）：**
- 将堆划分为多个 Region（1~32MB）
- 不再区分新生代和老年代的物理隔离
- 维护一个优先级列表，优先回收收益最大的 Region
- 可以通过 `-XX:MaxGCPauseMillis=200` 设定预期暂停时间

#### JVM 常用参数

```bash
# 堆设置
-Xms4g                  # 初始堆大小 4G
-Xmx4g                  # 最大堆大小 4G
-Xmn2g                  # 新生代大小 2G
-XX:MetaspaceSize=256m  # 元空间初始大小
-XX:MaxMetaspaceSize=512m # 元空间最大大小

# GC 选择
-XX:+UseG1GC            # 使用 G1 收集器（JDK 9+ 默认）
-XX:+UseParallelGC      # 使用 Parallel 收集器
-XX:+UseConcMarkSweepGC # 使用 CMS 收集器（JDK 14 废弃）

# 日志与诊断
-XX:+PrintGCDetails             # 打印 GC 详细日志
-XX:+PrintGCDateStamps          # 打印 GC 时间戳
-Xloggc:/path/to/gc.log         # GC 日志输出文件
-XX:+HeapDumpOnOutOfMemoryError # OOM 时自动 dump 堆
-XX:HeapDumpPath=/path/to/dump  # dump 路径

# 性能调优
-XX:MaxGCPauseMillis=200        # G1 最大 GC 停顿时间（毫秒）
-Xss512k                        # 每个线程栈大小
```

#### OOM 排查步骤

```bash
# 1. 使用 jps 查看 Java 进程
jps -l

# 2. 使用 jstat 监控 GC 情况（每 1 秒输出一次，共 10 次）
jstat -gcutil <pid> 1000 10

# 3. 使用 jmap 生成堆转储
jmap -dump:live,format=b,file=heap.hprof <pid>

# 4. 使用 jstack 查看线程堆栈（排查死锁、长时间等待）
jstack <pid> > thread_dump.txt

# 5. 使用 MAT 或 VisualVM 分析 heap.hprof
# 寻找：大对象、GC Roots 引用链、内存泄漏嫌疑对象
```

---

### 1.4 Java 8+ 新特性

#### Lambda 表达式

```java
// 传统写法
Comparator<String> oldWay = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
};

// Lambda 写法
Comparator<String> lambda = (a, b) -> a.length() - b.length();

// 方法引用（更简洁）
Comparator<String> methodRef = Comparator.comparingInt(String::length);

// 常见用法
Button button = new Button("Submit");
button.setOnAction(e -> System.out.println("Clicked"));
```

#### Stream API

Stream API 提供声明式的数据处理方式，支持链式操作和并行处理。

```java
// ========== 创建 Stream ==========
List<String> list = Arrays.asList("apple", "banana", "cherry");
Stream<String> stream1 = list.stream();                 // 集合
Stream<String> stream2 = Stream.of("a", "b", "c");      // 直接创建
IntStream stream3 = Arrays.stream(new int[]{1, 2, 3});  // 数组
Stream<Integer> stream4 = Stream.iterate(0, n -> n + 2); // 无限流

// ========== 中间操作（延迟执行） ==========
list.stream()
    .filter(s -> s.startsWith("a"))          // 过滤
    .map(String::toUpperCase)                // 转换
    .flatMap(s -> Arrays.stream(s.split(""))) // 扁平化
    .distinct()                              // 去重
    .sorted()                                // 排序
    .limit(2);                               // 截断

// ========== 终端操作（触发执行） ==========
// 收集
List<String> resultList = list.stream()
    .filter(s -> s.length() > 3)
    .collect(Collectors.toList());

// 分组
Map<Integer, List<String>> grouped = list.stream()
    .collect(Collectors.groupingBy(String::length));
// {5=[apple], 6=[banana, cherry]}

// 分区（true/false）
Map<Boolean, List<String>> partitioned = list.stream()
    .collect(Collectors.partitioningBy(s -> s.length() > 5));

// 拼接
String joined = list.stream().collect(Collectors.joining(", "));
// apple, banana, cherry

// 统计
IntSummaryStatistics stats = list.stream()
    .collect(Collectors.summarizingInt(String::length));
stats.getSum();    // 17
stats.getAverage();// 5.67

// 归约
int sum = Arrays.asList(1, 2, 3, 4, 5).stream()
    .reduce(0, Integer::sum); // 15

// ========== 并行流（多线程处理） ==========
long count = list.parallelStream()
    .filter(s -> s.startsWith("a"))
    .count();

// 使用注意：并行流使用 ForkJoinPool，适合 CPU 密集型大任务
// 不要用于有状态操作或 IO 密集型
```

#### Optional（优雅的空指针处理）

```java
User user = new User("张三", 25);

// ========== 创建 Optional ==========
Optional<String> empty = Optional.empty();                     // 空
Optional<String> nonNull = Optional.of("hello");               // 非空，传 null 会抛 NPE
Optional<String> nullable = Optional.ofNullable(user.getName()); // 可为空

// ========== 安全取值 ==========
// 传统做法
String name = user != null ? user.getName() : "default";

// Optional 做法
String name2 = Optional.ofNullable(user)
    .map(User::getName)
    .orElse("default"); // 或 orElseGet(() -> computeDefault())

// 抛出异常
String result = Optional.ofNullable(user.getName())
    .orElseThrow(() -> new IllegalArgumentException("value is null"));

// 消费
Optional.ofNullable(user.getName()).ifPresent(v -> System.out.println(v));

// 过滤与转换
Optional.ofNullable(user)
    .filter(u -> u.getAge() >= 18)
    .map(User::getName)
    .ifPresent(System.out::println);

// ========== 实际业务场景 ==========
public Address getShippingAddress(Order order) {
    return Optional.ofNullable(order)
        .map(Order::getUser)
        .map(User::getAddress)
        .orElseThrow(() -> new BusinessException("地址不存在"));
}
```

#### 新时间日期 API (java.time)

```java
// ==========  LocalDate / LocalTime / LocalDateTime ==========
LocalDate today = LocalDate.now();                         // 2025-06-02
LocalDate birthday = LocalDate.of(1995, Month.MARCH, 15);  // 1995-03-15
LocalDate nextWeek = today.plusWeeks(1);                   // +1 周
LocalDate lastMonth = today.minusMonths(1);                // -1 月

LocalTime now = LocalTime.now();                           // 10:30:00
LocalTime meeting = LocalTime.of(14, 30);

LocalDateTime dateTime = LocalDateTime.now();
LocalDateTime nextMeeting = dateTime.withHour(14).withMinute(30);

// ========== Instant（时间戳） ==========
Instant timestamp = Instant.now();                         // UTC 时间戳
Instant epoch = Instant.ofEpochMilli(System.currentTimeMillis());

// ========== Duration / Period ==========
Duration duration = Duration.between(startTime, endTime);  // 秒/纳秒精度
duration.toMinutes();                                      // 相差分钟数

Period period = Period.between(birthday, today);           // 年月日
period.getYears();                                         // 年龄

// ========== DateTimeFormatter ==========
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String formatted = LocalDateTime.now().format(formatter);  // 格式化
LocalDateTime parsed = LocalDateTime.parse("2025-06-02 10:30:00", formatter); // 解析

// ========== 时区处理 ==========
ZonedDateTime zoned = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
ZonedDateTime utc = zoned.withZoneSameInstant(ZoneOffset.UTC);
```

---

### 1.5 Java 版本演进（仅 LTS 版本）

#### Java 8 (2014) — 里程碑版本

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

// Lambda 表达式
list.stream().filter(x -> x > 5).collect(Collectors.toList());

// Stream API
int sum = list.stream().mapToInt(Integer::intValue).sum();

User user = new User("张三", 25);

// Optional
String name = Optional.ofNullable(user).map(User::getName).orElse("default");

// 新时间日期 API
LocalDateTime now = LocalDateTime.now();

// 接口默认方法和静态方法
public interface MyInterface {
    default void log() { System.out.println("default"); }
    static void util() { /* ... */ }
}

// CompletableFuture（异步编排）
CompletableFuture.supplyAsync(() -> "Hello").thenApply(s -> s + " World");

// 方法引用
list.forEach(System.out::println);

// Collectors 增强
List<User> users = Arrays.asList(new User("Alice", 30), new User("Bob", 25));
Map<String, List<User>> byType = users.stream()
    .collect(Collectors.groupingBy(User::getType));
```

**关键特性：** Lambda / Stream API / Optional / 新时间日期 API / CompletableFuture / 接口默认方法 / 方法引用

---

#### Java 11 (2018 LTS)

```java
String value = "  hello  ";

// 字符串增强
value.strip();                           // "hello"
"hello".repeat(3);                      // "hellohellohello"
"  A  ".isBlank();                      // false

// Optional.isEmpty()
Optional.ofNullable(value).isEmpty();

// Files.readString / writeString（简化文件读写）
try {
    String content = Files.readString(Path.of("test.txt"));
    Files.writeString(Path.of("output.txt"), "Hello");
} catch (IOException e) {
    System.err.println("文件读写失败: " + e.getMessage());
}

// HttpClient（标准化）
try {
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com"))
        .GET()
        .build();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
} catch (IOException | InterruptedException e) {
    System.err.println("HTTP 请求失败: " + e.getMessage());
}

// 集合转数组
List<String> items = List.of("a", "b", "c");
String[] array = items.toArray(String[]::new);

// 支持运行单个 Java 源文件（无需编译）
// java HelloWorld.java
```

**关键特性：** 字符串增强 / HttpClient 标准化 / Files 读写简化 / 单文件源码执行 / ZGC 实验性引入 / 移除 JavaEE 模块

---

#### Java 17 (2021 LTS)

```java
// 密封类（正式版）
public sealed interface Animal permits Cat, Dog { }
record Cat(String name) implements Animal { }
record Dog(String name) implements Animal { }

// 模式匹配 for switch（预览）
Shape shape = new Circle(5.0);
String desc = switch (shape) {
    case Circle c -> "圆, 半径=" + c.radius();
    case Rectangle r -> "矩形, 面积=" + r.width() * r.height();
    case null -> "未知形状";
    default -> "其他形状";
};

// Records 中定义局部枚举和接口
public record Box<T>(T value) {
    enum Size { SMALL, MEDIUM, LARGE }
}

// 强封装 JDK 内部 API（无法再通过反射访问内部 API）
```

**关键特性：** 密封类（正式） / switch 模式匹配（预览） / 强封装 JDK 内部 API / Records 正式、instanceof 模式匹配正式

---

#### Java 21 (2023 LTS) — 最新 LTS

```java
// 虚拟线程（正式版）
Thread.startVirtualThread(() -> {
    System.out.println("虚拟线程: " + Thread.currentThread());
});
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> processTask());
}

// 记录模式（正式版）
Object obj = new Point(3, 5);
if (obj instanceof Point(int x, int y)) {
    System.out.println(x + ", " + y);
}

// 模式匹配 for switch（正式版）
return switch (shape) {
    case Circle c -> "圆";
    case Rectangle r -> "矩形";
    case null -> "null";
    default -> "其他";
};

// 顺序集合（Sequenced Collections）
SequencedCollection<String> list = new ArrayList<>();
list.addFirst("a");
list.addLast("b");
list.getFirst();
list.getLast();
list.reversed();
```

**关键特性：** 虚拟线程（正式） / 记录模式（正式） / switch 模式匹配（正式） / 顺序集合 / 字符串模板（预览）

---

#### Java 版本总结

| 版本 | 发布日期 | LTS | 亮点 |
|------|----------|-----|------|
| 8 | 2014-03 | ✅ | Lambda、Stream、Optional、新时间日期 |
| 11 | 2018-09 | ✅ | HttpClient、字符串增强、ZGC 实验 |
| 17 | 2021-09 | ✅ | 密封类、Records 正式、switch 模式匹配预览 |
| 21 | 2023-09 | ✅ | 虚拟线程正式、记录模式正式、switch 模式匹配正式 |

---

## 2. 主流开发框架 (Frameworks)

### 2.1 Spring Framework 核心

#### IOC（控制反转 / 依赖注入）

IOC 容器负责管理对象的生命周期和依赖关系，对象不再自己创建依赖，而是由容器注入。

```java
// ========== 基于注解的配置 ==========
@Component  // 注册为 Bean
public class UserService {
    @Autowired          // 按类型注入
    private UserDao userDao;

    @Resource(name = "orderDao") // 按名称注入
    private OrderDao orderDao;

    @Value("${app.name}")       // 注入配置属性
    private String appName;
}

@Configuration          // 配置类
@ComponentScan("com.example")
public class AppConfig {
    @Bean               // 通过 @Bean 注入第三方类
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// ========== 构造函数注入（推荐） ==========
@Component
public class OrderService {
    private final UserService userService;
    private final ProductService productService;

    // 不可变引用，便于测试，避免循环依赖
    public OrderService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
}
```

**Bean 的作用域：**

| 作用域 | 描述 |
|--------|------|
| `singleton` | 单例（默认），整个容器只有一个实例 |
| `prototype` | 原型，每次获取创建一个新实例 |
| `request` | 每个 HTTP 请求一个实例 |
| `session` | 每个 HTTP Session 一个实例 |
| `application` | 每个 ServletContext 一个实例 |

```java
@Component
@Scope("prototype")
public class PrototypeBean { /* 每次获取都是新对象 */ }
```

**Bean 的生命周期：**

```
实例化 → 属性赋值 → Aware 接口回调 → BeanPostProcessor#postProcessBeforeInitialization
→ InitializingBean#afterPropertiesSet / @PostConstruct → BeanPostProcessor#postProcessAfterInitialization
→ 使用中... → 容器关闭 → DisposableBean#destroy / @PreDestroy
```

**BeanFactory vs ApplicationContext：**

`BeanFactory` 是 Spring IOC 容器的顶层接口，提供基础的 Bean 获取、类型查找功能，采用延迟加载策略 — 只有在调用 `getBean()` 时才创建 Bean。`ApplicationContext` 继承自 `BeanFactory`，增加了事件发布、国际化（MessageSource）、资源访问（ResourceLoader）、AOP 集成等企业级能力，并默认采用预加载策略 — 容器启动时就创建所有单例 Bean。

| 特性 | BeanFactory | ApplicationContext |
|------|-------------|-------------------|
| Bean 加载 | 延迟加载 | 预加载（可配置） |
| 事件发布 | 不支持 | 支持 ApplicationEventPublisher |
| 国际化 | 不支持 | 支持 MessageSource |
| 资源访问 | 不支持 | 支持 ResourceLoader |
| AOP 自动代理 | 需手动配置 | 支持自动注册 |
| 常用实现 | XmlBeanFactory（已废弃） | AnnotationConfigApplicationContext, ClassPathXmlApplicationContext |

```java
// ApplicationContext 示例
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
UserService userService = ctx.getBean(UserService.class);
String[] beanNames = ctx.getBeanDefinitionNames();
ctx.publishEvent(new UserCreatedEvent(this, userId)); // 发布事件
```

**@Import / @ImportResource：**

`@Import` 用于组合多个配置类，`@ImportResource` 用于在 Java 配置中引用 XML 配置文件。

```java
@Configuration
@Import({DataSourceConfig.class, SecurityConfig.class})
@ImportResource("classpath:legacy-config.xml")
public class AppConfig {
}
```

**FactoryBean：**

FactoryBean 是一个工厂接口，用于创建复杂的 Bean 对象，Spring 容器会自动识别并调用其 `getObject()` 方法。

```java
@Component
public class MyFactoryBean implements FactoryBean<MyService> {
    @Override
    public MyService getObject() {
        return MyService.builder()
            .timeout(5000)
            .retryCount(3)
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return MyService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

// 注入时直接得到 MyService，而不是 FactoryBean 本身
@Autowired
private MyService myService;

// 若想获取 FactoryBean 本身，在 Bean 名前加 "&"
@Autowired
private FactoryBean<MyService> myFactoryBean; // 或者 context.getBean("&myFactoryBean")
```

**条件化 Bean（@Profile / Condition）：**

`@Profile` 按环境激活不同 Bean，`Condition` 接口提供更灵活的条件判断。

```java
@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:mysql://prod-server:3306/db")
            .username("prod")
            .password("secret")
            .build();
    }
}

// 自定义 Condition
public class OnWindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("os.name").toLowerCase().contains("win");
    }
}

@Bean
@Conditional(OnWindowsCondition.class)
public FileSystemService windowsFileService() {
    return new WindowsFileService();
}
```

**循环依赖解析：**

Spring 容器通过三级缓存机制解决单例 Bean 的构造器循环依赖之外的场景：

| 缓存 | 名称 | 用途 |
|------|------|------|
| 一级缓存 | `singletonObjects` | 已完成初始化的单例 Bean |
| 二级缓存 | `earlySingletonObjects` | 提前曝光的半成品 Bean |
| 三级缓存 | `singletonFactories` | 用于生成半成品 Bean 的 ObjectFactory |

```java
// 以下场景能解决：setter/field 注入的循环依赖
@Component
public class A {
    @Autowired
    private B b;
}

@Component
public class B {
    @Autowired
    private A a;
}

// 以下场景无法解决：构造器注入的循环依赖（启动报 BeanCurrentlyInCreationException）
@Component
public class A {
    public A(B b) { }
}

@Component
public class B {
    public B(A a) { }
}
// 解决方案：改用 @Lazy 打破循环
@Component
public class A {
    public A(@Lazy B b) { }
}
```

**@Lookup（方法注入）：**

用于解决单例 Bean 依赖原型 Bean 时每次获取最新实例的问题。

```java
@Component
public abstract class SingletonBean {

    public void doSomething() {
        PrototypeBean prototype = createPrototypeBean();
        prototype.doSomething();
    }

    @Lookup
    protected abstract PrototypeBean createPrototypeBean();
}

// 或者使用具体方法 + 可覆写的返回值
@Component
public class SingletonBean {
    @Lookup
    public PrototypeBean createPrototypeBean() {
        return null; // Spring 会通过 CGLIB 覆写此方法
    }
}
```

**Bean 别名：**

```java
@Bean({"userService", "userSvc", "svc"})
public UserService userService() {
    return new UserService();
}

// 或通过 XML / BeanDefinition 注册别名
// context.getBean("userService") == context.getBean("userSvc")
```

#### AOP（面向切面编程）

AOP 将日志、事务、权限等横切关注点与业务逻辑分离。

```java
// ========== 自定义注解 + AOP 实现日志切面 ==========
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    String value() default "";
}

@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(logOperation)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();
        String operation = logOperation.value();

        try {
            Object result = joinPoint.proceed(); // 执行目标方法
            long cost = System.currentTimeMillis() - start;
            log.info("操作: {}, 方法: {}, 耗时: {}ms", operation, method, cost);
            return result;
        } catch (Exception e) {
            log.error("操作: {}, 方法: {}, 异常: {}", operation, method, e.getMessage());
            throw e;
        }
    }
}

// 使用
@Service
public class UserService {
    @LogOperation("创建用户")
    public User createUser(User user) { /* ... */ }
}
```

**AOP 通知类型：**
- `@Before`：方法执行前
- `@After`：方法执行后（无论是否异常）
- `@AfterReturning`：方法正常返回后
- `@AfterThrowing`：方法抛出异常后
- `@Around`：环绕通知，最强大

**execution 切入点表达式：**

切入点表达式控制切面织入的目标方法。

```java
@Aspect
@Component
public class ServiceAspect {

    // 匹配 com.example.service 包下所有类的所有方法
    @Before("execution(* com.example.service.*.*(..))")
    public void beforeService() {}

    // 精确匹配：返回类型、包、类、方法名、参数
    @AfterReturning("execution(public com.example.model.User com.example.service.UserService.findById(Long))")
    public void afterFind() {}

    // 匹配任意返回类型、service 子包、以 Service 结尾的类、任意方法、任意参数
    @Around("execution(* com.example.service..*Service.*(..))")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    // 匹配所有 public 方法且第一个参数为 Long 类型
    @Before("execution(public * *(Long, ..))")
    public void firstParamLong() {}
}
```

**常用切入点表达式（Pointcut Designators）：**

| 表达式 | 说明 |
|--------|------|
| `execution(public * *(..))` | 所有 public 方法 |
| `execution(* set*(..))` | 所有以 set 开头的方法 |
| `execution(* com.example..*.*(..))` | com.example 及其子包中所有类的所有方法 |
| `within(com.example.service.*)` | com.example.service 包中所有类的任意方法 |
| `this(com.example.service.UserService)` | 目标代理对象是 UserService 类型 |
| `target(com.example.service.UserService)` | 目标对象是 UserService 类型（非代理） |
| `args(String, int, ..)` | 第一个参数为 String，第二个为 int 的方法 |
| `@annotation(com.example.LogOperation)` | 标注了 @LogOperation 的方法 |
| `@within(org.springframework.transaction.annotation.Transactional)` | 类上标注了 @Transactional |
| `bean(userService)` | Bean 名称为 userService |
| `!execution(* com.example.service.UserService.*(..))` | 排除 UserService 的所有方法 |

切入点组合：`&&`（与）、`||`（或）、`!`（非）

```java
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceLayer() {}

@Pointcut("execution(public * *(..)) && @annotation(LogOperation)")
public void publicLogOperation() {}

@Around("serviceLayer() && !bean(specialService)")
public Object aroundExceptSpecial(ProceedingJoinPoint pjp) throws Throwable {
    return pjp.proceed();
}
```

**@DeclareParents（引入增强）：**

为目标类动态实现新接口，无需修改原类代码。

```java
// 定义要引入的接口
public interface Monitorable {
    void resetMetrics();
}

public class MonitorableImpl implements Monitorable {
    @Override
    public void resetMetrics() {
        System.out.println("Metrics reset");
    }
}

@Aspect
@Component
public class MonitoringAspect {
    // 为所有 UserService 类型的 Bean 引入 Monitorable 接口
    // defaultImpl 指定默认实现类
    @DeclareParents(value = "com.example.service.UserService+", defaultImpl = MonitorableImpl.class)
    private Monitorable implementedInterface;
}

// 使用
@Service
public class UserService { /* 原有代码无需改动 */ }

// 调用时直接转型
UserService userService = context.getBean(UserService.class);
Monitorable monitorable = (Monitorable) userService;
monitorable.resetMetrics();
```

**CGLIB vs JDK 动态代理：**

| 对比项 | JDK 动态代理 | CGLIB |
|--------|-------------|-------|
| 原理 | 基于接口的 Proxy + InvocationHandler | 基于继承的字节码增强（ASM） |
| 要求 | 目标类必须实现接口 | 目标类可以是具体类（不能是 final 类/方法） |
| 性能（创建） | 较快 | 较慢（需生成字节码） |
| 性能（调用） | 通过反射调用（JDK 8+ 优化后差距很小） | 通过生成子类方法调用，略快 |
| Spring 默认策略 | 目标类有接口 → JDK 代理 | 目标类无接口 → CGLIB |
| Spring Boot 2.x+ | 默认不再使用 JDK 代理，除非显式配置 `spring.aop.proxy-target-class=false` | 默认使用 CGLIB |

```yaml
# Spring Boot 配置代理方式
spring:
  aop:
    proxy-target-class: true  # 强制使用 CGLIB（默认）
```

**Spring AOP vs AspectJ：**

| 对比项 | Spring AOP | AspectJ |
|--------|------------|---------|
| 实现方式 | 运行时动态代理（JDK / CGLIB） | 编译期/加载期织入（字节码增强） |
| 织入时机 | 运行时 | 编译期（ajc）、加载期（LTW） |
| 支持连接点 | 仅方法执行（method execution） | 字段访问、构造器、初始化、方法调用等 |
| 性能 | 运行时代理调用有开销 | 无运行时开销 |
| 易用性 | 集成简单，与 Spring 无缝配合 | 需额外编译配置，学习成本高 |
| 适用场景 | 大多数企业应用（事务、日志、安全） | 对性能要求极高或需织入构造器/字段的场景 |

性能建议：Spring AOP 对于大多数业务场景已足够。如需高频调用（每秒数十万次），考虑重构为 AspectJ 或绕过 AOP。

#### 事务管理

```java
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackFor = Exception.class) // 默认回滚 RuntimeException
    public void createOrder(Order order) {
        orderDao.insert(order);                    // 插入订单
        inventoryService.deduct(order.getProductId(), order.getQuantity()); // 扣减库存
    }

    // 只读事务优化性能
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderDao.selectById(id);
    }

    // 手动设置隔离级别
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void pay(Long orderId) { /* 支付 */ }
}
```

**事务隔离级别：**

| 隔离级别 | 脏读 | 不可重复读 | 幻读 |
|----------|------|------------|------|
| `READ_UNCOMMITTED` | ✅ | ✅ | ✅ |
| `READ_COMMITTED` | ❌ | ✅ | ✅ |
| `REPEATABLE_READ` | ❌ | ❌ | ✅ |
| `SERIALIZABLE` | ❌ | ❌ | ❌ |

**事务传播行为：**

| 传播行为 | 描述 |
|----------|------|
| `REQUIRED`（默认） | 支持当前事务，不存在则新建 |
| `REQUIRES_NEW` | 挂起当前事务，新建一个独立事务 |
| `NESTED` | 在嵌套事务中执行（JDBC savepoint 实现） |
| `SUPPORTS` | 支持当前事务，不存在则以非事务方式执行 |
| `NOT_SUPPORTED` | 以非事务方式执行，挂起当前事务（如有） |
| `MANDATORY` | 必须在事务中执行，无事务则抛异常 |
| `NEVER` | 必须在非事务中执行，有事务则抛异常 |

```java
@Service
public class OrderService {

    // REQUIRES_NEW：订单创建成功独立提交，不受外部事务回滚影响
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOrder(Order order) {
        orderDao.insert(order);
    }

    // NESTED：使用 savepoint，外部事务回滚时可选择回滚到此保存点
    @Transactional(propagation = Propagation.NESTED)
    public void updateInventory(Long productId, int quantity) {
        inventoryDao.deduct(productId, quantity);
    }

    // MANDATORY：必须在已有事务中调用
    @Transactional(propagation = Propagation.MANDATORY)
    public void auditLog(String action) {
        auditDao.log(action);
    }
}
```

**@Transactional 自调用失效问题：**

Spring 事务基于 AOP 代理，同一个类中的方法直接调用（this.method()）不会经过代理对象，导致注解失效。

```java
@Service
public class OrderService {

    // 事务生效
    @Transactional
    public void createOrder(Order order) {
        insertOrder(order);
        // ↓ 这里直接调用了内部方法，事务注解失效！
        sendNotification(order.getUserId(), "订单创建成功");
    }

    // 事务不生效 —— 因为是被 this.sendNotification() 调用的
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(Long userId, String message) {
        notificationDao.insert(userId, message);
    }
}
```

解决方案：

```java
@Service
public class OrderService {
    @Autowired
    private OrderService self; // 自注入

    @Transactional
    public void createOrder(Order order) {
        insertOrder(order);
        self.sendNotification(order.getUserId(), "订单创建成功"); // 通过代理调用
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(Long userId, String message) {
        notificationDao.insert(userId, message);
    }
}

// 方案二：提取到单独的事务 Bean
@Service
public class NotificationService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void send(Long userId, String message) {
        notificationDao.insert(userId, message);
    }
}

// 方案三：编程式事务（见下方 TransactionTemplate）
```

**TransactionTemplate（编程式事务）：**

当声明式事务（@Transactional）无法满足需求时，使用编程式事务精确控制边界。

```java
@Service
public class PaymentService {

    private final TransactionTemplate transactionTemplate;

    public PaymentService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        // 自定义事务属性
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        this.transactionTemplate.setTimeout(30);
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        transactionTemplate.execute(status -> {
            try {
                accountDao.debit(fromId, amount);
                accountDao.credit(toId, amount);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly(); // 手动标记回滚
                throw e;
            }
        });
    }

    // 有返回值直接返回，无返回值用 executeWithoutResult
    public void logOperation(String action) {
        transactionTemplate.executeWithoutResult(status -> {
            auditDao.log(action);
        });
    }
}
```

**声明式事务 vs 编程式事务：**

| 方式 | 优点 | 缺点 |
|------|------|------|
| 声明式 `@Transactional` | 无侵入，配置简单，代码最简洁 | 自调用失效，粒度粗（类/方法级别） |
| 编程式 `TransactionTemplate` | 精确控制事务边界，动态决定 commit/rollback | 代码侵入，重复模板代码 |

**@TransactionalEventListener：**

在事务提交/回滚后处理事件，避免在未提交的事务中执行外部操作（如发消息、写审计日志）。

```java
@Component
public class OrderEventListener {

    // 在事务提交后执行
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        emailService.sendConfirmation(event.getOrder().getUserId());
    }

    // 在事务回滚后执行
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleOrderFailed(OrderCreatedEvent event) {
        alertService.notify("订单创建失败: " + event.getOrder().getId());
    }

    // 在事务完成后执行（无论提交还是回滚）
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleOrderCompleted(OrderCreatedEvent event) {
        metricsService.recordOrderAttempt();
    }
}
```

#### Spring Event（事件机制）

Spring 事件机制基于观察者模式，实现业务逻辑的解耦。核心组件：`ApplicationEventPublisher` 发布事件、`@EventListener` 监听事件。

**自定义事件：**

```java
// 事件类（Spring 4.2+ 不需要继承 ApplicationEvent）
public class OrderCreatedEvent {
    private final Order order;
    private final long timestamp;

    public OrderCreatedEvent(Order order) {
        this.order = order;
        this.timestamp = System.currentTimeMillis();
    }

    public Order getOrder() { return order; }
    public long getTimestamp() { return timestamp; }
}
```

**发布事件：**

```java
@Service
public class OrderService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    public Order createOrder(Order order) {
        orderDao.insert(order);
        // 发布事件，同步或异步执行
        publisher.publishEvent(new OrderCreatedEvent(order));
        return order;
    }
}
```

**监听事件：**

```java
@Component
public class OrderEventListener {

    // 同步监听
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("订单创建: {}", event.getOrder().getId());
        emailService.sendConfirmation(event.getOrder().getUserId());
    }

    // 条件监听：仅当金额大于 10000 时处理
    @EventListener(condition = "#event.order.amount > 10000")
    public void handleLargeOrder(OrderCreatedEvent event) {
        riskControlService.review(event.getOrder());
    }

    // 监听多个事件类型
    @EventListener({OrderCreatedEvent.class, OrderCancelledEvent.class})
    public void handleOrderChange(Object event) {
        if (event instanceof OrderCreatedEvent) { /* 创建 */ }
        if (event instanceof OrderCancelledEvent) { /* 取消 */ }
    }
}
```

**异步事件：**

结合 `@Async` 使事件监听异步执行，避免阻塞发布线程。

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "eventTaskExecutor")
    public Executor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("event-");
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

@Component
public class AsyncEventListener {

    @Async("eventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        // 异步执行，不阻塞事务提交线程
        emailService.sendConfirmation(event.getOrder().getUserId());
    }
}
```

**事件排序：**

```java
@Component
public class OrderedEventListener {

    // 值越小优先级越高。同一线程中按 @Order 顺序执行
    @EventListener
    @Order(1)
    public void firstHandler(OrderCreatedEvent event) {
        log.info("第一个处理");
    }

    @EventListener
    @Order(2)
    public void secondHandler(OrderCreatedEvent event) {
        log.info("第二个处理");
    }
}
```

#### Resource（资源抽象）

Spring `Resource` 接口统一封装了底层资源访问，屏蔽了 classpath、文件系统、网络等不同来源的差异。

```java
// ========== Resource 接口核心方法 ==========
public interface Resource extends InputStreamSource {
    boolean exists();
    boolean isReadable();
    boolean isOpen();           // 是否已打开流（如 InputStreamResource）
    URL getURL() throws IOException;
    File getFile() throws IOException;
    long contentLength() throws IOException;
    long lastModified() throws IOException;
    String getFilename();
    String getDescription();    // 资源描述，用于日志/错误信息
    InputStream getInputStream() throws IOException;  // 继承自 InputStreamSource
}

// ========== Resource 实现类 ==========
// ClassPathResource           — classpath:path     读取类路径下资源（jar 内也支持）
// FileSystemResource          — file:path          读取文件系统资源
// UrlResource                 — https://...         读取 URL 资源
// ByteArrayResource           — 无前缀             内存中字节数组
// InputStreamResource         — 无前缀             已打开的 InputStream 封装
// ServletContextResource      — 无前缀（Web 应用） ServletContext 中获取

// ========== ResourceLoader（资源加载器） ==========
@Component
public class ResourceService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext context;  // ApplicationContext 就是 ResourceLoader

    public void demonstrateResources() throws IOException {
        // classpath（默认前缀可省略，取决于 ApplicationContext 类型）
        Resource cp1 = context.getResource("classpath:data/schema.sql");
        Resource cp2 = context.getResource("classpath:/META-INF/spring.factories");

        // 文件系统
        Resource file = context.getResource("file:D:/config/app.properties");

        // URL
        Resource url = context.getResource("https://example.com/config.json");

        // 无前缀时：ClassPathXmlApplicationContext → classpath
        //           FileSystemXmlApplicationContext → file
        //           WebApplicationContext → ServletContext

        // 通用操作
        try (InputStream is = cp1.getInputStream()) {
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(content);
        }
        String filename = cp1.getFilename();      // schema.sql
        boolean exists  = cp1.exists();           // true
        long modified   = cp1.lastModified();     // 文件最后修改时间戳
        long length     = cp1.contentLength();    // 文件字节长度
        File fileObj    = cp1.getFile();          // 文件系统路径（jar 内资源会抛异常）
        URL urlObj      = cp1.getURL();           // URL 形式
    }
}

// ========== @Value 注入 Resource ==========
@Component
public class MyConfigLoader {

    // classpath 资源
    @Value("classpath:config/banner.txt")
    private Resource banner;

    // 文件系统资源（支持 SpEL + 占位符）
    @Value("file:${app.config.dir}/app.properties")
    private Resource appConfig;

    // URL 资源
    @Value("https://example.com/remote-config.json")
    private Resource remoteConfig;

    // 注入 Resource 数组（批量加载）
    @Value("classpath:config/*.xml")
    private Resource[] configFiles;

    @PostConstruct
    public void init() throws IOException {
        if (banner.exists()) {
            String content = new String(banner.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(content);
        }
    }
}

// ========== ResourcePatternResolver（批量匹配） ==========
@Component
public class ResourcesScanner {

    @Autowired
    private ResourcePatternResolver resolver; // ApplicationContext 已实现

    public void scanResources() throws IOException {
        // * 匹配一个路径段，** 递归匹配

        // 扫描所有 classpath 下的 mapper XML
        Resource[] mappers = resolver.getResources("classpath*:mapper/**/*.xml");

        // 扫描特定包下的 properties
        Resource[] props = resolver.getResources("classpath*:com/example/**/*.properties");

        // 文件系统也支持通配
        Resource[] files = resolver.getResources("file:D:/config/**/*.yml");

        for (Resource r : mappers) {
            System.out.println(r.getFilename() + " → " + r.getDescription());
        }
    }
}

// ========== 静态资源映射 ==========
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 自定义静态资源路径
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:D:/uploaded-files/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        // classpath 静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/");
    }
}

// ========== Controller 返回 Resource 供下载 ==========
@RestController
@RequestMapping("/files")
public class FileDownloadController {

    private final Path uploadDir = Path.of("D:/uploaded-files");

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        Path file = uploadDir.resolve(filename).normalize();
        if (!file.startsWith(uploadDir)) {
            return ResponseEntity.badRequest().build();
        }
        Resource resource = new FileSystemResource(file);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // 读取 classpath 中的模板文件
    @GetMapping("/template/{name}")
    public ResponseEntity<Resource> getTemplate(@PathVariable String name) {
        Resource resource = new ClassPathResource("templates/" + name);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain; charset=UTF-8"))
                .body(resource);
    }
}
```

**@Validated 分组校验：**

不同操作场景使用不同的校验规则。

```java
// 定义分组接口
public interface CreateGroup {}
public interface UpdateGroup {}

// 实体类
@Data
public class UserDTO {
    @Null(groups = CreateGroup.class)        // 创建时不能传 ID
    @NotNull(groups = UpdateGroup.class)     // 更新时必须传 ID
    private Long id;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @NotBlank(groups = CreateGroup.class)
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;
}

// Controller 中使用分组
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Result<Void> create(@Validated(CreateGroup.class) @RequestBody UserDTO dto) {
        userService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@Validated(UpdateGroup.class) @RequestBody UserDTO dto) {
        userService.update(dto);
        return Result.ok();
    }
}
```

**自定义校验器：**

```java
// 注解定义
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "手机号格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// 校验器实现
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 非空校验交由 @NotBlank
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}

// 使用
@Data
public class RegisterDTO {
    @NotBlank
    @PhoneNumber
    private String phone;
}
```

**Spring Validation vs Jakarta Validation：**

| 对比项 | Jakarta Validation（JSR 380） | Spring Validation |
|--------|-------------------------------|-------------------|
| 定位 | Java 标准规范，与框架无关 | Spring 框架集成的校验封装 |
| 核心注解 | `@NotNull`, `@Size`, `@Email` 等定义在 `jakarta.validation.constraints` | 使用相同的标准注解，额外提供 `@Validated`（分组校验入口） |
| 编程式校验 | `Validator.validate(obj)` | `LocalValidatorFactoryBean` 注入，用法一致 |
| 分组校验 | `groups` 属性 | `@Validated` 支持指定分组（标准规范不支持 Type-level 分组） |
| 方法级校验 | 不支持（Seam Validation 提供） | `@Validated` 注解在类上即可开启方法参数/返回值校验 |
| 自定义校验 | `@Constraint(validatedBy=...)` | 一致，同样方式定义 |

```java
// Jakarta Validation 编程式校验（与框架无关）
public class ManualValidation {
    public static void main(String[] args) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        UserDTO user = new UserDTO();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        for (ConstraintViolation<UserDTO> v : violations) {
            System.out.println(v.getPropertyPath() + ": " + v.getMessage());
        }
    }
}

// Spring 方法级校验
@Validated  // 开启方法校验
@Service
public class UserService {

    public User createUser(@Valid @NotNull UserDTO dto) {
        return new User(dto);
    }

    // 返回值校验
    @Valid
    public UserDTO getUser(@NotNull Long id) {
        return userDao.findById(id).orElseThrow();
    }
}
```

#### Task Execution & Scheduling（任务执行与调度）

**@Async（异步方法）：**

```java
@Configuration
@EnableAsync // 启用异步支持
public class AsyncConfig {
}

@Service
public class NotificationService {

    // 无返回值异步
    @Async
    public void sendEmail(String to, String subject, String body) {
        // 此方法在独立的线程池中执行
        mailSender.send(to, subject, body);
    }

    // 有返回值异步
    @Async
    public CompletableFuture<Result> sendSms(String phone, String message) {
        SmsResult result = smsClient.send(phone, message);
        return CompletableFuture.completedFuture(Result.of(result));
    }

    // 指定线程池
    @Async("taskExecutor")
    public void pushNotification(Long userId, String content) {
        pushService.push(userId, content);
    }
}

// 调用方
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Result<Void> send() {
        notificationService.sendEmail("test@example.com", "Title", "Body");
        return Result.ok(); // 立即返回，不等待邮件发送完成
    }
}
```

**@Scheduled（定时任务）：**

```java
@Configuration
@EnableScheduling // 启用定时任务
public class SchedulingConfig {
}

@Component
public class ScheduledTasks {

    // cron 表达式：秒 分 时 日 月 周
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨 1 点执行
    public void dailyReport() {
        reportService.generateDailyReport();
    }

    // 固定延迟（上次执行完成后延迟 5 秒）
    @Scheduled(fixedDelay = 5000)
    public void cleanTempFiles() {
        fileCleaner.clean();
    }

    // 固定频率（每 10 秒执行一次，不等待上次完成）
    @Scheduled(fixedRate = 10000)
    public void healthCheck() {
        healthService.check();
    }

    // 带初始延迟的固定频率
    @Scheduled(initialDelay = 30000, fixedRate = 60000)
    public void delayedTask() {
        cacheManager.refresh();
    }

    // 使用占位符配置
    // application.properties: task.clean.cron=0 0/5 * * * ?
    @Scheduled(cron = "${task.clean.cron}")
    public void configurableTask() {
        // ...
    }
}
```

**TaskExecutor 自定义与 ThreadPoolTaskExecutor：**

```java
@Configuration
@EnableAsync
public class TaskConfig {

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：即使空闲也保留的线程数
        executor.setCorePoolSize(5);
        // 最大线程数：当队列满时最多扩展到该数量
        executor.setMaxPoolSize(20);
        // 队列容量：缓冲等待执行的任务
        executor.setQueueCapacity(200);
        // 非核心线程空闲存活时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名前缀（便于排查）
        executor.setThreadNamePrefix("async-task-");
        // 拒绝策略：
        //   AbortPolicy（默认）：抛 RejectedExecutionException
        //   CallerRunsPolicy：由提交任务的线程执行
        //   DiscardPolicy：静默丢弃
        //   DiscardOldestPolicy：丢弃队列中最旧的任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务完成再关闭（优雅停机）
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean("scheduledExecutor")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("scheduled-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        return scheduler;
    }
}

// 直接注入指定的线程池
@Service
public class BatchService {

    @Async("taskExecutor")
    public CompletableFuture<List<Result>> processBatch(List<Long> ids) {
        List<Result> results = ids.parallelStream()
            .map(this::processOne)
            .collect(Collectors.toList());
        return CompletableFuture.completedFuture(results);
    }

    private Result processOne(Long id) {
        // 处理单个任务
        return new Result(id, true);
    }
}
```

**异常处理与监控：**

```java
@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("异步任务执行失败 - 方法: {}, 参数: {}, 异常: {}",
            method.getName(), Arrays.toString(params), ex.getMessage(), ex);
        alertService.notifyDevops("Async task failed: " + method.getName());
    }
}

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
```

---

### 2.2 Spring Boot

Spring Boot 通过"约定大于配置"和起步依赖简化了 Spring 应用的搭建。

```java
// ========== 入口类 ==========
@SpringBootApplication // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// ========== application.yml 配置 ==========
// server:
//   port: 8080
// spring:
//   datasource:
//     url: jdbc:mysql://localhost:3306/db?useUnicode=true&characterEncoding=utf-8
//     username: root
//     password: 123456
//     hikari:
//       pool-size: 20

// ========== 读取配置的最佳实践 ==========
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String name;
    private String version;
    private List<String> allowOrigins;
    private Map<String, String> headers;
}

@Component
public class AppInfo {
    @Autowired
    private AppProperties appProperties;
}

// ========== 条件化配置 ==========
@Configuration
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() { return new ConcurrentMapCacheManager(); }
}

@ConditionalOnClass(name = "redis.clients.jedis.Jedis")
@ConditionalOnMissingBean(CacheManager.class)
public class RedisAutoConfig { /* Redis 自动配置 */ }

// ========== 统一异常处理 ==========
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(500, "系统繁忙");
    }
}
```

**常用起步依赖：**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>    <!-- Web 开发 -->
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId> <!-- JPA -->
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId> <!-- Redis -->
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId><!-- 参数校验 -->
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>     <!-- 测试 -->
</dependency>
```

---

#### Spring Boot 版本演进

| 版本 | 日期 | 对应 Spring | Java 基线 | 亮点 |
|------|------|-------------|-----------|------|
| 1.x | 2014-04 | 4.x | 6/7 | 首个正式版，颠覆 XML 配置 |
| 2.0 | 2018-03 | 5.0 | 8 | Reactor/WebFlux 响应式支持、Kotlin 支持、Micrometer"
| 2.1 | 2018-10 | 5.1 | 8 | JUnit 5、OAuth 2.0 改进、CRaSH 移除"
| 2.2 | 2019-10 | 5.2 | 8/11 | 健康指标分组、延迟初始化、RSocket 支持"
| 2.3 | 2020-05 | 5.2 | 8/11 | 优雅关闭、分层 JAR、构建信息改进"
| 2.4 | 2020-11 | 5.3 | 8/11 | 配置文件逻辑调整、Cloud Native Buildpacks、@DataJpaTest 改进"
| 2.5 | 2021-05 | 5.3 | 8/11/16 | Java 16 支持、Docker 镜像构建改进、启动端点"
| 2.6 | 2021-11 | 5.3 | 8/11/17 | Java 17 支持、Redis 连接改进、自动配置去重"
| 2.7 | 2022-05 | 5.3 | 8/11/17 | 3.0 迁移指南、自动配置注册文件、DevTools 重连策略"
| 3.0 | 2022-11 | 6.0 | 17 | **Jakarta EE 9+**（javax→jakarta）、GraalVM 原生镜像、@AutoConfiguration、Observability、Problem Details"
| 3.1 | 2023-05 | 6.0 | 17 | 基于 HttpClient 的 RestClient、Docker Compose 支持、Testcontainers 集成、SSL 信息端点、服务连接"
| 3.2 | 2023-11 | 6.1 | 17/21 | 虚拟线程支持、RestClient 增强、@ContaineerBefore/After、Maven Plugin 分层构建优化"
| 3.3 | 2024-05 | 6.1 | 17/21 | 类数据共享（CDS）支持、改进 Docker Compose、@BatchSize 验证、Otel 指标改进"
| 3.4 | 2025-05 | 6.2 | 17/21/23 | Java 23 支持、CRaC 恢复、改进 RestClient SSL、SSL 热重载、PEM 编码证书支持"

```java
// ========== Spring Boot 3.x 关键变化 ==========
// 1. Jakarta EE 9+（javax.* → jakarta.*）
// javax.persistence → jakarta.persistence
// javax.servlet → jakarta.servlet
// javax.validation → jakarta.validation

// 2. GraalVM 原生镜像（AOT 编译）
// 启动时间从秒级降至毫秒级，内存占用大幅降低
// mvn -Pnative native:compile
// ./target/myapp  # 直接运行原生可执行文件

// 3. RestClient（Spring Boot 3.2+ 推荐的 HTTP 客户端）
@Bean
public RestClient restClient(RestClient.Builder builder) {
    return builder
        .baseUrl("https://api.example.com")
        .defaultHeader("Authorization", "Bearer " + token)
        .build();
}

@Service
public class UserService {
    private final RestClient restClient;

    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public User getUser(Long id) {
        return restClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .body(User.class);
    }
}

// 4. 虚拟线程（Spring Boot 3.2+）
spring:
  threads:
    virtual:
      enabled: true  # 启用虚拟线程

// 5. Docker Compose 集成（Spring Boot 3.1+）
// 启动时自动启动 docker-compose.yml 中的服务
spring:
  docker:
    compose:
      enabled: true
      lifecycle-management: start_only

// 6. Problem Details（RFC 9457，Spring Boot 3.0+）
spring.mvc.problemdetails.enabled: true
// 返回规范的错误响应：
// {
//   "type": "about:blank",
//   "title": "Bad Request",
//   "status": 400,
//   "detail": "Validation failed",
//   "instance": "/api/users"
// }

// 7. 优雅关闭（Spring Boot 2.3+）
server:
  shutdown: graceful  # 优雅关闭，等待请求处理完成
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
```

---

### 2.3 Spring MVC

Spring MVC 是基于 Servlet 的 Web 框架，用于处理 HTTP 请求/响应。

**处理流程：**

```
DispatcherServlet 接收请求 → HandlerMapping 找到 Controller
→ HandlerAdapter 调用 Controller → 返回 ModelAndView
→ ViewResolver 解析视图 → 渲染响应
```

```java
// ========== 基础 Controller ==========
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public Result<List<User>> list(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return Result.success(userService.findAll(page, size));
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.findById(id));
    }

    // POST /api/users
    @PostMapping
    public Result<User> create(@Valid @RequestBody UserCreateReq req) {
        return Result.success(userService.create(req));
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @Valid @RequestBody UserUpdateReq req) {
        return Result.success(userService.update(id, req));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}

// ========== 参数校验 ==========
@Data
public class UserCreateReq {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度 2-20")
    private String username;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 150, message = "年龄不能超过 150")
    private Integer age;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;
}

// ========== 拦截器 ==========
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 限流逻辑
        return true; // true 继续，false 拦截
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) { /* 后处理 */ }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) { /* 完成后清理 */ }
}

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://example.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

**Content Negotiation（内容协商）：**

```java
// ========== produces / consumes ==========
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 仅生产 JSON，仅消费 JSON
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<User> create(@RequestBody User user) { ... }

    // 生产 XML（需引入 jackson-dataformat-xml）
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public User getByIdXml(@PathVariable Long id) { ... }
}

// ========== Accept Header 控制 ==========
// 客户端通过 Accept 头指定期望格式：
//   Accept: application/json  → JSON
//   Accept: application/xml   → XML
// 服务端通过配置内容协商策略：
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .favorParameter(true)                    // ?format=json
            .parameterName("format")
            .ignoreAcceptHeader(false)                // 仍尊崇 Accept
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML);
    }
}
```

**File Upload / Download：**

```java
// ========== 文件上传 ==========
@PostMapping("/upload")
public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
    // 原始文件名
    String originalName = file.getOriginalFilename();
    // 文件大小
    long size = file.getSize();
    // 内容类型
    String contentType = file.getContentType();
    // 读取字节
    byte[] bytes = file.getBytes();
    // 保存到本地
    String path = "uploads/" + UUID.randomUUID() + "_" + originalName;
    file.transferTo(new File(path));
    return Result.success(path);
}

// 多文件上传
@PostMapping("/uploads")
public Result<List<String>> uploadMultiple(@RequestParam("files") List<MultipartFile> files) {
    List<String> paths = new ArrayList<>();
    for (MultipartFile file : files) {
        String path = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        file.transferTo(new File(path));
        paths.add(path);
    }
    return Result.success(paths);
}

// ========== 文件下载 ==========
@GetMapping("/download/{id}")
public ResponseEntity<Resource> download(@PathVariable Long id) {
    File file = fileService.getFileById(id);
    Resource resource = new FileSystemResource(file);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getName() + "\"")
        .body(resource);
}

// ========== StreamingResponseBody（大文件流式下载） ==========
@GetMapping("/download/stream/{id}")
public ResponseEntity<StreamingResponseBody> downloadLarge(@PathVariable Long id) {
    File file = fileService.getFileById(id);
    StreamingResponseBody body = outputStream -> {
        try (InputStream is = new FileInputStream(file)) {
            IOUtils.copy(is, outputStream);
        }
    };
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
        .body(body);
}
```

**Async Requests（异步请求）：**

```java
// ========== Callable（简单异步） ==========
@GetMapping("/async/callable")
public Callable<Result<User>> asyncGet() {
    return () -> {
        Thread.sleep(1000); // 模拟耗时
        return Result.success(userService.findById(1L));
    };
}

// ========== DeferredResult（更灵活的异步） ==========
// 适用于长轮询、消息推送场景
private final Map<String, DeferredResult<Result<String>>> deferredResults = new ConcurrentHashMap<>();

@GetMapping("/poll/{token}")
public DeferredResult<Result<String>> poll(@PathVariable String token) {
    DeferredResult<Result<String>> result = new DeferredResult<>(30_000L); // 30s 超时
    result.onTimeout(() -> result.setResult(Result.success("timeout")));
    deferredResults.put(token, result);
    return result;
}

// 另外的线程完成请求
@PostMapping("/notify/{token}")
public Result<Void> notify(@PathVariable String token, @RequestBody String message) {
    DeferredResult<Result<String>> result = deferredResults.remove(token);
    if (result != null) {
        result.setResult(Result.success(message));
    }
    return Result.success();
}

// ========== SSE（Server-Sent Events） ==========
@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter events() {
    SseEmitter emitter = new SseEmitter(60_000L);
    executorService.execute(() -> {
        try {
            for (int i = 0; i < 10; i++) {
                emitter.send(SseEmitter.event()
                    .id(String.valueOf(i))
                    .name("message")
                    .data("Event #" + i));
                Thread.sleep(1000);
            }
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    });
    return emitter;
}
```

**Exception Handling（异常处理）：**

```java
// ========== @ControllerAdvice 全局异常处理 ==========
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return Result.error(400, msg);
    }

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException ex) {
        return Result.error(ex.getCode(), ex.getMessage());
    }

    // 参数类型转换异常
    @ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
    public Result<Void> handleTypeMismatch(Exception ex) {
        return Result.error(400, "参数类型错误: " + ex.getMessage());
    }

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNotFound(NoHandlerFoundException ex) {
        return Result.error(404, "接口不存在");
    }

    // 方法不允许
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return Result.error(405, "不支持的请求方法: " + ex.getMethod());
    }

    // 兜底异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("未知异常", ex);
        return Result.error(500, "系统繁忙");
    }
}

// ========== HandlerExceptionResolver（优先级低于 @ControllerAdvice） ==========
@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp,
                                          Object handler, Exception ex) {
        if (ex instanceof BusinessException) {
            resp.setStatus(400);
            resp.setContentType("application/json");
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"code\":400,\"msg\":\"" + ex.getMessage() + "\"}");
            } catch (IOException e) { /* ignore */ }
            return new ModelAndView(); // 返回空 ModelAndView 表示已处理
        }
        return null; // 交给下一个 resolver
    }
}
```

**ResponseBodyAdvice（全局响应包装）：**

```java
// 自动将所有 Controller 返回值包装为 Result<T>
@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除已包装的、Swagger 等非业务接口
        return !returnType.getParameterType().equals(Result.class)
            && !returnType.getDeclaringClass().getName().startsWith("springfox");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                   MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request, ServerHttpResponse response) {
        // String 特殊处理（StringHttpMessageConverter 不能转换 Result）
        if (body instanceof String) {
            return objectMapper.writeValueAsString(Result.success(body));
        }
        return Result.success(body);
    }
}
```

**REST Client 对比：**

```java
// ========== RestTemplate（传统，已标记 @Deprecated） ==========
@Service
public class LegacyClient {
    private final RestTemplate restTemplate;

    public LegacyClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .defaultHeader("X-Request-Id", UUID.randomUUID().toString())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }

    public User getUser(Long id) {
        return restTemplate.getForObject("/api/users/{id}", User.class, id);
    }
}

// ========== WebClient（响应式，Spring WebFlux 引入） ==========
@Service
public class ReactiveClient {
    private final WebClient webClient = WebClient.builder()
        .baseUrl("http://example.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    public Mono<User> getUser(Long id) {
        return webClient.get()
            .uri("/api/users/{id}", id)
            .retrieve()
            .bodyToMono(User.class);
    }

    // 同步阻塞（非响应式环境仍可用）
    public User getUserBlocking(Long id) {
        return webClient.get()
            .uri("/api/users/{id}", id)
            .retrieve()
            .bodyToMono(User.class)
            .block(Duration.ofSeconds(10));
    }
}

// ========== RestClient（Spring 6.1+，替代 RestTemplate） ==========
@Service
public class ModernClient {
    private final RestClient restClient;

    public ModernClient(RestClient.Builder builder) {
        this.restClient = builder
            .baseUrl("http://example.com")
            .defaultHeader("X-Source", "internal")
            .build();
    }

    public User getUser(Long id) {
        return restClient.get()
            .uri("/api/users/{id}", id)
            .retrieve()
            .body(User.class);
    }
}
```

| 对比维度 | RestTemplate（旧） | WebClient（响应式） | RestClient（新） |
|---------|-------------------|--------------------|-------------------|
| 引入版本 | Spring 3 | Spring 5（WebFlux） | Spring 6.1 |
| 编程模型 | 阻塞式同步 | 响应式（Mono/Flux） | 阻塞式同步 |
| 是否推荐 | ❌ 已标记 Deprecated | ✅ 响应式首推 | ✅ 同步首推 |
| 自定义 | interceptors | filters/filters | interceptors |
| 异常处理 | ResponseErrorHandler | ExchangeFilterFunction | RestClient.ResponseSpec |

---

### 2.4 MyBatis / MyBatis-Plus

MyBatis 是一个半自动 ORM 框架，SQL 由开发者控制，MyBatis-Plus 在 MyBatis 基础上封装了通用 CRUD。

#### MyBatis

```xml
<!-- UserMapper.xml -->
<mapper namespace="com.example.mapper.UserMapper">
    <resultMap id="userMap" type="User">
        <id column="id" property="id"/>
        <result column="user_name" property="userName"/>
        <result column="create_time" property="createTime"/>
        <association property="dept" javaType="Dept">
            <id column="dept_id" property="id"/>
            <result column="dept_name" property="name"/>
        </association>
        <collection property="roles" ofType="Role">
            <id column="role_id" property="id"/>
            <result column="role_name" property="name"/>
        </collection>
    </resultMap>

    <select id="selectById" resultMap="userMap">
        SELECT u.*, d.id dept_id, d.name dept_name
        FROM user u
        LEFT JOIN dept d ON u.dept_id = d.id
        WHERE u.id = #{id}
    </select>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO user(user_name, age, email) VALUES(#{userName}, #{age}, #{email})
    </insert>

    <update id="updateById">
        UPDATE user SET user_name = #{userName} WHERE id = #{id}
    </update>

    <delete id="deleteById">DELETE FROM user WHERE id = #{id}</delete>
</mapper>
```

```java
public interface UserMapper {
    User selectById(@Param("id") Long id);
    int insert(User user);
    int updateById(User user);
    int deleteById(Long id);
}
```

#### MyBatis-Plus（推荐）

```java
// ========== Mapper 继承 BaseMapper ==========
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 无需任何代码即拥有通用 CRUD

    // 复杂查询仍需自定义
    @Select("SELECT * FROM user WHERE age > #{age}")
    List<User> selectAdultUsers(@Param("age") int age);
}

// ========== Service 继承 IService ==========
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    // 无需任何代码即拥有通用 Service 方法

    // 链式查询
    public List<User> findActiveUsers() {
        return lambdaQuery()
                .eq(User::getStatus, 1)
                .ge(User::getAge, 18)
                .orderByDesc(User::getCreateTime)
                .list();
    }

    // 分页查询
    public Page<User> pageUsers(int page, int size) {
        Page<User> pageInfo = new Page<>(page, size);
        return lambdaQuery()
                .like(StringUtils.isNotBlank(name), User::getUserName, name)
                .page(pageInfo);
    }

    // 批量操作
    public void batchSave(List<User> users) {
        saveBatch(users, 1000); // 每批 1000 条
    }
}
```

**MyBatis-Plus 核心特性：**

```java
// ========== 条件构造器 QueryWrapper / LambdaQueryWrapper ==========
// 推荐使用 LambdaQueryWrapper 避免字段名硬编码
List<User> users = userService.lambdaQuery()
    .eq(User::getStatus, 1)                    // =
    .ne(User::getAge, 0)                       // !=
    .gt(User::getAge, 18)                      // >
    .ge(User::getAge, 18)                      // >=
    .lt(User::getAge, 60)                      // <
    .le(User::getAge, 60)                      // <=
    .like(User::getUserName, "张")              // LIKE '%张%'
    .likeLeft(User::getUserName, "张")          // LIKE '%张'
    .likeRight(User::getUserName, "张")         // LIKE '张%'
    .in(User::getRoleId, Arrays.asList(1, 2, 3))// IN
    .between(User::getCreateTime, start, end)  // BETWEEN
    .isNull(User::getEmail)                    // IS NULL
    .orderByDesc(User::getCreateTime)          // ORDER BY
    .last("LIMIT 10")                          // 追加 SQL
    .list();

// ========== 分页插件 ==========
@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}

// ========== 乐观锁 ==========
// 实体字段加 @Version，更新时自动 +1，并发下防止覆盖
@Entity
public class Product {
    @Version
    private Integer version;
}

// ========== 自动填充 ==========
@Component
public class MetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
```

#### MyBatis Dynamic SQL（动态 SQL）

```xml
<mapper namespace="com.example.mapper.UserMapper">
    <!-- where 标签：自动处理 WHERE 和多余的 AND/OR -->
    <select id="selectByCondition" resultType="User">
        SELECT * FROM user
        <where>
            <if test="userName != null and userName != ''">
                AND user_name LIKE CONCAT('%', #{userName}, '%')
            </if>
            <if test="age != null">
                AND age = #{age}
            </if>
            <if test="email != null">
                AND email = #{email}
            </if>
        </where>
    </select>

    <!-- set 标签：自动处理 SET 和多余的逗号 -->
    <update id="updateSelective">
        UPDATE user
        <set>
            <if test="userName != null">user_name = #{userName},</if>
            <if test="age != null">age = #{age},</if>
            <if test="email != null">email = #{email},</if>
        </set>
        WHERE id = #{id}
    </update>

    <!-- foreach 标签：IN 查询 -->
    <select id="selectByIds" resultType="User">
        SELECT * FROM user WHERE id IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">
            #{id}
        </foreach>
    </select>

    <!-- foreach 批量插入 -->
    <insert id="batchInsert">
        INSERT INTO user(user_name, age, email) VALUES
        <foreach collection="list" item="user" separator=",">
            (#{user.userName}, #{user.age}, #{user.email})
        </foreach>
    </insert>

    <!-- choose / when / otherwise：条件分支 -->
    <select id="selectByOrder" resultType="User">
        SELECT * FROM user
        <where>
            <choose>
                <when test="sortBy == 'name'">
                    ORDER BY user_name
                </when>
                <when test="sortBy == 'age'">
                    ORDER BY age
                </when>
                <otherwise>
                    ORDER BY id
                </otherwise>
            </choose>
        </where>
    </select>

    <!-- bind 标签：复用变量 -->
    <select id="searchByName" resultType="User">
        <bind name="pattern" value="'%' + name + '%'"/>
        SELECT * FROM user WHERE user_name LIKE #{pattern}
    </select>
</mapper>
```

```java
// Java 注解方式动态 SQL（@SelectProvider）
@SelectProvider(type = UserSqlProvider.class, method = "selectByCondition")
List<User> selectByCondition(UserQuery query);

public class UserSqlProvider {
    public String selectByCondition(UserQuery query) {
        return new SQL() {{
            SELECT("*");
            FROM("user");
            if (query.getUserName() != null) {
                WHERE("user_name LIKE CONCAT('%', #{userName}, '%')");
            }
            if (query.getAge() != null) {
                WHERE("age = #{age}");
            }
            if (query.getEmail() != null) {
                WHERE("email = #{email}");
            }
        }}.toString();
    }
}
```

#### MyBatis-Plus Code Generator（代码生成器）

```java
// ========== AutoGenerator 快速生成 ==========
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/db", "root", "password")
            .globalConfig(builder -> builder
                .author("developer")
                .outputDir("src/main/java")
                .disableOpenDir())
            .packageConfig(builder -> builder
                .parent("com.example")
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller"))
            .strategyConfig(builder -> builder
                .addInclude("user", "role", "dept") // 表名
                .entityBuilder()
                    .enableLombok()
                    .enableTableFieldAnnotation()
                    .logicDeleteColumnName("deleted")
                    .versionColumnName("version")
                .serviceBuilder()
                    .formatServiceFileName("%sService")
                .controllerBuilder()
                    .enableRestStyle()
                    .enableHyphenStyle())
            .templateEngine(new VelocityTemplateEngine())
            .execute();
    }
}
```

#### MyBatis-Plus Multi-Datasource（多数据源）

```java
// ========== 添加依赖 ==========
// <dependency>
//     <groupId>com.baomidou</groupId>
//     <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
// </dependency>

// ========== 配置多数据源 ==========
// application.yml:
// spring:
//   datasource:
//     dynamic:
//       primary: master
//       strict: true
//       datasource:
//         master:
//           url: jdbc:mysql://localhost:3306/db_master
//           username: root
//           password: root
//         slave_1:
//           url: jdbc:mysql://localhost:3306/db_slave1
//           username: root
//           password: root
//         slave_2:
//           url: jdbc:mysql://localhost:3306/db_slave2
//           username: root
//           password: root

// ========== 使用 @DS 切换数据源 ==========
@Service
@DS("master")
public class UserService extends ServiceImpl<UserMapper, User> {

    @DS("slave_1") // 查询走从库
    public List<User> listFromSlave() {
        return list();
    }

    @DS("slave_2")
    public User getByIdFromSlave2(Long id) {
        return getById(id);
    }

    // 未标注 @DS 则走默认 primary
    public void saveMaster(User user) {
        save(user);
    }
}

// ========== 方法级别和类级别可组合 ==========
// 类上 @DS("master")，方法上 @DS("slave") 优先
```

#### MyBatis-Plus Custom TypeHandler（自定义类型处理器）

```java
// ========== JSON 字段处理器 ==========
@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter,
                                     JdbcType jdbcType) throws SQLException {
        ps.setString(i, MAPPER.writeValueAsString(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return json == null ? null : MAPPER.readValue(json, new TypeReference<List<String>>() {});
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return json == null ? null : MAPPER.readValue(json, new TypeReference<List<String>>() {});
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return json == null ? null : MAPPER.readValue(json, new TypeReference<List<String>>() {});
    }
}

// 实体中使用
@Data
@TableName(value = "user", autoResultMap = true)
public class User {
    private Long id;
    private String userName;

    @TableField(typeHandler = JsonListTypeHandler.class)
    private List<String> tags; // 数据库存储 JSON 字符串 ["a","b","c"]
}

// ========== 枚举类型处理器 ==========
public enum Gender {
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue  // MyBatis-Plus 注解，标记存储到数据库的值
    private final int code;
    private final String desc;
}

@Data
@TableName("user")
public class User {
    private Long id;
    private Gender gender; // 自动映射为 int
}
```

#### MyBatis Plugin（Interceptor 拦截器）

```java
// ========== 自定义拦截器：SQL 加密 ==========
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
public class EncryptInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        MetaObject meta = SystemMetaObject.forObject(handler);
        String originalSql = meta.getValue("delegate.boundSql.sql").toString();

        // 对敏感表进行加密改写
        if (originalSql.contains("credit_card")) {
            String encryptedSql = originalSql.replaceAll("credit_card_no",
                "AES_DECRYPT(credit_card_no, 'secret')");
            meta.setValue("delegate.boundSql.sql", encryptedSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) { }
}

// ========== 分页插件（MyBatis-Plus 已内置） ==========
@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新/删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
```

#### Logical Delete & Optimistic Lock（逻辑删除与乐观锁进阶）

```java
// ========== 逻辑删除 ==========
// application.yml:
// mybatis-plus:
//   global-config:
//     db-config:
//       logic-delete-field: deleted
//       logic-delete-value: 1      # 逻辑已删除值
//       logic-not-delete-value: 0  # 逻辑未删除值

@Data
@TableName("user")
public class User {
    private Long id;

    @TableLogic
    private Integer deleted; // 0-未删除，1-已删除

    private String userName;
}

// 使用：deleteById 实际执行 UPDATE user SET deleted=1 WHERE id=?
// 查询：selectList 自动加 WHERE deleted=0

// ========== 乐观锁进阶 ==========
@Data
@TableName("product")
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;

    @Version
    private Integer version;

    // 多字段乐观锁（自定义重试）
}

// 失败重试
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Retryable(value = OptimisticLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public boolean deductStock(Long productId, int quantity) {
        Product product = getById(productId);
        if (product.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        product.setStock(product.getStock() - quantity);
        return updateById(product); // version 不匹配则抛出 OptimisticLockException
    }
}

// 全局配置乐观锁重试（Spring Retry）
@Configuration
@EnableRetry
public class RetryConfig { }
```

#### MyBatis vs MyBatis-Plus vs JPA 对比

| 维度 | MyBatis | MyBatis-Plus | JPA / Hibernate |
|------|---------|--------------|------------------|
| SQL 控制 | 完全手写 SQL | 手写 SQL + 自动生成 | 自动生成，可 JPQL 覆盖 |
| 开发效率 | 中（需写大量 XML） | 高（BaseMapper 零 SQL） | 高（方法命名查询） |
| 动态 SQL | XML <if>/<where>/<foreach> | LambdaQueryWrapper 链式 | Criteria / Specification |
| 分页 | RowBounds / PageHelper | PaginationInnerInterceptor | Pageable 开箱即用 |
| 复杂查询 | ✅ 直接写 SQL 最灵活 | ✅ Lambda + 自定义 SQL | ❌ N+1 需 EntityGraph |
| 逻辑删除 | 需手动 SQL | @TableLogic 注解 | @SQLDelete + @Where |
| 乐观锁 | 需手动 version+1 | @Version 自动处理 | @Version 自动处理 |
| 自动填充 | 需自定义拦截器 | MetaObjectHandler | @EntityListeners |
| 多表关联 | association/collection | 同 MyBatis + @TableField(exist=false) | @ManyToOne/@OneToMany |
| 批量操作 | foreach XML | saveBatch 内置 | 需 batchSize + 手动 flush |
| 缓存 | 二级缓存需配置 | 同 MyBatis | L2 Cache + Redis 集成 |
| 代码生成 | MyBatis Generator | AutoGenerator（更便捷） | 无官方生成器 |
| 国内生态 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 国际生态 | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

---

### 2.5 JPA / Hibernate

```java
// ========== 实体类 ==========
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(nullable = false)
    private Integer age;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    // 关联关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @ManyToMany
    @JoinTable(name = "user_role",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}

// ========== Repository ==========
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // 方法命名查询（无需写 SQL）
    List<User> findByUserName(String userName);
    List<User> findByAgeBetween(int min, int max);
    List<User> findByDeptName(String deptName);

    // 自定义 JPQL
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    // 原生 SQL
    @Query(value = "SELECT * FROM user WHERE MATCH(user_name) AGAINST(?1)", nativeQuery = true)
    List<User> searchByFullText(String keyword);
}

// ========== 使用 ==========
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> search(String name, Integer minAge, Integer maxAge) {
        Specification<User> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, cb) ->
                cb.like(root.get("userName"), "%" + name + "%"));
        }
        if (minAge != null && maxAge != null) {
            spec = spec.and((root, query, cb) ->
                cb.between(root.get("age"), minAge, maxAge));
        }

         return userRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createTime"));
    }
}
```

#### JPA Auditing（审计功能）

```java
// ========== 启用审计 ==========
@SpringBootApplication
@EnableJpaAuditing // 开启 JPA 审计
public class Application { }

// ========== 实体 ==========
@Entity
@EntityListeners(AuditingEntityListener.class) // 激活审计监听
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @CreatedBy
    private String createUser;

    @LastModifiedBy
    private String updateUser;
}

// ========== AuditorAware 实现 ==========
@Component
public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // 从 SecurityContext 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.of("system");
        }
        return Optional.of(auth.getName());
    }
}
```

#### JPA Entity Graph（N+1 问题解决方案）

```java
// ========== 实体定义 ==========
@Entity
@NamedEntityGraph(name = "User.withRoles", attributeNodes = {
    @NamedAttributeNode("dept"),
    @NamedAttributeNode(value = "roles", subgraph = "roles.permissions")
},
subgraphs = {
    @NamedSubgraph(name = "roles.permissions",
                   attributeNodes = @NamedAttributeNode("permissions"))
})
public class User {
    @ManyToOne(fetch = FetchType.LAZY)
    private Dept dept;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles;
}

// ========== Repository 使用 ==========
public interface UserRepository extends JpaRepository<User, Long> {

    // 方法1：@EntityGraph 注解（推荐）
    @EntityGraph(value = "User.withRoles", type = EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    // 方法2：Spring Data 方法名（更简洁）
    @EntityGraph(attributePaths = {"dept", "roles"})
    List<User> findByStatus(Integer status);

}
```

// 另见：JOIN FETCH 方式
// @Query("SELECT u FROM User u JOIN FETCH u.dept LEFT JOIN FETCH u.roles WHERE u.id = :id")
// Optional<User> findByIdWithJoinFetch(@Param("id") Long id);

#### JPA Criteria API（动态查询，复杂场景建议用 Specification）

```java
@Service
public class UserQueryService {
    @PersistenceContext
    private EntityManager em;

    public Page<User> search(UserQuery query, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (query.getUserName() != null)
            predicates.add(cb.like(root.get("userName"), "%" + query.getUserName() + "%"));
        if (query.getMinAge() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), query.getMinAge()));
        if (query.getMaxAge() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("age"), query.getMaxAge()));

        cq.where(predicates.toArray(new Predicate[0])).orderBy(cb.desc(root.get("createTime")));
        TypedQuery<User> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(User.class)));
        countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        return new PageImpl<>(typedQuery.getResultList(), pageable, em.createQuery(countQuery).getSingleResult());
    }
}
```

#### JPA LockMode（锁机制）

```java
// 乐观锁（@Version）
@Entity
public class Product {
    @Id private Long id;
    private Integer stock;
    @Version
    private Long version; // 更新时自动校验，抛 OptimisticLockException
}

// 悲观锁
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT ... FOR UPDATE
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)  // SELECT ... FOR SHARE
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForShare(@Param("id") Long id);
}
```

#### JPA Batch Operations

```java
// application.yml:
// spring.jpa.properties.hibernate.jdbc.batch_size: 50
// spring.jpa.properties.hibernate.order_inserts: true
// spring.jpa.properties.hibernate.order_updates: true

@Service
public class BatchService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void batchInsert(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            em.persist(users.get(i));
            if (i > 0 && i % 50 == 0) { em.flush(); em.clear(); }
        }
        em.flush(); em.clear();
    }

    // 批量删除：直接 JPQL，不逐条
    @Transactional
    public int deleteByStatus(Integer status) {
        return em.createQuery("DELETE FROM User u WHERE u.status = :status")
                 .setParameter("status", status).executeUpdate();
    }
}
```

---

### 2.6 Spring AI（从简单对话到 Agent）

Spring AI 是 Spring 生态中的 AI 集成框架，提供统一的 API 对接大语言模型（LLM）、向量数据库、文档处理等 AI 能力。本节从基础对话开始，逐步深入到完整 Agent 实现。

#### 2.6.1 快速开始

**核心概念：**

| 概念 | 说明 | 类比 |
|------|------|------|
| `ChatClient` | AI 聊天客户端 | JDBC 的 Connection |
| `ChatMemory` | 聊天记忆（存储对话历史） | HttpSession |
| `EmbeddingModel` | 文本向量化模型 | 将文本转为浮点数组 |
| `VectorStore` | 向量数据库存储 | 存储和检索向量 |
| `Document` | 文档对象（文本 + 元数据） | 实体类 |
| `ToolCallback` | 工具调用（Function Calling） | RPC 调用描述 |
| `Advisor` | 聊天增强器（记忆、安全等） | Spring 拦截器 |

**Maven 依赖：**

```xml
<properties>
    <spring-ai.version>1.0.4</spring-ai.version>
</properties>

<dependencies>
    <!-- OpenAI -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    </dependency>
    <!-- 向量存储 -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
    </dependency>
    <!-- 文档读取 -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-tika-document-reader</artifactId>
    </dependency>
    <!-- PDF -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-pdf-document-reader</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>${spring-ai.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**配置：**

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o
          temperature: 0.7
          max-tokens: 4096
      embedding:
        options:
          model: text-embedding-3-small

    vectorstore:
      pgvector:
        index-type: HNSW
        distance-type: COSINE_DISTANCE
        dimensions: 1536

    retriever:
      similarity-threshold: 0.6
      top-k: 5
```

---

#### 2.6.2 简单对话

```java
@Configuration
public class AIConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("你是一个专业的 Java 后端工程师助手，擅长 Spring Boot 和微服务架构。")
            .build();
    }
}

@RestController
@RequestMapping("/ai")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // GET /ai/chat?message=你好
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    // POST /ai/chat
    @PostMapping("/chat")
    public String chatPost(@RequestBody ChatRequest request) {
        return chatClient.prompt()
            .user(u -> u.text(request.message()))
            .call()
            .content();
    }

    public record ChatRequest(String message) {}
}

// ========== 带系统参数注入 ==========
@Service
public class TranslationService {
    private final ChatClient chatClient;

    public TranslationService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String translate(String text, String targetLang) {
        return chatClient.prompt()
            .system(sp -> sp
                .param("lang", targetLang)
                .text("你是一个专业翻译助手。请将用户输入翻译成 {lang}。只返回翻译结果，不要解释。"))
            .user(text)
            .call()
            .content();
    }
}
```

---

#### 2.6.3 结构化输出

让 AI 返回非文本内容，直接解析为 Java 对象。

```java
// ========== 基本 Record 输出 ==========
public record CodeReview(
    String summary,
    List<String> issues,
    String severity,
    int score
) {}

@Service
public class CodeReviewService {
    private final ChatClient chatClient;

    public CodeReviewService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public CodeReview review(String code) {
        return chatClient.prompt()
            .user("请 review 以下代码并返回结构化结果：\n" + code)
            .call()
            .entity(CodeReview.class);
    }

    // 列表输出
    public List<String> extractTodos(String text) {
        return chatClient.prompt()
            .user("从以下文本中提取所有待办事项：\n" + text)
            .call()
            .entity(new ParameterizedTypeReference<List<String>>() {});
    }

    // Map 输出
    public Map<String, Object> analyzeSentiment(String text) {
        return chatClient.prompt()
            .user(u -> u.text("分析以下文本的情感，返回 JSON：\n" + text))
            .call()
            .entity(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}

// ========== 复杂嵌套结构 ==========
public record MeetingSummary(
    @JsonProperty("title") String title,
    @JsonProperty("date") String date,
    @JsonProperty("participants") List<String> participants,
    @JsonProperty("action_items") List<ActionItem> actionItems
) {
    public record ActionItem(String owner, String task, String deadline) {}
}

@Service
public class MeetingService {
    private final ChatClient chatClient;

    public MeetingService(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("你是一个会议记录分析助手。从会议记录中提取结构化信息。")
            .build();
    }

    public MeetingSummary summarize(String transcript) {
        return chatClient.prompt()
            .user(u -> u.text("请分析以下会议记录：\n{transcript}")
                .param("transcript", transcript))
            .call()
            .entity(MeetingSummary.class);
    }
}
```

---

#### 2.6.4 流式响应

```java
@RestController
@RequestMapping("/ai")
public class StreamController {
    private final ChatClient chatClient;

    public StreamController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // 基本流式
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }

    // SSE 格式
    @GetMapping(value = "/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamSSE(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content()
            .map(token -> ServerSentEvent.<String>builder()
                .data(token)
                .event("token")
                .build())
            .concatWithValues(ServerSentEvent.<String>builder()
                .data("[DONE]")
                .event("done")
                .build());
    }

    // 流式 + 工具调用
    @GetMapping(value = "/stream-tools", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamWithTools(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .tools("queryWeather", "searchProduct")  // 可用的工具
            .stream()
            .content();
    }
}
```

---

#### 2.6.5 多轮对话（记忆管理）

```java
// ========== 基于内存的记忆 ==========
@Service
public class MemoryChatService {
    private final ChatClient chatClient;

    public MemoryChatService(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("你是一个友好的聊天助手。")
            .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
            .user(message)
            .advisors(a -> a
                .param("chat_memory_conversation_id", sessionId)
                .param("chat_memory_response_size", 10))  // 保留最近10轮
            .call()
            .content();
    }
}

// ========== 基于数据库的记忆（持久化） ==========
@Service
public class PersistentMemoryChatService {
    private final ChatClient chatClient;

    public PersistentMemoryChatService(
            ChatClient.Builder builder,
            JdbcTemplate jdbcTemplate,
            ChatClient.Builder chatClientBuilder) {

        // 使用 JDBC 存储对话历史
        var chatMemory = new JdbcChatMemory(jdbcTemplate);

        this.chatClient = builder
            .defaultSystem("你是一个客户服务助手。")
            .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
            .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
            .user(message)
            .advisors(a -> a
                .param("chat_memory_conversation_id", sessionId)
                .param("chat_memory_response_size", 20))
            .call()
            .content();
    }
}

// ========== 带摘要的记忆（长对话压缩） ==========
@Service
public class SummarizingMemoryService {
    private final ChatClient chatClient;

    public SummarizingMemoryService(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("你是一个文档分析助手。")
            .defaultAdvisors(new SummarizingChatMemoryAdvisor(
                new InMemoryChatMemory(),  // 底层存储
                10,                         // 保留最近10条未摘要的消息
                true                        // 启用摘要
            ))
            .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
            .user(message)
            .advisors(a -> a
                .param("chat_memory_conversation_id", sessionId))
            .call()
            .content();
    }
}
```

---

#### 2.6.6 工具调用（Function Calling）

##### 简单工具

```java
@Component
@Slf4j
public class WeatherTools {
    @Tool(description = "根据城市名查询当前天气")
    public String getWeather(@ToolParam("城市名，如 北京、上海") String city) {
        log.info("查询天气: {}", city);
        // 实际会调用天气 API
        if ("北京".equals(city)) return "北京 晴 25°C 湿度40%";
        if ("上海".equals(city)) return "上海 多云 28°C 湿度65%";
        return city + " 23°C 晴";
    }

    @Tool(description = "查询未来一周天气预报")
    public List<String> forecast(@ToolParam("城市名") String city) {
        return List.of(
            city + " 周一 晴 25°C",
            city + " 周二 多云 23°C",
            city + " 周三 小雨 20°C"
        );
    }
}
```

##### 业务工具（数据库操作）

```java
@Component
@Slf4j
public class OrderServiceTools {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceTools(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Tool(description = "根据订单 ID 查询订单详情")
    public OrderInfo getOrder(@ToolParam("订单ID") Long orderId) {
        log.info("查询订单: {}", orderId);
        return orderRepository.findById(orderId)
            .map(o -> new OrderInfo(o.getId(), o.getStatus(), o.getAmount(),
                o.getCreateTime(), o.getItems()))
            .orElse(null);
    }

    @Tool(description = "查询用户所有订单")
    public List<OrderSummary> listOrders(@ToolParam("用户ID") Long userId) {
        return orderRepository.findByUserId(userId)
            .stream()
            .map(o -> new OrderSummary(o.getId(), o.getStatus(), o.getAmount(), o.getCreateTime()))
            .toList();
    }

    @Tool(description = "取消订单（仅待付款和待发货状态可取消）")
    public String cancelOrder(@ToolParam("订单ID") Long orderId,
                              @ToolParam(required = false) String reason) {
        var opt = orderRepository.findById(orderId);
        if (opt.isEmpty()) return "订单不存在";
        var order = opt.get();
        if (!Set.of("PENDING", "PAID").contains(order.getStatus())) {
            return "订单当前状态为 " + order.getStatus() + "，无法取消";
        }
        order.setStatus("CANCELLED");
        order.setCancelReason(reason);
        orderRepository.save(order);
        return "订单 " + orderId + " 已取消";
    }

    // 返回值必须是简单类型或 Record
    public record OrderInfo(Long id, String status, BigDecimal amount,
                            LocalDateTime createTime, List<OrderItem> items) {}
    public record OrderItem(Long productId, String name, Integer quantity, BigDecimal price) {}
    public record OrderSummary(Long id, String status, BigDecimal amount, LocalDateTime createTime) {}
}
```

##### 注册并使用工具

```java
@Configuration
public class ToolConfig {
    @Bean
    public ChatClient toolChatClient(
            ChatClient.Builder builder,
            WeatherTools weatherTools,
            OrderServiceTools orderTools) {
        return builder
            .defaultSystem("你是一个智能客服助手，可以查询天气、管理订单。")
            .defaultTools(weatherTools, orderTools)
            .build();
    }
}

@RestController
@RequestMapping("/ai/assistant")
public class AssistantController {
    private final ChatClient chatClient;

    public AssistantController(ChatClient toolChatClient) {
        this.chatClient = toolChatClient;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest req) {
        return chatClient.prompt()
            .user(req.message())
            .call()
            .content();
    }
}
```

##### 动态工具选择（按需注册）

```java
@Service
public class DynamicToolService {
    private final ChatClient.Builder builder;

    public DynamicToolService(ChatClient.Builder builder) {
        this.builder = builder;
    }

    // 根据业务场景选择可用工具
    public String chatWithContext(String message, String scene) {
        Object[] tools = switch (scene) {
            case "order" -> new Object[]{new OrderServiceTools(...)};
            case "weather" -> new Object[]{new WeatherTools()};
            case "all" -> new Object[]{new OrderServiceTools(...), new WeatherTools()};
            default -> new Object[]{};
        };

        return builder
            .build()
            .prompt()
            .user(message)
            .tools(tools)  // 此处注册本次调用可用工具
            .call()
            .content();
    }
}
```

---

#### 2.6.7 RAG（检索增强生成）

##### 基础 RAG

```java
// ========== 向量存储配置 ==========
@Configuration
public class VectorStoreConfig {
    @Bean
    public VectorStore vectorStore(JdbcClient jdbcClient, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcClient, embeddingModel)
            .vectorTableName("knowledge_vectors")
            .distanceType(PgDistanceType.COSINE_DISTANCE)
            .indexType(PgIndexType.HNSW)
            .dimensions(1536)
            .build();
    }
}

// ========== 知识库管理 ==========
@Service
public class KnowledgeService {
    private final VectorStore vectorStore;

    public KnowledgeService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocument(String content, String source, String category) {
        Document doc = new Document(content, Map.of(
            "source", source,
            "category", category,
            "timestamp", Instant.now().toString()
        ));
        vectorStore.add(List.of(doc));
    }

    public void addDocuments(List<Document> docs) {
        vectorStore.add(docs);
    }

    public List<Document> search(String query, int topK) {
        return vectorStore.similaritySearch(
            SearchRequest.query(query)
                .withTopK(topK)
                .withSimilarityThreshold(0.6));
    }

    public void deleteDocument(String docId) {
        vectorStore.delete(List.of(docId));
    }
}

// ========== RAG 问答服务 ==========
@Service
public class RAGQuestionAnswerService {
    private final ChatClient chatClient;
    private final KnowledgeService knowledgeService;

    public RAGQuestionAnswerService(ChatClient.Builder builder, KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
        this.chatClient = builder
            .defaultSystem("""
                你是一个知识库问答助手。根据提供的上下文回答问题。
                如果上下文信息不足以回答问题，请如实告知用户。
                引用来源时，请标明文档的 source 字段。
                """)
            .build();
    }

    public String ask(String question) {
        List<Document> docs = knowledgeService.search(question, 5);
        if (docs.isEmpty()) {
            return "抱歉，知识库中没有找到相关信息。";
        }

        String context = docs.stream()
            .map(d -> {
                String source = d.getMetadata().getOrDefault("source", "未知来源");
                return String.format("[来源: %s]\n%s", source, d.getContent());
            })
            .collect(Collectors.joining("\n\n---\n\n"));

        return chatClient.prompt()
            .user(u -> u.text("""
                请基于以下知识库内容回答问题：

                {context}

                问题：{question}
                """)
                .param("context", context)
                .param("question", question))
            .call()
            .content();
    }
}
```

##### 生产级 RAG（带重排序和过滤）

```java
@Service
public class AdvancedRAGService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;

    public AdvancedRAGService(ChatClient.Builder builder,
                              VectorStore vectorStore,
                              EmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.chatClient = builder
            .defaultSystem("你是一个精确的知识问答助手。")
            .build();
    }

    public String ask(String question, String category) {
        // 1. 向量检索（获取更多候选）
        List<Document> candidates = vectorStore.similaritySearch(
            SearchRequest.query(question)
                .withTopK(20)
                .withSimilarityThreshold(0.5));

        // 2. 按分类过滤
        List<Document> filtered = candidates.stream()
            .filter(d -> category == null || category.equals(d.getMetadata().get("category")))
            .toList();

        // 3. 重排序（用更精准的 Embedding 模型二次打分）
        var queryEmbed = embeddingModel.embed(question);
        List<ScoredDocument> reranked = filtered.parallelStream()
            .map(doc -> {
                var docEmbed = embeddingModel.embed(doc.getContent());
                double score = cosineSimilarity(queryEmbed, docEmbed);
                return new ScoredDocument(doc, score);
            })
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(5)
            .toList();

        // 4. 构建上下文
        String context = reranked.stream()
            .map(sd -> String.format("[相关度: %.2f] %s", sd.score, sd.document.getContent()))
            .collect(Collectors.joining("\n\n"));

        // 5. 生成回答
        return chatClient.prompt()
            .user(u -> u.text("问题：{question}\n\n参考内容：\n{context}")
                .param("question", question)
                .param("context", context))
            .call()
            .content();
    }

    private record ScoredDocument(Document document, double score) {}

    private double cosineSimilarity(Embedding a, Embedding b) {
        float[] va = a.getVector(), vb = b.getVector();
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < va.length; i++) {
            dot += va[i] * vb[i];
            na += va[i] * va[i];
            nb += vb[i] * vb[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
}
```

---

#### 2.6.8 文档处理（ETL 管线）

```java
// ========== 文档读取与处理 ==========
@Service
public class DocumentIngestionService {
    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // 读 PDF
    @EventListener(ApplicationReadyEvent.class)
    public void ingestPdf() {
        var reader = new PagePdfDocumentReader("classpath:/docs/product-manual.pdf");
        var docs = reader.read();
        // 分块处理
        var splitter = new TokenTextSplitter(500, 100);
        var chunks = splitter.split(docs);
        vectorStore.accept(chunks);
    }

    // 读 Word / HTML / 文本
    public void ingestFile(String filePath) {
        var reader = new TikaDocumentReader(filePath);
        var docs = reader.read();
        var splitter = new TokenTextSplitter(300, 50);
        var chunks = splitter.split(docs);
        vectorStore.add(chunks);
    }

    // 读网页
    public void ingestUrl(String url) {
        var reader = new UrlDocumentReader(url);
        var docs = reader.read();
        vectorStore.add(docs);
    }

    // 全量刷新知识库
    @Scheduled(cron = "0 0 3 * * ?")  // 每天凌晨3点
    public void refreshKnowledgeBase() {
        // 先清空
        vectorStore.delete(List.of());
        // 重新导入所有文档
        ingestPdf();
    }
}
```

---

#### 2.6.9 构建 Agent

##### Agent 模式 1：ReAct（思考-行动-观察）

```java
@Component
public class ReActAgent {
    private final ChatClient chatClient;

    public ReActAgent(ChatClient.Builder builder, WeatherTools weatherTools, OrderServiceTools orderTools) {
        this.chatClient = builder
            .defaultSystem("你是一个智能助手。你可以使用工具来获取信息或执行操作。遵循 Thought → Action → Observation → Final Answer 循环。")
            .defaultTools(weatherTools, orderTools)
            .build();
    }

    public String execute(String task) {
        return chatClient.prompt().user(task).call().content();
    }
}
```

##### Agent 模式 2：自主 Agent（自动决策 + 多轮工具调用）

```java
@Component
@Slf4j
public class AutonomousAgent {
    private final ChatClient chatClient;

    public AutonomousAgent(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("你是一个自主 AI Agent。分析用户请求，按需调用工具获取信息，给出综合回答。")
            .build();
    }

    public String execute(String task, Object... tools) {
        return chatClient.prompt().user(task).tools(tools).call().content();
    }
}

@Service
public class AgentOrchestrator {
    private final AutonomousAgent agent;
    private final OrderServiceTools orderTools;
    private final WeatherTools weatherTools;

    public AgentOrchestrator(AutonomousAgent agent,
                             OrderServiceTools orderTools,
                             WeatherTools weatherTools) {
        this.agent = agent;
        this.orderTools = orderTools;
        this.weatherTools = weatherTools;
    }

    public String handleComplexRequest(String request) {
        return agent.execute(request, orderTools, weatherTools);
    }
}
```

##### Agent 模式 3：Multi-Agent（多 Agent 协作）

```java
@Component
@Slf4j
public class MultiAgentOrchestrator {
    private final ChatClient orchestrator;
    private final ChatClient orderAgent;
    private final ChatClient weatherAgent;

    public MultiAgentOrchestrator(ChatClient.Builder builder, OrderServiceTools orderTools, WeatherTools weatherTools) {
        this.orchestrator = builder.defaultSystem("你是 Agent 调度器。根据用户请求分发给专业 Agent。输出格式：AGENT: <代理名> | QUERY: <查询>").build();
        this.orderAgent = builder.defaultSystem("你是一个订单管理专家。").defaultTools(orderTools).build();
        this.weatherAgent = builder.defaultSystem("你是一个天气预报专家。").defaultTools(weatherTools).build();
    }

    public String handle(String userRequest) {
        String routing = orchestrator.prompt().user("请调度以下请求：" + userRequest).call().content();
        return switch (routing) {
            case String s when s.contains("ORDER") -> orderAgent.prompt().user(userRequest).call().content();
            case String s when s.contains("WEATHER") -> weatherAgent.prompt().user(userRequest).call().content();
            default -> orchestrator.prompt().user("请直接回答：" + userRequest).call().content();
        };
    }
}
```

---

#### 2.6.10 支持的模型供应商

| 供应商 | Starter 依赖 | Chat | Embedding | Image | Audio |
|--------|-------------|------|-----------|-------|-------|
| OpenAI | `spring-ai-openai-spring-boot-starter` | ✅ | ✅ | ✅ | ✅ |
| Azure OpenAI | `spring-ai-azure-openai-spring-boot-starter` | ✅ | ✅ | ✅ | ❌ |
| Anthropic | `spring-ai-anthropic-spring-boot-starter` | ✅ | ❌ | ❌ | ❌ |
| Ollama(本地) | `spring-ai-ollama-spring-boot-starter` | ✅ | ✅ | ❌ | ❌ |
| Google Vertex AI | `spring-ai-vertex-ai-gemini-spring-boot-starter` | ✅ | ✅ | ❌ | ❌ |
| Amazon Bedrock | `spring-ai-bedrock-spring-boot-starter` | ✅ | ✅ | ❌ | ❌ |
| Mistral AI | `spring-ai-mistral-ai-spring-boot-starter` | ✅ | ✅ | ❌ | ❌ |
| DeepSeek | 通过 OpenAI 兼容模式接入 | ✅ | ❌ | ❌ | ❌ |
| 通义千问 | 通过 OpenAI 兼容模式接入 | ✅ | ❌ | ❌ | ❌ |

DeepSeek 和通义千问通过 OpenAI 兼容模式接入：
```yaml
spring:
  ai:
    openai:
      base-url: https://api.deepseek.com  # 或通义千问的 endpoint
      api-key: ${API_KEY}
      chat:
        options:
          model: deepseek-chat  # 或 qwen-turbo
```

---

---

## 3. 数据存储 (Data Storage)

### 3.1 MySQL

#### SQL 编写基础

```sql
-- ========== 基础查询 ==========
SELECT u.id, u.user_name, u.age, d.name AS dept_name
FROM user u
LEFT JOIN dept d ON u.dept_id = d.id
WHERE u.age > 18
  AND u.status = 1
ORDER BY u.create_time DESC
LIMIT 10 OFFSET 0;

-- ========== 聚合查询 ==========
SELECT d.name,
       COUNT(u.id) AS user_count,
       AVG(u.age) AS avg_age,
       MAX(u.age) AS max_age,
       MIN(u.age) AS min_age
FROM dept d
LEFT JOIN user u ON u.dept_id = d.id
GROUP BY d.id, d.name
HAVING user_count > 5
ORDER BY user_count DESC;

-- ========== 子查询 ==========
SELECT *
FROM user
WHERE dept_id IN (
    SELECT id FROM dept WHERE status = 1
);

-- ========== 窗口函数 ==========
-- 查询每个部门薪资排名前 3 的员工
SELECT * FROM (
    SELECT
        name,
        salary,
        dept_id,
        ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rn
    FROM employee
) t WHERE t.rn <= 3;

-- ========== 分页优化（深分页深度优化） ==========
-- ❸ 传统分页（越往后越慢）
SELECT * FROM user ORDER BY id LIMIT 100000, 20;

-- ✅ 优化方式 1：子查询
SELECT * FROM user
WHERE id >= (SELECT id FROM user ORDER BY id LIMIT 100000, 1)
ORDER BY id LIMIT 20;

-- ✅ 优化方式 2：游标分页（适合实时滚动）
SELECT * FROM user
WHERE id > 100000
ORDER BY id LIMIT 20;
```

#### 索引原理与优化

**B+ 树索引特性：**
- 非叶子节点只存 key，不存 data，每页能存更多 key
- 叶子节点通过链表相连，范围查询高效
- 通常 3~4 层即可存储千万级数据

```sql
-- ========== 索引类型 ==========
-- 主键索引（聚簇索引）
ALTER TABLE user ADD PRIMARY KEY (id);

-- 普通索引
ALTER TABLE user ADD INDEX idx_user_name (user_name);

-- 唯一索引
ALTER TABLE user ADD UNIQUE INDEX idx_email (email);

-- 复合索引（最左前缀原则）
ALTER TABLE user ADD INDEX idx_dept_age_name (dept_id, age, user_name);
-- 走索引场景：
-- WHERE dept_id = 1 AND age > 18
-- WHERE dept_id = 1 AND age > 18 AND user_name LIKE '张%'
-- 不走索引场景：
-- WHERE age > 18 （缺少 dept_id，不满足最左前缀）
-- WHERE user_name LIKE '张%' （缺少 dept_id，不满足最左前缀）

-- 全文索引（MyISAM 或 InnoDB 5.6+）
ALTER TABLE article ADD FULLTEXT INDEX ft_content (title, content);

-- ========== 查看执行计划 ==========
EXPLAIN SELECT * FROM user WHERE user_name = '张三';
-- type: ALL（全表扫描）/ ref（非唯一索引）/ const（主键/唯一索引）
-- rows：预估扫描行数，越小越好
-- Extra：Using index（覆盖索引）/ Using filesort（需要优化）/ Using temporary（需要优化）

-- ========== 索引优化建议 ==========
-- 1. 不要在索引列上使用函数
-- ❌ WHERE DATE(create_time) = '2025-06-02'
-- ✅ WHERE create_time >= '2025-06-02' AND create_time < '2025-06-03'

-- 2. 避免隐式类型转换
-- ❌ WHERE user_name = 123（user_name 是 varchar）
-- ✅ WHERE user_name = '123'

-- 3. 使用覆盖索引避免回表
-- 索引 idx_dept_age_name 包含了 dept_id, age, user_name
-- 以下查询只需要从索引中获取数据，无需回表
SELECT dept_id, age, user_name FROM user WHERE dept_id = 1;

-- 4. Like 前缀通配符会导致索引失效
-- ❌ WHERE user_name LIKE '%张%'
-- ✅ WHERE user_name LIKE '张%'
```

#### 事务与锁

```sql
-- ========== 查看当前事务隔离级别 ==========
SELECT @@transaction_isolation; -- MySQL 8.0
-- READ-COMMITTED（RC，默认在有些版本）/ REPEATABLE-READ（RR，InnoDB 默认）

-- ========== 手动事务 ==========
START TRANSACTION;
UPDATE account SET balance = balance - 100 WHERE id = 1;
UPDATE account SET balance = balance + 100 WHERE id = 2;
COMMIT; -- 或 ROLLBACK;

-- ========== InnoDB 锁 ==========
-- 行锁（只锁定涉及的行）
SELECT * FROM user WHERE id = 1 FOR UPDATE;           -- 排他锁（写锁）
SELECT * FROM user WHERE id = 1 LOCK IN SHARE MODE;   -- 共享锁（读锁）

-- 表锁
LOCK TABLES user READ;  -- 读锁，其他会话可读不可写
UNLOCK TABLES;

-- 间隙锁 (Gap Lock)：RR 隔离级别下，防止幻读
-- 当 WHERE 条件没有命中索引或范围查询时，会锁住间隙
```

#### 三大范式与反范式

| 范式 | 规则 | 示例 |
|------|------|------|
| 1NF | 字段不可再分 | 地址字段不包含"省市区"多个值 |
| 2NF | 非主键字段完全依赖主键 | 订单明细表只依赖订单+商品联合主键 |
| 3NF | 非主键字段不传递依赖主键 | 用户表中不包含部门地址（应通过部门ID关联） |

#### 主从复制架构

**Binlog（二进制日志）：**
- `row` 格式：记录每一行数据变更，最安全，占用空间大
- `statement` 格式：记录 SQL 语句，占用空间小，但某些函数（NOW()）会导致主从不一致
- `mixed` 格式：默认使用 statement，不确定时自动切换 row
- 生产推荐 `binlog_format = row`

**GTID（全局事务 ID）：**
- 每个事务分配唯一 `server_uuid:transaction_id`
- 简化主从切换：无需指定 binlog 文件和 position
- 自动跳过已执行事务，避免重复

**Semi-sync（半同步复制）：**
- 主库等待至少一个从库确认写入 relay log 后才返回客户端
- 相比异步复制，保证数据不丢失；相比全同步，性能可接受

```sql
-- 查看复制状态
SHOW SLAVE STATUS\G;


#### 分区表

```sql
-- RANGE 分区：按范围划分
CREATE TABLE orders (
    id BIGINT, order_date DATE, amount DECIMAL(10,2)
) PARTITION BY RANGE (YEAR(order_date)) (
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

-- LIST 分区：按枚举值划分
CREATE TABLE staff (
    id INT, name VARCHAR(50), region VARCHAR(20)
) PARTITION BY LIST COLUMNS(region) (
    PARTITION p_north VALUES IN ('北京','天津','河北'),
    PARTITION p_south VALUES IN ('广东','深圳','福建'),
    PARTITION p_east VALUES IN ('上海','浙江','江苏')
);

-- HASH 分区：按哈希取模分散数据
CREATE TABLE logs (
    id INT, log_time DATETIME, content TEXT
) PARTITION BY HASH(id) PARTITIONS 8;

-- KEY 分区：类似 HASH，使用 MySQL 内部哈希函数
CREATE TABLE sessions (
    session_id VARCHAR(64), data BLOB
) PARTITION BY KEY(session_id) PARTITIONS 16;
```


```
-- WITH RECURSIVE 树形查询
WITH RECURSIVE dept_tree AS (
    SELECT id, name, parent_id, 1 AS level
    FROM dept WHERE parent_id IS NULL
    UNION ALL
    SELECT d.id, d.name, d.parent_id, dt.level + 1
    FROM dept d
    JOIN dept_tree dt ON d.parent_id = dt.id
)
SELECT * FROM dept_tree ORDER BY level, id;
```

#### SQL 优化检查清单

| # | 规则 | 说明 |
|---|------|------|
| 1 | 避免 `SELECT *` | 只查询需要的列，利用覆盖索引 |
| 2 | 避免索引列上的函数 | `WHERE DATE(col)=...` 改用范围查询 |
| 3 | 避免隐式类型转换 | 索引字段是 VARCHAR 则传入字符串 |
| 4 | Like 不以 % 开头 | `LIKE '%xxx'` 导致索引失效 |
| 5 | 使用 EXISTS 代替 IN | 子查询结果集大时 EXISTS 更高效 |
| 6 | 避免 OR 条件 | 可能索引失效，用 UNION ALL 替代 |
| 7 | 避免 `WHERE 1=1` | 影响优化器分析，应用层动态拼接 |
| 8 | 深分页用游标 | `WHERE id > last_id LIMIT 20` |
| 9 | 用 UNION ALL 代替 UNION | UNION 会去重增加排序开销 |
| 10 | 减少 JOIN 数量 | 超过 3 个 JOIN 考虑重构 |
| 11 | JOIN 字段类型必须一致 | 类型不匹配导致索引失效 |
| 12 | GROUP BY 配合索引 | `ORDER BY NULL` 避免排序 |
| 13 | 批量操作用批处理 | 大量 INSERT 用 `VALUES (...), (...)` |
| 14 | 避免大事务 | 单事务操作行数建议 < 10000 |
| 15 | 监控慢查询 | `long_query_time=1s`，定期分析 slow_log |

---

### 3.2 Redis

Redis 是一个高性能的键值对内存数据库，支持多种数据结构。

#### 五种基本数据类型

```bash
# ========== 1. String（字符串） ==========
# 应用：缓存、计数器、分布式锁、Session 共享
SET user:1 '{"name":"张三","age":25}'    # 缓存用户信息
SET article:read:1001 0                  # 文章阅读数
INCR article:read:1001                   # +1
INCRBY article:read:1001 10              # +10
DECR article:read:1001                   # -1
GET article:read:1001                    # 获取
SETEX captcha:phone:13800138000 300 1234 # 验证码，300 秒过期
SETNX lock:order:1001 1                  # 分布式锁（不存在才设置）
MSET k1 v1 k2 v2                         # 批量设置
MGET k1 k2                               # 批量获取
STRLEN k1                                # 字符串长度

# ========== 2. List（列表，底层为双向链表） ==========
# 应用：消息队列、最新消息列表、时间线
LPUSH news:list "news1" "news2"          # 左侧插入（最新消息）
RPUSH news:list "news3"                  # 右侧插入
LPOP news:list                           # 从左侧弹出
RPOP news:list                           # 从右侧弹出
LRANGE news:list 0 -1                    # 获取全部
LLEN news:list                           # 长度
BRPOP queue 0                            # 阻塞式右侧弹出（实现消息队列）

# ========== 3. Set（集合，无序不可重复） ==========
# 应用：标签、共同好友、随机抽奖、去重
SADD user:1:tags "java" "redis" "spring" # 给用户添加标签
SMEMBERS user:1:tags                     # 获取所有标签
SISMEMBER user:1:tags "java"             # 是否包含
SINTER user:1:tags user:2:tags           # 交集（共同标签）
SUNION user:1:tags user:2:tags           # 并集
SDIFF user:1:tags user:2:tags            # 差集
SCARD user:1:tags                        # 元素数量
SRANDMEMBER user:1:tags 2                # 随机取 2 个
SPOP prize:pool 1                        # 随机弹出（抽奖）

# ========== 4. Hash（哈希，适合存储对象） ==========
# 应用：存储对象（如用户信息、购物车等）
HSET user:1001 name "张三" age 25 email "zhangsan@example.com"
HGET user:1001 name                      # 获取单个字段
HGETALL user:1001                        # 获取所有字段
HMSET user:1002 name "李四" age 30       # 批量设置
HMGET user:1002 name age                 # 批量获取
HINCRBY user:1001 age 1                  # 字段 +1
HEXISTS user:1001 email                  # 字段是否存在
HDEL user:1001 email                     # 删除字段
HLEN user:1001                           # 字段数量

# ========== 5. ZSet（有序集合，基于跳表） ==========
# 应用：排行榜、延时队列、优先级队列
ZADD leaderboard 100 "玩家A" 85 "玩家B" 200 "玩家C"  # 添加（分数+成员）
ZINCRBY leaderboard 10 "玩家A"                       # 加分
ZREVRANGE leaderboard 0 9 WITHSCORES                 # 排行榜 Top10（从高到低）
ZRANGE leaderboard 0 9 WITHSCORES                    # 从低到高
ZSCORE leaderboard "玩家A"                           # 获取分数
ZRANK leaderboard "玩家A"                            # 排名（从低到高）
ZREVRANK leaderboard "玩家A"                         # 排名（从高到低）
ZREM leaderboard "玩家C"                             # 删除成员
ZREVRANGEBYSCORE leaderboard 200 100                 # 按分数范围查询

# ========== 6. Bitmap（位图，本质是 String） ==========
# 应用：签到统计、用户在线状态
SETBIT user:sign:202501 0 1               # 用户第 1 天签到
SETBIT user:sign:202501 1 0               # 第 2 天未签到
GETBIT user:sign:202501 0                 # 查询第 1 天是否签到
BITCOUNT user:sign:202501                  # 本月签到天数
BITOP AND result user:sign:202501 user:sign:202502

# ========== 7. HyperLogLog（基数统计） ==========
# 应用：UV 统计（独立访客），有 0.81% 误差
PFADD uv:page:1 "user1" "user2" "user3"
PFCOUNT uv:page:1                          # 基数 3
PFMERGE uv:total uv:page:1 uv:page:2

# ========== 8. GEO（地理空间） ==========
# 应用：附近的人、地点搜索
GEOADD cities 116.39 39.91 "北京" 121.47 31.23 "上海"
GEODIST cities "北京" "上海" km            # 距离 1067.5km
GEORADIUS cities 116.39 39.91 100 km       # 北京方圆 100km 内的城市
GEOPOS cities "北京"                       # 获取经纬度
```

#### Java 操作 Redis（Spring Data Redis）

```java
// ========== 配置 ==========
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 使用 Jackson 序列化
        Jackson2JsonRedisSerializer<Object> serializer =
            new Jackson2JsonRedisSerializer<>(Object.class);
        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}

// ========== 使用 StringRedisTemplate（推荐） ==========
@Service
public class CacheService {
    @Autowired
    private StringRedisTemplate redis;

    // String
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redis.opsForValue().set(key, value, timeout, unit);
    }
    public String get(String key) {
        return redis.opsForValue().get(key);
    }
    public Long increment(String key) {
        return redis.opsForValue().increment(key);
    }

    // Hash
    public void hset(String key, String field, String value) {
        redis.opsForHash().put(key, field, value);
    }
    public Map<Object, Object> hgetAll(String key) {
        return redis.opsForHash().entries(key);
    }

    // List（消息队列）
    public Long push(String key, String value) {
        return redis.opsForList().rightPush(key, value);
    }
    public String pop(String key) {
        return redis.opsForList().leftPop(key);
    }
    public String bPop(String key, long timeout) {
        return redis.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    // Set
    public void sadd(String key, String... members) {
        redis.opsForSet().add(key, members);
    }
    public Set<String> smembers(String key) {
        return redis.opsForSet().members(key);
    }

    // ZSet（排行榜）
    public void zadd(String key, String member, double score) {
        redis.opsForZSet().add(key, member, score);
    }
    public Set<String> topN(String key, int n) {
        return redis.opsForZSet().reverseRange(key, 0, n - 1);
    }

    // 分布式锁（最简单版，正式用 Redisson）
    public boolean tryLock(String key, String value, long expireMs) {
        return Boolean.TRUE.equals(
            redis.opsForValue().setIfAbsent(key, value, expireMs, TimeUnit.MILLISECONDS));
    }

    public void unlock(String key, String value) {
        String current = redis.opsForValue().get(key);
        if (value.equals(current)) {
            redis.delete(key); // 用 Lua 脚本保证原子性更安全
        }
    }
}

// ========== 缓存注解 ==========
@Service
public class UserService {
    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public User getById(Long id) { /* 查询数据库 */ }

    @CachePut(value = "user", key = "#user.id") // 更新缓存
    public User update(User user) { /* 更新数据库 */ }

    @CacheEvict(value = "user", key = "#id")    // 删除缓存
    public void delete(Long id) { /* 删除数据 */ }

    @CacheEvict(value = "user", allEntries = true) // 清空全部
    public void clearAll() {}
}
```

#### Redis 典型应用场景

| 场景 | 数据结构 | 示例 |
|------|----------|------|
| 缓存 | String / Hash | `SET user:1001 '{json}'` |
| 分布式锁 | String | `SETNX lock:order:1001 1` |
| 计数器 | String | `INCR article:read:1001` |
| 排行榜 | ZSet | `ZADD leaderboard score member` |
| 消息队列 | List | `LPUSH queue msg` / `BRPOP queue` |
| 延迟队列 | ZSet | `ZADD delay_queue timestamp task` |
| 签到统计 | Bitmap | `SETBIT user:sign:202501 day 1` |
| UV 统计 | HyperLogLog | `PFADD uv page user` |
| 附近的人 | GEO | `GEORADIUS city lng lat radius` |
| 购物车 | Hash | `HSET cart:user1 sku_id quantity` |
| 布隆过滤器 | Bitmap（模块） | 防止缓存穿透 |
| Session 共享 | String | `SET session:token user_json EX 1800` |

#### Redis 的过期策略和淘汰策略

**过期策略（删除已过期的 key）：**
- **惰性删除**：访问时检查是否过期，过期则删除
- **定期删除**：每 100ms 随机抽取一批 key 检查并删除

**内存淘汰策略（`maxmemory-policy`）：**
- `noeviction`：不淘汰，写操作直接报错
- `allkeys-lru`：淘汰最近最少使用的 key（最常用）
- `allkeys-lfu`：淘汰最不经常使用的 key
- `volatile-lru`：在设置了过期时间的 key 中淘汰最近最少使用
- `volatile-ttl`：淘汰即将过期的 key
- `allkeys-random`：随机淘汰

```bash
# 配置文件设置
maxmemory 4gb
maxmemory-policy allkeys-lru
```

#### Redis 持久化

| 方式 | RDB（快照） | AOF（追加文件） |
|------|-------------|-----------------|
| 原理 | 定时全量快照 | 追加写操作命令 |
| 性能 | 高（子进程写） | 低（每次写都追加） |
| 数据丢失 | 可能丢最后一次快照后的数据 | 最多丢 1 秒数据 |
| 文件大小 | 小 | 大（可重写） |
| 恢复速度 | 快 | 慢 |
| 生产建议 | RDB + AOF 同时开启 |

```bash
# RDB 配置
save 900 1      # 900 秒内有 1 个 key 变更则触发
save 300 10     # 300 秒内有 10 个 key 变更则触发
save 60 10000   # 60 秒内有 10000 个 key 变更则触发

# AOF 配置
appendonly yes
appendfsync everysec  # 每秒 fsync，性能和数据安全的平衡
```

#### Redis 集群模式

| 模式 | 说明 | 节点数 |
|------|------|--------|
| 单机 | 开发测试用 | 1 |
| 主从 | 一主多从，主写从读 | ≥2 |
| 哨兵 | 主从 + 自动故障转移 | ≥3（奇数） |
| Cluster | 分片集群，自动分槽 | ≥6（3主3从） |

```bash
# Cluster 集群搭建（3主3从）
redis-cli --cluster create \
  192.168.1.1:7001 192.168.1.2:7002 192.168.1.3:7003 \
  192.168.1.4:7004 192.168.1.5:7005 192.168.1.6:7006 \
  --cluster-replicas 1

#### Redis 6/7 新特性

- Redis 6.0：ACL（细粒度权限控制）、RESP3、SSL/TLS、IO 多线程
- Redis 7.0：Redis Functions、AOF 优化、故障转移改进

#### Redisson 框架

```java
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setConnectionPoolSize(10);
        return Redisson.create(config);
    }
}

@Service
public class DistributedService {
    @Autowired
    private RedissonClient redisson;

    // RLock 分布式锁（支持 Watch Dog 自动续期、可重入）
    public void processOrder(Long orderId) {
        RLock lock = redisson.getLock("lock:order:" + orderId);
        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                // 业务逻辑
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) { lock.unlock(); }
        }
    }
}
```

#### Lua 脚本

```bash
# 原子释放锁（判断 value 匹配才删除）
EVAL "if redis.call('GET', KEYS[1]) == ARGV[1] then return redis.call('DEL', KEYS[1]) else return 0 end" 1 lock:order:1001 lock_uuid_abc123
```

---

### 3.3 Elasticsearch

Elasticsearch 是一个基于 Lucene 的分布式全文搜索引擎，常用于日志分析、商品搜索等。

#### 核心概念

| ES 概念 | 类比 MySQL |
|---------|------------|
| Index（索引） | Database（数据库） |
| Type（类型，7.x+ 废弃） | Table（表） |
| Document（文档） | Row（行） |
| Field（字段） | Column（列） |
| Mapping（映射） | Schema（表结构） |
| Shard（分片） | 水平分表 |
| Replica（副本） | 主从备份 |

#### 基础操作

```json
// ========== 创建索引 ==========
PUT /products
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "ik_max_word"      // 中文分词器
      },
      "category": {
        "type": "keyword"              // 精确匹配
      },
      "price": {
        "type": "double"
      },
      "description": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "tags": {
        "type": "keyword"
      },
      "createTime": {
        "type": "date",
        "format": "yyyy-MM-dd HH:mm:ss"
      }
    }
  }
}

// ========== 插入文档 ==========
POST /products/_doc/1
{
  "name": "华为 Mate 60 Pro",
  "category": "手机",
  "price": 6999.00,
  "description": "华为旗舰手机，搭载麒麟芯片，支持卫星通信",
  "tags": ["华为", "5G", "旗舰"],
  "createTime": "2025-06-01 10:00:00"
}

// ========== 搜索 ==========
GET /products/_search
{
  "query": {
    "multi_match": {
      "query": "华为旗舰",
      "fields": ["name", "description"]
    }
  },
  "filter": [
    { "term": { "category": "手机" } },
    { "range": { "price": { "gte": 5000, "lte": 10000 } } }
  ],
  "sort": [
    { "price": { "order": "asc" } }
  ],
  "from": 0,
  "size": 10,
  "highlight": {
    "fields": {
      "name": {},
      "description": {}
    }
  }
}

// ========== 聚合分析 ==========
GET /products/_search
{
  "size": 0,
  "aggs": {
    "by_category": {
      "terms": { "field": "category" },
      "aggs": {
        "avg_price": {
          "avg": { "field": "price" }
        }
      }
    }
  }
}
```

#### Java 操作 ES（Spring Data Elasticsearch）

```java
// ========== 实体映射 ==========
@Document(indexName = "products")
@Data
public class ProductDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String description;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

// ========== Repository ==========
public interface ProductRepository extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findByName(String name);
    List<ProductDocument> findByCategory(String category);
}

// ========== 复杂搜索 ==========
@Service
public class ProductSearchService {
    @Autowired
    private ElasticsearchRestTemplate template;

    public SearchHits<ProductDocument> search(String keyword, String category,
                                               Double minPrice, Double maxPrice, int page, int size) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 全文搜索
        if (StringUtils.hasText(keyword)) {
            boolQuery.must(QueryBuilders.multiMatchQuery(keyword, "name", "description"));
        }

        // 过滤
        if (StringUtils.hasText(category)) {
            boolQuery.filter(QueryBuilders.termQuery("category", category));
        }
        if (minPrice != null || maxPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price")
                .gte(minPrice).lte(maxPrice));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            .withPageable(PageRequest.of(page, size))
            .withSort(Sort.by(Sort.Direction.DESC, "createTime"))
            .build();

        return template.search(searchQuery, ProductDocument.class);
    }
}
```

#### ES 核心知识点

- **分词器**：`standard`（英文）、`ik_max_word`（最细粒度中文分词）、`ik_smart`（粗粒度中文分词）
- **倒排索引**：ES 的核心原理，从词到文档的映射，快速找到包含某个词的文档
- **分片与路由**：文档通过 `_routing` 路由到指定分片，默认 `_id` 的哈希值
- **分段存储**：Lucene 的段（Segment）是不可变的，合并时删除旧段
- **近实时**：写入后 1 秒才可搜索（refresh_interval 控制）

#### 集群架构（节点角色）

| 节点角色 | 配置 | 职责 |
|----------|------|------|
| **Master** | `node.roles: [master]` | 管理集群元数据、维护集群状态 |
| **Data** | `node.roles: [data]` | 存储分片、执行 CRUD 和搜索聚合 |
| **Ingest** | `node.roles: [ingest]` | 写入前的管道预处理（解析、转换） |
| **Coordinating** | `node.roles: []` | 接收请求、分发到数据节点、聚合结果 |


---

### 3.4 Oracle & PL/SQL

Oracle 数据库是业界领先的关系型数据库管理系统，广泛应用于金融、电信、政务等大型企业级系统。

#### Oracle 与 MySQL 的核心差异

| 维度 | Oracle | MySQL |
|------|--------|-------|
| 事务隔离级别默认值 | `READ_COMMITTED` | `REPEATABLE_READ` |
| 分页 | `ROWNUM` / `OFFSET FETCH` | `LIMIT OFFSET` |
| 自增主键 | `SEQUENCE` | `AUTO_INCREMENT` |
| 字符串拼接 | `\|\|` | `CONCAT()` |
| 别名 | 不支持 `AS`（直接空格） | 支持 `AS` |
| 伪列 | `ROWNUM`、`ROWID` | 无 |
| 表空间 | 逻辑存储单元（表空间→段→区→块） | 无此概念 |
| 连接语法 | 传统 `(+)` 或 ANSI JOIN | ANSI JOIN |
| 索引组织表 | IOT（索引组织表） | InnoDB 聚簇索引 |
| 物化视图 | ✅ 原生支持 | ❌ 不支持 |
| 闪回查询 | ✅ `FLASHBACK QUERY` | ❌ |
| 双机热备 | Data Guard / RAC | 主从复制 |

#### JDBC 连接

```bash
# Maven 依赖
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>23.3.0.23.09</version>
    <scope>runtime</scope>
</dependency>

# 如果需要 UCP 连接池
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ucp</artifactId>
    <version>23.3.0.23.09</version>
</dependency>
```

```yaml
# application.yml - Oracle 数据源配置
spring:
  datasource:
    url: jdbc:oracle:thin:@//192.168.1.100:1521/orclpdb1
    # SID 方式：jdbc:oracle:thin:@192.168.1.100:1521:orcl
    username: scott
    password: tiger
    driver-class-name: oracle.jdbc.OracleDriver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000

# MyBatis-Plus Oracle 配置
mybatis-plus:
  global-config:
    db-config:
      db-type: oracle
      id-type: input  # Oracle 主键通常用序列，需手动设置
```

#### Oracle SQL 常用语法

```sql
-- ========== 分页查询 ==========
-- 方式一：ROWNUM（Oracle 11g 及以前）
SELECT * FROM (
    SELECT t.*, ROWNUM AS rn
    FROM (
        SELECT * FROM emp ORDER BY sal DESC
    ) t
    WHERE ROWNUM <= 20
) WHERE rn > 10;

-- 方式二：OFFSET FETCH（Oracle 12c+）
SELECT * FROM emp
ORDER BY sal DESC
OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY;

-- ========== 字符串处理 ==========
-- 拼接：使用 || 而非 CONCAT()
SELECT '员工: ' || ename || ', 薪资: ' || sal AS info FROM emp;

-- 字符串函数
SELECT LENGTH(ename), SUBSTR(ename, 1, 3), INSTR(ename, 'A')
FROM emp;

-- ========== 日期处理 ==========
SELECT SYSDATE FROM dual;                         -- 当前日期时间
SELECT SYSTIMESTAMP FROM dual;                    -- 当前时间戳（含时区）
SELECT TO_DATE('2025-06-03', 'YYYY-MM-DD') FROM dual;
SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM dual;
SELECT LAST_DAY(SYSDATE) FROM dual;               -- 当月最后一天
SELECT ADD_MONTHS(SYSDATE, -3) FROM dual;         -- 三个月前

-- ========== 空值处理 ==========
-- NVL：若为 NULL 则返回默认值
SELECT NVL(comm, 0) FROM emp;

-- NVL2：expr1 不为 NULL 返回 expr2，否则返回 expr3
SELECT NVL2(comm, '有提成', '无提成') FROM emp;

-- COALESCE：返回第一个非 NULL 值
SELECT COALESCE(comm, bonus, 0) FROM emp;

-- NULLIF：两值相等返回 NULL，否则返回第一个值
SELECT NULLIF(deptno, 10) FROM emp;

-- ========== 条件表达式 ==========
-- DECODE（Oracle 专有）
SELECT ename, deptno,
       DECODE(deptno,
              10, '财务部',
              20, '研发部',
              30, '销售部',
              '其他') AS dept_name
FROM emp;

-- CASE WHEN（标准 SQL，推荐）
SELECT ename, sal,
       CASE WHEN sal > 5000 THEN '高'
            WHEN sal > 3000 THEN '中'
            ELSE '低'
       END AS level
FROM emp;

-- ========== 层次查询（树形结构） ==========
-- 查询部门树
SELECT LEVEL, deptno, dname, mgr
FROM dept
START WITH mgr IS NULL
CONNECT BY PRIOR deptno = mgr;

-- ========== MERGE（合并/ UPSERT） ==========
MERGE INTO emp e
USING (SELECT 7369 AS empno, 'SMITH' AS ename FROM dual) s
ON (e.empno = s.empno)
WHEN MATCHED THEN UPDATE SET e.ename = s.ename
WHEN NOT MATCHED THEN INSERT (empno, ename) VALUES (s.empno, s.ename);

-- ========== 伪列 ==========
SELECT ROWID, ROWNUM, empno FROM emp;
-- ROWID：行的物理地址，可用于快速定位
-- ROWNUM：结果集的行号（排序前赋予）

-- ========== 物化视图 ==========
CREATE MATERIALIZED VIEW mv_dept_sal
REFRESH COMPLETE ON DEMAND
AS
SELECT d.dname, AVG(e.sal) AS avg_sal, SUM(e.sal) AS total_sal
FROM dept d JOIN emp e ON e.deptno = d.deptno
GROUP BY d.dname;

-- 手动刷新
EXEC DBMS_MVIEW.REFRESH('mv_dept_sal');

-- ========== 闪回查询 ==========
-- 查询过去某个时间点的数据
SELECT * FROM emp AS OF TIMESTAMP
    TO_TIMESTAMP('2025-06-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS');

-- 闪回表
FLASHBACK TABLE emp TO TIMESTAMP
    TO_TIMESTAMP('2025-06-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS');
```

#### 序列 (Sequence)

Oracle 没有 `AUTO_INCREMENT`，使用序列生成自增主键。

```sql
-- ========== 创建序列 ==========
CREATE SEQUENCE seq_emp_id
    START WITH 10000
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 999999999
    CACHE 20          -- 缓存 20 个值，提高性能
    NOCYCLE;          -- 不循环

-- ========== 使用序列 ==========
SELECT seq_emp_id.NEXTVAL FROM dual;  -- 获取下一个值
SELECT seq_emp_id.CURRVAL FROM dual;  -- 获取当前值（需先调用 NEXTVAL）

-- 插入时使用
INSERT INTO emp (empno, ename) VALUES (seq_emp_id.NEXTVAL, '张三');

-- ========== MyBatis-Plus 序列配置 ==========
-- 实体类
@TableName(value = "emp", autoResultMap = true)
public class Emp {
    @TableId(type = IdType.INPUT)
    private Long empno;
    // ...
}

-- Mapper XML 中使用序列
<insert id="insert" useGeneratedKeys="false">
    <selectKey keyProperty="empno" resultType="Long" order="BEFORE">
        SELECT seq_emp_id.NEXTVAL FROM dual
    </selectKey>
    INSERT INTO emp(empno, ename) VALUES(#{empno}, #{ename})
</insert>
```

---

#### 用户管理与权限体系

Oracle 的权限体系分为系统权限和对象权限两级。

```sql
-- ========== 用户管理 ==========
-- 创建用户
CREATE USER app_user IDENTIFIED BY "App123!@#"
    DEFAULT TABLESPACE users
    TEMPORARY TABLESPACE temp
    QUOTA 500M ON users
    PROFILE default
    ACCOUNT UNLOCK;

-- 修改密码
ALTER USER app_user IDENTIFIED BY "NewPass456!@#";

-- 锁定/解锁
ALTER USER app_user ACCOUNT LOCK;
ALTER USER app_user ACCOUNT UNLOCK;

-- 删除用户（CASCADE 同时删除用户的对象）
DROP USER app_user CASCADE;

-- ========== 权限授予 ==========
-- 系统权限
GRANT CREATE SESSION TO app_user;               -- 连接数据库
GRANT CREATE TABLE TO app_user;                  -- 创建表
GRANT CREATE VIEW TO app_user;                   -- 创建视图
GRANT CREATE PROCEDURE TO app_user;              -- 创建存储过程
GRANT CREATE SEQUENCE TO app_user;               -- 创建序列
GRANT CREATE TRIGGER TO app_user;                -- 创建触发器
GRANT CREATE SYNONYM TO app_user;                -- 创建同义词
GRANT CREATE JOB TO app_user;                    -- 创建调度任务
GRANT UNLIMITED TABLESPACE TO app_user;          -- 无限表空间配额

-- 对象权限
GRANT SELECT, INSERT, UPDATE, DELETE ON scott.emp TO app_user;
GRANT SELECT ON scott.emp TO app_user WITH GRANT OPTION;  -- 允许转授

-- 角色管理（推荐使用角色简化权限管理）
CREATE ROLE rl_app_developer;
GRANT CREATE TABLE, CREATE VIEW, CREATE PROCEDURE TO rl_app_developer;
GRANT rl_app_developer TO app_user;

-- 预定义角色
-- CONNECT: 连接数据库（仅CREATE SESSION）
-- RESOURCE: 创建表/序列/过程等
-- DBA: 所有系统权限（仅限管理员）

-- ========== 权限查看 ==========
SELECT * FROM USER_SYS_PRIVS;           -- 当前用户系统权限
SELECT * FROM USER_TAB_PRIVS;           -- 当前用户对象权限
SELECT * FROM USER_ROLE_PRIVS;          -- 当前用户角色
SELECT * FROM DBA_SYS_PRIVS WHERE GRANTEE = 'APP_USER';
SELECT * FROM DBA_TAB_PRIVS WHERE OWNER = 'SCOTT';

```

---

#### 表空间管理

```sql
-- ========== 创建表空间 ==========
-- 小文件表空间（传统）
CREATE SMALLFILE TABLESPACE app_data
    DATAFILE '/u01/oradata/orcl/app_data01.dbf' SIZE 1G
    AUTOEXTEND ON NEXT 100M MAXSIZE 32G
    EXTENT MANAGEMENT LOCAL AUTOALLOCATE
    SEGMENT SPACE MANAGEMENT AUTO;

-- 大文件表空间（单个文件可达 32TB）
CREATE BIGFILE TABLESPACE app_large
    DATAFILE '/u01/oradata/orcl/app_large01.dbf' SIZE 10G
    AUTOEXTEND ON NEXT 1G MAXSIZE 32G;

-- 临时表空间（排序、临时数据）
CREATE TEMPORARY TABLESPACE temp2
    TEMPFILE '/u01/oradata/orcl/temp02.dbf' SIZE 1G
    AUTOEXTEND ON NEXT 100M MAXSIZE 8G;

-- UNDO 表空间
CREATE UNDO TABLESPACE undotbs2
    DATAFILE '/u01/oradata/orcl/undotbs02.dbf' SIZE 2G;

-- ========== 表空间管理 ==========
-- 添加数据文件
ALTER TABLESPACE app_data ADD DATAFILE
    '/u01/oradata/orcl/app_data02.dbf' SIZE 1G AUTOEXTEND ON;

-- 修改数据文件大小
ALTER DATABASE DATAFILE '/u01/oradata/orcl/app_data01.dbf' RESIZE 2G;

-- 设置表空间只读/读写
ALTER TABLESPACE app_data READ ONLY;
ALTER TABLESPACE app_data READ WRITE;

-- 查看表空间使用率
SELECT
    df.tablespace_name,
    ROUND(df.bytes / 1024 / 1024 / 1024, 2) AS total_gb,
    ROUND((df.bytes - fs.bytes) / 1024 / 1024 / 1024, 2) AS used_gb,
    ROUND(fs.bytes / 1024 / 1024 / 1024, 2) AS free_gb,
    ROUND((1 - fs.bytes / df.bytes) * 100, 2) AS pct_used
FROM (SELECT tablespace_name, SUM(bytes) bytes FROM dba_data_files GROUP BY tablespace_name) df
JOIN (SELECT tablespace_name, SUM(bytes) bytes FROM dba_free_space GROUP BY tablespace_name) fs
    ON df.tablespace_name = fs.tablespace_name
ORDER BY pct_used DESC;
```

---

#### UNDO 与 REDO

```sql
-- UNDO：事务回滚、一致性读
-- 查看 UNDO 配置
SHOW PARAMETER undo;
-- undo_management: AUTO（自动管理，推荐）/ MANUAL
-- undo_tablespace: UNDOTBS1
-- undo_retention: 900（秒，UNDO 保留时间，12c+ 自动调整）

-- REDO：事务恢复、实例恢复
-- 查看 REDO 信息
SELECT GROUP#, BYTES, STATUS, MEMBERS FROM v$log;
SELECT GROUP#, MEMBER, STATUS FROM v$logfile;

-- 添加 Redo Log 组
ALTER DATABASE ADD LOGFILE GROUP 4
    '/u01/oradata/orcl/redo04.log' SIZE 500M;

-- 强制日志切换
ALTER SYSTEM SWITCH LOGFILE;

-- 归档模式
ARCHIVE LOG LIST;
-- 启用归档模式（需重启到 MOUNT 状态）
-- SHUTDOWN IMMEDIATE;
-- STARTUP MOUNT;
-- ALTER DATABASE ARCHIVELOG;
-- ALTER DATABASE OPEN;
```

---

#### Oracle 网络配置

```bash
# ========== tnsnames.ora（客户端连接配置） ==========
ORCL =
  (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.1.100)(PORT = 1521))
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = orclpdb1)   # PDB 服务名
    )
  )

ORCL_SID =
  (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.1.100)(PORT = 1521))
    (CONNECT_DATA =
      (SID = orcl)                 # 非 CDB 或 SID 方式
    )
  )

# ========== listener.ora（监听器配置，在服务器端） ==========
LISTENER =
  (DESCRIPTION_LIST =
    (DESCRIPTION =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 0.0.0.0)(PORT = 1521))
    )
  )

# 监听器管理命令
# lsnrctl start       # 启动监听
# lsnrctl stop        # 停止监听
# lsnrctl status      # 查看监听状态
# lsnrctl reload      # 重载配置
```



### 3.5 PL/SQL 编程

#### 块结构（匿名块）

```sql
DECLARE
    v_name   VARCHAR2(50);
    v_sal    NUMBER(10,2);
BEGIN
    SELECT ename, sal INTO v_name, v_sal FROM emp WHERE empno = 7369;
    DBMS_OUTPUT.PUT_LINE('员工: ' || v_name || ', 薪资: ' || v_sal);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('未找到员工');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('错误: ' || SQLERRM);
END;
/
```

#### 变量声明

```sql
-- %TYPE：引用表列的类型
v_ename emp.ename%TYPE;

-- %ROWTYPE：引用整行记录类型
v_emp emp%ROWTYPE;

-- RECORD 类型（自定义记录）
DECLARE
    TYPE emp_record IS RECORD (
        ename emp.ename%TYPE,
        sal   emp.sal%TYPE,
        hiredate emp.hiredate%TYPE
    );
    v_emp_info emp_record;
BEGIN
    SELECT ename, sal, hiredate INTO v_emp_info FROM emp WHERE empno = 7369;
    DBMS_OUTPUT.PUT_LINE(v_emp_info.ename || ' - ' || v_emp_info.sal);
END;
/

-- 关联数组（INDEX BY TABLE）
DECLARE
    TYPE sal_table IS TABLE OF emp.sal%TYPE INDEX BY PLS_INTEGER;
    v_salaries sal_table;
BEGIN
    v_salaries(1) := 5000;
    v_salaries(2) := 6000;
    DBMS_OUTPUT.PUT_LINE(v_salaries(1));
END;
/
```

#### 控制流

```sql
-- IF-THEN-ELSIF-ELSE
IF v_sal > 5000 THEN
    v_level := '高';
ELSIF v_sal > 3000 THEN
    v_level := '中';
ELSE
    v_level := '低';
END IF;

-- CASE 表达式
v_level := CASE
    WHEN v_sal > 5000 THEN '高'
    WHEN v_sal > 3000 THEN '中'
    ELSE '低'
END;

-- 基本 LOOP
LOOP
    v_counter := v_counter + 1;
    EXIT WHEN v_counter > 10;
END LOOP;

-- WHILE LOOP
WHILE v_counter <= 10 LOOP
    v_counter := v_counter + 1;
END LOOP;

-- FOR LOOP
FOR i IN 1..10 LOOP
    DBMS_OUTPUT.PUT_LINE(i);
END LOOP;

-- 反向 FOR
FOR i IN REVERSE 1..10 LOOP
    DBMS_OUTPUT.PUT_LINE(i);
END LOOP;
```

#### 游标

```sql
-- 显式游标
DECLARE
    CURSOR c_emp IS SELECT ename, sal FROM emp WHERE deptno = 10;
    v_ename emp.ename%TYPE;
    v_sal   emp.sal%TYPE;
BEGIN
    OPEN c_emp;
    LOOP
        FETCH c_emp INTO v_ename, v_sal;
        EXIT WHEN c_emp%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_ename || ' - ' || v_sal);
    END LOOP;
    CLOSE c_emp;
END;
/

-- Cursor FOR Loop（自动打开、提取、关闭）
DECLARE
    CURSOR c_emp IS SELECT ename, sal FROM emp WHERE deptno = 10;
BEGIN
    FOR rec IN c_emp LOOP
        DBMS_OUTPUT.PUT_LINE(rec.ename || ' - ' || rec.sal);
    END LOOP;
END;
/

-- 游标带参数
DECLARE
    CURSOR c_emp(p_deptno NUMBER) IS
        SELECT ename, sal FROM emp WHERE deptno = p_deptno;
BEGIN
    FOR rec IN c_emp(20) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.ename || ' - ' || rec.sal);
    END LOOP;
END;
/

-- 游标变量（REF CURSOR）
DECLARE
    TYPE ref_cur IS REF CURSOR;
    c_ref ref_cur;
    v_ename emp.ename%TYPE;
    v_sal   emp.sal%TYPE;
BEGIN
    OPEN c_ref FOR 'SELECT ename, sal FROM emp WHERE deptno = :1' USING 10;
    LOOP
        FETCH c_ref INTO v_ename, v_sal;
        EXIT WHEN c_ref%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_ename || ' - ' || v_sal);
    END LOOP;
    CLOSE c_ref;
END;
/

-- SYS_REFCURSOR（弱类型 REF CURSOR，常用于返回结果集）
CREATE OR REPLACE FUNCTION get_emp_by_dept(p_deptno NUMBER) RETURN SYS_REFCURSOR IS
    c SYS_REFCURSOR;
BEGIN
    OPEN c FOR SELECT ename, sal FROM emp WHERE deptno = p_deptno;
    RETURN c;
END;
/
```

#### 异常处理

```sql
-- 预定义异常
BEGIN
    SELECT ename INTO v_ename FROM emp WHERE empno = 9999; -- 无数据
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('未找到记录');
    WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE('返回多行');
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('违反唯一约束');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('未知错误: ' || SQLERRM);
END;
/

-- 用户自定义异常
DECLARE
    e_salary_too_high EXCEPTION;
    v_sal emp.sal%TYPE := 50000;
BEGIN
    IF v_sal > 30000 THEN
        RAISE e_salary_too_high;
    END IF;
EXCEPTION
    WHEN e_salary_too_high THEN
        DBMS_OUTPUT.PUT_LINE('薪资过高，请检查');
END;
/

-- RAISE_APPLICATION_ERROR（返回自定义错误码给调用者）
BEGIN
    IF v_sal > 30000 THEN
        RAISE_APPLICATION_ERROR(-20001, '薪资不能超过 30000');
    END IF;
END;
/

-- PRAGMA EXCEPTION_INIT（关联 Oracle 错误号）
DECLARE
    e_invalid_number EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_invalid_number, -1722);
BEGIN
    -- 某些操作
EXCEPTION
    WHEN e_invalid_number THEN
        DBMS_OUTPUT.PUT_LINE('数字转换错误');
END;
/
```

#### 存储过程

```sql
CREATE OR REPLACE PROCEDURE raise_salary(
    p_empno   IN  emp.empno%TYPE,
    p_percent IN  NUMBER DEFAULT 10,
    p_new_sal OUT emp.sal%TYPE
) IS
    v_current_sal emp.sal%TYPE;
BEGIN
    SELECT sal INTO v_current_sal FROM emp WHERE empno = p_empno;
    UPDATE emp SET sal = sal * (1 + p_percent / 100) WHERE empno = p_empno;
    p_new_sal := v_current_sal * (1 + p_percent / 100);
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, '员工不存在: ' || p_empno);
    WHEN OTHERS THEN
        ROLLBACK; RAISE;
END raise_salary;
/
```

#### 函数

```sql
CREATE OR REPLACE FUNCTION get_annual_sal(p_empno IN emp.empno%TYPE) RETURN NUMBER IS
    v_annual_sal emp.sal%TYPE;
BEGIN
    SELECT sal * 12 + NVL(comm, 0) INTO v_annual_sal FROM emp WHERE empno = p_empno;
    RETURN v_annual_sal;
END get_annual_sal;
/
-- 调用：SELECT ename, get_annual_sal(empno) AS annual_sal FROM emp;
```

#### 触发器

```sql
CREATE OR REPLACE TRIGGER trg_emp_audit
    AFTER INSERT OR UPDATE OR DELETE ON emp FOR EACH ROW
BEGIN
    INSERT INTO emp_audit_log(empno, action, old_sal, new_sal, change_by, change_time)
    VALUES (COALESCE(:NEW.empno, :OLD.empno), 
            CASE WHEN INSERTING THEN 'INSERT' WHEN UPDATING THEN 'UPDATE' ELSE 'DELETE' END,
            :OLD.sal, :NEW.sal, USER, SYSDATE);
END;
/
```

#### 包（Package）

```sql
-- 包规范（接口）
CREATE OR REPLACE PACKAGE pkg_employee IS
    FUNCTION get_salary(p_empno NUMBER) RETURN NUMBER;
    PROCEDURE update_salary(p_empno NUMBER, p_new_sal NUMBER);
    PROCEDURE increase_all(p_percent NUMBER);
END pkg_employee;
/

-- 包体（实现）
CREATE OR REPLACE PACKAGE BODY pkg_employee IS
    
    -- 私有变量（包状态）
    g_counter NUMBER := 0;
    
    FUNCTION get_salary(p_empno NUMBER) RETURN NUMBER IS
        v_sal emp.sal%TYPE;
    BEGIN
        SELECT sal INTO v_sal FROM emp WHERE empno = p_empno;
        RETURN v_sal;
    END get_salary;
    
    PROCEDURE update_salary(p_empno NUMBER, p_new_sal NUMBER) IS
    BEGIN
        UPDATE emp SET sal = p_new_sal WHERE empno = p_empno;
        g_counter := g_counter + 1;
        COMMIT;
    END update_salary;
    
    PROCEDURE increase_all(p_percent NUMBER) IS
    BEGIN
        UPDATE emp SET sal = sal * (1 + p_percent / 100);
        COMMIT;
    END increase_all;
    
END pkg_employee;
/

-- 调用
-- pkg_employee.update_salary(7369, 5000);
-- SELECT pkg_employee.get_salary(7369) FROM dual;
```

#### 动态 SQL

```sql
-- EXECUTE IMMEDIATE
DECLARE
    v_sql   VARCHAR2(200);
    v_ename emp.ename%TYPE;
BEGIN
    v_sql := 'SELECT ename FROM emp WHERE empno = :1';
    EXECUTE IMMEDIATE v_sql INTO v_ename USING 7369;
    DBMS_OUTPUT.PUT_LINE(v_ename);
END;
/

-- 批量操作
DECLARE
    v_sql VARCHAR2(200);
BEGIN
    v_sql := 'UPDATE emp SET sal = sal * 1.1 WHERE deptno = :1';
    EXECUTE IMMEDIATE v_sql USING 20;
    COMMIT;
END;
/

-- OPEN FOR（动态游标）
DECLARE
    c SYS_REFCURSOR;
    v_ename emp.ename%TYPE;
    v_sal   emp.sal%TYPE;
BEGIN
    OPEN c FOR 'SELECT ename, sal FROM emp WHERE deptno = :1 ORDER BY sal DESC' USING 10;
    LOOP
        FETCH c INTO v_ename, v_sal;
        EXIT WHEN c%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_ename || ' - ' || v_sal);
    END LOOP;
    CLOSE c;
END;
/
```

#### 常用内置包

| 包名 | 用途 | 示例 |
|------|------|------|
| `DBMS_OUTPUT` | 输出调试信息 | `DBMS_OUTPUT.PUT_LINE('msg')` |
| `DBMS_MVIEW` | 物化视图管理 | `DBMS_MVIEW.REFRESH('mv_name')` |
| `DBMS_SCHEDULER` | 调度任务 | `DBMS_SCHEDULER.CREATE_JOB(...)` |
| `UTL_FILE` | 文件读写 | `UTL_FILE.FOPEN('DIR', 'file.txt', 'w')` |
| `DBMS_METADATA` | 获取 DDL | `DBMS_METADATA.GET_DDL('TABLE', 'EMP')` |
| `DBMS_STATS` | 收集统计信息 | `DBMS_STATS.GATHER_TABLE_STATS('SCOTT', 'EMP')` |
| `DBMS_LOB` | 大对象操作 | `DBMS_LOB.READ(lob, amount, offset, buffer)` |
| `UTL_MAIL` | 发送邮件 | `UTL_MAIL.SEND(...)` |
| `DBMS_LOCK` | 用户自定义锁 | `DBMS_LOCK.REQUEST(lock_id, timeout)` |

#### Spring Boot 集成 Oracle

```yaml
# application-oracle.yml
spring:
  datasource:
    url: jdbc:oracle:thin:@//192.168.1.100:1521/orclpdb1
    username: app_user
    password: app_pass
    driver-class-name: oracle.jdbc.OracleDriver
    hikari:
      connection-test-query: SELECT 1 FROM dual
      maximum-pool-size: 20
  jpa:
    database: oracle
    hibernate:
      ddl-auto: validate            # 生产环境用 validate 或 none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.OracleDialect
        default_batch_fetch_size: 100
```

```java
// JPA 实体使用序列
@Entity
@Table(name = "emp")
@SequenceGenerator(name = "seq_emp", sequenceName = "seq_emp_id", allocationSize = 1)
public class Emp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_emp")
    private Long empno;

    @Column(name = "ename")
    private String ename;

    // ...
}

// MyBatis-Plus Oracle 分页配置
@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
        return interceptor;
    }
}
```

---

## 4. 微服务与分布式 (Microservices & Distributed)

### 4.1 Spring Cloud Alibaba

#### Nacos（注册中心 + 配置中心）

```yaml
# application.yml
spring:
  application:
    name: user-service
  cloud:
    nacos:
      server-addr: 192.168.1.10:8848
      discovery:
        namespace: dev
        group: DEFAULT_GROUP
      config:
        file-extension: yaml
        namespace: dev
        group: DEFAULT_GROUP
        refresh-enabled: true
```

```java
// ========== 启动类 ==========
@SpringBootApplication
@EnableDiscoveryClient  // 注册到 Nacos
public class UserServiceApplication { /* ... */ }

// ========== 服务间调用 ==========
@FeignClient(name = "order-service", path = "/api/orders")
public interface OrderFeignClient {
    @GetMapping("/user/{userId}")
    List<Order> getOrdersByUserId(@PathVariable Long userId);
}

@Service
public class UserService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    public UserVO getUserWithOrders(Long userId) {
        User user = userMapper.selectById(userId);
        List<Order> orders = orderFeignClient.getOrdersByUserId(userId);
        return UserVO.builder().user(user).orders(orders).build();
    }
}

// ========== 动态配置刷新 ==========
@RefreshScope  // 配置变化时刷新 Bean
@Component
@ConfigurationProperties(prefix = "order")
@Data
public class OrderProperties {
    private Integer timeout;
    private Integer maxRetry;
}
```

#### OpenFeign（声明式 HTTP 客户端）

```java
// ========== Feign 客户端配置 ==========
@FeignClient(
    name = "inventory-service",
    path = "/api/inventory",
    fallbackFactory = InventoryFallbackFactory.class
)
public interface InventoryFeignClient {
    @GetMapping("/{skuId}")
    Result<Integer> getStock(@PathVariable("skuId") Long skuId);

    @PostMapping("/deduct")
    Result<Void> deductStock(@Valid @RequestBody DeductReq req);
}

// ========== 熔断降级 ==========
@Component
@Slf4j
public class InventoryFallbackFactory implements FallbackFactory<InventoryFeignClient> {
    @Override
    public InventoryFeignClient create(Throwable cause) {
        return new InventoryFeignClient() {
            @Override
            public Result<Integer> getStock(Long skuId) {
                log.error("获取库存失败，降级处理", cause);
                return Result.success(0); // 返回兜底数据
            }

            @Override
            public Result<Void> deductStock(DeductReq req) {
                log.error("扣减库存失败，降级处理", cause);
                return Result.error(500, "库存服务不可用");
            }
        };
    }
}

// ========== 自定义配置 ==========
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor tokenInterceptor() {
        return template -> {
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String token = attributes.getRequest().getHeader("Authorization");
                template.header("Authorization", token);
            }
        };
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 3); // 重试 3 次
    }
}
```

#### Gateway（路由网关）

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service           # 负载均衡
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1                # 去掉 /api 前缀
            - name: RequestRateLimiter     # 限流
              args:
                key-resolver: "#{@ipKeyResolver}"
                redis-rate-limiter:
                  replenishRate: 10        # 每秒令牌数
                  burstCapacity: 20        # 突发容量

        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - StripPrefix=1

      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin
```

```java
@Configuration
public class GatewayConfig {
    // IP 限流 Key 解析器
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNull(
                exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
            return Mono.just(ip);
        };
    }
}

// ========== 自定义全局过滤器（鉴权） ==========
@Component
@Order(-1)
public class AuthGlobalFilter implements GlobalFilter {
    private static final List<String> WHITE_LIST = Arrays.asList("/api/auth/login", "/api/auth/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 白名单放行
        if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 校验 Token（实际项目调用 Auth 服务验证）
        return chain.filter(exchange);
    }
}
```

#### Sentinel（流控与熔断）

```java
// ========== 限流注解使用 ==========
@Service
public class OrderService {
    // 资源名限流
    @SentinelResource(value = "createOrder", blockHandler = "blockHandler", fallback = "fallback")
    public Order createOrder(Order order) {
        // 核心业务逻辑
        return order;
    }

    // 限流后的处理（发生在限流或熔断时）
    public Order blockHandler(Order order, BlockException ex) {
        throw new BusinessException("系统繁忙，请稍后重试");
    }

    // 业务异常时的降级处理
    public Order fallback(Order order, Throwable ex) {
        log.error("创建订单失败", ex);
        return null;
    }
}

// ========== 流量控制规则 ==========
@Configuration
public class SentinelConfig {
    @PostConstruct
    public void initRules() {
        // QPS 限流规则
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("createOrder");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);  // QPS 模式
        flowRule.setCount(100);                           // 每秒最多 100 请求
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));

        // 熔断降级规则
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("createOrder");
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT); // RT 模式
        degradeRule.setCount(1000);  // 平均响应时间超过 1000ms 触发熔断
        degradeRule.setTimeWindow(10); // 熔断 10 秒后恢复
        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
    }
}

// ========== 控制台连接配置 ==========
// application.yml
// spring:
//   cloud:
//     sentinel:
//       transport:
//         dashboard: localhost:8080
```

#### Seata（分布式事务）

```java
// ========== 创建订单（全局事务入口） ==========
@GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
public void createOrder(Order order) {
    // 1. 创建订单
    orderDao.insert(order);

    // 2. 扣减库存（远程调用）
    inventoryFeignClient.deductStock(order.getSkuId(), order.getQuantity());

    // 3. 扣减余额（远程调用）
    accountFeignClient.deductBalance(order.getUserId(), order.getTotalPrice());

    // 任意一步失败，全部回滚
}
```

**Seata 的 AT 模式原理：**
1. 业务数据 + `undo_log`（回滚日志），先写 `undo_log` 再写业务数据
2. TM（事务管理器）开启全局事务
3. RM（资源管理器）注册分支事务
4. 全部成功 → 全局提交，删除 `undo_log`
5. 任意失败 → TM 通知全局回滚，根据 `undo_log` 恢复数据

---

### 4.2 消息队列 (MQ)

#### RocketMQ

```java
// ========== 生产者 ==========
@Service
public class OrderMessageProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送普通消息
    public void sendOrderCreateMessage(Order order) {
        rocketMQTemplate.convertAndSend("order-topic", order);
    }

    // 发送延迟消息（用于取消超时未支付订单）
    public void sendDelayCancelMessage(Long orderId) {
        Message<Long> message = MessageBuilder.withPayload(orderId).build();
        rocketMQTemplate.syncSend(
            "order-delay-topic",
            message,
            3000,           // 超时时间
            4               // 延迟级别：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        );
    }

    // 发送事务消息
    @Transactional
    public void createOrderWithTransaction(Order order) {
        Message<Order> message = MessageBuilder.withPayload(order).build();
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(
            "order-tx-topic", message, null);
    }
}

// 事务消息监听器
@RocketMQTransactionListener(txProducerGroup = "order-tx-producer-group")
public class OrderTransactionListener implements RocketMQLocalTransactionListener {
    @Autowired
    private OrderDao orderDao;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            Order order = (Order) ((RocketMQLocalTransactionMessage) msg).getPayload();
            orderDao.insert(order); // 执行本地事务
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // 检查本地事务状态
        Order order = (Order) ((RocketMQLocalTransactionMessage) msg).getPayload();
        return orderDao.selectById(order.getId()) != null
            ? RocketMQLocalTransactionState.COMMIT
            : RocketMQLocalTransactionState.ROLLBACK;
    }
}

// ========== 消费者 ==========
@Component
@RocketMQMessageListener(
    topic = "order-topic",
    consumerGroup = "order-consumer-group",
    consumeMode = ConsumeMode.ORDERLY      // 顺序消费
)
public class OrderMessageConsumer implements RocketMQListener<Order> {
    @Override
    public void onMessage(Order order) {
        log.info("收到订单消息: {}", order.getId());
        // 处理订单（如发短信、更新统计等）
    }
}

// ========== 消息幂等性处理 ==========
// 消费者端必须做幂等处理，防止重复消费
@Component
@RocketMQMessageListener(topic = "payment-topic", consumerGroup = "payment-group")
public class PaymentConsumer implements RocketMQListener<Payment> {
    @Autowired
    private RedisTemplate<String, String> redis;

    @Override
    public void onMessage(Payment payment) {
        String idempotentKey = "payment:" + payment.getOrderId();

        // 使用 SETNX 实现幂等
        Boolean success = redis.opsForValue()
            .setIfAbsent(idempotentKey, "1", 1, TimeUnit.DAYS);

        if (Boolean.TRUE.equals(success)) {
            // 首次处理
            processPayment(payment);
        } else {
            log.info("支付消息已处理过: {}", payment.getOrderId());
        }
    }
}
```

#### RabbitMQ

```java
// ========== 配置 ==========
@Configuration
public class RabbitMQConfig {
    // 直连交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    // 主题交换机
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    // 队列
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order.queue").build();
    }

    // 延迟队列（死信队列实现）
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable("delay.queue")
            .withArgument("x-dead-letter-exchange", "dead.exchange")
            .withArgument("x-dead-letter-routing-key", "dead.key")
            .withArgument("x-message-ttl", 30000)  // 30 秒
            .build();
    }

    // 绑定
    @Bean
    public Binding binding(Queue orderQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(orderQueue).to(directExchange).with("order.key");
    }
}

// ========== 生产者 ==========
@Service
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(Order order) {
        rabbitTemplate.convertAndSend("direct.exchange", "order.key", order);
    }

    public void sendDelayMessage(Object message, long delayMs) {
        rabbitTemplate.convertAndSend("delay.exchange", "delay.key", message, msg -> {
            msg.getMessageProperties().setDelay((int) delayMs); // 需要插件支持
            return msg;
        });
    }
}

// ========== 消费者 ==========
@Component
public class OrderMessageListener {
    @RabbitListener(queues = "order.queue")
    public void handleOrder(Order order, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            processOrder(order);
            channel.basicAck(tag, false);     // 手动确认
        } catch (Exception e) {
            channel.basicNack(tag, false, true); // 重新入队
        }
    }
}
```

#### Kafka

```java
// ========== 生产者 ==========
@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLog(String log) {
        kafkaTemplate.send("log-topic", log);
    }

    public void sendWithKey(String key, String value) {
        kafkaTemplate.send("log-topic", key, value);  // 相同 key 进入同一分区
    }
}

// ========== 消费者 ==========
@Component
public class KafkaConsumer {
    @KafkaListener(topics = "log-topic", groupId = "log-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("收到消息: key={}, value={}, offset={}",
            record.key(), record.value(), record.offset());
    }
}
```

#### MQ 对比总结

| 特性 | RocketMQ | RabbitMQ | Kafka |
|------|----------|----------|-------|
| 开发公司 | 阿里 | Pivotal | Apache / LinkedIn |
| 协议 | 自定义 | AMQP | 自定义 |
| 吞吐量 | 高（10万+/s） | 中（万级/s） | 极高（百万级/s） |
| 延迟 | 毫秒级 | 微秒级 | 毫秒级 |
| 消息可靠性 | 极高 | 高 | 高 |
| 事务消息 | ✅ | ❌ | ❌ |
| 延迟消息 | ✅（内置） | ✅（插件） | ❌ |
| 顺序消息 | ✅ | ❌ | ✅（分区内） |
| 适用场景 | 金融级业务 | 中小型系统 | 日志/大数据/流处理 |
| 运维复杂度 | 中 | 低 | 高 |

---

### 4.3 Docker

#### 基础命令

```bash
# ========== 镜像管理 ==========
docker pull mysql:8.0           # 拉取镜像
docker images                   # 查看本地镜像
docker rmi mysql:8.0            # 删除镜像
docker build -t my-app:1.0 .    # 从 Dockerfile 构建镜像

# ========== 容器管理 ==========
docker run -d --name my-mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -v /data/mysql:/var/lib/mysql \
  mysql:8.0

docker ps                       # 查看运行中的容器
docker ps -a                    # 查看所有容器
docker stop my-mysql            # 停止
docker start my-mysql           # 启动
docker restart my-mysql         # 重启
docker rm my-mysql              # 删除

docker logs -f my-mysql         # 查看日志
docker exec -it my-mysql bash   # 进入容器
docker cp app.jar container:/app/  # 文件复制

# ========== 镜像仓库 ==========
docker tag my-app:1.0 my-registry.com/my-app:1.0
docker push my-registry.com/my-app:1.0
docker pull my-registry.com/my-app:1.0
```

#### Dockerfile

```dockerfile
# ========== 多阶段构建（减小镜像体积） ==========
# 第一阶段：编译
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# 第二阶段：运行
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Docker Compose

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: my-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: mydb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - my-network

  redis:
    image: redis:7
    container_name: my-redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - my-network

  app:
    build: .
    container_name: my-app
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/mydb
      SPRING_REDIS_HOST: redis
    depends_on:
      - mysql
      - redis
    networks:
      - my-network

volumes:
  mysql-data:
  redis-data:

networks:
  my-network:
    driver: bridge
```

---

## 5. 开发与运维工具 (Tools & DevOps)

### 5.1 Git

#### 常用命令

```bash
# ========== 基础操作 ==========
git init                                    # 初始化仓库
git clone git@github.com:user/repo.git      # 克隆远程仓库
git status                                  # 查看状态
git add .                                   # 暂存所有变更
git commit -m "feat: 新增用户登录功能"        # 提交
git log --oneline --graph                   # 查看日志

# ========== 分支管理 ==========
git branch feature-login                    # 创建分支
git checkout feature-login                  # 切换分支
git checkout -b feature-login               # 创建并切换
git merge feature-login                     # 合并到当前分支
git branch -d feature-login                 # 删除本地分支
git push origin --delete feature-login      # 删除远程分支

# ========== 远程操作 ==========
git remote -v                               # 查看远程仓库
git fetch origin                            # 拉取（不合并）
git pull origin main                        # 拉取并合并
git push origin main                        # 推送
git push -u origin feature-login            # 推送并设置上游

# ========== 撤销与回退 ==========
git checkout -- file.txt                    # 撤销工作区修改
git reset HEAD file.txt                     # 撤销暂存区
git reset --hard HEAD~1                     # 回退 1 个版本（丢弃修改）
git reset --soft HEAD~1                     # 回退 1 个版本（保留修改）
git revert HEAD                            # 撤销某次提交（生成新提交）

# ========== 冲突解决 ==========
# 1. git pull 后提示冲突
# 2. 手动编辑冲突文件（<<<<<<< HEAD ... ======= ... >>>>>>> branch）
# 3. git add . && git commit

# ========== 暂存 ==========
git stash                                   # 暂存当前修改
git stash list                              # 查看暂存列表
git stash pop                               # 恢复并删除
git stash apply stash@{0}                   # 恢复但不删除

# ========== 标签 ==========
git tag v1.0.0                              # 创建标签
git tag -a v1.0.0 -m "Release version 1.0" # 创建带注释的标签
git push origin v1.0.0                      # 推送标签

# ========== Git Flow 提交规范 ==========
# feat:    新功能
# fix:     修复 bug
# docs:    文档修改
# style:   代码格式（不影响功能）
# refactor:代码重构
# test:    增加测试
# chore:   构建/工具变更
# perf:    性能优化
```

#### .gitignore 示例

```gitignore
# Java
*.class
*.jar
*.war
target/
!.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS
.DS_Store
Thumbs.db

# Logs
logs/
*.log

# Environment
.env
*.env.*
application-local.yml
application-dev.yml

# Build
build/
dist/
node_modules/
```

---

### 5.2 Maven / Gradle

#### Maven

```xml
<!-- pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>user-service</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>3.5.5</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

```bash
# Maven 常用命令
mvn clean                    # 清理 target
mvn compile                  # 编译
mvn test                     # 运行测试
mvn package                  # 打包
mvn install                  # 安装到本地仓库
mvn deploy                   # 部署到远程仓库
mvn clean package -DskipTests # 跳过测试打包
mvn dependency:tree          # 查看依赖树
mvn help:effective-pom       # 查看有效 POM
```

#### Gradle

```groovy
// build.gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.example'
version = '1.0.0'

java {
    sourceCompatibility = '17'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    implementation 'com.baomidou:mybatis-plus-spring-boot3-starter:3.5.5'
    runtimeOnly 'com.mysql:mysql-connector-j'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

```bash
# Gradle 常用命令
gradle clean           # 清理
gradle build           # 编译+测试+打包
gradle test            # 运行测试
gradle bootRun         # 运行 Spring Boot 应用
gradle dependencies    # 查看依赖树
```

#### Maven vs Gradle

| 特性 | Maven | Gradle |
|------|-------|--------|
| 配置语言 | XML | Groovy / Kotlin DSL |
| 构建速度 | 较慢 | 快（增量编译、构建缓存） |
| 依赖管理 | 成熟稳定 | 更灵活 |
| 学习曲线 | 低 | 中 |
| 大型项目 | 配置臃肿 | 简洁 |
| 国内流行度 | 极高（主流） | 逐渐上升（Android 必用） |

#### Maven 多模块项目

```xml
<!-- 父 POM：parent/pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>parent-project</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>common</module>
        <module>dal</module>
        <module>service</module>
        <module>web</module>
    </modules>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>3.2.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                <version>3.5.5</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

```xml
<!-- 子模块：web/pom.xml -->
<project>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>parent-project</artifactId>
        <version>1.0.0</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>web-module</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>service-module</artifactId>
        </dependency>
        <!-- 继承父 POM 管理的版本，无需指定 version -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### Maven Profile（环境配置）

通过 profile 区分开发/测试/生产环境。

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <env>dev</env>
            <db.url>jdbc:mysql://localhost:3306/dev_db</db.url>
        </properties>
    </profile>

    <profile>
        <id>prod</id>
        <properties>
            <env>prod</env>
            <db.url>jdbc:mysql://prod-host:3306/prod_db</db.url>
        </properties>
    </profile>
</profiles>
```

```bash
# 激活 profile
mvn clean package -P prod                    # 激活 prod profile
mvn clean package -P dev,test                # 同时激活多个 profile
mvn help:all-profiles                        # 查看所有 profile
mvn help:effective-pom                       # 查看生效的 POM（含 profile 展开）
```



---


```java
@Mojo(name = "count-lines", requiresProject = true, threadSafe = true)
public class CountLinesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private File sourceDir;

    @Parameter(defaultValue = "java")
    private String extension;

    @Override
    public void execute() throws MojoExecutionException {
        int count = 0;
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith("." + extension));
        if (files == null) {
            getLog().warn("Source directory not found: " + sourceDir);
            return;
        }
        for (File file : files) {
            try {
                count += Files.readAllLines(file.toPath()).size();
            } catch (IOException e) {
                getLog().warn("Failed to read file: " + file.getName(), e);
            }
        }
        getLog().info("Total lines: " + count);
    }
}
```

```bash
# 安装自定义插件
mvn install

# 在其他项目中使用
# mvn com.example:my-plugin:count-lines -Dextension=java
```



---



### 5.3 单元测试 (JUnit + Mockito)

```java
// ========== JUnit 5 基础 ==========
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUserName("test");
        user.setAge(20);

        User created = userService.create(user);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUserName()).isEqualTo("test");
    }

    @Test
    void testFindUserNotFound() {
        assertThrows(BusinessException.class, () -> {
            userService.findById(-1L);
        });
    }

    @ParameterizedTest
    @CsvSource({"18, true", "16, false", "20, true"})
    void testIsAdult(int age, boolean expected) {
        assertThat(userService.isAdult(age)).isEqualTo(expected);
    }
}

// ========== Mockito（隔离外部依赖） ==========
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private InventoryFeignClient inventoryFeignClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrderSuccess() {
        // 模拟库存充足
        when(inventoryFeignClient.getStock(1L)).thenReturn(Result.success(100));

        // 模拟插入成功
        Order order = new Order();
        order.setSkuId(1L);
        order.setQuantity(2);
        when(orderDao.insert(any())).thenReturn(1);

        // 执行
        Order result = orderService.create(order, 1L);

        // 验证
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
        verify(orderDao, times(1)).insert(any());
        verify(inventoryFeignClient, times(1)).deductStock(any());
    }

    @Test
    void testCreateOrderInsufficientStock() {
        when(inventoryFeignClient.getStock(1L)).thenReturn(Result.success(0));

        assertThrows(BusinessException.class, () -> {
            orderService.create(new Order(), 1L);
        });

        verify(orderDao, never()).insert(any());
    }
}

// ========== MockMvc（测试 Controller） ==========
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUserName("张三");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.userName").value("张三"));
    }
}



#### @SpringBootTest 随机端口

启动完整的嵌入式 Web 服务器进行端到端测试。

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void testCreateAndQuery() {
        // 先创建用户
        User req = new User().setUserName("张三");
        ResponseEntity<User> createResp = restTemplate.postForEntity(
                "/api/users", req, User.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Long id = createResp.getBody().getId();

        // 再查询
        ResponseEntity<User> queryResp = restTemplate.getForEntity(
                "/api/users/{id}", User.class, id);
        assertThat(queryResp.getBody().getUserName()).isEqualTo("张三");
    }

    @Test
    void testHealth() {
        ResponseEntity<String> resp = restTemplate.getForEntity(
                "/actuator/health", String.class);
        assertThat(resp.getBody()).contains("\"status\":\"UP\"");
    }
}
```



---



### 5.4 Linux

#### 常用命令

```bash
# ========== 文件操作 ==========
ls -la                 # 查看文件列表（包含隐藏文件）
pwd                    # 当前路径
cd /opt/app            # 切换目录
cp -r src dst          # 递归复制
mv old new             # 移动/重命名
rm -rf /path           # 强制递归删除（⚠️ 危险！）
mkdir -p a/b/c         # 创建多级目录
touch file.txt         # 创建空文件
find / -name "*.log"   # 查找文件

# ========== 查看与编辑文件 ==========
cat file.txt           # 查看全部内容
less file.txt          # 分页查看（q 退出）
head -n 20 file.txt    # 查看前 20 行
tail -n 20 file.txt    # 查看后 20 行
tail -f app.log        # 实时查看日志（常用）
vim file.txt           # 编辑器

# ========== 进程管理 ==========
ps -ef                 # 查看所有进程
ps aux | grep java     # 查找 Java 进程
top                    # 实时系统监控（CPU/内存）
htop                   # top 增强版
kill -9 <pid>          # 强制终止进程
nohup java -jar app.jar > app.log 2>&1 &  # 后台运行 Java 应用

# ========== 网络 ==========
netstat -tlnp          # 查看监听端口
ss -tlnp               # 查看监听端口（更高效）
curl http://localhost:8080/actuator/health  # 请求接口
ping baidu.com         # 网络连通性
telnet 192.168.1.1 3306 # 测试端口连通性

# ========== 磁盘与内存 ==========
df -h                  # 磁盘空间
du -sh /path           # 查看目录大小
free -h                # 内存使用
uptime                 # 系统运行时间与负载

# ========== 权限 ==========
chmod +x script.sh     # 添加执行权限
chmod 755 file         # rwxr-xr-x
chown user:group file  # 修改文件所有者

# ========== 日志分析 ==========
grep "ERROR" app.log | wc -l           # 统计错误数
grep -o 'cost=[0-9]*' app.log | sort -rn | head -10  # 找出最慢的请求
awk '{print $4}' app.log | sort | uniq -c | sort -rn  # IP 访问统计
sed -n '/2025-06-02 10:00/,/2025-06-02 11:00/p' app.log  # 时间段日志
journalctl -u my-app -f  # systemd 日志

# ========== 压缩与归档 ==========
tar -czf archive.tar.gz /path/to/dir    # 压缩
tar -xzf archive.tar.gz                 # 解压
zip -r archive.zip /path/to/dir         # zip 压缩
unzip archive.zip                       # zip 解压
```

#### 快速排查 Java 问题

```bash
# 1. 查看进程
ps aux | grep java

# 2. 查看端口
netstat -tlnp | grep java

# 3. 查看日志
tail -f /opt/app/logs/error.log

# 4. CPU 过高定位
top -H -p <pid>            # 查看线程 CPU 占用
printf "%x\n" <thread-id>  # 线程 ID 转十六进制
jstack <pid> | grep <hex>  # 查看线程栈

# 5. 内存问题
jmap -heap <pid>           # 查看堆内存
jstat -gcutil <pid> 1000   # GC 监控
```

#### Shell 脚本示例

```bash
#!/bin/bash
# deploy.sh：Spring Boot 部署脚本

APP_NAME="user-service"
APP_JAR="${APP_NAME}.jar"
APP_PORT=8080
JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError"

# 启动
start() {
    local pid=$(ps aux | grep ${APP_JAR} | grep -v grep | awk '{print $2}')
    if [ -n "$pid" ]; then
        echo "${APP_NAME} 已在运行 (PID: $pid)"
        return 1
    fi

    nohup java ${JAVA_OPTS} -jar ${APP_JAR} > ${APP_NAME}.log 2>&1 &
    echo "${APP_NAME} 已启动，PID: $!"

    sleep 3
    # 健康检查
    for i in {1..10}; do
        if curl -s http://localhost:${APP_PORT}/actuator/health | grep -q "UP"; then
            echo "健康检查通过"
            return 0
        fi
        sleep 2
    done
    echo "健康检查失败"
    return 1
}

# 停止
stop() {
    local pid=$(ps aux | grep ${APP_JAR} | grep -v grep | awk '{print $2}')
    if [ -z "$pid" ]; then
        echo "${APP_NAME} 未运行"
        return 0
    fi

    kill $pid
    sleep 5
    if ps -p $pid > /dev/null 2>&1; then
        kill -9 $pid
    fi
    echo "${APP_NAME} 已停止"
}

case "$1" in
    start)   start ;;
    stop)    stop ;;
    restart) stop; sleep 2; start ;;
    *)       echo "用法: $0 {start|stop|restart}" ;;
esac
```

#### systemd（服务管理常用命令）

```bash
# Service Unit 示例：/etc/systemd/system/user-service.service
systemctl daemon-reload      systemctl start user-service
systemctl stop user-service   systemctl restart user-service
systemctl status user-service systemctl enable user-service
systemctl disable user-service

# 日志
journalctl -u user-service               # 查看服务日志
journalctl -u user-service -f            # 实时跟踪日志
journalctl -u user-service --since "1 hour ago"
journalctl -u user-service -p err        # 只看错误级别
journalctl --vacuum-size=500M            # 清理日志至 500M
```

#### Linux 性能调优（常用）

```bash
# ulimit（用户资源限制）
ulimit -a                              # 查看所有限制
# 永久修改：/etc/security/limits.conf
# * soft nofile 65536
# * hard nofile 65536

# 系统内核参数调优（/etc/sysctl.conf）
vm.swappiness = 10                     # 降低 swap 使用倾向
vm.max_map_count = 262144              # 最大内存映射区域数（ES 需要）
net.core.somaxconn = 1024
net.ipv4.tcp_tw_reuse = 1
net.ipv4.ip_local_port_range = 1024 65535
sysctl -p                              # 立即生效
```

#### 任务调度（crontab）

```bash
crontab -l                             # 查看当前用户计划任务
crontab -e                             # 编辑计划任务
# 格式：分 时 日 月 周  命令
*/5 * * * * /opt/scripts/health-check.sh    # 每 5 分钟执行
0 2 * * * /opt/scripts/daily-backup.sh      # 每天凌晨 2 点执行
grep CRON /var/log/syslog                   # 查看 cron 执行日志
```

---



> **参考说明**：本文档涵盖的技术栈均为企业级 Java 后端开发的核心内容。每个技术点均包含概念解释和实用代码示例，适合作为面试复习和日常开发的参考资料。建议结合实际项目进行练习以加深理解。
