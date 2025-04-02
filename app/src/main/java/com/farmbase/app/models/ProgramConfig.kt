package com.farmbase.app.models

import com.google.gson.annotations.SerializedName

data class Icon(
    @SerializedName("icon_id")
    val iconId: String,
    @SerializedName("icon_url")
    val iconUrl: String
)

data class Organization(
    val id: String,
    val name: String
)

data class ProductType(
    val id: String,
    val name: String
)

data class Program(
    @SerializedName("program_id")
    val programId: String,
    @SerializedName("program_name")
    val programName: String,
    val organization: Organization,
    @SerializedName("country_of_operation_id")
    val countryOfOperationId: String,
    @SerializedName("product_type")
    val productType: ProductType
)

data class Role(
    @SerializedName("role_id")
    val roleId: String,
    val name: String,
    val abbreviation: String,
    @SerializedName("child_portfolios")
    val childPortfolios: List<String>
)

data class Activity(
    @SerializedName("activity_id")
    val activityId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("entity")
    val entity: List<String>,
    @SerializedName("entity_type")
    val entityType: String,
    @SerializedName("facial_verification_flag")
    val facialVerificationFlag: Int,
    @SerializedName("location_check_flag")
    val locationCheckFlag: Int,
    @SerializedName("form_id")
    val formId: String,
    @SerializedName("days_to_late")
    val daysToLate: Int,
    @SerializedName("days_to_very_late")
    val daysToVeryLate: Int,
    @SerializedName("scheduled_activity_flag")
    val scheduledActivityFlag: Int
)



data class Currency(
    val name: String,
    @SerializedName("iso_code")
    val isoCode: String,
    @SerializedName("unicode_symbol")
    val unicodeSymbol: String
)

data class Country(
    @SerializedName("country_id")
    val countryId: String,
    val name: String,
    val flag: String,
    val currencies: List<Currency>,
    @SerializedName("country_code")
    val countryCode: String
)

data class CoverageArea(
    val id: String,
    val name: String,
    val type: String
)

data class Region(
    val name: String,
    val code: String,
    val organization: String,
    @SerializedName("country_operation")
    val countryOperation: String,
    val address: String,
    @SerializedName("coverage_area")
    val coverageArea: List<String>,
    @SerializedName("delete_flag")
    val deleteFlag: Int,
    val status: Int,
    val hubs: List<String>,
    val id: String,
    val created_at: String,
    val updated_at: String
)

data class Zone(
    val name: String,
    val region: String,
    val organization: String,
    @SerializedName("country_operation")
    val countryOperation: String,
    val address: String,
    val status: Int,
    @SerializedName("delete_flag")
    val deleteFlag: Int,
    val hubs: List<String>,
    val id: String,
    val created_at: String,
    val updated_at: String
)

data class Hub(
    val name: String,
    val region: String,
    val organization: String,
    @SerializedName("country_operation")
    val countryOperation: String,
    val zone: String,
    val address: String,
    val status: Int,
    @SerializedName("delete_flag")
    val deleteFlag: Int,
    val id: String,
    val created_at: String,
    val updated_at: String
)

data class ProgramConfig(
    val icons: List<Icon>,
    val program: Program,
    val roles: List<Role>,
    val activities: List<Activity>,
    val country: Country,
    val coverageAreas: List<CoverageArea>,
    val program_phase: List<String>,
    val regions: List<Region>,
    val zones: List<Zone>,
    val hubs: List<Hub>
)
