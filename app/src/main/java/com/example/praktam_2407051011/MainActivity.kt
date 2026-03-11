package com.example.praktam_2407051011

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    AktivitasScreen()
                }
            }
        }
    }
}

@Composable
fun AktivitasScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Pengingat Aktivitas Harian",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(DataSource.dummyAktivitas) { aktivitas ->
                AktivitasItem(aktivitas)
            }
        }
    }
}

@Composable
fun AktivitasItem(aktivitas: Aktivitas) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            Image(
                painter = painterResource(id = aktivitas.imageRes),
                contentDescription = aktivitas.nama,
                modifier = Modifier.size(75.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = aktivitas.nama,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Jam: ${aktivitas.jam}",
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = aktivitas.deskripsi,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Selesai")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AktivitasPreview() {
    PrakTAM_2407051011Theme {
        AktivitasScreen()
    }
}
