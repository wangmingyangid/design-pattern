package org.wmy.bean.demo;

import cn.hutool.core.bean.BeanUtil;
import org.wmy.bean.SearchWord;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wmy
 * @create 2021-06-17 15:16
 *
 *
 */
public class Demo {

    private HashMap<String,SearchWord> currentSearchMap = new HashMap<>();

    private long lastUpdateTime = -1;

    /**
     * 根据数据库数据进行对 currentSearchMap 进行刷新
     * 要求：保障当前系统在使用的 map 是某一时刻的版本；
     *      更新 map 的过程不能影响正常使用的过程。
     */
    public void shallowCopyRefresh(){
        // 1.浅拷贝赋值一份对象
        HashMap<String,SearchWord> newSearchMap = (HashMap<String,SearchWord>)currentSearchMap.clone();
        // 2.从数据库获取数据
        List<SearchWord> searchWords = getDataFromDB(lastUpdateTime);
        if (searchWords == null) {
            return;
        }
        long maxNewUpdatedTime = lastUpdateTime;
        // 3.遍历得到的数据，对newSearchMap中的数据进行跟新
        for (SearchWord searchWord : searchWords) {
            // 获取最大更新时间
            if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
                maxNewUpdatedTime = searchWord.getLastUpdateTime();
            }
            // 3.1 如果 newSearchMap 中不存在，放入该数据
            if (!newSearchMap.containsKey(searchWord.getKeyWord())) {
                newSearchMap.put(searchWord.getKeyWord(),searchWord);
            }else {
                // 3.2 如果 newSearchMap 中存在，更新数据
                // 这里必须放入新的对象，因为 clone 属于浅拷贝，如果在原来的基础上修改
                // 会导致 currentSearchMap 中的数据版本不一致
                SearchWord newSearchWord = new SearchWord();
                BeanUtil.copyProperties(searchWord,newSearchWord,false);
                newSearchMap.put(newSearchWord.getKeyWord(),newSearchWord);
            }

        }

        lastUpdateTime = maxNewUpdatedTime;
        currentSearchMap = newSearchMap;
    }

    /**
     * 采用 deep-copy 实现对象之间的拷贝
     * 方法一：序列化拷贝
     * 方法二：递归拷贝对象、对象的引用对象以及引用对象的引用对象
     *        直到要拷贝的对象只包含基本数据类型数据，没有引用对象为止
     */
    public void deepCopyRefresh() {

        Object obj = deepCopy(currentSearchMap);
        HashMap<String,SearchWord> newSearchMap = (HashMap<String, SearchWord>) obj;
        // todo 省略更新的代码，在更新的过程中不需要创建新的对象啦，因为是深拷贝
    }

    public void recursiveDeepCopyRefresh(){
        HashMap<String,SearchWord> newSearchMap = new HashMap<>();
        for (Map.Entry<String, SearchWord> entry : currentSearchMap.entrySet()) {

            SearchWord searchWord = entry.getValue();
            // 这里也可以用 HuTool 工具，该工具实现的也是浅拷贝，因为SearchWord中只有基本数据类型，所以没有问题
            newSearchMap.put(entry.getKey(),new SearchWord(searchWord.getKeyWord(),
                    searchWord.getLastUpdateTime(),searchWord.getNum()));
        }
        // todo 省略更新的代码，在更新的过程中不需要创建新的对象啦，因为是深拷贝
    }

    /**
     *
     * 实际的数据流向：
     *     ObjectOutputStream->ByteArrayOutputStream->ByteArrayInputStream ->ObjectInputStream
     *     深度拷贝=== 从序列化对象又转为序列化对象
     *
     * @param object 该对象必须实现序列化接口；对象中的transient和static类型成员变量不会被读取和写入
     * @return 深度复制后的对象
     *
     *
     */
    private Object deepCopy(Object object) {
        ByteArrayOutputStream bo;
        ObjectOutputStream oo = null;
        ByteArrayInputStream bi;
        ObjectInputStream oi = null;
        try{
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            // 获取内存中的缓存数据并转成数组
            oo.writeObject(object);

            // bo.toByteArray() 看源码会进行数组复制
            bi = new ByteArrayInputStream(bo.toByteArray());
            oi = new ObjectInputStream(bi);
            return oi.readObject();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                oo.close();
                oi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private List<SearchWord> getDataFromDB(long lastUpdateTime){

        // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
        return null;
    }
}
