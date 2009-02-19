// Copyright (C) 2002-2003 Jon A. Maxwell (JAM)
//
// This class is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

// original was: package netx.jnlp.util;

package de.liga.util.thread;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * This list stores objects automatically using weak references.
 * Objects are added and removed from the list as normal, but may
 * turn to null at any point (ie, indexOf(x) followed by get(x)
 * may return null).  The weak references are only removed when
 * the trimToSize method is called so that the indices remain
 * constant otherwise.<p>
 *
 * @author <a href="mailto:jmaxwell@users.sourceforge.net">Jon A. Maxwell (JAM)</a> - initial author
 * Roman Stumm, added more constructors, added Generics!
 * @version $Revision: 1.1.1.1 $
 */
public final class WeakList<E> extends AbstractList<E> {

  /* list of weak references */
  private final List<WeakReference<E>> refs;

  /**
   * Create a weak random-access list.
   */
  public WeakList() {
    refs = new ArrayList<WeakReference<E>>();
  }

  public WeakList(int initialSize)
  {
    refs = new ArrayList<WeakReference<E>>(initialSize);
  }

  public WeakList(List<E> otherList)
  {
    this(otherList.size());
    addAll(otherList);
  }

  /**
   * Extract the hard reference out of a weak reference.
   */
  private E deref(WeakReference<E> o) {
    if (o != null)
      return o.get();
    else
      return null;
  }

  /**
   * Returns the object at the specified index, or null if the
   * object has been collected.
   */
  public E get(int index) {
    return deref(refs.get(index));
  }

  /**
   * Returns the size of the list, including already collected
   * objects.
   */
  public int size() {
    return refs.size();
  }

  /**
   * Sets the object at the specified position and returns the
   * previous object at that position or null if it was already
   * collected.
   */
  public E set(int index, E element) {
    WeakReference<E> ref = new WeakReference<E>(element);
    return deref(refs.set(index, ref));
  }

  /**
   * Inserts the object at the specified position in the list.
   * Automatically creates a weak reference to the object.
   */
  public void add(int index, E element) {
    refs.add(index, new WeakReference<E>(element));
  }

  /**
   * Removes the object at the specified position and returns it
   * or returns null if it was already collected.
   */
  public E remove(int index) {
    return deref(refs.remove(index));
  }

  /**
   * Returns a list of hard references to the objects.  The
   * returned list does not include the collected elements, so its
   * indices do not necessarily correlate with those of this list.
   */
  public List hardList() {
    List result = new ArrayList(size());

    for (int i = 0; i < size(); i++) {
      Object tmp = get(i);

      if (tmp != null)
        result.add(tmp);
    }

    return result;
  }

  /**
   * Compacts the list by removing references to collected
   * objects.
   */
  public void trimToSize() {
    for (int i = size(); i-- > 0;) {
      if (get(i) == null)
        remove(i);
    }
  }

}
