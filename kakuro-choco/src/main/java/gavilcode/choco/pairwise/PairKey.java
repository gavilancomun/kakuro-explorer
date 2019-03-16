package gavilcode.choco.pairwise;

import java.util.Objects;

public class PairKey implements Comparable<PairKey> {

public Integer Property1Id;
public String Property1Value;
public Integer Property2Id;
public String Property2Value;

@Override
public int hashCode() {
  int hash = 3;
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
  final PairKey other = (PairKey) obj;
  if (!Objects.equals(this.Property1Value, other.Property1Value)) {
    return false;
  }
  if (!Objects.equals(this.Property2Value, other.Property2Value)) {
    return false;
  }
  if (!Objects.equals(this.Property1Id, other.Property1Id)) {
    return false;
  }
  if (!Objects.equals(this.Property2Id, other.Property2Id)) {
    return false;
  }
  return true;
}

@Override
public String toString() {
  return Property1Id + ", " + Property1Value + ", " + Property2Id + ", " + Property2Value;
}

@Override
public int compareTo(PairKey other) {
  if (Objects.equals(Property1Id, other.Property1Id)) {
    if (Objects.equals(Property2Id, other.Property2Id)) {
      if (Property1Value.equals(other.Property1Value)) {
        return Property2Value.compareTo(other.Property2Value);
      }
      else {
        return Property1Value.compareTo(other.Property1Value);
      }
    }
    else {
      return Property2Id.compareTo(other.Property2Id);
    }
  }
  else {
    return Property1Id.compareTo(other.Property1Id);
  }
}
}
