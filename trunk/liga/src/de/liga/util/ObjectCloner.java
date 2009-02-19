package de.liga.util;

import java.io.*;

/**
 * This class uses serialization to clone an object with all dependents
 */
public class ObjectCloner implements Serializable {

  public ObjectCloner() {
  }

  public Serializable clone(Serializable input) {
    try {
      return deserialize(serialize(input));
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }


  public static byte[] serialize(Serializable obj) {
    ByteArrayOutputStream baos;
    ObjectOutputStream oos;
    byte[] store = null;
    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      try {
        oos.writeObject(obj);
        oos.flush();
        store = baos.toByteArray();
      } finally {
          baos.close();
          oos.close();
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe.getMessage());
    }
    return store;
  }

  /**
   * De-Serialize an Object from a byte array.
   *
   * @param store
   * @return the object or null if an <code>IOException</code> occured
   */
  public Serializable deserialize(byte[] store) throws IOException {
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    Serializable obj = null;
    try {
      bais = new ByteArrayInputStream(store);
      ois = new ObjectInputStream(bais);
        obj = (Serializable) ois.readObject();
    } catch (Exception cnfe) {
      throw new RuntimeException(cnfe.getMessage());
    } finally {
      if (bais != null) bais.close();
      if (ois != null) ois.close();
    }
    return obj;
  }

}
