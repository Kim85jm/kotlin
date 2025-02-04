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

package org.jetbrains.jet.plugin.refactoring;

import com.intellij.codeInsight.unwrap.ScopeHighlighter;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupAdapter;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.resolve.java.AnalyzerFacadeForJVM;
import org.jetbrains.jet.lang.types.lang.JetStandardLibrary;
import org.jetbrains.jet.lang.types.JetType;
import org.jetbrains.jet.lang.types.NamespaceType;
import org.jetbrains.jet.lang.types.checker.JetTypeChecker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Alefas
 * Date: 25.01.12
 */
public class JetRefactoringUtil {

    private JetRefactoringUtil() {
    }

    public interface SelectExpressionCallback {
        void run(@Nullable JetExpression expression);
    }

    public static void selectExpression(@NotNull Editor editor,
                                        @NotNull PsiFile file,
                                        @NotNull SelectExpressionCallback callback) throws IntroduceRefactoringException {
        if (editor.getSelectionModel().hasSelection()) {
            int selectionStart = editor.getSelectionModel().getSelectionStart();
            int selectionEnd = editor.getSelectionModel().getSelectionEnd();
            String text = file.getText();
            while (selectionStart < selectionEnd && Character.isSpaceChar(text.charAt(selectionStart))) ++selectionStart;
            while (selectionStart < selectionEnd && Character.isSpaceChar(text.charAt(selectionEnd - 1))) --selectionEnd;
            callback.run(findExpression(editor, file, selectionStart, selectionEnd));
        } else {
            int offset = editor.getCaretModel().getOffset();
            smartSelectExpression(editor, file, offset, callback);
        }
    }

    private static void smartSelectExpression(@NotNull Editor editor, @NotNull PsiFile file, int offset,
                                             @NotNull final SelectExpressionCallback callback)
            throws IntroduceRefactoringException {
        if (offset < 0) throw new IntroduceRefactoringException(JetRefactoringBundle.message("cannot.refactor.not.expression"));
        PsiElement element = file.findElementAt(offset);
        if (element == null) throw new IntroduceRefactoringException(JetRefactoringBundle.message("cannot.refactor.not.expression"));
        if (element instanceof PsiWhiteSpace) {
            smartSelectExpression(editor, file, offset - 1, callback);
            return;
        }
        ArrayList<JetExpression> expressions = new ArrayList<JetExpression>();
        while (element != null && !(element instanceof JetBlockExpression) && !(element instanceof JetNamedFunction)
               && !(element instanceof JetClassBody) && !(element instanceof JetSecondaryConstructor)) {
            if (element instanceof JetExpression && !(element instanceof JetStatementExpression)) {
                boolean addExpression = true;
                if (element.getParent() instanceof JetQualifiedExpression) {
                    JetQualifiedExpression qualifiedExpression = (JetQualifiedExpression) element.getParent();
                    if (qualifiedExpression.getReceiverExpression() != element) {
                        addExpression = false;
                    }
                } else if (element.getParent() instanceof JetCallElement) {
                    addExpression = false;
                } else if (element.getParent() instanceof JetOperationExpression) {
                    JetOperationExpression operationExpression = (JetOperationExpression) element.getParent();
                    if (operationExpression.getOperationReference() == element) {
                        addExpression = false;
                    }
                }
                if (addExpression) {
                    JetExpression expression = (JetExpression) element;
                    BindingContext bindingContext = AnalyzerFacadeForJVM.analyzeFileWithCache((JetFile) expression.getContainingFile(),
                            AnalyzerFacadeForJVM.SINGLE_DECLARATION_PROVIDER)
                                .getBindingContext();
                    JetType expressionType = bindingContext.get(BindingContext.EXPRESSION_TYPE, expression);
                    if (expressionType == null || !(expressionType instanceof NamespaceType) &&
                                                  !JetTypeChecker.INSTANCE.equalTypes(JetStandardLibrary.
                                                          getInstance().getTuple0Type(), expressionType)) {
                        expressions.add(expression);
                    }
                }
            }
            element = element.getParent();
        }
        if (expressions.size() == 0) throw new IntroduceRefactoringException(JetRefactoringBundle.message("cannot.refactor.not.expression"));

        final DefaultListModel model = new DefaultListModel();
        for (JetExpression expression : expressions) {
            model.addElement(expression);
        }

        final ScopeHighlighter highlighter = new ScopeHighlighter(editor);

        final JList list = new JBList(model);
        
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component rendererComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                StringBuilder buffer = new StringBuilder();
                JetExpression element = (JetExpression) value;
                if (element.isValid()) {
                    setText(getExpressionShortText(element));
                }
                return rendererComponent;
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                highlighter.dropHighlight();
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex < 0) return;
                JetExpression expression = (JetExpression) model.get(selectedIndex);
                ArrayList<PsiElement> toExtract = new ArrayList<PsiElement>();
                toExtract.add(expression);
                highlighter.highlight(expression, toExtract);
            }
        });

        JBPopupFactory.getInstance().createListPopupBuilder(list).
                setTitle(JetRefactoringBundle.message("expressions.title")).setMovable(false).setResizable(false).
                setRequestFocus(true).setItemChoosenCallback(new Runnable() {
            @Override
            public void run() {
                callback.run((JetExpression) list.getSelectedValue());
            }
        }).addListener(new JBPopupAdapter() {
            @Override
            public void onClosed(LightweightWindowEvent event) {
                highlighter.dropHighlight();
            }
        }).createPopup().showInBestPositionFor(editor);
        
    }

    public static String getExpressionShortText(@NotNull JetExpression expression) { //todo: write appropriate implementation
        String expressionText = expression.getText();
        if (expressionText.length() > 20) {
            expressionText = expressionText.substring(0, 17) + "...";
        }
        return expressionText;
    }

    @Nullable
    private static JetExpression findExpression(@NotNull Editor editor, @NotNull PsiFile file,
                                               int startOffset, int endOffset) throws IntroduceRefactoringException{
        PsiElement element = PsiTreeUtil.findElementOfClassAtRange(file, startOffset, endOffset, JetExpression.class);
        if (element == null || element.getTextRange().getStartOffset() != startOffset ||
            element.getTextRange().getEndOffset() != endOffset) {
            //todo: if it's infix expression => add (), then commit document then return new created expression
            throw new IntroduceRefactoringException(JetRefactoringBundle.message("cannot.refactor.not.expression"));
        } else if (!(element instanceof JetExpression)) {
            throw new IntroduceRefactoringException(JetRefactoringBundle.message("cannot.refactor.not.expression"));
        } else if (element instanceof JetBlockExpression) {
            List<JetElement> statements = ((JetBlockExpression) element).getStatements();
            if (statements.size() == 1) {
                JetElement elem = statements.get(0);
                if (elem.getText().equals(element.getText()) && elem instanceof JetExpression) {
                    return (JetExpression) elem;
                }
            }
        }
        return (JetExpression) element;
    }

    public static class IntroduceRefactoringException extends Exception {
        private String myMessage;

        public IntroduceRefactoringException(String message) {
            myMessage = message;
        }

        public String getMessage() {
            return myMessage;
        }
    }

}
