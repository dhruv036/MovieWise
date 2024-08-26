package io.dhruv.movieapp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.dhruv.movieapp.ui.viewmodel.MainViewModel
import io.dhruv.movieapp.R

@Composable
fun MovieDetailScreen(viewModel: MainViewModel, position: Int, popBack: () -> Unit) {

    val movie by viewModel.movies.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .padding(4.dp)
                .clickable {
                    popBack()
                }
                .size(32.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.results[position].posterPath}",
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(500.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(1) {
                    Text(
                        text = movie.results[position].title ?: "",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        lineHeight = TextUnit(1f, TextUnitType.Em),
                        fontWeight = FontWeight(500)
                    )
                    Text(
                        text = movie.results[position].overview ?: "",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        modifier = Modifier
                            .padding(top = 24.dp)
                    )
                }
            }
        }

    }

}