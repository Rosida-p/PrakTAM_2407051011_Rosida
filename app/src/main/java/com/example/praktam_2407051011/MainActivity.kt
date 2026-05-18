package com.example.praktam_2407051011

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.praktam_2407051011.data.model.Aktivitas
import com.example.praktam_2407051011.data.repository.AktivitasRepository
import com.example.praktam_2407051011.ui.theme.PrakTAM_2407051011Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            PrakTAM_2407051011Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNav()
                }
            }
        }
    }
}

@Composable
fun AppNav() {

    val navController = rememberNavController()

    var aktivitasList by remember {
        mutableStateOf<List<Aktivitas>>(emptyList())
    }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {

        composable("list") {

            AktivitasListScreen(

                onItemClick = { index ->
                    navController.navigate("detail/$index")
                },

                onAktivitasLoaded = { fetchedAktivitas ->
                    aktivitasList = fetchedAktivitas
                }
            )
        }

        composable(
            route = "detail/{index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val index =
                backStackEntry.arguments?.getInt("index") ?: 0

            if (aktivitasList.isNotEmpty()) {

                DetailScreen(
                    aktivitas = aktivitasList[index],

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun AktivitasListScreen(
    onItemClick: (Int) -> Unit,
    onAktivitasLoaded: (List<Aktivitas>) -> Unit = {}
) {

    var aktivitasList by remember {
        mutableStateOf<List<Aktivitas>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    val repository = remember {
        AktivitasRepository()
    }

    LaunchedEffect(Unit) {

        try {

            isLoading = true

            aktivitasList =
                repository.getAktivitas()

            onAktivitasLoaded(aktivitasList)

            isLoading = false
            isError = aktivitasList.isEmpty()

        } catch (e: Exception) {

            isLoading = false
            isError = true
        }
    }

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }

    } else if (isError || aktivitasList.isEmpty()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Gagal Memuat Data",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pastikan koneksi internet Anda menyala",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),

            contentPadding = PaddingValues(24.dp),

            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {

                Text(
                    "Pengingat Aktivitas Harian",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Aktivitas Prioritas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    itemsIndexed(aktivitasList) { index, aktivitas ->

                        AktivitasRowItem(
                            aktivitas = aktivitas
                        ) {
                            onItemClick(index)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Daftar Aktivitas Harian",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            itemsIndexed(aktivitasList) { index, aktivitas ->

                AktivitasItem(
                    aktivitas = aktivitas
                ) {
                    onItemClick(index)
                }
            }
        }
    }
}
    @Composable
    fun AktivitasRowItem(
        aktivitas: Aktivitas,
        onClick: () -> Unit
    ) {

        var isFavorite by remember {
            mutableStateOf(false)
        }

        Card(
            modifier = Modifier
                .width(160.dp)
                .clickable { onClick() },

            shape = RoundedCornerShape(12.dp)
        ) {

            Column {

                Box {

                    AsyncImage(
                        model = aktivitas.imageUrl,

                        contentDescription = aktivitas.judul,
                        placeholder = painterResource(id = R.drawable.belajar),

                        error = painterResource(id = R.drawable.belajar),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),

                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                        },

                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    ) {

                        Icon(
                            imageVector =
                                if (isFavorite)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,

                            contentDescription = "Favorite",

                            tint =
                                if (isFavorite)
                                    Color.Red
                                else
                                    Color.White
                        )
                    }
                }

                Column(
                    Modifier.padding(8.dp)
                ) {

                    Text(aktivitas.judul)

                    Text(
                        aktivitas.jam,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    @Composable
    fun AktivitasItem(
        aktivitas: Aktivitas,
        onClick: () -> Unit
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },

            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                Modifier.padding(16.dp)
            ) {

                AsyncImage(
                    model = aktivitas.imageUrl,

                    contentDescription = aktivitas.judul,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),

                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    aktivitas.judul,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(aktivitas.deskripsi)

                Text("Jam: ${aktivitas.jam}")
            }
        }
    }

    @Composable
    fun DetailScreen(
        aktivitas: Aktivitas,
        onBack: () -> Unit
    ) {

        var isLoading by remember {
            mutableStateOf(false)
        }

        val isFavorite = remember {
            mutableStateOf(false)
        }

        val coroutineScope = rememberCoroutineScope()

        val snackbarHostState = remember {
            SnackbarHostState()
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Box {

                    AsyncImage(
                        model = aktivitas.imageUrl,

                        contentDescription = aktivitas.judul,
                        placeholder = painterResource(id = R.drawable.belajar),

                        error = painterResource(id = R.drawable.belajar),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),

                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = {
                            isFavorite.value =
                                !isFavorite.value
                        },

                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    ) {

                        Icon(
                            imageVector =
                                if (isFavorite.value)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,

                            contentDescription = "Favorite",

                            tint =
                                if (isFavorite.value)
                                    Color.Red
                                else
                                    Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    aktivitas.judul,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(aktivitas.deskripsi)

                Spacer(modifier = Modifier.height(6.dp))

                Text("Jam: ${aktivitas.jam}")

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        coroutineScope.launch {

                            isLoading = true

                            delay(2000)

                            snackbarHostState.showSnackbar(
                                "Aktivitas ${aktivitas.judul} selesai!"
                            )

                            isLoading = false
                        }
                    },

                    modifier = Modifier.fillMaxWidth(),

                    enabled = !isLoading
                ) {

                    if (isLoading) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Memproses...")

                    } else {

                        Text("Selesai")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBack,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Kembali")
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,

                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            )
        }
    }

