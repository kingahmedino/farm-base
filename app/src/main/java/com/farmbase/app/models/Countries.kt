package com.farmbase.app.models

enum class Countries(val countryIso: String, val countryName: String, val countryCode: String) {
    Andorra("AD", "Andorra", "+376"), UnitedArabEmirates(
        "AE",
        "United Arab Emirates",
        "+971"
    ),
    Afghanistan("AF", "Afghanistan", "+93"), AntiguaAndBarbuda(
        "AG",
        "Antigua and Barbuda",
        "+1"
    ),
    Anguilla("AI", "Anguilla", "+1"), Albania("AL", "Albania", "+355"), Armenia(
        "AM",
        "Armenia",
        "+374"
    ),
    Angola("AO", "Angola", "+244"), Antarctica("AQ", "Antarctica", "+672"), Argentina(
        "AR",
        "Argentina",
        "+54"
    ),
    AmericanSamoa("AS", "American Samoa", "+1"), Austria("AT", "Austria", "+43"), Australia(
        "AU",
        "Australia",
        "+61"
    ),
    Aruba("AW", "Aruba", "+297"), AlandIslands("AX", "Aland Islands", "+358"), Azerbaijan(
        "AZ",
        "Azerbaijan",
        "+994"
    ),
    BosniaAndHerzegovina("BA", "Bosnia and Herzegovina", "+387"), Barbados(
        "BB",
        "Barbados",
        "+1"
    );

    companion object {
        fun getAllCountryNames(): List<String> {
            return entries.map { it.countryName }
        }
    }
}