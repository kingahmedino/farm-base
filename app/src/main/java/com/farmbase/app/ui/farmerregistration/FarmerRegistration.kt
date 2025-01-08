import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.farmerregistration.FarmerRegistrationViewModel
import com.farmbase.app.ui.farmerregistration.ValidationResult
import com.farmbase.app.ui.widgets.DropdownField
import com.farmbase.app.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerRegistrationScreen(
    viewModel: FarmerRegistrationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    farmer: Farmer? = null
) {
    val validationState by viewModel.validationState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(validationState) {
        when (validationState) {
            is ValidationResult.Success -> {
                onNavigateBack()
                viewModel.clearValidation()
            }
            is ValidationResult.Error -> {
                showErrorDialog = true
            }
            null -> {
                showErrorDialog = false
            }
        }
    }

    if (showErrorDialog) {
        val errors = (validationState as ValidationResult.Error).errors
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                viewModel.clearValidation()
            },
            title = { Text("Validation Error") },
            text = {
                Column {
                    errors.forEach { error ->
                        Text(error)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    viewModel.clearValidation()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (farmer == null) "New farmer profile" else "Edit farmer profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            FarmerForm(
                farmer = farmer,
                onSubmit = { name, email, phoneNumber, location, specialty -> 
                    viewModel.submitFarmer(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        location = location,
                        specialtyCrops = specialty,
                        farmerId = farmer?.id
                    )
                }
            )
        }
    }
}

@Composable
fun FarmerForm(
    farmer: Farmer?,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(farmer?.name ?: "") }
    var email by rememberSaveable { mutableStateOf(farmer?.email ?: "") }
    var phoneNumber by rememberSaveable { mutableStateOf(farmer?.phoneNumber ?: "") }
    var location by rememberSaveable { mutableStateOf(farmer?.location ?: "") }
    var specialtyCrops by rememberSaveable { mutableStateOf(farmer?.specialtyCrops ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { value ->
                name = value
            },
            label = { Text("Name") },
            placeholder = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { value ->
                email = value
            },
            label = { Text("Email") },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { value ->
                phoneNumber = value
            },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            value = location,
            onValueChange = { value ->
                location = value
            },
            label = { Text("Location") },
            placeholder = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        DropdownField(
            value = specialtyCrops,
            onValueChange = { value ->
                specialtyCrops = value
            },
            label = "Specialty Crops",
            options = Constants.SPECIALTY_CROPS,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onSubmit(name, email, phoneNumber, location, specialtyCrops)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Submit")
        }
    }
}