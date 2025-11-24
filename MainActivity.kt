package np.ict.mad.navigationui

import android.R
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import np.ict.mad.navigationui.ui.theme.NavigationUITheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationUITheme {
                NavigationUIApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun NavigationUIApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TIMER) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when(currentDestination){
            AppDestinations.TIMER -> CountDownTimerScreen()
            AppDestinations.OTP -> OTPScreen()
        }
        /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }*/
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    TIMER("Timer", Icons.Default.Home),
    OTP("OTP", Icons.Default.Lock),
}

@Composable
fun CountDownTimerScreen(){
    var seconds by remember { mutableStateOf(10) }
    var isRunning by remember { mutableStateOf(false) }

    // Countdown logic
    LaunchedEffect(isRunning) {
        while (isRunning && seconds > 0) {
            delay(1000)
            seconds--

            if (seconds == 0) {
                isRunning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Countdown Timer",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Display countdown (always shows MM:SS format)
        val displayMinutes = seconds / 60
        val displaySeconds = seconds % 60
        Text(
            text = String.format("%02d:%02d", displayMinutes, displaySeconds),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Control buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Play button
            Button(
                onClick = {
                    if (seconds > 0) {
                        isRunning = !isRunning
                    }
                },
                modifier = Modifier.width(120.dp).height(50.dp)
            ) {
                Text(if (isRunning) "Pause" else "Play")
            }

            // Reset button
            OutlinedButton(
                onClick = {
                    isRunning = false
                    seconds = 60
                },
                modifier = Modifier.width(120.dp).height(50.dp)
            ) {
                Text("Reset")
            }
        }

        // Show time up message
        if (seconds == 0 && !isRunning) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "⏰ Time is up!",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

// Helper function to generate random 6-digit OTP
fun generateRandomOTP(): String {
    return Random.nextInt(100000, 999999).toString()
}
@Composable
fun OTPScreen(){
    // Generate random 6-digit number
    var otpCode by remember { mutableStateOf(generateRandomOTP()) }
    var countdown by remember { mutableStateOf(15) }

    // Auto-refresh OTP every 15 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Wait 1 second
            countdown--

            if (countdown <= 0) {
                // Generate new OTP and reset countdown
                otpCode = generateRandomOTP()
                countdown = 15
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "OTP Generator",
            style = MaterialTheme.typography.headlineLarge
            //fontSize = 28.sp,
            //fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your 6-digit OTP",
            style = MaterialTheme.typography.headlineSmall,
            //fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Display OTP in large font
        Text(
            text = otpCode,
            style = MaterialTheme.typography.headlineLarge
            /*fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            letterSpacing = 8.sp*/
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Show countdown
        Text(
            text = "Next refresh in: $countdown seconds",
            style = MaterialTheme.typography.headlineSmall
            //fontSize = 14.sp,
            //color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Manual refresh button
        Button(
            onClick = {
                otpCode = generateRandomOTP()
                countdown = 15
            },
            //modifier = Modifier
            //    .fillMaxWidth()
            //    .height(50.dp)
        ) {
            Text("Generate New OTP",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavigationUITheme {
        Greeting("Android")
    }
}
*/
