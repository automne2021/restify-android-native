package com.restify.android.ui.screens.model3d

import androidx.annotation.DrawableRes
import com.restify.android.R

data class Model3DItem(
    val name: String,
    val path: String
)

data class ModelCategory(
    val title: String,
    @DrawableRes val iconRes: Int,
    val items: List<Model3DItem>
)

val modelCategories = listOf(
    ModelCategory(
        title = "Inner Peace",
        iconRes = R.drawable.ic_inner_peace,
        items = listOf(
            Model3DItem(name = "Singing Nepali Bowl", path = "models/singing_nepali_bowl.glb"),
            Model3DItem(name = "Hand Pan", path = "models/handpan.glb"),
            Model3DItem(name = "Kalimba", path = "models/kalimba.glb"),
            Model3DItem(name = "Meditation", path = "models/buddha.glb")
        )
    ),
    ModelCategory(
        title = "Vietnam Essence",
        iconRes = R.drawable.ic_vn_flag,
        items = listOf(
            Model3DItem(name = "Ao Dai", path = "models/ao_dai.glb"),
            Model3DItem(name = "Pho", path = "models/pho.glb"),
            Model3DItem(name = "Conical Hat", path = "models/conical_hat.glb"),
            Model3DItem(name = "Dong Son Bronze Drum", path = "models/drum.glb"),
            Model3DItem(name = "Monochord", path = "models/monochord.glb"),
            Model3DItem(name = "Xa Xi", path = "models/xaxi.glb")
        )
    )
)

