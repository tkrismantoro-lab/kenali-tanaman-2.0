package com.example.data.knowledge

import com.example.model.PlantKnowledge
import com.example.model.PlantProblemRecord

object PlantKnowledgeRepository {
    val allPlants: List<PlantKnowledge> by lazy {
        PlantCatalogPart1.plants + PlantCatalogPart2.plants
    }

    val allProblems: List<PlantProblemRecord> by lazy {
        PlantProblemsPart1.problems + PlantProblemsPart2.problems
    }

    fun findPlantById(id: String): PlantKnowledge? =
        allPlants.firstOrNull { it.id == id }

    fun findPlantByNameOrMatch(name: String): PlantKnowledge? {
        val q = name.trim().lowercase()
        return allPlants.firstOrNull { plant ->
            plant.commonName.lowercase().contains(q) ||
                q.contains(plant.commonName.lowercase().substringBefore(" ")) ||
                plant.localNames.any { it.lowercase() == q || q.contains(it.lowercase()) }
        }
    }

    fun problemsForCrop(cropId: String): List<PlantProblemRecord> =
        allProblems.filter { it.cropId == cropId }

    fun searchPlants(query: String): List<PlantKnowledge> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allPlants
        return allPlants.filter { plant ->
            plant.commonName.lowercase().contains(q) ||
                plant.scientificName.lowercase().contains(q) ||
                plant.category.lowercase().contains(q) ||
                plant.localNames.any { it.lowercase().contains(q) }
        }
    }

    fun searchProblems(query: String): List<PlantProblemRecord> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allProblems
        return allProblems.filter { p ->
            p.title.lowercase().contains(q) ||
                p.cropName.lowercase().contains(q) ||
                p.symptomDescription.lowercase().contains(q)
        }
    }
}
