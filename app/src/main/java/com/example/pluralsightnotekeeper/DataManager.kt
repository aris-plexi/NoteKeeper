package com.example.pluralsightnotekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }

    private fun initializeNotes() {
        var note = NoteInfo(courses["android_intents"], "Dynamic intent resolution",
        "Wow, intents allow components to be resolved at runtime")
        notes.add(note)

        note = NoteInfo(courses["android_async"], "Service default threads",
        "Did you know that by default an Android Service will tie up the UI thread?")
        notes.add(note)
    }

    private fun initializeCourses() {
        var course = CourseInfo("android_intents", "Android Programming with Intents")
        courses.set(course.courseId, course)

        course = CourseInfo(courseId = "android_async", title = "Android Async Programming and Services")
        courses.set(course.courseId, course)

        course = CourseInfo(title = "Java Fundamentals: The Java Language", courseId = "java_lang")
        courses.set(course.courseId, course)

        course = CourseInfo("java_core", "Java Fundamentals: The Core Platform")
        courses.set(course.courseId, course)

    }
}