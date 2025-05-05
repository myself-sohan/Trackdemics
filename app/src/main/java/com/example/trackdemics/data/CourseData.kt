package com.example.trackdemics.data

object AllCourseData {

    data class SemesterCourses(
        val semester: Int,
        val branch: String,
        val courses: List<CourseWithSchedule>
    )

    data class CourseWithSchedule(
        val code: String,
        val name: String,
        val schedule: WeeklySchedule,
        val location: String? = null
    )

    data class WeeklySchedule(
        val monday: ClassTiming? = null,
        val tuesday: ClassTiming? = null,
        val wednesday: ClassTiming? = null,
        val thursday: ClassTiming? = null,
        val friday: ClassTiming? = null
    )

    data class ClassTiming(
        val startTime: String,
        val endTime: String
    )

    val semester2CSE = SemesterCourses(
        semester = 2,
        branch = "CSE",
        courses = listOf(
            CourseWithSchedule(
                "MA102", "Engineering Mathematics-II", WeeklySchedule(
                    monday = ClassTiming("09:00", "09:55"),
                    tuesday = ClassTiming("10:00", "10:55"),
                    thursday = ClassTiming("09:00", "09:55"),
                    friday = ClassTiming("10:00", "10:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "CB102", "Environmental Science", WeeklySchedule(
                    monday = ClassTiming("10:00", "10:55"),
                    friday = ClassTiming("09:00", "09:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "CS152", "Python Programming", WeeklySchedule(
                    monday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("13:00", "16:55")
                ), "LAB"
            ),
            CourseWithSchedule(
                "ME152", "Workshop Practice", WeeklySchedule(
                    tuesday = ClassTiming("13:00", "16:55")
                ), "Workshop"
            ),
            CourseWithSchedule(
                "VA102", "Skill Development & Prototyping", WeeklySchedule(
                    monday = ClassTiming("13:00", "16:55"),
                ), "LAB"
            ),
            CourseWithSchedule(
                "EE102", "Basic Electrical & Electronics Engineering", WeeklySchedule(
                    tuesday = ClassTiming("09:00", "09:55"),
                    wednesday = ClassTiming("10:00", "10:55"),
                    thursday = ClassTiming("10:00", "10:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "EE152", "Basic Electrical & Electronics Lab", WeeklySchedule(
                    thursday = ClassTiming("14:00", "17:55")
                ), "LAB"
            ),
            CourseWithSchedule(
                "PH101", "Engineering Chemistry", WeeklySchedule(
                    wednesday = ClassTiming("09:00", "09:55"),
                    thursday = ClassTiming("13:00", "13:55"),
                    friday = ClassTiming("11:00", "11:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "PH151", "Engineering Physics Lab", WeeklySchedule(
                    friday = ClassTiming("14:00", "17:55")
                ), "LAB"
            ),
            CourseWithSchedule(
                "HS102", "Creativity, Innovation and Entrepreneurship", WeeklySchedule(
                    tuesday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("11:00", "11:55"),
                    thursday = ClassTiming("11:00", "11:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "HS104", "Ethics and Morals", WeeklySchedule(
                    wednesday = ClassTiming("11:00", "11:55"),
                    friday = ClassTiming("13:00", "13:55")
                ), "LH 1"
            ),
        )
    )

    val semester4CSE = SemesterCourses(
        semester = 4,
        branch = "CSE",
        courses = listOf(
            CourseWithSchedule(
                "CS220", "Principles of Programming Languages", WeeklySchedule(
                    monday = ClassTiming("10:00", "10:55"),
                    tuesday = ClassTiming("10:00", "10:55"),
                    wednesday = ClassTiming("10:00", "10:55")
                ), "CA-301"
            ),
            CourseWithSchedule(
                "CS204", "Object Oriented Programming and Design", WeeklySchedule(
                    tuesday = ClassTiming("11:00", "12:55"),
                    thursday = ClassTiming("11:00", "12:55")
                ), "CA-301"
            ),
            CourseWithSchedule(
                "CS206", "Data Communication", WeeklySchedule(
                    monday = ClassTiming("12:00", "12:55"),
                    wednesday = ClassTiming("14:00", "14:55"),
                    friday = ClassTiming("12:00", "12:55")
                ), "CA-301"
            ),
            CourseWithSchedule(
                "CS202", "Computer Organization", WeeklySchedule(
                    monday = ClassTiming("14:00", "15:55"),
                    tuesday = ClassTiming("15:00", "16:55"),
                ), "CA-301"
            ),
            CourseWithSchedule(
                "CS212", "Analysis and Design of Algorithms", WeeklySchedule(
                    wednesday = ClassTiming("15:00", "15:55"),
                    thursday = ClassTiming("10:00", "10:55"),
                    friday = ClassTiming("14:00", "14:55")
                ), "CA-301"
            ),
            CourseWithSchedule(
                "CS252", "Computer Organization Lab", WeeklySchedule(
                    friday = ClassTiming("10:00", "11:55")
                ), "LAB-1"
            ),
            CourseWithSchedule(
                "CS254", "Object Oriented Programming and Design Lab", WeeklySchedule(
                    thursday = ClassTiming("14:00", "16:55")
                ), "LAB-2"
            ),
            CourseWithSchedule(
                "CS256", "Data Communication Lab", WeeklySchedule(
                    wednesday = ClassTiming("11:00", "12:55")
                ), "LAB-1"
            ),
            CourseWithSchedule(
                "OE-1", "Open Elective 1", WeeklySchedule(
                    tuesday = ClassTiming("14:00", "14:55"),
                    friday = ClassTiming("15:00", "15:55")
                ), "CA-301"
            )
        )
    )
    val semester6CSE = SemesterCourses(
        semester = 6,
        branch = "CSE",
        courses = listOf(
            CourseWithSchedule(
                "CS302", "Software Engineering", WeeklySchedule(
                    monday = ClassTiming("10:00", "11:55"),
                    tuesday = ClassTiming("11:00", "11:55"),
                    friday = ClassTiming("11:00", "11:55")
                ), "CA-302"
            ),
            CourseWithSchedule(
                "CS304", "Compiler Design", WeeklySchedule(
                    monday = ClassTiming("15:00", "15:55"),
                    tuesday = ClassTiming("10:00", "10:55"),
                    wednesday = ClassTiming("12:00", "12:55"),
                    thursday = ClassTiming("12:00", "12:55")
                ), "CA-302"
            ),
            CourseWithSchedule(
                "CS312", "Computer Graphics", WeeklySchedule(
                    tuesday = ClassTiming("16:00", "16:55"),
                    thursday = ClassTiming("15:00", "15:55"),
                    friday = ClassTiming("10:00", "10:55")
                ), "CA-302"
            ),
            CourseWithSchedule(
                "CS322", "Cryptography and Network Security", WeeklySchedule(
                    monday = ClassTiming("12:00", "12:55"),
                    wednesday = ClassTiming("15:00", "15:55"),
                    friday = ClassTiming("12:00", "12:55")
                ), "CA-302"
            ),
            CourseWithSchedule(
                "CS352", "Software Engineering Lab", WeeklySchedule(
                    tuesday = ClassTiming("14:00", "15:55")
                ), "LAB-2"
            ),
            CourseWithSchedule(
                "CS354", "Compiler Design Lab", WeeklySchedule(
                    wednesday = ClassTiming("10:00", "11:55")
                ), "LAB-2"
            ),
            CourseWithSchedule(
                "CS382", "Minor Project", WeeklySchedule(
                    thursday = ClassTiming("10:00", "11:55")
                ), "NA"
            ),
            CourseWithSchedule(
                "HS394", "Indian Culture and Civilization", WeeklySchedule(
                    monday = ClassTiming("14:00", "14:55"),
                    wednesday = ClassTiming("14:00", "14:55")
                ), "LH1"
            ),
            CourseWithSchedule(
                "OE-3", "Open Elective 3", WeeklySchedule(
                    tuesday = ClassTiming("12:00", "12:55"),
                    friday = ClassTiming("14:00", "14:55")
                ), "CA-302"
            )
        )
    )

    val semester8CSE = SemesterCourses(
        semester = 8,
        branch = "CSE",
        courses = listOf(
            CourseWithSchedule(
                "CS414", "Cloud Computing", WeeklySchedule(
                    monday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("14:00", "14:55"),
                    friday = ClassTiming("11:00", "11:55")
                ), "CA-202"
            ),
            CourseWithSchedule(
                "CS418", "Natural Language Processing", WeeklySchedule(
                    monday = ClassTiming("14:00", "14:55"),
                    wednesday = ClassTiming("12:00", "12:55"),
                    friday = ClassTiming("15:00", "15:55")
                ), "CA-202"
            ),
            CourseWithSchedule(
                "HS492", "Entrepreneurship", WeeklySchedule(
                    tuesday = ClassTiming("12:00", "12:55"),
                    friday = ClassTiming("12:00", "12:55")
                ), "LH 2"
            ),
        )
    )
    val semester2ECE = SemesterCourses(
        semester = 2,
        branch = "ECE",
        courses = listOf(
            CourseWithSchedule(
                "MA102", "Engineering Mathematics-II", WeeklySchedule(
                    monday = ClassTiming("09:00", "09:55"),
                    tuesday = ClassTiming("10:00", "10:55"),
                    thursday = ClassTiming("09:00", "09:55"),
                    friday = ClassTiming("10:00", "10:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "CB102", "Environmental Science", WeeklySchedule(
                    monday = ClassTiming("10:00", "10:55"),
                    friday = ClassTiming("09:00", "09:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "CS152(T)", "Python Programming", WeeklySchedule(
                    monday = ClassTiming("11:00", "11:55"),
                ), "LAB"
            ),
            CourseWithSchedule(
                "CS152", "Python Programming", WeeklySchedule(
                    monday = ClassTiming("13:00", "16:55"),
                ), "LAB"
            ),
            CourseWithSchedule(
                "ME152", "Workshop Practice", WeeklySchedule(
                    friday = ClassTiming("14:00", "17:55")
                ), "Workshop"
            ),
            CourseWithSchedule(
                "VA102", "Skill Development & Prototyping", WeeklySchedule(
                    thursday = ClassTiming("14:00", "17:55"),
                ), "LAB"
            ),
            CourseWithSchedule(
                "EE102", "Basic Electrical & Electronics Engineering", WeeklySchedule(
                    tuesday = ClassTiming("09:00", "09:55"),
                    wednesday = ClassTiming("10:00", "10:55"),
                    thursday = ClassTiming("10:00", "10:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "EE152", "Basic Electrical & Electronics Lab", WeeklySchedule(
                    tuesday = ClassTiming("13:00", "16:55")
                ), "LAB"
            ),
            CourseWithSchedule(
                "PH101", "Engineering Chemistry", WeeklySchedule(
                    wednesday = ClassTiming("09:00", "09:55"),
                    thursday = ClassTiming("13:00", "13:55"),
                    friday = ClassTiming("11:00", "11:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "PH151", "Engineering Physics Lab", WeeklySchedule(
                    wednesday = ClassTiming("13:00", "16:55")
                ), "LAB"
            ),
            CourseWithSchedule(
                "HS102", "Creativity, Innovation and Entrepreneurship", WeeklySchedule(
                    tuesday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("11:00", "11:55"),
                    thursday = ClassTiming("11:00", "11:55")
                ), "LH 1"
            ),
            CourseWithSchedule(
                "HS104", "Ethics and Morals", WeeklySchedule(
                    wednesday = ClassTiming("11:00", "11:55"),
                    friday = ClassTiming("13:00", "13:55")
                ), "LH 1"
            ),
        )
    )
    val semester4ECE = SemesterCourses(
        semester = 4,
        branch = "ECE",
        courses = listOf(
            CourseWithSchedule(
                code = "EC202",
                name = "Signals & Systems",
                schedule = WeeklySchedule(
                    monday = ClassTiming("16:00", "16:55"),
                    tuesday = ClassTiming("10:00", "10:55"),
                    friday = ClassTiming("10:00", "10:55")
                ),
                location = "CD 202"
            ),
            CourseWithSchedule(
                code = "EC204",
                name = "Electronic Circuits",
                schedule = WeeklySchedule(
                    monday = ClassTiming("10:00", "10:55"),
                    tuesday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("11:00", "12:55"),
                ),
                location = "CD 202"
            ),
            CourseWithSchedule(
                code = "EC212",
                name = "Probability Theory and Stochastic Process",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("12:00", "12:55"),
                    wednesday = ClassTiming("10:00", "10:55")
                ),
                location = "CD 201"
            ),
            CourseWithSchedule(
                code = "EC224",
                name = "Computer Architecture",
                schedule = WeeklySchedule(
                    monday = ClassTiming("14:00", "15:55"),
                    tuesday = ClassTiming("09:00", "09:55")
                ),
                location = "CD 202"
            ),
            CourseWithSchedule(
                code = "EC206",
                name = "Microprocessor and Micro Controller",
                schedule = WeeklySchedule(
                    thursday = ClassTiming("16:00", "16:55"),
                    friday = ClassTiming("11:00", "12:55")
                ),
                location = "CD 202"
            ),
            CourseWithSchedule(
                code = "EC252",
                name = "Signals and Systems Lab",
                schedule = WeeklySchedule(
                    thursday = ClassTiming("11:00", "12:55")
                ),
                location = "LAB"
            ),
            CourseWithSchedule(
                code = "EC254",
                name = "Electronic Circuits Lab",
                schedule = WeeklySchedule(
                    monday = ClassTiming("11:00", "12:55")
                ),
                location = "LAB"
            ),
            CourseWithSchedule(
                code = "EC256",
                name = "Microcontroller & Microprocessor",
                schedule = WeeklySchedule(
                    thursday = ClassTiming("09:00", "10:55")
                ),
                location = "LAB"
            ),
            CourseWithSchedule(
                code = "OE-1",
                name = "Open Elective 1",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("14:00", "14:55"),
                    friday = ClassTiming("15:00", "15:55")
                ),
                location = "CD 201"
            )
        )
    )
    val semester6ECE = SemesterCourses(
        semester = 6,
        branch = "ECE",
        courses = listOf(
            CourseWithSchedule(
                code = "EC302",
                name = "Digital and Analog Integrated Circuits",
                location = "CD 202",
                schedule = WeeklySchedule(
                    monday = ClassTiming("11:00", "11:55"),
                    tuesday = ClassTiming("14:00", "14:55"),
                    wednesday = ClassTiming("14:00", "14:55")
                )
            ),
            CourseWithSchedule(
                code = "EC304",
                name = "RF and Microwave Engineering",
                location = "CD 201",
                schedule = WeeklySchedule(
                    monday = ClassTiming("15:00", "16:55"),
                    tuesday = ClassTiming("15:00", "15:55"),
                    wednesday = ClassTiming("12:00", "12:55")
                )
            ),
            CourseWithSchedule(
                code = "EC310",
                name = "Artificial Neural Networks & Applications",
                location = "CD 202",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("10:00", "10:55"),
                    thursday = ClassTiming("16:00", "16:55"),
                    friday = ClassTiming("10:00", "11:55")
                )
            ),
            CourseWithSchedule(
                code = "EC314",
                name = "Data Communication & Networks",
                location = "CD 201",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("11:00", "11:55"),
                    wednesday = ClassTiming("10:00", "11:55"),
                    thursday = ClassTiming("11:00", "12:55"),
                )
            ),
            CourseWithSchedule(
                code = "EC352",
                name = "Digital and Analog Integrated Circuits Lab",
                location = "Lab",
                schedule = WeeklySchedule(
                    thursday = ClassTiming("14:00", "15:55")
                )
            ),
            CourseWithSchedule(
                code = "EC354",
                name = "RF and Microwave Engineering Lab",
                location = "Lab",
                schedule = WeeklySchedule(
                    wednesday = ClassTiming("15:00", "16:55")
                )
            ),
            CourseWithSchedule(
                code = "EC320",
                name = "Control Systems",
                location = "CD 202",
                schedule = WeeklySchedule(
                    monday = ClassTiming("10:00", "10:55"),
                    tuesday = ClassTiming("16:00", "16:55")
                )
            ),
            CourseWithSchedule(
                code = "OE-3",
                name = "Open Elective 3",
                location = "CD-202",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("12:00", "12:55"),
                    friday = ClassTiming("14:00", "14:55")
                )
            ),
            CourseWithSchedule(
                code = "HS394",
                name = "Indian Culture and Civilization",
                location = "LH 1",
                schedule = WeeklySchedule(
                    monday = ClassTiming("14:00", "14:55"),
                    wednesday = ClassTiming("14:00", "14:55")
                )
            )
        )
    )
    val semester8ECE = SemesterCourses(
        semester = 8,
        branch = "ECE",
        courses = listOf(
            CourseWithSchedule(
                code = "EC410",
                name = "Pattern Recognition and Applications (NPTEL)",
                schedule = WeeklySchedule(
                    monday = ClassTiming("12:00", "12:55"),
                    tuesday = ClassTiming("11:00", "11:55"),
                ),
                location = "Online"
            ),
            CourseWithSchedule(
                code = "EC414",
                name = "VLSI Design Flow: RTL to GDS (NPTEL)",
                schedule = WeeklySchedule(
                    thursday = ClassTiming("10:00", "11:55"),
                ),
                location = "LH 3"
            ),
            CourseWithSchedule(
                code = "HS492",
                name = "Entrepreneurship",
                schedule = WeeklySchedule(
                    tuesday = ClassTiming("12:00", "12:55"),
                    friday = ClassTiming("12:00", "12:55")
                ),
                location = "LH 2"
            )
        )
    )


    val allSemesters = listOf(
        semester2CSE,
        semester4CSE,
        semester6CSE,
        semester8CSE,
        semester2ECE,
        semester4ECE,
        semester6ECE,
        semester8ECE
    )
}
