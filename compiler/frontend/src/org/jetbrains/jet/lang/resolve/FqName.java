/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.lang.resolve;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Stepan Koltsov
 */
public class FqName {

    public static final FqName ROOT = new FqName("");

    @NotNull
    private final FqNameUnsafe fqName;

    // cache
    private transient FqName parent;


    public FqName(@NotNull String fqName) {
        this.fqName = new FqNameUnsafe(fqName, this);
    }

    public FqName(@NotNull FqNameUnsafe fqName) {
        this.fqName = fqName;

        validateFqName();
    }

    private FqName(@NotNull FqNameUnsafe fqName, FqName parent) {
        this.fqName = fqName;
        this.parent = parent;

        validateFqName();
    }


    private void validateFqName() {
        if (!isValidAfterUnsafeCheck(fqName.getFqName())) {
            throw new IllegalArgumentException("incorrect fq name: " + fqName);
        }
    }

    private static boolean isValidAfterUnsafeCheck(@NotNull String qualifiedName) {
        // TODO: There's a valid name with escape char ``
        return qualifiedName.indexOf('<') < 0;
    }

    public static boolean isValid(@Nullable String qualifiedName) {
        return qualifiedName != null &&
               FqNameUnsafe.isValid(qualifiedName) &&
               isValidAfterUnsafeCheck(qualifiedName);
    }

    @NotNull
    public String getFqName() {
        return fqName.getFqName();
    }

    @NotNull
    public FqNameUnsafe toUnsafe() {
        return fqName;
    }

    public boolean isRoot() {
        return fqName.isRoot();
    }

    @NotNull
    public FqName parent() {
        if (parent != null) {
            return parent;
        }

        if (isRoot()) {
            throw new IllegalStateException("root");
        }

        parent = new FqName(fqName.parent());

        return parent;
    }

    @NotNull
    public FqName child(@NotNull String name) {
        return new FqName(fqName.child(name), this);
    }

    @NotNull
    public String shortName() {
        return fqName.shortName();
    }

    @NotNull
    public List<FqName> path() {
        final List<FqName> path = Lists.newArrayList();
        path.add(ROOT);
        fqName.walk(new FqNameUnsafe.WalkCallback() {
            @Override
            public void segment(@NotNull String shortName, @NotNull FqNameUnsafe fqName) {
                // TODO: do not validate
                path.add(new FqName(fqName));
            }
        });
        return path;
    }

    @NotNull
    public List<String> pathSegments() {
        return fqName.pathSegments();
    }


    @NotNull
    public static FqName topLevel(@NotNull String shortName) {
        return new FqName(FqNameUnsafe.topLevel(shortName));
    }


    @Override
    public String toString() {
        return fqName.toString();
    }

    @Override
    public boolean equals(Object o) {
        // generated by Idea
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FqName that = (FqName) o;

        if (fqName != null ? !fqName.equals(that.fqName) : that.fqName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // generated by Idea
        return fqName != null ? fqName.hashCode() : 0;
    }
}
