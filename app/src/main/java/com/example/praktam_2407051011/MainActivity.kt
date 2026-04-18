package com.example.praktam_2407051011

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.praktam_2407051011.Model.Aktivitas
import com.example.praktam_2407051011.Model.DataSource
import com.example.praktam_2407051011.ui.theme.PrakTAM_2407051011Theme

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

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            AktivitasListScreen { index ->
                navController.navigate("detail/$index")
            }
        }

        composable(
            route = "detail/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->

            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val data = DataSource.dummyAktivitas[index]

            DetailScreen(
                aktivitas = data,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun AktivitasListScreen(onItemClick: (Int) -> Unit) {

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

            Text("Aktivitas Prioritas", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                itemsIndexed(DataSource.dummyAktivitas) { index, aktivitas ->
                    AktivitasRowItem(aktivitas) {
                        onItemClick(index)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Daftar Aktivitas Harian", style = MaterialTheme.typography.titleMedium)
        }

        itemsIndexed(DataSource.dummyAktivitas) { index, aktivitas ->
            AktivitasItem(aktivitas) {
                onItemClick(index)
            }
        }
    }
}

@Composable
fun AktivitasRowItem(aktivitas: Aktivitas, onClick: () -> Unit) {

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {

            Box {
                Image(
                    painter = painterResource(aktivitas.imageRes),
                    contentDescription = aktivitas.nama,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(Modifier.padding(8.dp)) {
                Text(aktivitas.nama)
                Text(
                    aktivitas.jam,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AktivitasItem(aktivitas: Aktivitas, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Image(
                painter = painterResource(aktivitas.imageRes),
                contentDescription = aktivitas.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(aktivitas.nama, style = MaterialTheme.typography.titleLarge)
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

    var isLoading by remember { mutableStateOf(false) }

    val isFavorite = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Box {
                Image(
                    painter = painterResource(id = aktivitas.imageRes),
                    contentDescription = aktivitas.nama,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        isFavorite.value = !isFavorite.value
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
                        imageVector = if (isFavorite.value)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite.value) Color.Red else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(aktivitas.nama, style = MaterialTheme.typography.titleLarge)
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
                            "Aktivitas ${aktivitas.nama} selesai!"
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
                    Spacer(modifier = Modifier.width(8.dp))
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
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}