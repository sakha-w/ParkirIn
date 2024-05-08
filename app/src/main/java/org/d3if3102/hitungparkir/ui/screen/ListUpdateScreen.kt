package org.d3if3102.hitungparkir.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3102.hitungparkir.R
import org.d3if3102.hitungparkir.database.LogKendaraanDb
import org.d3if3102.hitungparkir.navigation.Screen
import org.d3if3102.hitungparkir.ui.theme.HitungParkirTheme
import org.d3if3102.hitungparkir.util.ViewModelFactory

const val KEY_ID_LOGKENDARAAN = "idLogKendaraan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUpdateScreen(
    modifier: Modifier,
    navController: NavHostController,
    id: Long? = null
) {
    val context = LocalContext.current
    val db = LogKendaraanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: ListScreenViewModel = viewModel(factory = factory)
    var kendaraan by rememberSaveable { mutableStateOf("- Pilih Kendaraan -") }
    var lamaParkir by rememberSaveable { mutableStateOf("") }
    var platNo by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() })
                    {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.list_parkir),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                title = {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.update_parkir),
                            fontWeight = W800
                        )

                    }

                },
                modifier = Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFF2F5F8),
                ),
                actions = {
                    DeleteAction(
                        delete = {
                            showDialog = true
                        },
                        navController = navController
                    )
                    DisplayDeleteDialog(
                        openDialog = showDialog,
                        onDissmissRequest = { showDialog = false }
                        ) {
                        showDialog = true
                        viewModel.delete(id!!)
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { padding ->
        UpdateContent(
            modifier = Modifier.padding(padding),
            navController,
            onKendaraanChange = { newValue ->
                kendaraan = newValue
            },
            onLamaParkirChange = { newValue ->
                lamaParkir = newValue
            },
            onPlatNoChange = { newValue ->
                platNo = newValue
            },
            id = id
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateContent(
    modifier: Modifier,
    navController: NavHostController,
    onKendaraanChange: (String) -> Unit = {},
    onLamaParkirChange: (String) -> Unit = {},
    onPlatNoChange: (String) -> Unit = {},
    id: Long? = null
) {
    val context = LocalContext.current
    val db = LogKendaraanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: ListScreenViewModel = viewModel(factory = factory)
    var isExpanded by remember { mutableStateOf(false) }
    var kendaraan by rememberSaveable { mutableStateOf("- Pilih Kendaraan -") }
    var lamaParkir by rememberSaveable { mutableStateOf("") }
    var lamaParkirInv by rememberSaveable { mutableStateOf(false) }
    var hasil by rememberSaveable { mutableStateOf(0f) }
    var platNo by rememberSaveable { mutableStateOf("") }
    var platNoInv by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getLogKendaraan(id) ?: return@LaunchedEffect
        kendaraan = data.kendaraan
        lamaParkir = data.lamaParkir
        platNo = data.platNo
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F0DA))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                TextField(
                    value = kendaraan,
                    onValueChange = { newValue ->
                        kendaraan = newValue
                        onKendaraanChange(newValue)
                    },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Motor") },
                        onClick = {
                            kendaraan = "Motor"
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Mobil") },
                        onClick = {
                            kendaraan = "Mobil"
                            isExpanded = false
                        }
                    )
                }
            }
            OutlinedTextField(
                value = lamaParkir,
                onValueChange = { newValue ->
                    lamaParkir = newValue
                    onLamaParkirChange(newValue)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = { IconChanger(lamaParkirInv, "Jam") },
                supportingText = { ErrorHint(lamaParkirInv) },
                label = { Text(text = stringResource(id = R.string.masukkan_jam)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
            OutlinedTextField(
                value = platNo,
                onValueChange = { newValue ->
                    platNo = newValue
                    onPlatNoChange(newValue)
                },
                trailingIcon = { IconChanger(platNoInv, " ") },
                supportingText = { ErrorHint(platNoInv) },
                label = { Text(text = stringResource(id = R.string.isi_plat)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
            Button(
                onClick = {
                    if (id != null) {
                        lamaParkirInv = (lamaParkir == "" || lamaParkir == "0")
                        platNoInv = (platNo == "" || platNo == "0")
                        if (lamaParkirInv || platNoInv) {
                            Toast.makeText(
                                context,
                                "Lama Parkir dan Plat Nomor harus di isi!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        hasil = hitungParkir(lamaParkir.toFloat(), kendaraan)
                        viewModel.update(id, kendaraan, lamaParkir, platNo)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 60.dp, vertical = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.simpan))
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit, navController: NavHostController) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.main)) },
                onClick = { navController.navigate(Screen.Home.route) })
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus)) },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }

}


private fun hitungParkir(lamaParkir: Float, kendaraan: String): Float {
    return when (kendaraan) {
        "Motor" -> lamaParkir * 2000
        "Mobil" -> lamaParkir * 5000
        else -> 0f
    }

}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListUpdateScreenPreview() {
    HitungParkirTheme {
        ListUpdateScreen(
            modifier = Modifier.fillMaxWidth(),
            navController = rememberNavController()
        )
    }
}