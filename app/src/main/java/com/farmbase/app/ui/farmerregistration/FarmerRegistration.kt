import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.farmerregistration.FarmerRegistrationViewModel
import com.farmbase.app.ui.farmerregistration.UploadState
import com.farmbase.app.ui.farmerregistration.ValidationResult
import com.farmbase.app.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerRegistrationScreen(
    viewModel: FarmerRegistrationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    farmer: Farmer? = null
) {
    val validationState by viewModel.validationState.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    var showValidationErrorDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(validationState) {
        when (validationState) {
            is ValidationResult.Success -> {
                onNavigateBack()
                viewModel.clearValidation()
            }

            is ValidationResult.Error -> {
                showValidationErrorDialog = true
            }

            null -> {
                showValidationErrorDialog = false
            }
        }
    }

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                onNavigateBack()
            }

            is UploadState.Error -> {
                showErrorDialog = true
            }

            else -> {}
        }
    }

    if (showValidationErrorDialog) {
        val errors = (validationState as ValidationResult.Error).errors
        AlertDialog(
            onDismissRequest = {
                showValidationErrorDialog = false
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
                    showValidationErrorDialog = false
                    viewModel.clearValidation()
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        val errorMessage = (uploadState as UploadState.Error).message
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            title = { Text("Error") },
            text = {
                Text(errorMessage)
            },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
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
                isLoading = uploadState is UploadState.Loading,
                onSubmit = { name, email, phoneNumber, location, specialty, profilePictureUri ->
                    viewModel.submitFarmer(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        location = location,
                        specialtyCrops = specialty,
                        profilePictureUri = profilePictureUri,
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
    isLoading: Boolean,
    onSubmit: (String, String, String, String, String, Uri?) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(farmer?.name ?: "") }
    var email by rememberSaveable { mutableStateOf(farmer?.email ?: "") }
    var phoneNumber by rememberSaveable { mutableStateOf(farmer?.phoneNumber ?: "") }
    var location by rememberSaveable { mutableStateOf(farmer?.location ?: "") }
    var specialtyCrops by rememberSaveable { mutableStateOf(farmer?.specialtyCrops ?: "") }
    var profilePictureUri: Uri? by rememberSaveable {
        mutableStateOf(
            if (farmer?.profilePictureUrl != null)
                Uri.parse(farmer.profilePictureUrl)
            else
                null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoPicker(
            imageUri = profilePictureUri,
            onImagePicked = { uri ->
                profilePictureUri = uri
            },
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { value ->
                name = value
            },
            label = { Text("Name") },
            placeholder = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = email,
            onValueChange = { value ->
                email = value
            },
            label = { Text("Email") },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { value ->
                phoneNumber = value
            },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = location,
            onValueChange = { value ->
                location = value
            },
            label = { Text("Location") },
            placeholder = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading
        )

        DropdownField(
            value = specialtyCrops,
            onValueChange = { value ->
                specialtyCrops = value
            },
            label = "Specialty Crops",
            options = Constants.SPECIALTY_CROPS,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Button(
            onClick = {
                onSubmit(name, email, phoneNumber, location, specialtyCrops, profilePictureUri)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Submit")
            }
        }
    }
}