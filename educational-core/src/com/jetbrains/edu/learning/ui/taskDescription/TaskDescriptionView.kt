package com.jetbrains.edu.learning.ui.taskDescription

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.SeparatorComponent
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.jetbrains.edu.learning.EduSettings
import com.jetbrains.edu.learning.EduUtils
import com.jetbrains.edu.learning.checker.CheckResult
import com.jetbrains.edu.learning.courseFormat.tasks.Task
import com.jetbrains.edu.learning.editor.EduFileEditorManagerListener
import com.jetbrains.edu.learning.ui.taskDescription.check.CheckPanel
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel


class TaskDescriptionView(val project: Project) : SimpleToolWindowPanel(true, true), DataProvider, Disposable {
  private lateinit var checkPanel: CheckPanel
  private lateinit var taskTextTW : TaskDescriptionToolWindow
  private lateinit var taskTextPanel : JComponent
  var currentTask: Task? = null

  fun init() {
    val panel = JPanel()
    panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

    taskTextTW = if (EduUtils.hasJavaFx() && EduSettings.getInstance().shouldUseJavaFx()) JavaFxToolWindow() else SwingToolWindow()
    taskTextPanel = taskTextTW.createTaskInfoPanel(project)
    panel.addWithLeftAlignment(taskTextPanel)
    panel.addWithLeftAlignment(SeparatorComponent(10, 15))

    val bottomPanel = JPanel(BorderLayout())
    bottomPanel.border = JBUI.Borders.empty(0, 15, 15, 15)
    checkPanel = CheckPanel()
    bottomPanel.add(checkPanel, BorderLayout.NORTH)
    panel.addWithLeftAlignment(bottomPanel)

    UIUtil.setBackgroundRecursively(panel, EditorColorsManager.getInstance().globalScheme.defaultBackground)

    setContent(panel)

    project.messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                                           EduFileEditorManagerListener(taskTextTW, project))
//    val task = EduUtils.getCurrentTask(project)
//    setCurrentTask(project, task)
  }

  fun checkStarted() {
    checkPanel.checkStarted()
  }

  fun checkFinished(checkResult: CheckResult) {
    checkPanel.checkFinished(checkResult)
  }

  private fun JPanel.addWithLeftAlignment(component: JComponent) {
    add(component)
    component.alignmentX = Component.LEFT_ALIGNMENT
  }

  override fun dispose() {

  }

  fun setTaskText(task: Task?) {
    taskTextTW.setTaskText(project, task)
  }

  companion object {

    @JvmStatic
    fun getInstance(project: Project): TaskDescriptionView {
      if (!EduUtils.isStudyProject(project)) {
        error("Attempt to get TaskDescriptionView for non-edu project")
      }
      return ServiceManager.getService(project, TaskDescriptionView::class.java)
    }
  }
}