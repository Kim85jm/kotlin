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

package org.jetbrains.k2js.test;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.JetTestUtils;
import org.jetbrains.jet.compiler.JetCoreEnvironment;

/**
 * @author Pavel Talanov
 */
public abstract class TestWithEnvironment extends UsefulTestCase {

    @Nullable
    protected JetCoreEnvironment myEnvironment;

    @NotNull
    public Project getProject() {
        assert myEnvironment != null : "Environment should be created beforehand.";
        return myEnvironment.getProject();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createEnvironmentWithMockJdk();
    }

    @Override
    protected void tearDown() throws Exception {
        myEnvironment = null;
        super.tearDown();
    }

    protected void createEnvironmentWithMockJdk() {
        myEnvironment = JetTestUtils.createEnvironmentWithMockJdk(getTestRootDisposable());
    }
}
