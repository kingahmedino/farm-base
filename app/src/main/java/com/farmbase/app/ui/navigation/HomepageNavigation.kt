package com.farmbase.app.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.farmbase.app.ui.confirmAction.ConfirmActionScreen
import com.farmbase.app.ui.homepage.HomepageScreen
import com.farmbase.app.ui.selectHomepage.SelectHomepageScreen
import com.farmbase.app.ui.selectProgram.SelectProgramScreen


fun NavGraphBuilder.homePageNavigationRoute(
    modifier: Modifier,
    navHostController: NavHostController
    ) {


    composable<ConfirmAction> {
        ConfirmActionScreen(
            onContinueClicked = { navHostController.navigate(SelectProgram) },
            onSelectAnotherClicked = { navHostController.navigate(SelectHomepage) },
            onBackButtonClicked = {},
        )
    }

    composable<SelectProgram> {
        SelectProgramScreen(
            onNextButtonClicked = {
                navHostController.navigate(SelectHomepage)
            }
        )
    }

    composable<SelectHomepage> {
        SelectHomepageScreen(
            onBackButtonClicked = {
                navHostController.navigateUp()
            },
            onNextButtonClicked = {
                navHostController.navigate(MyHomepage("Poultry Hub Lead"))
            }
        )
    }

    composable<MyHomepage> { backStackEntry ->
        val arguments: MyHomepage = backStackEntry.toRoute()
        HomepageScreen(
            role = arguments.role,
            onBackButtonClicked = { navHostController.navigateUp() }
        )
    }

}
