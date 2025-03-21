package com.farmbase.app.ui.homepage

sealed class HomepageClickEvents {
    data class PortfolioActivityCardCLicked(val index: Int, val name: String) : HomepageClickEvents()
    data class ActivityCardCLicked(val index: Int, val name: String) : HomepageClickEvents()
    data class HistoryActivityCardCLicked(val index: Int, val name: String) : HomepageClickEvents()
    data class PortfolioCardCLicked(val index: Int, val name: String) : HomepageClickEvents()
}

enum class ActivityType {
    PORTFOLIO_ACTIVITY,
    ACTIVITY,
    PORTFOLIO,
    HISTORY
}