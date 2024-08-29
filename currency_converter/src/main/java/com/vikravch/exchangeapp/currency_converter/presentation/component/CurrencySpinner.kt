import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vikravch.exchangeapp.currency_converter.R
import com.vikravch.exchangeapp.currency_converter.presentation.ui.theme.ExchangeAppTheme

@Composable
fun CurrencySpinner(
    list: List<CurrencySpinnerData>,
    preselected: CurrencySpinnerData,
    onSelectionChanged: (myData: CurrencySpinnerData) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String = "",
    tag: String = ""
) {
    var selected = preselected
    var expanded by remember { mutableStateOf(false) } // initial value

    val constraintModifier = modifier
        .background(
            color = Color.Transparent
        )

    ExchangeAppTheme {
        Box(modifier = constraintModifier) {
            if (isError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = errorText,
                        modifier = Modifier.padding(start = 12.dp, bottom = 2.dp),
                        color = Color.White,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
                    .height(50.dp)
                    .background(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        )
                ) {
                    if (selected.id == "0") {
                        Text(
                            text = selected.name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = selected.name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        null,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, end = 16.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(Color.White)
                            .clickable {

                            }
                            .testTag(tag),

                        ) {
                        list.forEach { listEntry ->

                            DropdownMenuItem(
                                onClick = {
                                    selected = listEntry
                                    expanded = false
                                    onSelectionChanged(selected)
                                },
                                modifier = Modifier.background(Color.White),
                                text = {
                                    Text(
                                        text = listEntry.name,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }

    }

}


@Preview(showBackground = true)
@Composable
fun SpinnerSample_Preview() {
    MaterialTheme {
        val myData = listOf(
            CurrencySpinnerData("0", "Apples"),
            CurrencySpinnerData("1", "Bananas"),
            CurrencySpinnerData("2", "Kiwis")
        )
        CurrencySpinner(
            myData,
            preselected = myData.first(),
            onSelectionChanged = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

data class CurrencySpinnerData(
    val id: String,
    val name: String
)