package org.unizd.rma.saric.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.unizd.rma.saric.presentation.camera.CameraScreen
import org.unizd.rma.saric.presentation.pet.PetsScreen
import org.unizd.rma.saric.presentation.pet.create.AddPetScreen
import org.unizd.rma.saric.presentation.pet.detail.PetDetailScreen

sealed class Screen(val route: String) {
    object Pets : Screen("pets_screen")
    object PetDetail : Screen("pet_detail_screen/{petId}") {
        fun createRoute(petId: Int) = "pet_detail_screen/$petId"
    }
    object AddPet : Screen("add_pet_screen")
    object EditPet : Screen("edit_pet/{petId}") {
        fun createRoute(petId: Int) = "edit_pet/$petId"
    }
    object Camera : Screen("camera_screen/{petId}") {
        fun createRoute(petId: Int) = "camera_screen/$petId"
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Pets.route
    ) {
        composable(Screen.Pets.route) {
            PetsScreen(
                onAddClick = {
                    navController.navigate(Screen.AddPet.route)
                },
                onNavigateToDetail = { pet ->
                    navController.navigate(Screen.PetDetail.createRoute(pet.id))
                }
            )
        }

        composable(Screen.AddPet.route) {
            AddPetScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            PetDetailScreen(
                petId = petId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { pet ->
                    navController.navigate(Screen.EditPet.createRoute(pet.id))
                },
                onPhotoClick = {
                    navController.navigate(Screen.Camera.createRoute(petId))
                }
            )
        }

        composable(
            route = Screen.EditPet.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            AddPetScreen(
                pet = null,
                petIdForEdit = petId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Camera.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            CameraScreen(
                context = context,
                petId = petId,
                onPhotoTaken = { _ ->
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}