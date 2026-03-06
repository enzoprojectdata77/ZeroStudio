package com.itsaky.androidide.treesitter.markdown;

import com.itsaky.androidide.treesitter.annotations.GenerateNativeHeaders;

import com.itsaky.androidide.treesitter.TSLanguage;
import com.itsaky.androidide.treesitter.TSLanguageCache;

/**
 * Tree Sitter for Markdown and Markdown Inline.
 *
 * @author Akash Yadav
 * @author android_zero
 */
public final class TSLanguageMarkdown {

  static {
    System.loadLibrary("tree-sitter-markdown");
  }

  private TSLanguageMarkdown() {
    throw new UnsupportedOperationException();
  }

  /**
   * @deprecated Tree Sitter language instances are <code>static const</code> and hence, they do not change. The
   * name of this method is misleading, use {@link TSLanguageMarkdown#getInstance()} instead.
   */
  @Deprecated
  public static TSLanguage newInstance() {
    return getInstance();
  }
  
  /**
   * Get the instance of the Markdown language.
   *
   * @return The instance of the Markdown language.
   */
  public static TSLanguage getInstance() {
    var language = TSLanguageCache.get("markdown");
    if (language != null) {
      return language;
    }

    language = TSLanguage.create("markdown", Native.getInstance());
    TSLanguageCache.cache("markdown", language);
    return language;
  }

  /**
   * Get the instance of the Markdown Inline language.
   */
  public static TSLanguage getInlineInstance() {
    var language = TSLanguageCache.get("markdown_inline");
    if (language != null) {
      return language;
    }

    language = TSLanguage.create("markdown_inline", Native.getInlineInstance());
    TSLanguageCache.cache("markdown_inline", language);
    return language;
  }

  @GenerateNativeHeaders(fileName = "markdown")
  public static class Native {
    public static native long getInstance();
    public static native long getInlineInstance();
  }
}