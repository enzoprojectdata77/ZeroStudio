package com.intellij.psi;

import com.intellij.psi.tree.IElementType;

public interface TokenType {
    IElementType WHITE_SPACE = new IElementType("WHITE_SPACE");
    IElementType BAD_CHARACTER = new IElementType("BAD_CHARACTER");
}