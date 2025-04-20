package com.example.hockeyapp.data.remote


import android.app.DownloadManager.Query
import com.example.hockeyapp.data.models.Event
import com.example.hockeyapp.data.models.News
import com.example.hockeyapp.data.models.Player
import com.example.hockeyapp.data.models.Team
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Auth operations
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password).await().user
        }
    }

    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        return withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password).await().user
        }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Team operations
    suspend fun registerTeam(team: Team): String {
        return withContext(Dispatchers.IO) {
            val newTeam = firestore.collection("teams").document()
            newTeam.set(team.copy(id = newTeam.id)).await()
            newTeam.id
        }
    }

    suspend fun getAllTeams(): List<Team> {
        return withContext(Dispatchers.IO) {
            firestore.collection("teams")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Team::class.java)
        }
    }

    suspend fun getTeamById(teamId: String): Team? {
        return withContext(Dispatchers.IO) {
            firestore.collection("teams").document(teamId).get().await().toObject(Team::class.java)
        }
    }

    // Player operations
    suspend fun registerPlayer(player: Player): String {
        return withContext(Dispatchers.IO) {
            val newPlayer = firestore.collection("players").document()
            newPlayer.set(player.copy(id = newPlayer.id)).await()

            // Update team with new player
            val teamRef = firestore.collection("teams").document(player.teamId)
            firestore.runTransaction { transaction ->
                val team = transaction.get(teamRef).toObject(Team::class.java)
                val updatedPlayerIds = team?.playerIds?.toMutableList() ?: mutableListOf()
                updatedPlayerIds.add(newPlayer.id)
                transaction.update(teamRef, "playerIds", updatedPlayerIds)
            }.await()

            newPlayer.id
        }
    }

    suspend fun getPlayersByTeam(teamId: String): List<Player> {
        return withContext(Dispatchers.IO) {
            firestore.collection("players")
                .whereEqualTo("teamId", teamId)
                .get()
                .await()
                .toObjects(Player::class.java)
        }
    }

    suspend fun getAllPlayers(): List<Player> {
        return withContext(Dispatchers.IO) {
            firestore.collection("players")
                .get()
                .await()
                .toObjects(Player::class.java)
        }
    }

    // Event operations
    suspend fun createEvent(event: Event): String {
        return withContext(Dispatchers.IO) {
            val newEvent = firestore.collection("events").document()
            newEvent.set(event.copy(id = newEvent.id)).await()
            newEvent.id
        }
    }

    suspend fun getAllEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            firestore.collection("events")
                .orderBy("startDate", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Event::class.java)
        }
    }

    // News operations
    suspend fun publishNews(news: News): String {
        return withContext(Dispatchers.IO) {
            val newNews = firestore.collection("news").document()
            newNews.set(news.copy(id = newNews.id)).await()
            newNews.id
        }
    }

    suspend fun getAllNews(): List<News> {
        return withContext(Dispatchers.IO) {
            firestore.collection("news")
                .orderBy("publishedDate", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(News::class.java)
        }
    }

    // Storage operations
    suspend fun uploadImage(imageBytes: ByteArray, path: String): String {
        return withContext(Dispatchers.IO) {
            val fileName = "${UUID.randomUUID()}.jpg"
            val ref = storage.reference.child("$path/$fileName")
            ref.putBytes(imageBytes).await()
            ref.downloadUrl.await().toString()
        }
    }
}