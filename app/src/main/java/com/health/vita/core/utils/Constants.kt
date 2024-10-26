package com.health.vita.core.utils

object DatabaseNames{

    //This constants are used to store the name of the collections in the database in a uniform way

    val  physicalLevel : Map <Int, String> = mapOf(

        1 to "Sedentario",
        2 to "Ligero",
        3 to "Moderado",
        4 to "Atlético",
        5 to "Muy activo"

    )

    val  physicalTarget : Map <Int, String> = mapOf(

        1 to "Perder peso",
        2 to "Probar el coach de IA",
        3 to  "Ganar masa muscular",
        4 to "Mejorar mi alimentación"

    )

    val sex : Map <Int, String> = mapOf(

        1 to "Masculino",
        2 to "Femenino"

    )


}