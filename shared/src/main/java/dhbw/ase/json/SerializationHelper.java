package dhbw.ase.json;

import com.google.gson.Gson;

public class SerializationHelper<T> {
    Gson gson;
    Class<T> serializationClass;

    public SerializationHelper(Class<T> serializationClass) {
        gson = new Gson();
        this.serializationClass = serializationClass;
    }

    public T deserialize(String json) {
        return gson.fromJson(json, serializationClass);
    }

    public String serialize(T obj) {
        return gson.toJson(obj);
    }
}
