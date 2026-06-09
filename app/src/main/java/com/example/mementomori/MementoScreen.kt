package com.example.mementomori

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.imePadding


enum class MementoTab {
    Today,
    Calendar,
    Settings
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MementoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val storedSettings = remember {
        loadMementoSettings(context)
    }

    var savedBirthDate by remember { mutableStateOf(storedSettings.birthDate) }
    var savedLifeExpectancy by remember { mutableStateOf(storedSettings.lifeExpectancyYears) }

    var draftBirthDate by remember { mutableStateOf(savedBirthDate) }
    var draftLifeExpectancy by remember { mutableStateOf(savedLifeExpectancy.toString()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showIntro by remember { mutableStateOf(!storedSettings.hasSeenIntro) }

    var titleTapCount by remember { mutableStateOf(0) }
    var showEasterEgg by remember { mutableStateOf(false) }

    var selectedLifeWeek by remember {
        mutableStateOf<SelectedLifeWeek?>(null)
    }

    var customQuotes by remember {
        mutableStateOf(loadCustomQuotes(context))
    }

    val tabs = listOf(
        MementoTab.Today,
        MementoTab.Calendar,
        MementoTab.Settings
    )

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = if (storedSettings.hasSavedData) 0 else 2,
        pageCount = { tabs.size }
    )

    val selectedTab = tabs[pagerState.currentPage]

    if (showIntro) {
        MementoIntroScreen(
            onStartClick = {
                markIntroAsSeen(context)
                showIntro = false

                coroutineScope.launch {
                    pagerState.scrollToPage(2)
                }
            },
            modifier = modifier
        )

        return
    }

    val stats = calculateLifeStats(
        birthDate = savedBirthDate,
        lifeExpectancyYears = savedLifeExpectancy
    )

    Column(
        modifier = modifier.imePadding()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val tab = tabs[page]

            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = if (tab == MementoTab.Calendar) 12.dp else 24.dp,
                        vertical = 24.dp
                    )
            ) {
                when (tab) {
                    MementoTab.Today -> {
                        Column {
                            Text(
                                text = "Memento Mori",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    titleTapCount += 1

                                    if (titleTapCount >= 3) {
                                        showEasterEgg = true
                                        titleTapCount = 0
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Recuerda que el tiempo es finito.",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            HeroLifeCard(
                                weeksLived = stats.weeksLived,
                                totalWeeks = stats.totalWeeks,
                                percentLived = stats.percentLived,
                                quote = getDailyMementoQuote(customQuotes)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            LifeStatCard("Días vividos", formatWholeNumber(stats.daysLived))
                            LifeStatCard("Semanas vividas", formatWholeNumber(stats.weeksLived))
                            LifeStatCard("Semanas restantes", formatWholeNumber(stats.weeksRemaining))
                            LifeStatCard("Total de semanas estimadas", formatWholeNumber(stats.totalWeeks))
                            LifeStatCard("Horizonte estimado", formatDisplayDate(stats.estimatedEndDate))
                        }
                    }

                    MementoTab.Calendar -> {
                        Column {
                            Text(
                                text = "Calendario de vida",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Un año por renglón. Una década por bloque.",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            LifeCalendarLargeCard(
                                weeksLived = stats.weeksLived,
                                lifeExpectancyYears = savedLifeExpectancy,
                                selectedWeek = selectedLifeWeek,
                                onWeekSelected = { week ->
                                    selectedLifeWeek = week
                                }
                            )
                        }
                    }

                    MementoTab.Settings -> {
                        Column {
                            Text(
                                text = "Ajustes",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Tus datos se guardan solo en este dispositivo.",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            MementoSettingsForm(
                                draftBirthDate = draftBirthDate,
                                draftLifeExpectancy = draftLifeExpectancy,
                                onBirthDateClick = {
                                    showDatePicker = true
                                },
                                onLifeExpectancyChange = { newValue ->
                                    draftLifeExpectancy = newValue
                                },
                                onSaveClick = { birthDate, lifeExpectancyYears ->
                                    savedBirthDate = birthDate
                                    savedLifeExpectancy = lifeExpectancyYears

                                    saveMementoSettings(
                                        context = context,
                                        birthDate = birthDate,
                                        lifeExpectancyYears = lifeExpectancyYears
                                    )

                                    refreshMementoWidgets(context)

                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                },
                                onClearClick = {
                                    showClearDataDialog = true
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomQuotesCard(
                                customQuotes = customQuotes,
                                onAddQuote = { quote ->
                                    customQuotes = addCustomQuote(context, quote)
                                    refreshMementoWidgets(context)
                                },
                                onDeleteQuote = { quote ->
                                    customQuotes = deleteCustomQuote(context, quote)
                                    refreshMementoWidgets(context)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PrivacyCard()

                            Spacer(modifier = Modifier.height(96.dp))

                            AppSignature()
                        }
                    }
                }
            }
        }

        if (selectedTab == MementoTab.Calendar && selectedLifeWeek != null) {
            SelectedLifeWeekBottomBar(
                week = selectedLifeWeek!!,
                onClearClick = {
                    selectedLifeWeek = null
                }
            )
        }
        CompactBottomBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                val page = tabs.indexOf(tab)

                coroutineScope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        )

    if (showDatePicker) {
        MementoBirthDatePickerDialog(
            initialDate = draftBirthDate,
            onDateSelected = { selectedDate ->
                draftBirthDate = selectedDate
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    if (showClearDataDialog) {
        ConfirmClearDataDialog(
            onConfirm = {
                clearMementoSettings(context)

                savedBirthDate = DEFAULT_BIRTH_DATE
                savedLifeExpectancy = DEFAULT_LIFE_EXPECTANCY_YEARS

                draftBirthDate = DEFAULT_BIRTH_DATE
                draftLifeExpectancy = DEFAULT_LIFE_EXPECTANCY_YEARS.toString()

                customQuotes = emptyList()

                refreshMementoWidgets(context)

                showClearDataDialog = false
                showIntro = true
                coroutineScope.launch {
                    pagerState.scrollToPage(2)
                }
            },
            onDismiss = {
                showClearDataDialog = false
            }
        )
    }

        if (showEasterEgg) {
            MementoEasterEggDialog(
                onDismiss = {
                    showEasterEgg = false
                }
            )
        }
}}

