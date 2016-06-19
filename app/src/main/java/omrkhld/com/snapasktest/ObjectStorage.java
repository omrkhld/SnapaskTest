package omrkhld.com.snapasktest;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Omar on 19/6/2016.
 */
public class ObjectStorage<T extends Serializable> {
    private Context context;
    private Class<T> clazz;

    public ObjectStorage(Class<T> clazz, Context context) {
        this.context = context;
        this.clazz = clazz;
    }

    public Context getContext() {
        return context;
    }

    public boolean exists(String name) {
        File file = getContext().getFileStreamPath(getFileName(name));
        return file.exists();
    }

    public boolean delete(String name) {
        File file = getContext().getFileStreamPath(getFileName(name));
        return file.delete();
    }

    @SuppressWarnings("unchecked")
    public T load(String name) {
        ObjectInputStream ois = null;
        T result = null;
        try {
            FileInputStream fis = context.getApplicationContext().openFileInput(getFileName(name));
            ois = new ObjectInputStream(fis);
            result = (T) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean save(String name, T object) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput(getFileName(name), Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getFileName(String name) {
        return clazz.getName() + "_" + name.hashCode();
    }
}
