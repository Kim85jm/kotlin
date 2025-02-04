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

package org.jetbrains.jet.di;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
* @author abreslav
*/
public class DependencyInjectorGenerator {

    public static final String INDENT_STEP = "    ";
    private static final String LOCK_NAME = "__lock__";

    private final boolean lazy;
    private final Multimap<DiType, Field> typeToField = HashMultimap.create();
    private final Set<Field> satisfied = Sets.newHashSet();
    private final Set<Field> fields = Sets.newLinkedHashSet();
    private final Set<Parameter> parameters = Sets.newLinkedHashSet();

    private final Set<Field> backsParameter = Sets.newHashSet();

    public DependencyInjectorGenerator(boolean lazy) {
        this.lazy = lazy;
    }

    public void generate(String targetSourceRoot, String injectorPackageName, String injectorClassName) throws IOException {
        String outputFileName = targetSourceRoot + "/" + injectorPackageName.replace(".", "/") + "/" + injectorClassName + ".java";

        File file = new File(outputFileName);
        File tmpfile = new File(outputFileName + ".tmp");
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (parentFile.mkdirs()) {
                System.out.println("Directory created: " + parentFile.getAbsolutePath());
            }
            else {
                throw new IllegalStateException("Cannot create directory: " + parentFile);
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(tmpfile);
        System.out.println("File opened: " + tmpfile.getAbsolutePath());

        PrintStream out = new PrintStream(fileOutputStream);
        try {
            for (Field field : Lists.newArrayList(fields)) {
                satisfyDependenciesFor(field, field);
            }

            String copyright = "injector-generator/copyright.txt";
            out.println(FileUtil.loadFile(new File(copyright)));
            out.println();

            out.println("package " + injectorPackageName + ";");
            out.println();

            generateImports(out, injectorPackageName);
            out.println();

            out.println("/* This file is generated by " + AllInjectorsGenerator.class.getName() + ". DO NOT EDIT! */");
            out.println("public class " + injectorClassName + " {");
// Needed for double-checked locking
//            out.println();
//            out.println("    private static final Object " + LOCK_NAME + " = new Object();");
            out.println();
            generateFields(out);
            out.println();
            generateConstructor(injectorClassName, out);
            out.println();
            generateGetters(out);
// Needed to fix double-checked locking
//            out.println();
//            generateMakeFunction(out);
            out.println("}"); // class

            fileOutputStream.close();

            if (!tmpfile.renameTo(file)) {
                throw new RuntimeException("failed to rename " + tmpfile + " to " + file);
            }
            System.out.println("Renamed " + tmpfile + " to " + file);
        }
        finally {
            fileOutputStream.close();
            System.out.println("File closed");
        }
    }

    public void addPublicParameter(Class<?> type) {
        addPublicParameter(new DiType(type));
    }

    public void addPublicParameter(DiType type) {
        addPublicParameter(type, true);
    }

    public void addPublicParameter(DiType type, boolean required) {
        addParameter(true, type, var(type), required);
    }


    public void addParameter(Class<?> type) {
        addParameter(DiType.fromReflectionType(type));
    }

    public void addParameter(DiType type) {
        addParameter(type, true);
    }

    public void addParameter(Class<?> type, boolean required) {
        addParameter(new DiType(type), required);
    }

    public void addParameter(DiType type, boolean required) {
        addParameter(false, type, var(type), required);
    }



    public void addParameter(boolean reexport, @NotNull DiType type, @Nullable String name, boolean required) {
        Field field = addField(reexport, type, name, null);
        Parameter parameter = new Parameter(type, name, field, required);
        parameters.add(parameter);
        field.setInitialization(new ParameterExpression(parameter));
        backsParameter.add(field);
        typeToField.put(type, field);
    }

    public Field addPublicField(Class<?> type) {
        return addPublicField(new DiType(type));
    }

    public Field addPublicField(DiType type) {
        return addField(true, type, null, null);
    }

    public Field addField(Class<?> type) {
        return addField(new DiType(type));
    }

    public Field addField(DiType type) {
        return addField(false, type, null, null);
    }

    public Field addField(boolean isPublic, Class<?> type, @Nullable String name, @Nullable Expression init) {
        return addField(isPublic, new DiType(type), name, init);
    }

    public Field addField(boolean isPublic, DiType type, @Nullable String name, @Nullable Expression init) {
        Field field = Field.create(isPublic, type, name == null ? var(type) : name, init);
        fields.add(field);
        typeToField.put(type, field);
        return field;
    }

    private void generateImports(PrintStream out, String injectorPackageName) {
        for (Field field : fields) {
            generateImportDirectives(out, field.getType(), injectorPackageName);
        }
        for (Parameter parameter : parameters) {
            generateImportDirectives(out, parameter.getType(), injectorPackageName);
        }
        for (Parameter parameter : parameters) {
            if (parameter.isRequired()) {
                generateImportDirective(out, NotNull.class, injectorPackageName);
                break;
            }
        }
    }

    private void generateImportDirectives(PrintStream out, DiType type, String injectorPackageName) {
        generateImportDirective(out, type.getClazz(), injectorPackageName);
        for (DiType typeParameter : type.getTypeParameters()) {
            generateImportDirectives(out, typeParameter, injectorPackageName);
        }
    }

    private void generateImportDirective(PrintStream out, Class<?> type, String injectorPackageName) {
        if (type.isPrimitive()) return;
        String importedPackageName = type.getPackage().getName();
        if ("java.lang".equals(importedPackageName)
            || injectorPackageName.equals(importedPackageName)) {
            return;
        }
        out.println("import " + type.getCanonicalName() + ";");
    }

    private void generateFields(PrintStream out) {
        for (Field field : fields) {
            if (lazy || field.isPublic()) {
                String _final = backsParameter.contains(field) ? "final " : "";
                out.println("    private " + _final + field.getType().getSimpleName() + " " + field.getName() + ";");
            }
        }
    }

    private void generateConstructor(String injectorClassName, PrintStream out) {
        String indent = "        ";

        // Constructor parameters
        if (parameters.isEmpty()) {
            out.println("    public " + injectorClassName + "() {");
        }
        else {
            out.println("    public " + injectorClassName + "(");
            for (Iterator<Parameter> iterator = parameters.iterator(); iterator.hasNext(); ) {
                Parameter parameter = iterator.next();
                out.print(indent);
                if (parameter.isRequired()) {
                    out.print("@NotNull ");
                }
                out.print(parameter.getType().getSimpleName() + " " + parameter.getName());
                if (iterator.hasNext()) {
                    out.println(",");
                }
            }
            out.println("\n    ) {");
        }

        if (lazy) {
            // Remember parameters
            for (Parameter parameter : parameters) {
                out.println(indent + "this." + parameter.getField().getName() + " = " + parameter.getName() + ";");
            }
        }
        else {
            // Initialize fields
            for (Field field : fields) {
                if (!backsParameter.contains(field) || field.isPublic()) {
                    String prefix = field.isPublic() ? "this." : field.getTypeName() + " ";
                    out.println(indent + prefix + field.getName() + " = " + field.getInitialization() + ";");
                }
            }
            out.println();

            // Call setters
            for (Field field : fields) {
                for (SetterDependency dependency : field.getDependencies()) {
                    String prefix = field.isPublic() ? "this." : "";
                    out.println(indent + prefix + dependency.getDependent().getName() + "." + dependency.getSetterName() + "(" + dependency.getDependency().getName() + ");");
                }
                if (!field.getDependencies().isEmpty()) {
                    out.println();
                }
            }

            // call @PostConstruct
            for (Field field : fields) {
                // TODO: type of field may be different from type of object
                List<Method> postConstructMethods = getPostConstructMethods(field.getType().getClazz());
                for (Method postConstruct : postConstructMethods) {
                    out.println(indent + field.getName() + "." + postConstruct.getName() + "();");
                }
                if (postConstructMethods.size() > 0) {
                    out.println();
                }
            }
        }

        out.println("    }");
    }

    private static List<Method> getPostConstructMethods(Class<?> clazz) {
        List<Method> r = Lists.newArrayList();
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(PostConstruct.class) != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalStateException("@PostConstruct method must have no arguments: " + method);
                }
                r.add(method);
            }
        }
        return r;
    }

    private void generateGetters(PrintStream out) {
        String indent0 = "    ";
        String indent1 = indent0 + INDENT_STEP;
        String indent2 = indent1 + INDENT_STEP;
        String indent3 = indent2 + INDENT_STEP;
        String indent4 = indent3 + INDENT_STEP;
        for (Field field : fields) {
            if (!lazy && !field.isPublic()) continue;
            String visibility = field.isPublic() ? "public" : "private";
            out.println(indent0 + visibility + " " + field.getTypeName() + " " + field.getGetterName() + "() {");

            if (lazy && !backsParameter.contains(field)) {
                Expression initialization = field.getInitialization();
                assert initialization != null : field;

                // Double-checked locking
                out.println(indent1 + "if (this." + field.getName() + " == null) {");

                out.println(indent2 + "this." + field.getName() + " = " + initialization + ";");
                // Invoke setters
                for (SetterDependency dependency : field.getDependencies()) {
                    out.println(indent2 + "this." + field.getName() + "." + dependency.getSetterName() + "(" + dependency.getDependency().getGetterName() + "());");
                }

                out.println(indent1 + "}"); // Outer if

                /*
                // Double-checked locking
                out.println(indent1 + "if (this." + field.getName() + " == null) {");
                out.println(indent2 + "synchronized (" + LOCK_NAME + ") {");
                out.println(indent3 + "if (this." + field.getName() + " == null) {");

                out.println(indent4 + "this." + field.getName() + " = " + initialization + ";");
                // Invoke setters
                for (SetterDependency dependency : field.getDependencies()) {
                    out.println(indent4 + "this." + field.getName() + "." + dependency.getSetterName() + "(" + dependency.getDependency().getGetterName() + "());");
                }

                out.println(indent3 + "}"); // Inner if
                out.println(indent2 + "}"); // synchronized
                out.println(indent1 + "}"); // Outer if
                */
            }

            out.println(indent1 + "return this." + field.getName() + ";");
            out.println(indent0 + "}");
            out.println();
        }
    }

    private void satisfyDependenciesFor(Field field, Field neededFor) {
        if (!satisfied.add(field)) return;
        if (backsParameter.contains(field)) return;

        if (field.getInitialization() == null) {
            initializeByConstructorCall(field, neededFor);
        }

        // Sort setters in order to get deterministic behavior
        List<Method> declaredMethods = Lists.newArrayList(field.getType().getClazz().getDeclaredMethods());
        Collections.sort(declaredMethods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (Method method : declaredMethods) {
            if (method.getAnnotation(javax.inject.Inject.class) == null
                || !method.getName().startsWith("set")
                || method.getParameterTypes().length != 1) continue;

            Type parameterType = method.getGenericParameterTypes()[0];


            Field dependency = findDependencyOfType(DiType.fromReflectionType(parameterType), field + ": " + method + ": " + fields, field);

            field.getDependencies().add(new SetterDependency(field, method.getName(), dependency));
        }
    }

    private Field findDependencyOfType(DiType parameterType, String errorMessage, Field neededFor) {
        List<Field> fields = Lists.newArrayList();
        for (Map.Entry<DiType, Field> entry : typeToField.entries()) {
            if (parameterType.isAssignableFrom(entry.getKey())) {
                fields.add(entry.getValue());
            }
        }
        
        Field dependency;
        if (fields.isEmpty()) {
            
            if (parameterType.getClazz().isPrimitive() || parameterType.getClazz().getPackage().getName().equals("java.lang")) {
                throw new IllegalArgumentException(
                        "cannot declare magic field of type " + parameterType + ": " + errorMessage);
            }
            
            dependency = addField(parameterType);
            satisfyDependenciesFor(dependency, neededFor);
        }
        else if (fields.size() == 1) {
            dependency = fields.iterator().next();
        }
        else {
            throw new IllegalArgumentException("Ambiguous dependency: " + errorMessage);
        }
        return dependency;
    }

    private void initializeByConstructorCall(Field field, Field neededFor) {
        DiType type = field.getType();

        if (type.getClazz().isInterface()) {
            throw new IllegalArgumentException("cannot instantiate interface: " + type.getClazz().getName() + " needed for " + neededFor);
        }
        if (Modifier.isAbstract(type.getClazz().getModifiers())) {
            throw new IllegalArgumentException("cannot instantiate abstract class: " + type.getClazz().getName() + " needed for " + neededFor);
        }

        // Note: projections are not computed here

        // Look for constructor
        Constructor<?>[] constructors = type.getClazz().getConstructors();
        if (constructors.length == 0 || !Modifier.isPublic(constructors[0].getModifiers())) {
            throw new IllegalArgumentException("No constructor: " + type.getClazz().getName() + " needed for " + neededFor);
        }
        Constructor<?> constructor = constructors[0];

        // Find arguments
        ConstructorCall dependency = new ConstructorCall(constructor);
        Type[] parameterTypes = constructor.getGenericParameterTypes();
        for (Type parameterType : parameterTypes) {
            Field fieldForParameter = findDependencyOfType(DiType.fromReflectionType(parameterType), "constructor: " + constructor + ", parameter: " + parameterType, field);
            dependency.getConstructorArguments().add(fieldForParameter);
        }

        field.setInitialization(dependency);
    }

    private String var(@NotNull DiType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.decapitalize(type.getClazz().getSimpleName()));
        if (type.getTypeParameters().size() > 0) {
            sb.append("Of");
        }
        for (DiType parameter : type.getTypeParameters()) {
            sb.append(StringUtil.capitalize(var(parameter)));
        }
        return sb.toString();
    }

    private void generateMakeFunction(PrintStream out) {
        out.println("    private static <T> T make(Class<T> theClass) {");
        out.println("        try {                                     ");
        out.println("            return theClass.newInstance();        ");
        out.println("        }                                         ");
        out.println("        catch (InstantiationException e) {        ");
        out.println("            throw new IllegalStateException(e);   ");
        out.println("        }                                         ");
        out.println("        catch (IllegalAccessException e) {        ");
        out.println("            throw new IllegalStateException(e);   ");
        out.println("        }                                         ");
        out.println("    }                                             ");
    }
}
