package com.example.formvalidation.uii

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.formvalidation.model.Country
import com.example.formvalidation.model.Name
import com.example.formvalidation.model.Flags

@Composable
fun CountryListScreen(onCountryClick: (Country) -> Unit) {
    val countries = listOf(
        Country(Name("Germany"), Flags("https://flagcdn.com/de.svg"), listOf("Berlin"), "Europe", 83000000),
        Country(Name("France"), Flags("https://flagcdn.com/fr.svg"), listOf("Paris"), "Europe", 67000000),
        Country(Name("Italy"), Flags("https://flagcdn.com/it.svg"), listOf("Rome"), "Europe", 60000000)
    )


    LazyColumn {
        items(countries) { country ->
            Text(
                text = country.name.common,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCountryClick(country) }
                    .padding(16.dp)
            )
        }
    }
}
