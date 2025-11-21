package com.example.trackdemics.data

object AllBusData {

    data class Route(
        val routeNumber: Int,
        val routeName: String,
        val stops: List<String>
    )

    data class BusTiming(
        val time: String,
        val direction: String,
        val route: Int,
        val busType: String,
        val specialNotes: String? = null
    )

    data class CampusBusTiming(
        val time: String,
        val busName: String,
        val route: List<String>,
        val direction: String // "To Campus" or "From Campus"
    )

    // Define all bus routes
    val allRoutes = listOf(
        Route(
            routeNumber = 1,
            routeName = "Laitumkhrah - NITM Sohra (Cathedral Church)",
            stops = listOf(
                "Laitumkhrah Police Point",
                "Beat House",
                "Fire Brigade Bus Stop",
                "Nongthymmai",
                "Madanriting",
                "Mawblei",
                "Laitkor",
                "NITM Sohra"
            )
        ),
        Route(
            routeNumber = 2,
            routeName = "Laitumkhrah - NITM Sohra (via Rynjah)",
            stops = listOf(
                "Laitumkhrah Police Point",
                "Beat House",
                "Bethany Hospital",
                "Rynjah",
                "Lapalang",
                "Supercare Hospital",
                "Madanriting",
                "Mawblei",
                "Laitkor",
                "NITM Sohra"
            )
        ),
        Route(
            routeNumber = 3,
            routeName = "Mawpat - NITM Sohra (via NEHU)",
            stops = listOf(
                "Mawpat",
                "Langkyrding",
                "Lumshyiap",
                "Golflink",
                "Mawroh",
                "Mawkynroh",
                "NEHU",
                "Kyntonmasser",
                "Mawdatbaki",
                "Motsyiar",
                "Iewrynghep",
                "Jaiaw Langsning",
                "Umshyrpi",
                "Laimer",
                "12 mer",
                "Mylliem",
                "Sohra"
            )
        ),
        Route(
            routeNumber = 4,
            routeName = "Mawpat - NITM Sohra (via Phudmuri)",
            stops = listOf(
                "Mawpat",
                "Langkyrding",
                "Lumshyiap",
                "Golflink",
                "Mawroh",
                "Phudmuri",
                "Mawdatbaki",
                "Motsyiar",
                "Iewrynghep",
                "Jaiaw Langsning",
                "Umshyrpi",
                "Laimer",
                "12 mer",
                "Mylliem",
                "Sohra"
            )
        )
    )

    // Off-Campus Bus Schedule (Shillong to NITM Sohra)
    val offCampusSchedule = mapOf(
        "MON" to listOf(
            BusTiming("6:45 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 2, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 4, "Bus", null),
            BusTiming("7:10 AM", "Shillong to NITM", 1, "Bus", null),
            BusTiming("7:20 AM", "Shillong to NITM", 2, "Traveller", "For Faculties and Officers"),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("4:50 PM", "NITM to Shillong", 3, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:10 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 1, "Traveller", "For Faculties and Officers")
        ),
        "TUE" to listOf(
            BusTiming("6:45 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 2, "Bus", null),
            BusTiming("7:10 AM", "Shillong to NITM", 1, "Bus", null),
            BusTiming("7:20 AM", "Shillong to NITM", 2, "Traveller", "For Faculties and Officers"),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:10 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 1, "Traveller", "For Faculties and Officers")
        ),
        "WED" to listOf(
            BusTiming("6:45 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 4, "Bus", null),
            BusTiming("7:10 AM", "Shillong to NITM", 1, "Bus", null),
            BusTiming("7:20 AM", "Shillong to NITM", 2, "Traveller", "For Faculties and Officers"),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:10 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 1, "Traveller", "For Faculties and Officers")
        ),
        "THU" to listOf(
            BusTiming("6:45 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 4, "Bus", null),
            BusTiming("7:10 AM", "Shillong to NITM", 1, "Bus", null),
            BusTiming("7:20 AM", "Shillong to NITM", 2, "Traveller", "For Faculties and Officers"),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:10 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 1, "Traveller", "For Faculties and Officers")
        ),
        "FRI" to listOf(
            BusTiming("6:45 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("7:00 AM", "Shillong to NITM", 2, "Bus", null),
            BusTiming("7:10 AM", "Shillong to NITM", 1, "Bus", null),
            BusTiming("7:20 AM", "Shillong to NITM", 2, "Traveller", "For Faculties and Officers"),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 4, "Bus", null),
            BusTiming("5:10 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("5:15 PM", "NITM to Shillong", 2, "Bus", null),
            BusTiming("5:00 PM", "NITM to Shillong", 1, "Traveller", "For Faculties and Officers")
        ),
        "SAT" to listOf(
            BusTiming("9:00 AM", "Shillong to NITM", 3, "Bus", null),
            BusTiming("10:00 AM", "Shillong to NITM", 4, "Bus", null),
            BusTiming("4:00 PM", "NITM to Shillong", 1, "Bus", null),
            BusTiming("4:30 PM", "NITM to Shillong", 1, "Bus", null)
        )
    )

    // In-Campus Bus Schedule (Inside NITM Sohra - Working Days)
    val inCampusScheduleWorkingDays = listOf(
        CampusBusTiming("8:25 AM", "Local Bus 1", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("9:00 AM", "Local Bus 2", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("9:30 AM", "Local Bus 1", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("10:00 AM", "Local Bus 1", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("10:15 AM", "Shillong Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("10:30 AM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("10:45 AM", "Shillong Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("11:00 AM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("11:15 AM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("11:30 AM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("11:45 AM", "Shillong Bus 3", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("12:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("12:15 PM", "Shillong Bus 4", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("12:30 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("12:45 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("1:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("1:15 PM", "Shillong Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("1:15 PM", "Shillong Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("1:45 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("1:45 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("2:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("2:15 PM", "Shillong Bus 3", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("2:30 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("2:45 PM", "Shillong Bus 4", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("3:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("3:15 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("3:30 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("3:45 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("4:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("4:15 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("4:30 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("4:45 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("5:00 PM", "Local Bus", listOf("A. Quarter", "D. Quarter", "PhD Hostel", "Boys Hostel", "A Block", "C Block"), "To Campus"),
        CampusBusTiming("5:30 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("6:00 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("6:30 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("7:00 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("7:30 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("8:00 PM", "Local Bus 2", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus"),
        CampusBusTiming("8:30 PM", "Local Bus 1", listOf("C Block", "A Block", "Boys Hostel", "PhD Hostel"), "From Campus"),
        CampusBusTiming("8:45 PM", "Local Bus 1", listOf("PhD Hostel", "D. Quarter", "A. Quarter"), "From Campus")
    )

    // In-Campus Bus Schedule (Saturdays and Holidays)
    val inCampusScheduleWeekends = listOf(
        CampusBusTiming("9:30 AM", "Bus 1", listOf("PhD Hostel", "Boys Hostel", "C Block"), "To Campus"),
        CampusBusTiming("1:00 PM", "Bus 1", listOf("C Block", "Boys Hostel", "PhD Hostel"), "From Campus"),
        CampusBusTiming("2:30 PM", "Bus 1", listOf("PhD Hostel", "Boys Hostel", "C Block"), "To Campus"),
        CampusBusTiming("7:00 PM", "Bus 1", listOf("C Block", "Boys Hostel", "PhD Hostel"), "From Campus")
    )

    // Sohra Market Trip Schedule
    val sohraMarketTrip = listOf(
        CampusBusTiming("10:45 AM", "Market Bus", listOf("A. Quarter", "PhD Hostel", "Boys Hostel", "Admin", "Main Gate", "Cherapunjee Market"), "To Market"),
        CampusBusTiming("1:45 PM", "Market Bus", listOf("Cherapunjee Market", "Main Gate", "Admin", "Boys Hostel", "PhD Hostel", "A. Quarter"), "From Market")
    )
}
