package com.flowlikeariver.kakuro2;

import java.util.Objects;

public class Pair<T, U> {

public final T left;
public final U right;

public Pair(T left, U right) {
  this.left = left;
  this.right = right;
}

@Override
public int hashCode() {
  int hash = 7;
  hash = 79 * hash + Objects.hashCode(this.left);
  hash = 79 * hash + Objects.hashCode(this.right);
  return hash;
}

@Override
public boolean equals(Object obj) {
  if (this == obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (getClass() != obj.getClass()) {
    return false;
  }
  final Pair<?, ?> other = (Pair<?, ?>) obj;
  return Objects.equals(this.left, other.left) && Objects.equals(this.right, other.right);
}

/**
 * @return the left
 */
public T getLeft() {
  return left;
}

/**
 * @return the right
 */
public U getRight() {
  return right;
}

@Override
public String toString() {
  return getClass().getName().replaceAll(".*\\.", "") + "[left=" + left.toString() + ", right=" + right.toString() + "]";
}

}
