package com.jetbrains.edu.python.learning.checkio;

import com.jetbrains.edu.learning.LanguageSettings;
import com.jetbrains.edu.learning.courseFormat.Course;
import com.jetbrains.edu.learning.newproject.CourseProjectGenerator;
import com.jetbrains.edu.python.learning.PyCourseBuilder;
import com.jetbrains.edu.python.learning.checkio.newProject.PyCheckiOCourseProjectGenerator;
import com.jetbrains.python.newProject.PyNewProjectSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PyCheckiOCourseBuilder extends PyCourseBuilder {
  @Nullable
  @Override
  public String getTaskTemplateName() {
    return null;
  }

  @Nullable
  @Override
  public String getTestTemplateName() {
    return null;
  }

  @NotNull
  @Override
  public LanguageSettings<PyNewProjectSettings> getLanguageSettings() {
    return new PyCheckiOLanguageSettings();
  }

  @Nullable
  @Override
  public CourseProjectGenerator<PyNewProjectSettings> getCourseProjectGenerator(@NotNull Course course) {
    return new PyCheckiOCourseProjectGenerator(this, course);
  }
}
