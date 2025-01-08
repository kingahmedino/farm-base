import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmbase.app.ui.farmerregistration.FarmerRegistrationViewModel
import com.farmbase.app.ui.farmerregistration.ValidationResult

data class FarmerFormState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val specialtyCrops: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerRegistrationScreen(
    viewModel: FarmerRegistrationViewModel,
    onNavigateBack: () -> Unit,
    farmerId: Int? = null
) {
    val selectedFarmer by viewModel.selectedFarmer.collectAsState()
    var formState by remember { mutableStateOf(FarmerFormState()) }
    val validationState by viewModel.validationState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(farmerId) {
        farmerId?.let {
            viewModel.loadFarmer(it)
        }
    }

    LaunchedEffect(selectedFarmer) {
        selectedFarmer?.let { farmer ->
            formState = FarmerFormState(
                name = farmer.name,
                email = farmer.email,
                phoneNumber = farmer.phoneNumber,
                location = farmer.location,
                specialtyCrops = farmer.specialtyCrops
            )
        }
    }

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
                title = { Text(if (farmerId == null) "New farmer profile" else "Edit farmer profile") },
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
                formState = formState,
                onSubmit = { name, email, phoneNumber, location, specialty -> 
                    viewModel.submitFarmer(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        location = location,
                        specialtyCrops = specialty,
                        farmerId = farmerId
                    )
                }
            )
        }
    }
}

@Composable
fun FarmerForm(
    formState: FarmerFormState,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(formState.name) }
    var email by remember { mutableStateOf(formState.email) }
    var phoneNumber by remember { mutableStateOf(formState.phoneNumber) }
    var location by remember { mutableStateOf(formState.location) }
    var specialtyCrops by remember { mutableStateOf(formState.name) }

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
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { value ->
                email = value
            },
            label = { Text("Email") },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { value ->
                phoneNumber = value
            },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = { value ->
                location = value
            },
            label = { Text("Location") },
            placeholder = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = specialtyCrops,
            onValueChange = { value ->
                specialtyCrops = value
            },
            label = { Text("Specialty Crops") },
            placeholder = { Text("Enter specialty crops") },
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