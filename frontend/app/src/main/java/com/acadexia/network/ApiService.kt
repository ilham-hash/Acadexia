package com.acadexia.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Courses
    @GET("courses")
    suspend fun getCourses(): Response<List<Course>>

    @POST("courses")
    suspend fun createCourse(@Body course: CourseRequest): Response<Course>

    @GET("courses/{courseId}")
    suspend fun getCourse(@Path("courseId") courseId: String): Response<Course>

    @PUT("courses/{courseId}")
    suspend fun updateCourse(@Path("courseId") courseId: String, @Body course: CourseRequest): Response<Course>

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(@Path("courseId") courseId: String): Response<Void>

    // Tasks
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("tasks")
    suspend fun createTask(@Body task: TaskRequest): Response<Task>

    @PUT("tasks/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: String, @Body task: TaskRequest): Response<Task>

    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String): Response<Void>

    @POST("tasks/plan/generate")
    suspend fun generatePlan(): Response<StudyPlan>

    // AI
    @POST("ai/chat")
    suspend fun chatWithAI(@Body request: ChatRequest): Response<ChatResponse>

    @POST("ai/summary/{courseId}")
    suspend fun generateSummary(@Path("courseId") courseId: String): Response<SummaryResponse>

    @POST("ai/quiz/generate")
    suspend fun generateQuiz(@Body request: GenerateQuizRequest): Response<GenerateQuizResponse>

    @POST("ai/quiz/submit")
    suspend fun submitQuizAnswer(@Body request: SubmitAnswerRequest): Response<SubmitAnswerResponse>

    // Progress
    @POST("progress/study-time")
    suspend fun updateStudyTime(@Body request: StudyTimeRequest): Response<ProgressResponse>

    @GET("progress")
    suspend fun getProgress(): Response<ProgressData>

    @GET("progress/stats")
    suspend fun getStats(): Response<StatsResponse>
}

// Request/Response Models
data class SignupRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String, val user: UserData)
data class UserData(val id: String, val name: String, val email: String, val xp: Int = 0, val level: Int = 1)

data class CourseRequest(val title: String, val subject: String, val content: String)
data class Course(val _id: String, val title: String, val subject: String, val content: String, val summary: String? = null)

data class TaskRequest(val title: String, val description: String? = null, val subject: String? = null, val deadline: String? = null, val priority: String = "medium", val estimatedTime: Int = 1)
data class Task(val _id: String, val title: String, val status: String, val priority: String, val deadline: String? = null)

data class ChatRequest(val message: String)
data class ChatResponse(val message: String, val response: String)

data class SummaryResponse(val courseId: String, val summary: String)

data class GenerateQuizRequest(val courseId: String, val numQuestions: Int = 5)
data class GenerateQuizResponse(val quizId: String, val questions: List<QuizQuestion>)
data class QuizQuestion(val question: String, val options: List<String>, val type: String)

data class SubmitAnswerRequest(val quizId: String, val questionIndex: Int, val userAnswer: String)
data class SubmitAnswerResponse(val isCorrect: Boolean, val correctAnswer: String, val score: Int, val percentage: Int)

data class StudyTimeRequest(val subject: String, val dailyMinutes: Int)
data class ProgressResponse(val message: String, val xp: Int, val level: Int, val streak: Int, val badges: List<String>)

data class StudyPlan(val plan: List<PlanDay>, val recommendation: String)
data class PlanDay(val day: String, val tasks: List<PlanTask>)
data class PlanTask(val name: String, val hours: Int)

data class ProgressData(val progress: List<Subject>, val xp: Int, val level: Int, val streak: Int, val badges: List<String>)
data class Subject(val subject: String, val dailyStudyTime: Int, val totalStudyTime: Int)

data class StatsResponse(val level: Int, val xp: Int, val streak: Int, val badges: List<String>, val totalStudyTime: Int, val totalQuizzes: Int, val avgQuizScore: Int, val subjects: List<SubjectStats>)
data class SubjectStats(val subject: String, val studyTime: Int)
