package pl.tecna.aurea.engine.forms.merge.impl;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import pl.tecna.aurea.engine.forms.merge.Context;
import pl.tecna.aurea.engine.forms.merge.Key;

public class PathKey implements Key {

  private String path;

  @Inject
  public PathKey(@Assisted Context context) {
    path = context.getPath();
  }
  
  public String getPath() {
    return path;
  }

  @Override
  public int compareTo(Key compared) {
    if (compared instanceof PathKey) {
      PathKey pathKey = (PathKey) compared;
      return path.contentEquals(pathKey.getPath()) ? 0 : -1;
    } else {
      throw new IllegalArgumentException("Unsupported Key implementation (key type:" + compared.getClass() + ")");
    }
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
        .append("PathKey[")
        .append(path)
        .append("]");
    return builder.toString();
  }

}
