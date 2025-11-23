package com.example.trackdemics.screens.transport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.transport.components.ConfirmationDialog
import com.example.trackdemics.screens.transport.components.HeaderArea
import com.example.trackdemics.widgets.TrackdemicsAppBar

data class ScheduleItem(
    val id: Int,
    val time: String,
    val routeTitle: String,
    val routeCodes: String,
    val note: String? = null,
    val day: String = "MON",      // "MON", "TUE", ..., "SAT"
    val inside: Boolean = false, // true => Inside Campus, false => Outside Campus
    val seatsTaken: Int? = null,
    val capacity: Int? = null,
    val status: String? = null,      // optional status text (e.g. "Book", "Booking Confirmed", "Bus Full")
    val tracker: String? = null      // <-- tracker text (populated dynamically per UI state)
) {
    // helper to show the string like "10/30"
    fun seatsText() = "${seatsTaken}/${capacity}"
}

@Composable
fun BusScheduleScreen(
    navController: NavController,
    role: String,
    bookedBusId: Int? = null,
    updatedSeats: Int? = null,
) {
    // Sample data to populate list (mirror the Figma content)
    // --- REPLACE data class and sample data with this ---


    // All schedules for all combinations (MON/ SAT × Inside/Outside).
    // Fill entries to match the four visual types from your Figma screenshots.
    val allSchedules = remember {
        mutableStateListOf(
            // --- Monday, OUTSIDE Campus (MON + outside) ----
            // MON (100 - 111)
            ScheduleItem(
                100,
                "6:00 AM",
                "Route 2",
                "4821",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                101,
                "6:25 AM",
                "Route 3",
                "1934   8801",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            // 07:00 AM from Shillong -> NITM (required)
            ScheduleItem(
                102,
                "7:00 AM",
                "Route 1",
                "Traveller",
                note = "For Faculties and Officers",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                103,
                "7:10 AM",
                "Route 4",
                "4023",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                104,
                "7:30 AM",
                "Route 2",
                "2556   9932",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                105,
                "8:00 AM",
                "Route 3",
                "7689",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                106,
                "4:00 PM",
                "Route 1",
                "8245   3102",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                107,
                "4:20 PM",
                "Route 4",
                "5391",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                108,
                "4:40 PM",
                "Route 2",
                "2278   6660",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                109,
                "5:00 PM",
                "Route 3",
                "9902",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            // 05:30 PM from NITM -> Shillong (required)
            ScheduleItem(
                110,
                "5:30 PM",
                "Route 1",
                "Traveller",
                note = "For Faculties and Officers",
                day = "MON",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                111,
                "6:10 PM",
                "Route 4",
                "2765   8088",
                day = "MON",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),

            // TUE (112 - 123)
            ScheduleItem(
                112,
                "6:05 AM",
                "Route 3",
                "3176",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                113,
                "6:30 AM",
                "Route 1",
                "4801   6612",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            // 07:00 AM from Shillong -> NITM (required)
            ScheduleItem(
                114,
                "7:00 AM",
                "Route 2",
                "Traveller",
                note = "For Faculties and Officers",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                115,
                "7:15 AM",
                "Route 4",
                "3993",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                116,
                "7:40 AM",
                "Route 3",
                "8432   1765",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                117,
                "8:10 AM",
                "Route 1",
                "2219",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                118,
                "4:10 PM",
                "Route 2",
                "6688   1312",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                119,
                "4:35 PM",
                "Route 3",
                "5003",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                120,
                "4:55 PM",
                "Route 4",
                "9662   4420",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                121,
                "5:15 PM",
                "Route 1",
                "7130",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            // 05:30 PM required entry (use existing close time 5:45 -> normalize to 5:30 with required attributes)
            ScheduleItem(
                122,
                "5:30 PM",
                "Route 2",
                "Traveller",
                note = "For Faculties and Officers",
                day = "TUE",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                123,
                "6:20 PM",
                "Route 4",
                "4829   9900",
                day = "TUE",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),

            // WED (124 - 135)
            ScheduleItem(
                124,
                "6:10 AM",
                "Route 2",
                "8340   4521",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                125,
                "6:35 AM",
                "Route 4",
                "5276",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            // 07:00 AM from Shillong -> NITM (required)
            ScheduleItem(
                126,
                "7:00 AM",
                "Route 1",
                "Traveller",
                note = "For Faculties and Officers",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                127,
                "7:20 AM",
                "Route 3",
                "9441   2203",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                128,
                "7:45 AM",
                "Route 2",
                "3510",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                129,
                "8:05 AM",
                "Route 4",
                "6092   4488",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                130,
                "4:05 PM",
                "Route 1",
                "1255",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                131,
                "4:30 PM",
                "Route 3",
                "7702   2333",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                132,
                "4:50 PM",
                "Route 2",
                "4198",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                133,
                "5:10 PM",
                "Route 4",
                "5580   9001",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            // 05:30 PM required entry (replace a nearby time)
            ScheduleItem(
                134,
                "5:30 PM",
                "Route 1",
                "Traveller",
                note = "For Faculties and Officers",
                day = "WED",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                135,
                "6:00 PM",
                "Route 2",
                "6611   4772",
                day = "WED",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),

            // THU (136 - 147)
            ScheduleItem(
                136,
                "6:15 AM",
                "Route 4",
                "2784",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                137,
                "6:40 AM",
                "Route 1",
                "5433   2920",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            // 07:00 AM from Shillong -> NITM (required)
            ScheduleItem(
                138,
                "7:00 AM",
                "Route 3",
                "Traveller",
                note = "For Faculties and Officers",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                139,
                "7:25 AM",
                "Route 2",
                "9300   3671",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                140,
                "7:50 AM",
                "Route 4",
                "4611",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                141,
                "8:20 AM",
                "Route 1",
                "1044   7216",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                142,
                "4:15 PM",
                "Route 2",
                "3902",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                143,
                "4:45 PM",
                "Route 3",
                "6177   2244",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                144,
                "5:05 PM",
                "Route 4",
                "7338",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                145,
                "5:25 PM",
                "Route 1",
                "2566   9907",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            // 05:30 PM required entry
            ScheduleItem(
                146,
                "5:30 PM",
                "Route 2",
                "Traveller",
                note = "For Faculties and Officers",
                day = "THU",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                147,
                "6:30 PM",
                "Route 3",
                "3077   1188",
                day = "THU",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),

            // FRI (148 - 159)
            ScheduleItem(
                148,
                "6:05 AM",
                "Route 1",
                "9112   4005",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                149,
                "6:30 AM",
                "Route 2",
                "6544",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            // 07:00 AM from Shillong -> NITM (required)
            ScheduleItem(
                150,
                "7:00 AM",
                "Route 3",
                "Traveller",
                note = "For Faculties and Officers",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                151,
                "7:15 AM",
                "Route 4",
                "4099   5500",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                152,
                "7:40 AM",
                "Route 1",
                "1888",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                153,
                "8:05 AM",
                "Route 2",
                "3321   9442",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                154,
                "4:00 PM",
                "Route 3",
                "5770",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                155,
                "4:30 PM",
                "Route 1",
                "8033   1199",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                156,
                "4:50 PM",
                "Route 4",
                "6600",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                157,
                "5:10 PM",
                "Route 2",
                "2944   7007",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            // 05:30 PM required entry
            ScheduleItem(
                158,
                "5:30 PM",
                "Route 1",
                "Traveller",
                note = "For Faculties and Officers",
                day = "FRI",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                159,
                "6:05 PM",
                "Route 4",
                "9011   3666",
                day = "FRI",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),

            // --- Monday, INSIDE Campus (MON + inside) ----

            // --- Saturday, OUTSIDE Campus (SAT + outside) ----
            ScheduleItem(
                12,
                "9:00 AM",
                "Route 1",
                "5850   6822",
                seatsTaken = 51,
                capacity = 60,
                status = "Book",
                day = "SAT",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                13,
                "10:00 AM",
                "Route 1",
                "4133",
                seatsTaken = 11,
                capacity = 30,
                status = "Booking Confirmed",
                day = "SAT",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                14,
                "5:00 PM",
                "Route 1",
                "9059",
                seatsTaken = 30,
                capacity = 30,
                status = "Bus Full",
                day = "SAT",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                15,
                "5:30 PM",
                "Route 2",
                "Not yet updated",
                day = "SAT",
                inside = false,
                tracker = "Shillong to NITM Sohra"
            ),
            ScheduleItem(
                12,
                "9:30 AM",
                "Route 1",
                "5850",
                seatsTaken = 30,
                capacity = 30,
                status = "Bus Full",
                day = "SAT",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                13,
                "10:00 AM",
                "Route 1",
                "4133",
                seatsTaken = 11,
                capacity = 30,
                status = "Booking Confirmed",
                day = "SAT",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                14,
                "5:00 PM",
                "Route 1",
                "9059   6244",
                seatsTaken = 10,
                capacity = 60,
                status = "Book",
                day = "SAT",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),
            ScheduleItem(
                15,
                "5:30 PM",
                "Route 2",
                "Not yet updated",
                day = "SAT",
                inside = false,
                tracker = "NITM Sohra to Shillong"
            ),

            // --- Saturday, INSIDE Campus (SAT + inside) ----
            ScheduleItem(
                200,
                "8:00 AM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                201,
                "8:48 AM",
                "Route A",
                "Local Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                202,
                "9:36 AM",
                "Route A",
                "Local Bus 2",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                203,
                "10:24 AM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                204,
                "11:12 AM",
                "Route A",
                "Local Bus 1",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                205,
                "12:00 PM",
                "Route A",
                "Local Bus 2",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                206,
                "12:48 PM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                207,
                "1:36 PM",
                "Route A",
                "Local Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                208,
                "2:24 PM",
                "Route A",
                "Local Bus 2",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                209,
                "3:12 PM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                210,
                "4:00 PM",
                "Route A",
                "Local Bus 1",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                211,
                "4:48 PM",
                "Route A",
                "Local Bus 2",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                212,
                "5:36 PM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                213,
                "6:24 PM",
                "Route A",
                "Local Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                214,
                "7:12 PM",
                "Route A",
                "Local Bus 2",
                day = "MON",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                215,
                "8:00 PM",
                "Route A",
                "Shillong Bus 1",
                day = "MON",
                inside = true,
                tracker = "Towards Living Quarters"
            ),

            // TUE (216 - 231)  -- start with Towards, alternate -> 8 Towards, 8 From
            ScheduleItem(
                216,
                "8:00 AM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                217,
                "8:48 AM",
                "Route A",
                "Local Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                218,
                "9:36 AM",
                "Route A",
                "Local Bus 2",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                219,
                "10:24 AM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                220,
                "11:12 AM",
                "Route A",
                "Local Bus 1",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                221,
                "12:00 PM",
                "Route A",
                "Local Bus 2",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                222,
                "12:48 PM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                223,
                "1:36 PM",
                "Route A",
                "Local Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                224,
                "2:24 PM",
                "Route A",
                "Local Bus 2",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                225,
                "3:12 PM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                226,
                "4:00 PM",
                "Route A",
                "Local Bus 1",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                227,
                "4:48 PM",
                "Route A",
                "Local Bus 2",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                228,
                "5:36 PM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                229,
                "6:24 PM",
                "Route A",
                "Local Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                230,
                "7:12 PM",
                "Route A",
                "Local Bus 2",
                day = "TUE",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                231,
                "8:00 PM",
                "Route A",
                "Shillong Bus 1",
                day = "TUE",
                inside = true,
                tracker = "From Living Quarters"
            ),

            // WED (232 - 247)  -- start with From, alternate -> 8 From, 8 Towards
            ScheduleItem(
                232,
                "8:00 AM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                233,
                "8:48 AM",
                "Route A",
                "Local Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                234,
                "9:36 AM",
                "Route A",
                "Local Bus 2",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                235,
                "10:24 AM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                236,
                "11:12 AM",
                "Route A",
                "Local Bus 1",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                237,
                "12:00 PM",
                "Route A",
                "Local Bus 2",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                238,
                "12:48 PM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                239,
                "1:36 PM",
                "Route A",
                "Local Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                240,
                "2:24 PM",
                "Route A",
                "Local Bus 2",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                241,
                "3:12 PM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                242,
                "4:00 PM",
                "Route A",
                "Local Bus 1",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                243,
                "4:48 PM",
                "Route A",
                "Local Bus 2",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                244,
                "5:36 PM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                245,
                "6:24 PM",
                "Route A",
                "Local Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                246,
                "7:12 PM",
                "Route A",
                "Local Bus 2",
                day = "WED",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                247,
                "8:00 PM",
                "Route A",
                "Shillong Bus 1",
                day = "WED",
                inside = true,
                tracker = "Towards Living Quarters"
            ),

            // THU (248 - 263)  -- start with Towards, alternate -> 8 Towards, 8 From
            ScheduleItem(
                248,
                "8:00 AM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                249,
                "8:48 AM",
                "Route A",
                "Local Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                250,
                "9:36 AM",
                "Route A",
                "Local Bus 2",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                251,
                "10:24 AM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                252,
                "11:12 AM",
                "Route A",
                "Local Bus 1",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                253,
                "12:00 PM",
                "Route A",
                "Local Bus 2",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                254,
                "12:48 PM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                255,
                "1:36 PM",
                "Route A",
                "Local Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                256,
                "2:24 PM",
                "Route A",
                "Local Bus 2",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                257,
                "3:12 PM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                258,
                "4:00 PM",
                "Route A",
                "Local Bus 1",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                259,
                "4:48 PM",
                "Route A",
                "Local Bus 2",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                260,
                "5:36 PM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                261,
                "6:24 PM",
                "Route A",
                "Local Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                262,
                "7:12 PM",
                "Route A",
                "Local Bus 2",
                day = "THU",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                263,
                "8:00 PM",
                "Route A",
                "Shillong Bus 1",
                day = "THU",
                inside = true,
                tracker = "From Living Quarters"
            ),

            // FRI (264 - 279)  -- start with From, alternate -> 8 From, 8 Towards
            ScheduleItem(
                264,
                "8:00 AM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                265,
                "8:48 AM",
                "Route A",
                "Local Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                266,
                "9:36 AM",
                "Route A",
                "Local Bus 2",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                267,
                "10:24 AM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                268,
                "11:12 AM",
                "Route A",
                "Local Bus 1",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                269,
                "12:00 PM",
                "Route A",
                "Local Bus 2",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                270,
                "12:48 PM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                271,
                "1:36 PM",
                "Route A",
                "Local Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                272,
                "2:24 PM",
                "Route A",
                "Local Bus 2",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                273,
                "3:12 PM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                274,
                "4:00 PM",
                "Route A",
                "Local Bus 1",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                275,
                "4:48 PM",
                "Route A",
                "Local Bus 2",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                276,
                "5:36 PM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                277,
                "6:24 PM",
                "Route A",
                "Local Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                278,
                "7:12 PM",
                "Route A",
                "Local Bus 2",
                day = "FRI",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                279,
                "8:00 PM",
                "Route A",
                "Shillong Bus 1",
                day = "FRI",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                16,
                "1:00 PM",
                "Route P",
                "Local Bus 1",
                day = "SAT",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                17,
                "1:45 PM",
                "Route S",
                "Local Bus 2",
                day = "SAT",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                18,
                "7:00 PM",
                "Route P",
                "Local Bus 1",
                day = "SAT",
                inside = true,
                tracker = "Towards Living Quarters"
            ),
            ScheduleItem(
                16,
                "8:00 AM",
                "Route P",
                "Local Bus 1",
                day = "SAT",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                17,
                "7:00 AM",
                "Route S",
                "Local Bus 2",
                day = "SAT",
                inside = true,
                tracker = "From Living Quarters"
            ),
            ScheduleItem(
                18,
                "1:00 PM",
                "Route P",
                "Local Bus 1",
                day = "SAT",
                inside = true,
                tracker = "From Living Quarters"
            ),

            )
    }

    // UI state
    var selectedDay by remember { mutableStateOf("SAT") }
    var insideCampus by remember { mutableStateOf(false) }
    var selectedRoute by remember { mutableStateOf(false) }
    var routineclick by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val cancelingIndex = remember { mutableIntStateOf(-1) }
    // Map to hold which bus (by id) is currently booked by the user in this session
    val bookedMap = remember { mutableStateMapOf<Int, Boolean>() }
    LaunchedEffect(key1 = bookedBusId, key2 = updatedSeats) {
        if (bookedBusId != null && updatedSeats != null) {
            val index = allSchedules.indexOfFirst { it.id == bookedBusId }
            if (index >= 0) {
                val current = allSchedules[index]
                allSchedules[index] = current.copy(seatsTaken = updatedSeats)
                bookedMap[bookedBusId] = true
            }
        }
    }
    // --- Filter by selectedDay and campus toggle (insideCampus) ---
    /// REPLACE the existing filteredSchedules computation with this:
    val filteredSchedules = remember(allSchedules, selectedDay, insideCampus, selectedRoute) {
        val currentTracker = if (!insideCampus) {
            // Outside campus: selectedRoute == true => NITM Sohra to Shillong
            if (selectedRoute) "NITM Sohra to Shillong" else "Shillong to NITM Sohra"
        } else {
            // Inside campus: selectedRoute == true => From Living Quarters
            if (selectedRoute) "From Living Quarters" else "Towards Living Quarters"
        }

        allSchedules.filter { item ->
            item.day.equals(selectedDay, ignoreCase = true)
                    && item.inside == insideCampus
                    // only include items whose inherent tracker matches the currently selected direction
                    && (item.tracker?.equals(currentTracker, ignoreCase = true) ?: false)
        }
    }


    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    when (role) {
                        "STUDENT" -> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                        "PROFESSOR" -> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
                        "ADMIN" -> navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
                    }
                },
                isScheduleScreen = true,
                isActionScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background
            )
        }
    )
    { paddingValues ->
        if (showDialog.value) {
            ConfirmationDialog(
                onDismissRequest = {
                    showDialog.value = false
                    cancelingIndex.value = -1
                },
                onConfirm = {
                    // user confirmed cancellation: undo booking for the selected bus
                    if (cancelingIndex.value >= 0 && cancelingIndex.value < allSchedules.size) {
                        val current = allSchedules[cancelingIndex.value]
                        // decrement seatsTaken safely
                        allSchedules[cancelingIndex.value] = current.copy(
                            seatsTaken = (current.seatsTaken?.minus(1))?.coerceAtLeast(0)
                        )
                        // mark unbooked
                        bookedMap[current.id] = false
                    }
                    showDialog.value = false
                    cancelingIndex.value = -1
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            //.background(Color(0xFFF4F6F8))
        ) {
            // Gradient header area
            HeaderArea(
                navController = navController,
                role = role
            ) {
                routineclick = true
            }
            if (routineclick) {
                RouteDetailsDialog() {
                    routineclick = false
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Day tabs and campus toggle
            DayTabsRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CampusToggle(
                inside = insideCampus,
                onToggle = { insideCampus = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Route pill
            RoutePill(
                selectedRoute = selectedRoute,
                inside = insideCampus
            )
            {
                selectedRoute = !selectedRoute
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Schedule list
            // --- REPLACE LazyColumn(schedules) { ... } with this filtered list ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB7E2F5))
            )
            {
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(filteredSchedules) { index, item ->
                        // We intentionally pass the same ScheduleItem used before;
                        // ScheduleCard uses time, routeTitle, routeCodes, note so it continues to work unchanged.
                        if (selectedDay == "SAT" && !insideCampus) {
                            val isBooked = bookedMap[item.id] == true
                            WeekendOutsideScheduleCard(
                                bus = item,
                                isBooked = isBooked,
                                onBookClicked = {
                                    // Book: mark in bookedMap and increase seatsTaken by 1
                                    // Cancel: unmark and decrease seatsTaken by 1 (not below 0)
                                    val encodedDate = java.net.URLEncoder.encode(item.time, "utf-8")
                                    val encodedRoute =
                                        java.net.URLEncoder.encode(item.tracker, "utf-8")
                                    val route =
                                        "${TrackdemicsScreens.SeatBookingScreen.name}/${item.id}/$encodedDate/$encodedRoute/${item.routeCodes}/${item.seatsTaken}/${item.capacity}/false/$role"
                                    navController.navigate(route)
                                },
                                onCancelClicked = {
                                    // Cancel: unmark and decrease seatsTaken by 1 (not below 0)
                                    showDialog.value = true
                                    cancelingIndex.value = index
                                }
                            )
                        } else
                            ScheduleCard(item)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }
            }
        }
    }
}


@Composable
fun RouteDetailsDialog(
    routeDetails: List<Pair<String, String>> = defaultRouteDetails(),
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        // Card with thin blue border and rounded corners to match the Figma look.
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 45.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Title area
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
                )
                {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        text = "Route Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                // inside your composable
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .heightIn(max = 360.dp), // allow scrolling when content is long
                    // you can add contentPadding or verticalArrangement if needed
                ) {
                    items(routeDetails) { pair ->
                        val (title, desc) = pair

                        Text(
                            text = title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = desc,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

// Close button centered, pill-shaped
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            }
        }
    }
}

/** Default content that mirrors the screenshot — replace with your real data if needed. */
private fun defaultRouteDetails(): List<Pair<String, String>> = listOf(
    "Route 1" to "Laitumkhrah Police Point, Beat House, Fire Brigade Bus Stop, Nongthymmai, Madanriting, Mawblei and Laitkor.",
    "Route 2" to "Laitumkhrah Police Point, Beat House, Bethany Hospital, Rynjah, Lapalang, Supercare Hospital, Madanriting, Mawblei and Laitkor.",
    "Route 3" to "Mawpat, Langkyrding, Lumshyiap, Golflink, Mawroh, Mawkynroh, NEHU, Kyntonmasser, Mawdabtaki, Motsyiar, Iewrynghep, Jaiaw Langsning, Umshyrpi, Laimer, 12 mer, Mylliem, Sohra",
    "Route 4" to "Mawpat, Langkyrding, Lumshyiap, Golflink, Mawroh, Phudmuri, Mawdabtaki, Motsyiar, Iewrynghep, Jaiaw Langsning, Umshyrpi, Laimer, 12 mer, Mylliem, Sohra",
    "Route A" to "A. Quarter, D. Quarter, PhD Hostel, Boys Hostel, A Block, C Block",
    "Route S" to "A. Quarter, PhD Hostel, Boys Hostel, Admin, Main Gate, Cherapunjee Market",
    "Route P" to "PhD Hostel, Boys Hostel, C Block"
)

@Composable
private fun DayTabsRow(selectedDay: String, onDaySelected: (String) -> Unit) {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        for (d in days) {
            val isSelected = d == selectedDay
            Card(
                modifier = Modifier
                    .width(55.dp)
                    .height(35.dp)
                    .clickable { onDaySelected(d) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(15.dp),
                border = BorderStroke(
                    1.dp,
                    color = Color.Gray
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = d,
                        color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
private fun CampusToggle(
    inside: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val primaryBlue = Color(0xFF0F517D)
    val lightBg = Color(0xFFE9F4FB)
    val unselectedBorder = Color(0xFFDDEAF3)

    // asymmetrical shapes: left has big rounding on the end, right has big rounding on the start
    val leftShape = RoundedCornerShape(
        topStart = 0.dp, bottomStart = 30.dp,
        topEnd = 30.dp, bottomEnd = 0.dp
    )
    val rightShape = RoundedCornerShape(
        topStart = 30.dp, bottomStart = 0.dp,
        topEnd = 0.dp, bottomEnd = 30.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // Inside campus surface
        Surface(
            modifier = Modifier
                .weight(0.5f)
                .height(56.dp)
                .clickable { onToggle(true) },
            shape = leftShape,
            color =
                if (inside) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.25.dp, color = MaterialTheme.colorScheme.onSurface),
            tonalElevation = 20.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Inside\nCampus",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    color = if (inside) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Outside campus surface
        Surface(
            modifier = Modifier
                .weight(0.5f)
                .height(56.dp)
                .clickable { onToggle(false) },
            shape = rightShape,
            color =
                if (inside) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.onPrimaryContainer,
            border = BorderStroke(1.25.dp, color = MaterialTheme.colorScheme.onSurface),
            tonalElevation = 20.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Outside\nCampus",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    color = if (inside) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}


@Composable
private fun RoutePill(
    selectedRoute: Boolean,
    inside: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    )
    {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = if (selectedRoute && !inside)
                    "NITM Sohra to Shillong"
                else if (!selectedRoute && !inside)
                    "Shillong to NITM Sohra"
                else if (!selectedRoute && inside)
                    "Towards Living Quarters"
                else
                    "From Living Quarters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,

                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "swap",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun ScheduleCard(bus: ScheduleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (bus.note != null) 90.dp else 80.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
        )
        {
            Text(
                text = bus.time,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                // Left column: date/time + route + code
                Column(
                    modifier =
                        Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                )
                {

                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "pin",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bus.routeTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                // Right column: seats info and Book action
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = bus.routeCodes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (bus.note != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = bus.note,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Composable
fun WeekendOutsideScheduleCard(
    bus: ScheduleItem,
    isBooked: Boolean,
    onBookClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isBooked) 130.dp else 110.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 12.dp),
        )
        {
            Text(
                text = bus.time,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                // Left column: date/time + route + code
                Column(modifier = Modifier.weight(1f))
                {

                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "pin",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bus.routeTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = bus.routeCodes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Right column: seats info and Book action
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                )
                {
                    if (bus.routeCodes == "Not yet updated") {
                        Image(
                            painter = painterResource(id = R.drawable.upload),
                            contentDescription = "open",
                            modifier = Modifier
                                .size(45.dp)
                                .padding(end = 5.dp)
                        )
                    } else if (bus.seatsTaken != bus.capacity) {
                        Text(
                            text = bus.seatsText(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (!isBooked) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onBookClicked() }
                            )
                            {
                                Text(
                                    text = "Book",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "open",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onCancelClicked() }
                            )
                            {
                                Text(
                                    text = "Cancel",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    } else {
                        Text(
                            text = bus.seatsText(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Bus Full",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }
            }
            if (isBooked) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = "Booking Confirmed",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}