package io.dhruv.movieapp.ui.view


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.dhruv.movieapp.ui.viewmodel.MainViewModel
import io.dhruv.movieapp.R
import io.dhruv.movieapp.data.model.MovieDetails

//@Preview(showBackground = true)
@Composable
fun MovieListScreen(viewModel: MainViewModel, onItemClicked: (Int) -> Unit = {}) {
    val searchText by viewModel.searchValue.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
    ) {
        TextField(
            value = searchText,
            placeholder = {
                Text(
                    text = "Search Movies",
                    color = Color(0xFF9AA4B2),
                    fontFamily = FontFamily(Font(R.font.inter_medium))
                )
            },
            onValueChange = viewModel::onSearchTextChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color(0XFFE3E8EF), RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = Color(0xFF9AA4B2),
                cursorColor = Color(0xFF9AA4B2),
                backgroundColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF9AA4B2)
                )
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(18.dp))

        if (isSearching) {
            Log.e("TAG", "MovieListScreen: ", )
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(movies.results.size) { movie ->
                    MovieItem(movies.results.get(movie), modifier = Modifier.clickable {
                        onItemClicked(movie)
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItem(movie: MovieDetails = MovieDetails(), modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w400${movie.posterPath}",
            contentDescription = null,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.movie_image)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(modifier =Modifier.fillMaxWidth()) {
            Text(
                text = movie?.title ?: "Avenger -End Games",
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                color = Color.Black
            )
        }
    }
}
