package com.mevalera.feelslike.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mevalera.feelslike.R
import com.mevalera.feelslike.ui.theme.poppinsFamily

@Composable
fun ErrorContent(message: AnnotatedString) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.error_title),
            fontSize = dimensionResource(R.dimen.error_title_text_size).value.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.error_spacer_height)))
        Text(
            textAlign = TextAlign.Center,
            text = message,
            fontSize = dimensionResource(R.dimen.error_message_text_size).value.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = poppinsFamily,
        )
    }
}
