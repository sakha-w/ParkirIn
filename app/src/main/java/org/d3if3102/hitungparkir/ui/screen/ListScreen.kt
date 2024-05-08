package org.d3if3102.hitungparkir.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3102.hitungparkir.R
import org.d3if3102.hitungparkir.database.LogKendaraanDb
import org.d3if3102.hitungparkir.model.LogKendaraan
import org.d3if3102.hitungparkir.navigation.Screen
import org.d3if3102.hitungparkir.ui.theme.HitungParkirTheme
import org.d3if3102.hitungparkir.util.SettingsDataStore
import org.d3if3102.hitungparkir.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(modifier: Modifier, navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(initial = true)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.list_parkir),
                            fontWeight = FontWeight.W800
                        )
                    }
                },
                modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFF2F5F8)
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveLayout(!showList)
                            }
                        }
                    ) {
                        Icon(
                            painter =
                            painterResource(
                                if (showList) R.drawable.ic_grid_view
                                else R.drawable.ic_list_view
                            ),
                            contentDescription = stringResource(if (showList) R.string.tampilan_grid else R.string.tampilan_list),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        ListScreenContent(Modifier.padding(padding), navController, showList)
    }
}

@Composable
fun ListScreenContent(modifier: Modifier, navController: NavHostController, showList: Boolean) {
    val context = LocalContext.current
    val db = LogKendaraanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: ListScreenViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    val openDialog by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F0DA))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_display),
            contentDescription = "Main Background",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)
        )
        if (data.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.list_kosong))
            }
        } else {
            if (showList) {
                LazyColumn(
                    modifier = modifier.fillMaxSize().padding(start = 12.dp, end = 12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(data) {
                        ListItem(logKendaraan = it) {
                            navController.navigate(Screen.ListUpdate.withId(it.id))
                        }
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 72.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp, 20.dp, 8.dp, 64.dp)
                ) {
                    items(data) {
                        GridItem(logKendaraan = it) {
                            navController.navigate(Screen.ListUpdate.withId(it.id))
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ListItem(logKendaraan: LogKendaraan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = logKendaraan.kendaraan,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = R.string.list_jam, logKendaraan.lamaParkir),
                maxLines = 1
            )
            Text(
                text = logKendaraan.platNo,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GridItem(logKendaraan: LogKendaraan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = logKendaraan.kendaraan,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = R.string.list_jam, logKendaraan.lamaParkir),
                maxLines = 1
            )
            Text(
                text = logKendaraan.platNo,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListScreenPreview() {
    HitungParkirTheme {
        ListScreen(modifier = Modifier.fillMaxWidth(), navController = rememberNavController())
    }
}