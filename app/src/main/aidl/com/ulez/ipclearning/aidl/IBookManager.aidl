// IBookManager.aidl
package com.ulez.ipclearning.aidl;

// Declare any non-default types here with import statements

import com.ulez.ipclearning.aidl.Book;//尽管在相同的包下，还需要导入
import com.ulez.ipclearning.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    List<Book> getBookList(); // 返回书籍列表
    void addBook(in Book book); // 添加书籍
    void registerListener(IOnNewBookArrivedListener listener); // 注册接口
    void unregisterListener(IOnNewBookArrivedListener listener); // 注册接口
}
