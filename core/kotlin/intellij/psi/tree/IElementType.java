package com.intellij.psi.tree;

import org.jetbrains.annotations.NotNull;

public class IElementType {
    private final String debugName;

    public IElementType(@NotNull String debugName) {
        this.debugName = debugName;
    }

    public IElementType(@NotNull String debugName, Object language) {
        this.debugName = debugName;
    }

    @Override
    public String toString() {
        return debugName;
    }
}