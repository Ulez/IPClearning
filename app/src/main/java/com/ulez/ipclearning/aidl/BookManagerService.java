package com.ulez.ipclearning.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


import com.ulez.ipclearning.Book;
import com.ulez.ipclearning.IBookManager;
import com.ulez.ipclearning.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();//支持并发读写。
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
//            SystemClock.sleep(5000);
            Log.i(TAG, "sleep 5s");
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG, "addBook");
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.i(TAG, "registerListener");
            mListenerList.register(listener);

            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.i(TAG, "registerListener, current size:" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.i(TAG, "unregisterListener");
            boolean success = mListenerList.unregister(listener);
            if (success) {
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG, "unregisterListener, current size:" + N);
        }
    };

    public BookManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "android原始"));
        mBookList.add(new Book(2, "ios原始"));
        mBookList.add(new Book(3, "art原始"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 4; i < 100; i++) {
                    SystemClock.sleep(1000);
                    mBookList.add(new Book(i, "android--" + i));
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind return mBinder");
        return mBinder;
    }
}
